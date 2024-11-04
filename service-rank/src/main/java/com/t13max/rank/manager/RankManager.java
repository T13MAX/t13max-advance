package com.t13max.rank.manager;

import com.alibaba.fastjson.JSONObject;
import com.alipay.remoting.util.ConcurrentHashSet;
import com.alipay.sofa.rpc.common.utils.JSONUtils;
import com.t13max.common.manager.ManagerBase;
import com.t13max.data.dao.PlayerBasicInfoManager;
import com.t13max.data.entity.RankDumpData;
import com.t13max.data.entity.RankKeyData;
import com.t13max.data.mongo.MongoManager;
import com.t13max.data.mongo.collection.XSet;
import com.t13max.data.mongo.util.UuidUtil;
import com.t13max.data.redis.RedisManager;
import com.t13max.game.api.args.*;
import com.t13max.game.api.entity.OwnRankInfo;
import com.t13max.game.api.entity.PlayerBasicInfo;
import com.t13max.game.api.entity.RankInfo;
import com.t13max.game.api.entity.RankViewInfo;
import com.t13max.game.enums.RankEnum;
import com.t13max.game.enums.RankSortEnum;
import com.t13max.game.rank.RankCondition;
import com.t13max.rank.consts.Const;
import com.t13max.rank.util.Log;
import org.bson.Document;
import org.redisson.api.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 排行榜管理器
 *
 * @author t13max
 * @since 17:29 2024/9/4
 */
public class RankManager extends ManagerBase {

    private final ScheduledExecutorService autoSaveExecutor = Executors.newSingleThreadScheduledExecutor();

    private final Set<String> localKeySet = new ConcurrentHashSet<>();

    private RankKeyData rankKeyData;

    private RSet<String> keySet;

    private RBlockingQueue<String> rankQueue;

    private volatile boolean running = false;

    private volatile LocalDateTime dumpDateTime;

    private volatile LocalDateTime lastScheduleTime;

    public static RankManager inst() {
        return inst(RankManager.class);
    }

