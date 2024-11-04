package com.t13max.match.manager;

import com.alibaba.fastjson2.JSONObject;
import com.t13max.common.config.BaseConfig;
import com.t13max.common.manager.ManagerBase;
import com.t13max.common.run.Application;
import com.t13max.data.redis.RedisManager;
import com.t13max.match.consts.MatchEnum;
import com.t13max.game.msg.ErrorCode;
import com.t13max.match.consts.Const;
import com.t13max.match.entity.MatchServiceInfo;
import com.t13max.match.interfaces.IMatchPlayer;
import com.t13max.match.util.Log;
import com.t13max.util.TimeUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 匹配管理器
 * 目前一个实例管理多个匹配 一个匹配无法分区段交给多个实例 待优化
 *
 * @author t13max
 * @since 16:21 2024/11/4
 */
public class MatchManager extends ManagerBase {

    //匹配线程池
    private ScheduledExecutorService matchExecutor;
    //缓存 防止多次访问redis
    private Map<Integer, Set<Integer>> dimensionCache = new HashMap<>();
    //当前实例信息 为空则没初始化
    private MatchServiceInfo matchServiceInfo;
    //是否已注册
    private boolean register;
    //是否停止
    private volatile boolean stop;

    public static MatchManager inst() {
        return inst(MatchManager.class);
    }


    protected void onShutdown() {
        this.stop = true;
        Log.MATCH.debug("匹配, 执行destroy逻辑");
        matchExecutor.shutdown();
        signOut();
        for (MatchEnum matchEnum : MatchEnum.values()) {
            matchEnum.getMatch().shutdown();
        }
    }


    protected void init() {
        Log.MATCH.info("匹配, 已开启匹配");
        matchExecutor = Executors.newSingleThreadScheduledExecutor();
        matchExecutor.scheduleAtFixedRate(this::schedule, Const.DELAY, Const.PERIOD, TimeUnit.SECONDS);
    }


    /**
     * 定时执行
     *
     * @Author t13max
     * @Date 11:41 2024/4/28
     */
    private void schedule() {

        if (stop) return;

        try {

            if (!register) {
                //尝试注册
                if (!register()) {
                    return;
                }
                Log.MATCH.info("匹配, 注册本服务器成功, 可以执行匹配逻辑");
            }

            RBucket<Long> distributeTimeBucket = RedisManager.inst().getBucket(Const.DISTRIBUTE_TIME);
            if (!distributeTimeBucket.isExists()) {
                distribute(true);
                long distributeTime = TimeUtil.nowMills();
                distributeTimeBucket.set(distributeTime);
            }

            long lastMills = distributeTimeBucket.get();

            long nowMills = System.currentTimeMillis();

            //一定间隔尝试执行一次
            if (nowMills - lastMills > Const.DISTRIBUTE_INTERVAL) {
                distribute();
                distributeTimeBucket.set(nowMills);
            }

            //匹配
            doMatch();
        } catch (Exception exception) {
            exception.printStackTrace();
            //异常处理 可能是redis炸了之类的
        }
    }

    /**
     * 注册
     *
     * @Author t13max
     * @Date 16:02 2024/4/28
     */
    private boolean register() {

        if (this.matchServiceInfo == null) {
            BaseConfig config = Application.config();
            MatchServiceInfo matchServiceInfo = new MatchServiceInfo();
            matchServiceInfo.setServiceName(config.getInstanceName());
            this.matchServiceInfo = matchServiceInfo;
        }

        //注册进redis 表明自己可以执行匹配逻辑了
        RMap<String, MatchServiceInfo> serviceMap = RedisManager.inst().getMap(Const.SERVICE_KEY);
        serviceMap.put(matchServiceInfo.getServiceName(), matchServiceInfo);
        this.register = true;
        return true;
    }

    /**
     * 注销
     * 这里是正常注销 如果服务异常暴毙 会有其他服务帮忙注销
     *
     * @Author t13max
     * @Date 16:23 2024/4/28
     */
    private void signOut() {
        //正常注销
        signOut(matchServiceInfo.getServiceName());
    }

