package com.t13max.game.feature.activity;

import com.t13max.common.manager.ManagerBase;
import com.t13max.game.feature.activity.enums.ActModelEnum;
import com.t13max.game.feature.activity.enums.ActOpenEnum;
import com.t13max.game.player.Player;
import com.t13max.game.player.PlayerManager;
import com.t13max.game.util.Log;
import com.t13max.template.helper.ActivityHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateActivity;
import com.t13max.util.TimeUtil;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: t13max
 * @since: 10:53 2024/6/5
 */
public class ActivityManager extends ManagerBase {

    private final ScheduledExecutorService activityExecutor = Executors.newSingleThreadScheduledExecutor();

    //当前开启的活动
    @Getter
    private final Map<Integer, Long> activeAct = new ConcurrentHashMap<>();
    //已关闭的活动
    @Getter
    private final Map<Integer, Long> closedAct = new ConcurrentHashMap<>();

    public static ActivityManager inst() {
        return inst(ActivityManager.class);
    }

    @Override
    protected void onShutdown() {
        activityExecutor.shutdown();
    }

    @Override
    public List<Class<? extends ManagerBase>> getDependents() {
        //依赖读表模块
        return Collections.singletonList(TemplateManager.class);
    }

    @Override
    public void init() {
        long nowMills = TimeUtil.nowMills();
        //遍历所有活动
        for (TemplateActivity templateActivity : TemplateManager.inst().helper(ActivityHelper.class).getAll()) {
            long startMills = calcStartMills(templateActivity);
            long endMills = calcEndMills(templateActivity);

            if (endMills <= startMills) {
                Log.game.error("活动时间配置错误, 结束时间早于开始时间, actId={}", templateActivity.getId());
                continue;
            }
            //活动已经结束了 忽略
            if (endMills < nowMills) {
                closedAct.put(templateActivity.getId(), calcEndMills(templateActivity));
                continue;
            }
            long startDelay = startMills - nowMills;
            long endDelay = endMills - nowMills;

            //未来开启
            if (startDelay > 0) {
                activityExecutor.schedule(() -> this.onStart(templateActivity), startDelay, TimeUnit.MILLISECONDS);
                activityExecutor.schedule(() -> this.onEnd(templateActivity), endDelay, TimeUnit.MILLISECONDS);
            } else {
                //已经开了但是还没结束
                this.onStart(templateActivity);
                activityExecutor.schedule(() -> this.onEnd(templateActivity), endDelay, TimeUnit.MILLISECONDS);
            }
        }
    }

    private void onStart(TemplateActivity templateActivity) {
        ActModelEnum actModelEnum = ActModelEnum.getActModelEnum(templateActivity.type);
        if (actModelEnum == null) {
            Log.game.error("活动开启失败, model不存在, act={}, model={}", templateActivity.getId(), templateActivity.type);
            return;
        }

        activeAct.put(templateActivity.getId(), this.calcEndMills(templateActivity));

        for (Player player : PlayerManager.inst().getOnlinePlayerList()) {
            actModelEnum.getActModel().onStart(player, templateActivity);
        }
    }

    private void onEnd(TemplateActivity templateActivity) {
        ActModelEnum actModelEnum = ActModelEnum.getActModelEnum(templateActivity.type);
        if (actModelEnum == null) {
            Log.game.error("活动结束失败, model不存在, act={}, model={}", templateActivity.getId(), templateActivity.type);
            return;
        }

        activeAct.remove(templateActivity.getId());

        for (Player player : PlayerManager.inst().getOnlinePlayerList()) {
            actModelEnum.getActModel().onEnd(player, templateActivity);
        }
    }

    public boolean checkActOpen(int actId) {
        return activeAct.containsKey(actId);
    }

    public long calcEndMills(TemplateActivity templateActivity) {
        return parseMills(templateActivity, false);
    }

    public long calcStartMills(TemplateActivity templateActivity) {
        return parseMills(templateActivity, true);
    }

    public long parseMills(TemplateActivity templateActivity, boolean start) {
        long mills = -1;
        ActOpenEnum actOpenEnum = ActOpenEnum.getActOpenEnum(templateActivity.openType);
        if (actOpenEnum == null) {
            return mills;
        }
        String startTime = start ? templateActivity.startTime : templateActivity.endTime;

        switch (actOpenEnum) {
            case WEEK_DAY -> {
                //待实现 懒得写
                /*String startTime = templateActivity.getStartTime();
                int weekDay = Integer.parseInt(startTime);
                TimeUtil.getSpecificDayTime(DayOfWeek.of(weekDay));*/
            }
            case DATE -> {
                mills = TimeUtil.parseTime(startTime);
            }
        }
        return mills;
    }
}