    @Override
    protected void onShutdown() {
        autoSaveExecutor.shutdown();
        while (!rankQueue.isEmpty()) {
            updateRankQueue();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.rankDump();
    }

    @Override
    protected void init() {
        keySet = RedisManager.inst().getSet(getRankDbCacheKey(Const.RANK_ALL_VIEW));
        rankQueue = RedisManager.inst().getBlockingQueue(Const.RANK_QUEUE);
        //反序列化
        autoSaveExecutor.scheduleAtFixedRate(this::schedule, 200, 200, TimeUnit.MILLISECONDS);

        this.rankRestore();

        LocalDateTime now = LocalDateTime.now();
        dumpDateTime = now.with(LocalTime.of(5, 0)); // 今天5点
        if (now.isAfter(dumpDateTime)) {
            dumpDateTime = dumpDateTime.plusDays(1); // 如果当前时间已过5点，设置为明天5点
        }

        rankKeyData = MongoManager.inst().findById(RankKeyData.class, Const.RANK_KEY_DATA_ID);
        if (rankKeyData == null) {
            rankKeyData = new RankKeyData();
            MongoManager.inst().save(rankKeyData);
        }

        this.running = true;
    }

    /**
     * 排行榜数据落地
     *
     * @Author t13max
     * @Date 16:06 2024/10/31
     */
    private void rankDump() {
        MongoManager.inst().deleteAll(RankDumpData.class);
        XSet<String> rankKeySet = rankKeyData.getRankKeySet();
        Log.RANK.info("rks:{}", rankKeySet);
        if (!rankKeySet.isEmpty()) {
            for (String key : rankKeySet) {
                RLock rlock = RedisManager.inst().getLock(key + ":" + Const.UP_RANK_LOCK);
                rlock.lock();
                try {
                    RScoredSortedSet<String> scoredSortedSet = RedisManager.inst().getScoredSortedSet(key); //排行榜数据
                    if (!scoredSortedSet.isEmpty()) {
                        Log.RANK.info("key:{} dump rank data", key);
                        RMap<Long, String> pidRankMap = RedisManager.inst().getMap(key + ":" + Const.PID_MAPPER_RANK_DATA);
                        RMap<Long, RankInfo> rankMap = RedisManager.inst().getMap(key + ":" + Const.RANK_DATA);
                        RankDumpData rankDumpData = new RankDumpData();
                        rankDumpData.setId(UuidUtil.getNextId());
                        rankDumpData.setRankKey(key);
                        rankDumpData.setScoredSet(scoredSortedSet.dump());
                        rankDumpData.setMapperMap(pidRankMap.dump());//有序队列排行key值
                        rankDumpData.setRankData(rankMap.dump());//排行榜数据
                        MongoManager.inst().save(rankDumpData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    rlock.unlock();
                }
            }
        }
    }

    /**
     * 从数据库中恢复排行榜数据
     *
     * @Author t13max
     * @Date 16:06 2024/10/31
     */
    private void rankRestore() {
        RLock rl = RedisManager.inst().getLock(Const.RANK_RESTORE_LOCK);
        rl.lock();
        try {
            RBucket<String> rBucket = RedisManager.inst().getBucket(Const.RANK_RESTORE_STATE);
            if (!rBucket.isExists()) {
                for (String key : rankKeyData.getRankKeySet()) {
                    RScoredSortedSet<String> scoredSortedSet = RedisManager.inst().getScoredSortedSet(key); //排行榜数据
                    List<RankDumpData> listRankDumpData = MongoManager.inst().findList(RankDumpData.class, new Document("rankKey", key));
                    if (!listRankDumpData.isEmpty()) {
                        RankDumpData dumpData = listRankDumpData.get(0);
                        if (Objects.nonNull(dumpData)) {
                            Log.RANK.debug(dumpData.toString());
                            scoredSortedSet.clear();
                            scoredSortedSet.restore(dumpData.getScoredSet());
                            RMap<Long, String> pidRankMap = RedisManager.inst().getMap(key + ":" + Const.PID_MAPPER_RANK_DATA);
                            pidRankMap.clear();
                            pidRankMap.restore(dumpData.getMapperMap());
                            RMap<Long, RankInfo> rankMap = RedisManager.inst().getMap(key + ":" + Const.RANK_DATA);
                            rankMap.clear();
                            rankMap.restore(dumpData.getRankData());
                        }
                    }
                }
                rBucket.set("isRestore ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rl.unlock();
        }
    }

    /**
     * 200ms执行一次
     *
     * @Author t13max
     * @Date 17:01 2024/10/31
     */
    private void schedule() {

        LocalDateTime now = LocalDateTime.now();

        this.updateRankQueue();

        if (running && dumpDateTime.isAfter(now)) {
            dumpDateTime = dumpDateTime.plusDays(1);// 如果当前时间已过5点，设置为明天5点
            RLock rlock = RedisManager.inst().getLock(Const.RANK_TIMING_DUMP);
            if (rlock.tryLock()) {
                try {
                    while (!rankQueue.isEmpty()) {
                        updateRankQueue();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    rankDump();
                } finally {
                    rlock.unlock();
                }
            }
        }

        LocalDateTime refreshTime = lastScheduleTime.plusMinutes(5);
        if (now.isAfter(refreshTime)) {
            refreshRankData();
        }

        this.lastScheduleTime = now;
    }

    /**
     * 定时刷新排行榜数据
     * 把排行榜的数据更新到View 也就是玩家可以看到的地方
     *
     * @Author t13max
     * @Date 16:04 2024/11/4
     */
    private void refreshRankData() {
        if (!running) {
            return;
        }
        try {
            Set<String> keySet = new HashSet<>(this.keySet.readAll());
            if (keySet.isEmpty()) {
                return;
            }
            for (String key : keySet) {
                try {
                    //可以通过在redis里记录最后刷新时间, 最近5s如果刷过就skip, 也可避免多实例重复刷
                    RLock rlock = RedisManager.inst().getLock(key + ":" + Const.REF_RANK_LOCK);
                    if (rlock.tryLock()) {
                        try {//5秒超时
                            RankEnum rankEnum = splitKeys(key);
                            int statisticalCount = rankEnum.getShow() + Const.EXCESS_QUERY_COUNT;
                            long start = System.currentTimeMillis();
                            RScoredSortedSet<String> scoredSortedSet = RedisManager.inst().getScoredSortedSet(key);
                            if (!scoredSortedSet.isEmpty()) {
                                RMap<Long, RankInfo> rankMap = RedisManager.inst().getMap(key + ":" + Const.RANK_DATA);                      //排行比较数据
                                int rank = 1;
                                Collection<String> pidConditionList = scoredSortedSet.valueRangeReversed(0, statisticalCount - 1);
                                int size = pidConditionList.size();
                                List<String> pidStrList = new LinkedList<>(pidConditionList);
                                Set<Long> pidSet = new HashSet<>();
                                List<Long> pidList = new LinkedList<>();
                                pidStrList.forEach(pidStr -> {
                                    long pid = convert(pidStr);
                                    pidList.add(pid);
                                    pidSet.add(pid);
                                });
                                Map<Long, RankInfo> mapRankAll = rankMap.getAll(pidSet);
                                RMap<String, List<RankViewInfo>> rankKeyViewMap = RedisManager.inst().getMap(Const.VIEW_RANK_DATA);
                                List<RankViewInfo> RankViewInfoList = new LinkedList<>();
                                for (Long pid : pidList) {
                                    RankInfo rankData = mapRankAll.get(pid);
                                    if (Objects.nonNull(rankData)) {
                                        RankViewInfo rankViewInfo = new RankViewInfo();
                                        rankViewInfo.setRank(rank);
                                        rankViewInfo.setRankData(rankData);
                                        RankViewInfoList.add(rankViewInfo);
                                        rank++;
                                    }
                                }
                                rankKeyViewMap.put(key, RankViewInfoList);
                            }
                            this.keySet.remove(key);
                            this.localKeySet.remove(key);

                            long millisSecondsUsed = (System.currentTimeMillis() - start);
                            Log.RANK.debug("refresh rank job consume time : {} ms", millisSecondsUsed);
                        } finally {
                            rlock.unlock();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新排行榜更新任务队列
     *
     * @Author t13max
     * @Date 16:05 2024/11/4
     */
    private void updateRankQueue() {
        try {
            if (rankQueue.isEmpty()) {
                return;
            }
            List<String> list = new LinkedList<>();
            rankQueue.drainTo(list, Const.MAX_ELEMENTS);
            updateRank(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新排行榜
     *
     * @Author t13max
     * @Date 16:05 2024/11/4
     */
    private void updateRank(List<String> jsonList) {
        Set<Long> localPidSet = new HashSet<>();
        for (String jsonStr : jsonList) {
            Log.RANK.info("update rank data:{}", jsonStr);
            Map<String, Map<Integer, List<RankInfo>>> map = JSONUtils.parseObject(jsonStr, Map.class);
            try {
                map.forEach((keysEnum, v) -> {
                    Map<Integer, List<RankInfo>> rankDataListMap = v;
                    rankDataListMap.keySet().parallelStream().forEach((Integer serverId) -> {
                        List<RankInfo> list = JSONObject
                                .parseArray(rankDataListMap.get(serverId).toString(), RankInfo.class);
                        String key = getRankKey(keysEnum, serverId);//排行榜key
                        RLock rlock = RedisManager.inst().getLock(key + ":" + Const.UP_RANK_LOCK);
                        try {
                            rlock.lock();
                            int size = list.size();
                            Map<Long, String> tmpPidRankMap = new HashMap<>();
                            Map<Long, RankInfo> tmpPidDataMap = new HashMap<>();
                            Map<Long, Map<String, Double>> tempPidScoreMap = new HashMap<>();
                            for (RankInfo rankData : list) {
                                String scoreKey = calculateScoreKey(rankData.getConditions(),
                                        rankData.getPid()); //重新设置新分数.如果存在多条件查询,则进行key值设置.
                                long score = rankData.getScore();
                                if (rankData.getRankEnum().getSortEnum() == RankSortEnum.ASC) {
                                    score = String.valueOf(score).length() > 10 ? score / 1000 : score; //TODO:优化数值长度
                                    score = Integer.MAX_VALUE - score;
                                }
                                Map<String, Double> tmpScoreMap = new HashMap<>();
                                tmpScoreMap.put(scoreKey, (double) score);
                                tempPidScoreMap.put(rankData.getPid(), tmpScoreMap);
                                tmpPidRankMap.put(rankData.getPid(), scoreKey);
                                tmpPidDataMap.put(rankData.getPid(), rankData);
                            }

                            RScoredSortedSet<String> scoredSortedSet = RedisManager.inst().getScoredSortedSet(key); //排行榜数据
                            RMap<Long, String> pidRankMap = RedisManager.inst().getMap(key + ":" + Const.PID_MAPPER_RANK_DATA);//有序队列排行key值
                            RMap<Long, RankInfo> rankMap = RedisManager.inst().getMap(key + ":" + Const.RANK_DATA);//排行榜数据

                            Collection<String> oldScoreKeys = pidRankMap.getAll(list.stream().map(RankInfo::getPid).collect(Collectors.toSet())).values();
                            scoredSortedSet.removeAll(oldScoreKeys);
                            long start = System.currentTimeMillis();
                            Map<String, Double> tmpMap = new HashMap<>();
                            tempPidScoreMap.forEach((x, c) -> {
                                tmpMap.putAll(c);
                            });
                            scoredSortedSet.addAll(tmpMap);
                            RankEnum rankEnum = splitKeysEnum(keysEnum);
                            int exceedMax = scoredSortedSet.size() - rankEnum.getNum();
                            if (exceedMax > 0) { //为了保证效率,可以先进行批量排序,在进行批量数据清除. 无须单个比较
                                //末位淘汰
                                Collection<String> pidConditionList = scoredSortedSet.valueRange(0, exceedMax - 1);
                                Set<Long> pidSet = new HashSet<>();
                                pidConditionList.forEach(pidStr -> {
                                    long pid = convert(pidStr);
                                    pidSet.add(pid);
                                });
                                scoredSortedSet.removeRangeByRank(0, exceedMax - 1);
                                Long[] delKey = new Long[pidSet.size()];
                                pidSet.toArray(delKey);
                                pidRankMap.fastRemove(delKey);
                                rankMap.fastRemove(delKey);
                                tmpPidRankMap.keySet().removeIf(pidSet::contains);
                                tmpPidDataMap.keySet().removeIf(pidSet::contains);
                            }
                            if (!tmpPidRankMap.isEmpty()) {
                                pidRankMap.putAll(tmpPidRankMap);//保存最新的玩家关系条件
                            }
                            if (!tmpPidDataMap.isEmpty()) {
                                rankMap.putAll(tmpPidDataMap);
                            }
                            long millisSecondsUsed = (System.currentTimeMillis() - start);
                            Log.RANK.debug("ordered queues consume time : {} ms", millisSecondsUsed);
                            localKeySet.add(key);
                            if (!rankKeyData.getRankKeySet().contains(key)) {
                                rankKeyData.getRankKeySet().add(key);
                                MongoManager.inst().save(rankKeyData);
                            }
                            localPidSet.addAll(tmpPidRankMap.keySet());

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            rlock.unlock();
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!localKeySet.isEmpty()) {
            keySet.addAll(localKeySet);
        }
    }

    /**
     * 更新排行信息
     *
     * @Author t13max
     * @Date 15:44 2024/10/31
     */
    public void updateRankList(Map<String, Map<Integer, List<RankInfo>>> rankDataMap) {
        if (!rankQueue.offer(JSONUtils.toJSONString(rankDataMap))) {
            Log.RANK.error("updateRankList error!");
        }
    }

    /**
     * 查询排行榜信息
     *
     * @Author t13max
     * @Date 15:44 2024/10/31
     */
    public List<RankViewInfo> viewRankInfo(RankInfoReq request) {
        Log.RANK.info("viewRankInfo:{}", request);

        RankEnum rankEnum = request.getRankEnum();
        String key = getRankKey(rankEnum, request.getParam1(), request.getServerId());
        RMap<String, List<RankViewInfo>> rankPidViewMap = RedisManager.inst().getMap(Const.VIEW_RANK_DATA);
        List<RankViewInfo> RankViewInfoList = rankPidViewMap.get(key);
        if (RankViewInfoList.isEmpty()) {
            return Collections.emptyList();
        }
        //只返回最大查询排名数据
        int show = rankEnum.getShow();
        List<RankViewInfo> newRankDataList = new ArrayList<>(show);
        int resultSize = RankViewInfoList.size();
        int loopCount = Math.min(resultSize, show);
        for (int i = 0; i < loopCount; i++) {
            try {
                newRankDataList.add(getRankViewInfo(RankViewInfoList.get(i)));
            } catch (Exception e) {
                Log.RANK.error(e.getMessage());
                e.printStackTrace();
            }
        }
        Log.RANK.info("result view rank data :{}", newRankDataList);
        return newRankDataList;
    }

    private RankViewInfo getRankViewInfo(RankViewInfo RankViewInfo) throws Exception {
        long pid = RankViewInfo.getRankData().getPid();
        Map<Long, PlayerBasicInfo> pbMap = PlayerBasicInfoManager.inst().searchPlayerBasicInfo(Collections.singleton(pid));
        if (!pbMap.isEmpty()) {
            RankViewInfo.setPlayerBasicInfo(pbMap.get(pid));
        } else {
            Log.RANK.error("player basic data is null : {}", pid);
            throw new Exception("player basic data is null : {}" + pid);
        }
        return RankViewInfo;
    }

    public OwnRankInfo selectRankOwn(RankOwnReq request) {
        Log.RANK.info("selectRankOwn:{}", request);
        OwnRankInfo ownRankData = new OwnRankInfo();
        try {
            String key = getRankKey(request.getRankEnum(), request.getParam1(), request.getServerId());
            RMap<String, List<RankViewInfo>> rankKeyViewMap = RedisManager.inst().getMap(Const.VIEW_RANK_DATA);
            List<RankViewInfo> rankViewInfoList = rankKeyViewMap.get(key);
            if (!rankViewInfoList.isEmpty()) {
                Optional<RankViewInfo> optional = rankViewInfoList.stream()
                        .filter(rankViewInfo -> rankViewInfo.getRankData().getPid() == request.getPlayerId()).findFirst();
                if (optional.isPresent()) {
                    RankViewInfo RankViewInfo = getRankViewInfo(optional.get());
                    ownRankData.setScore(RankViewInfo.getRankData().getScore());
                    ownRankData.setRank(RankViewInfo.getRank());
                    ownRankData.setPlayerBasicInfo(RankViewInfo.getPlayerBasicInfo());
                    return ownRankData;
                }
            }
            //不在快照得内,则直接查实时排名<快照中每次会已经产出了一定的过剩查询数量,防止排名显示误差>
            RScoredSortedSet<String> scoredSortedSet = RedisManager.inst().getScoredSortedSet(key);
            RMap<Long, String> pidRankMap = RedisManager.inst().getMap(key + ":" + Const.PID_MAPPER_RANK_DATA);
            String scoreKey = pidRankMap.get(request.getPlayerId());
            Integer rank = scoredSortedSet.revRank(scoreKey);
            Double score = scoredSortedSet.getScore(scoreKey);
            if (Objects.nonNull(rank) && Objects.nonNull(score)) {
                rank += 1;
            } else {
                rank = -1;
                score = 0.0;
            }
            ownRankData.setRank(rank);
            long pid = request.getPlayerId();
            ownRankData.setPlayerBasicInfo(PlayerBasicInfoManager.inst().searchPlayerBasicInfo(Collections.singleton(pid)).get(pid));
            ownRankData.setScore(score.longValue());
        } catch (Exception e) {
            Log.RANK.error(e.getMessage());
            e.printStackTrace();
        }
        Log.RANK.info("result own rank data :{}", ownRankData);
        return ownRankData;
    }

    private Long convert(String pidCondition) {
        return Long.parseLong(pidCondition.substring(pidCondition.lastIndexOf(":") + 1));
    }

    private RankEnum splitKeysEnum(String keysEnum) {
        String[] sb = keysEnum.split("-");
        return RankEnum.valueOf(sb[0]);
    }

    private RankEnum splitKeys(String key) {
        String[] sb = key.split(":");
        String[] tt = sb[1].split("-");
        return RankEnum.valueOf(tt[0]);
    }

    private String getRankKey(String keysEnum, int serverId) {
        String rankKey;
        if (serverId > 0) {
            rankKey = getRankDbCacheKey(keysEnum + "-" + serverId);
        } else {
            rankKey = getRankDbCacheKey(keysEnum);
        }
        return rankKey;
    }

    private String getRankKey(RankEnum rankEnum, int param1, int serverId) {
        StringBuilder key = new StringBuilder(rankEnum.name());
        if (param1 != -1) {
            key.append("-");
            key.append(param1);
        }
        if (serverId > 0) {
            key.append("-");
            key.append(serverId);
        }
        return getRankDbCacheKey(key.toString());
    }

    private String getRankDbCacheKey(String className) {
        return String.format("%s:%s", Const.RANK_DB_CACHE, className);
    }

    private String calculateScoreKey(List<RankCondition> rankConditionList, long pid) {
        //分数只保留10位以内
        String scoreKey;
        StringBuilder sb = new StringBuilder("");
        if (rankConditionList != null && rankConditionList.size() > 0) {
            int len = rankConditionList.size();
            long[] strList = new long[len];
            int i = 0;
            for (RankCondition rankCondition : rankConditionList) {
                if (rankCondition.getRankSortEnum() == RankSortEnum.ASC) {
                    long score =
                            String.valueOf(rankCondition.getScore()).length() > 10 ? rankCondition.getScore()
                                    / 1000 : rankCondition.getScore();
                    strList[i] = Integer.MAX_VALUE - score;
                } else {
                    strList[i] = rankCondition.getScore();
                }
                i++;
            }
            for (long sl : strList) {
                sb.append(padStart(String.valueOf(sl), 10, '0'));

            }
        } else {
            long val = Long.MAX_VALUE - pid; //TODO:如果没有排序条件，则对玩家pid进行升序排序
            sb.append(String.valueOf(val));
        }
        sb.append(":");
        scoreKey = sb.substring(0, sb.length()) + pid;
        return scoreKey;
    }

    private String padStart(String string, int minLength, char padChar) {
        if (string.length() >= minLength) {
            return string;
        } else {
            StringBuilder sb = new StringBuilder(minLength);

            for (int i = string.length(); i < minLength; ++i) {
                sb.append(padChar);
            }

            sb.append(string);
            return sb.toString();
        }
    }
}