    private void signOut(String serviceName) {
        RMap<String, MatchServiceInfo> serviceMap = RedisManager.inst().getMap(Const.SERVICE_KEY);
        serviceMap.remove(serviceName);
    }

    private void signOut(List<String> serviceNameList) {
        RMap<String, MatchServiceInfo> serviceMap = RedisManager.inst().getMap(Const.SERVICE_KEY);
        for (String name : serviceNameList) {
            serviceMap.remove(name);
        }
    }

    /**
     * 匹配
     *
     * @Author t13max
     * @Date 13:38 2024/4/28
     */
    private void doMatch() {

        Log.MATCH.debug("匹配, 匹配开始!");
        RMap<String, MatchServiceInfo> serviceMap = RedisManager.inst().getMap(Const.SERVICE_KEY);
        MatchServiceInfo serviceInfo = serviceMap.get(this.matchServiceInfo.getServiceName());

        long nowMills = TimeUtil.nowMills();
        serviceInfo.setLastUpdateMills(nowMills);

        //本地缓存
        this.matchServiceInfo = serviceInfo;
        serviceMap.put(serviceInfo.getServiceName(), serviceInfo);

        Log.MATCH.debug("匹配, 匹配中, matchServiceInfo更新完毕, serviceInfo={}", serviceInfo);

        //这个实例所管理的多个匹配以此执行
        for (Integer id : matchServiceInfo.getMatchEnumList()) {
            MatchEnum matchEnum = MatchEnum.getMatchEnum(id);
            RMap<Integer, Set<Integer>> dimensionMap = RedisManager.inst().getMap(Const.DIMENSION);
            Set<Integer> scopeList = dimensionMap.get(matchEnum.getId());
            //排一下序 倒序
            List<Integer> sortList = new ArrayList<>(scopeList);
            sortList.sort((e1, e2) -> e2 - e1);
            scopeList.forEach(e -> handleTimeoutPlayers(matchEnum));
            //匹配
            scopeList.forEach(e -> matchHandle(matchEnum, e));

        }
    }

    /**
     * 一种玩法的处理
     *
     * @Author t13max
     * @Date 11:45 2024/4/29
     */
    private void matchHandle(MatchEnum matchEnum, int scope) {
        Log.MATCH.debug("匹配, 根据scope匹配玩家");
        List<Long> tmpMtchedList = new LinkedList<>();
        matchHandleByScope(matchEnum, scope, tmpMtchedList);
        if (!tmpMtchedList.isEmpty()) {
            //降级匹配 要不要对scope做一下合法性校验呢??
            matchHandleByScope(matchEnum, scope - 1, tmpMtchedList);
            //还为空 打机器人吧
            if (!tmpMtchedList.isEmpty()) {
                matchRobot(matchEnum, scope, tmpMtchedList);
            }
        }
    }

    /**
     * 超时处理
     *
     * @Author t13max
     * @Date 13:40 2024/4/29
     */
    private void handleTimeoutPlayers(MatchEnum matchEnum) {
        Log.MATCH.debug("匹配, 处理超时玩家");
        //对应玩法的超时处理
        matchEnum.getMatch().handleTimeout();
    }

    /**
     * 匹配机器人
     *
     * @Author t13max
     * @Date 13:43 2024/4/29
     */
    private void matchRobot(MatchEnum matchEnum, int scope, List<Long> tmpMtchedList) {
        List<IMatchPlayer> playerInfoList = new ArrayList<>();
        for (Long playerId : tmpMtchedList) {
            RBucket<IMatchPlayer> playerInfoBucket = RedisManager.inst().getBucket(getPlayerInfoKey(playerId));
            playerInfoList.add(playerInfoBucket.get());
        }
        matchEnum.getMatch().matchRobot(scope, playerInfoList);
    }

    /**
     * 处理某一分段的玩家
     *
     * @Author t13max
     * @Date 11:51 2024/4/29
     */
    private void matchHandleByScope(MatchEnum matchEnum, int scope, List<Long> tmpMtchedList) {

        String matchSetKey = getMatchSetKey(matchEnum, scope);

        //是否为降级匹配
        boolean downMatch = !tmpMtchedList.isEmpty();

        RScoredSortedSet<Long> scoredSortedSet = RedisManager.inst().getScoredSortedSet(matchSetKey);
        int size = scoredSortedSet.size();

        Collection<Long> ids;
        if (downMatch) {
            //期望取出的数量
            int popNum = matchEnum.getNum() - tmpMtchedList.size();
            //不够就算了
            if (size < popNum) return;
            ids = scoredSortedSet.pollFirst(popNum);
        } else {
            //不够就算了
            if (size < matchEnum.getNum()) return;
            ids = scoredSortedSet.pollFirst(size);
        }

        for (Long playerId : ids) {
            tmpMtchedList.add(playerId);
            if (tmpMtchedList.size() == matchEnum.getNum()) {
                //匹配成功咯
                this.matchSuccess(matchEnum, scope, tmpMtchedList);
            }
        }

    }

    /**
     * 一组匹配成功
     *
     * @Author t13max
     * @Date 14:22 2024/4/28
     */
    private void matchSuccess(MatchEnum matchEnum, int scope, List<Long> playerIds) {

        Log.MATCH.debug("匹配, 匹配成功 恭喜{}牵手成功!", playerIds);

        //取得对应的匹配信息
        List<IMatchPlayer> playerInfoList = new ArrayList<>();
        String[] deleteKeys = new String[2];
        int i = 0;
        for (Long playerId : playerIds) {
            deleteKeys[i++] = String.valueOf(playerId);
            RBucket<IMatchPlayer> playerInfoBucket = RedisManager.inst().getBucket(getPlayerInfoKey(playerId));

            if (!playerInfoBucket.isExists()) continue;
            playerInfoList.add(playerInfoBucket.get());
        }

        //删掉玩家匹配数据
        for (Long playerId : playerIds) {
            RBucket<IMatchPlayer> bucket = RedisManager.inst().getBucket(getPlayerInfoKey(playerId));
            bucket.delete();
        }
        RMap<Long, Long> timeoutMap = RedisManager.inst().getMap(Const.MATCH_TIMEOUT);
        for (Long playerId : playerIds) {
            timeoutMap.remove(playerId);
        }

        //清空这个集合
        playerIds.clear();
        //根据playerIds获取玩家信息
        matchEnum.getMatch().matchSuccess(playerInfoList);
    }

    /**
     * 分配任务
     *
     * @Author t13max
     * @Date 13:35 2024/4/28
     */
    private void distribute(boolean first) {

        String selfServiceName = matchServiceInfo.getServiceName();

        RBucket<String> mainServiceNameBucket = RedisManager.inst().getBucket(Const.MAIN_SERVICE_NAME);
        String mainServiceName = mainServiceNameBucket.get();

        long timeoutMills = Const.DISTRIBUTE_INTERVAL + Const.DISTRIBUTE_INTERVAL_REDUNDANCY;//比间隔长一点 让主连任

        if (mainServiceName == null) {
            //为空 是第一次或者是过期了 进行争夺

            if (!mainServiceNameBucket.trySet(selfServiceName, timeoutMills, TimeUnit.MILLISECONDS)) {
                //没争夺到 算了
                Log.MATCH.debug("匹配, 分配任务, 没争夺到锁, 算了");
                return;
            } else {
                Log.MATCH.debug("匹配, 分配任务, 成功抢到了锁, 登基!");
            }
        } else if (mainServiceName.equals(selfServiceName)) {
            //不为空 自己是主 则分配任务 延长时间
            if (mainServiceNameBucket.expire(timeoutMills, TimeUnit.MILLISECONDS)) {
                //延长成功
            } else {
                //失败了
                return;
            }
        } else {
            Log.MATCH.debug("匹配, 分配任务, 不为空, 自己也不是主, 跳过");
            return;
        }

        //真正分配任务逻辑
        doDistribute(first);
    }

    /**
     * 真正分配任务逻辑
     *
     * @Author t13max
     * @Date 11:29 2024/6/12
     */
    private void doDistribute(boolean first) {
        Log.MATCH.debug("匹配, 执行分配逻辑");

        Map<String, MatchServiceInfo> serviceMap = new HashMap<>();
        List<String> nameList = new ArrayList<>();
        List<String> removeList = new LinkedList<>();

        RMap<Object, MatchServiceInfo> serviceObjMap = RedisManager.inst().getMap(Const.SERVICE_KEY);
        RMap<Object, Object> dimensionMap = RedisManager.inst().getMap(Const.DIMENSION);

        try {

            long nowMills = System.currentTimeMillis();
            for (Object value : serviceObjMap.values()) {
                if (value instanceof String matchServiceStr) {
                    MatchServiceInfo matchServiceInfo = JSONObject.parseObject(matchServiceStr, MatchServiceInfo.class);
                    //这么长时间没干活了 挂了吧
                    if (nowMills - matchServiceInfo.getLastUpdateMills() > Const.SERVICE_TIME_OUT && !first) {
                        removeList.add(matchServiceInfo.getServiceName());
                    } else {
                        nameList.add(matchServiceInfo.getServiceName());
                        matchServiceInfo.getMatchEnumList().clear();
                        serviceMap.put(matchServiceInfo.getServiceName(), matchServiceInfo);
                    }
                } else {
                    //理论上 走不到这
                    Log.MATCH.error("谁往SERVICE_KEY里面放{}了?!", value == null ? null : value.getClass());
                }
            }

            if (nameList.isEmpty()) {
                return;
            }

            int index = 0;
            for (Object dimensionObj : dimensionMap.keySet()) {
                if (dimensionObj instanceof String dimensionStr) {
                    //轮一圈了 从头开始
                    if (index >= nameList.size()) index = 0;
                    //解析
                    Integer id = JSONObject.parseObject(dimensionStr, Integer.class);

                    String serviceName = nameList.get(index);
                    //理论上 他不可能为空
                    MatchServiceInfo matchServiceInfo = serviceMap.get(serviceName);
                    matchServiceInfo.getMatchEnumList().add(id);
                    index++;
                } else {
                    //理论上 他一定是字符串
                    Log.MATCH.error("肿么可能 他竟然不是字符串??? {}", dimensionObj.getClass().getSimpleName());
                }
            }

            //不对劲的删掉
            for (String serviceName : removeList) {
                serviceMap.remove(serviceName);
            }

        } catch (Exception e) {
            Log.MATCH.error("匹配, 执行分配任务出错, error={}", e.getMessage());
        } finally {
            try {
                signOut(removeList);
                //批量弄进去
                serviceObjMap.putAll(serviceMap);

            } catch (Exception e) {
                Log.MATCH.error("匹配, 执行分配任务后, finally执行出错, error={}", e.getMessage());
            }
        }
    }

    private void distribute() {

        distribute(false);
    }

    /**
     * 获取玩家匹配信息的key
     *
     * @Author t13max
     * @Date 15:13 2024/4/28
     */

    public String getPlayerInfoKey(long playerId) {
        return Const.MATCH_PLAYER_INFO + ":" + playerId;
    }

    private String getPlayerInfoKey(String playerIdStr) {
        return Const.MATCH_PLAYER_INFO + ":" + playerIdStr;
    }

    /**
     * 获取set的key
     *
     * @Author t13max
     * @Date 11:56 2024/4/29
     */

    public String getMatchSetKey(MatchEnum matchEnum, int scope) {
        return Const.MATCH_SET + ":" + matchEnum.getId() + ":" + scope;
    }

    /**
     * 加入匹配
     *
     * @Author t13max
     * @Date 11:37 2024/4/28
     */

    public ErrorCode joinMatch(IMatchPlayer matchPlayer) {

        try {
            MatchEnum matchEnum = matchPlayer.getMatchEnum();
            int scope = matchPlayer.getScope();
            if (matchPlayer.isDirectRobot()) {
                directRobot(matchPlayer);
            } else {
                doJoinMatch(matchPlayer);
            }
            //刷新scope
            refreshScope(matchEnum, scope);
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.FAIL;
        }

        return ErrorCode.SUCCESS;
    }

    /**
     * 加入匹配 缓存玩家匹配数据 加入到zset
     *
     * @Author t13max
     * @Date 15:46 2024/4/28
     */
    private void doJoinMatch(IMatchPlayer matchPlayer) {
        long playerId = matchPlayer.getPlayerId();
        MatchEnum matchEnum = matchPlayer.getMatchEnum();
        int scope = matchPlayer.getScope();
        double score = matchPlayer.getScore();

        long currentTimeMillis = TimeUtil.nowMills();

        //理论上一个玩家同时只匹配一个玩法 设置三分钟过期
        RBucket<IMatchPlayer> playerInfoBucket = RedisManager.inst().getBucket(getPlayerInfoKey(playerId));
        playerInfoBucket.set(matchPlayer, Const.DATA_TIME_OUT, TimeUnit.MILLISECONDS);

        //设置超时时间
        RMap<Long, Long> timeoutMap = RedisManager.inst().getMap(Const.MATCH_TIMEOUT);
        timeoutMap.put(playerId, currentTimeMillis);
        //添加到对应玩法的对应分段匹配池
        RScoredSortedSet<Long> scoredSortedSet = RedisManager.inst().getScoredSortedSet(getMatchSetKey(matchEnum, scope));
        scoredSortedSet.add(score, playerId);
    }

    /**
     * 刷新scope
     *
     * @Author t13max
     * @Date 15:39 2024/6/11
     */
    private void refreshScope(MatchEnum matchEnum, int scope) {
        //当前存在的匹配和对应的分段们
        Set<Integer> scopeList = dimensionCache.get(matchEnum.getId());
        if (scopeList == null || scopeList.isEmpty()) {
            scopeList = new HashSet<>();
            dimensionCache.put(matchEnum.getId(), scopeList);
        }
        if (scopeList.contains(scope)) {
            return;
        }
        scopeList.add(scope);
        RMap<Integer, Set<Integer>> dimensionMap = RedisManager.inst().getMap(Const.DIMENSION);
        dimensionMap.put(matchEnum.getId(), scopeList);
    }

    /**
     * 停止匹配
     *
     * @Author t13max
     * @Date 13:41 2024/4/29
     */

    public ErrorCode stopMatch(long uuid) {

        ErrorCode errorCode;

        try {
            //停止匹配的逻辑
            errorCode = doStopMatch(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            errorCode = ErrorCode.FAIL;
        }

        return errorCode;
    }

    /**
     * 取消匹配
     *
     * @Author t13max
     * @Date 14:24 2024/4/29
     */
    private ErrorCode doStopMatch(long playerId) {

        RBucket<IMatchPlayer> matchPlayerBucket = RedisManager.inst().getBucket(getPlayerInfoKey(playerId));

        if (!matchPlayerBucket.isExists()) {
            return ErrorCode.FAIL;
        }

        IMatchPlayer matchPlayer = matchPlayerBucket.get();
        if (matchPlayer == null) {
            return ErrorCode.FAIL;
        }

        matchPlayer.getMatchEnum().getMatch().cancelMatch(playerId);

        String matchSetKey = getMatchSetKey(matchPlayer.getMatchEnum(), matchPlayer.getScope());
        //尝试移除
        RScoredSortedSet<Object> scoredSortedSet = RedisManager.inst().getScoredSortedSet(matchSetKey);

        if (!scoredSortedSet.remove(playerId)) {
            return ErrorCode.FAIL;
        }

        matchPlayerBucket.delete();

        return ErrorCode.SUCCESS;
    }

    /**
     * 直接对战机器人 不加入匹配
     *
     * @Author t13max
     * @Date 13:39 2024/5/20
     */
    private void directRobot(IMatchPlayer matchPlayer) {
        RBucket<IMatchPlayer> playerBucket = RedisManager.inst().getBucket(getPlayerInfoKey(matchPlayer.getPlayerId()));
        playerBucket.set(matchPlayer);
        //过期时间设置特殊值
        RMap<Long, Long> timeoutMap = RedisManager.inst().getMap(Const.MATCH_TIMEOUT);
        timeoutMap.put(matchPlayer.getPlayerId(), Const.DIRECT_ROBOT_SPECIAL_VALUE);
    }

}
