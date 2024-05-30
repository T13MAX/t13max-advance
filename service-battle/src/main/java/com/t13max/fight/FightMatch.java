package com.t13max.fight;

import battle.api.FightMatchUpdatePush;
import battle.entity.FightMatchPb;
import com.t13max.fight.enums.FightEnum;
import com.t13max.fight.enums.SmallRoundEnum;
import com.t13max.fight.event.*;
import com.t13max.fight.hero.FightHero;
import com.t13max.fight.member.IFightMember;
import com.t13max.fight.moveBar.ActionMoveBar;
import com.t13max.fight.moveBar.MoveBarUnit;
import com.t13max.fight.msg.DoActionArgs;
import com.t13max.fight.skill.IFightSkill;
import com.t13max.util.Log;
import com.t13max.util.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 战斗类
 *
 * @author: t13max
 * @since: 14:53 2024/4/10
 */
@Data
@NoArgsConstructor
public class FightMatch {

    private long id;

    private FightEnum fightEnum;

    private SmallRoundEnum smallRoundEnum;

    private Map<Long, IFightMember> memberMap = new HashMap<>();

    private Map<Long, FightHero> heroMap = new HashMap<>();

    private ActionMoveBar actionMoveBar;

    private FightHero curActionHero;

    private volatile DoActionArgs doActionArgs;

    private LinkedList<DoActionArgs> extraActionList = new LinkedList<>();

    private FightContext fightContext;

    private int round;

    private long lastFightStatusChangeMills;


    /**
     * 构造方法 只初始化一些基本的数据
     *
     * @Author t13max
     * @Date 15:59 2024/5/27
     */
    public FightMatch(long id) {
        this.id = id;
        this.fightEnum = FightEnum.INIT;
        this.smallRoundEnum = SmallRoundEnum.DETERMINE_NEXT_ACTION_UNIT;
        this.round = 0;
        this.lastFightStatusChangeMills = TimeUtil.nowMills();
    }

    public void tick() {

        Log.battle.debug("开始tick!");
        switch (this.fightEnum) {
            case INIT -> {
                //超时销毁
                if (checkTimeout(FightConst.WAIT_JOIN_TIMEOUT_MILLS)) {
                    changeFightState(FightEnum.FINISHED);
                    return;
                }
                //暂时没有其他操作 直接进入回合
                changeFightState(FightEnum.SMALL_ROUND);
            }
            case SMALL_ROUND -> {
                smallRoundTick();
            }
            case FOOT_UP -> {
                footUp();
            }
            case FINISHED -> {
                //不做处理 外部销毁
            }
        }
    }

    private void footUp() {

        fightContext.getFightEventBus().postEvent(new FootUpEvent(this));

        changeFightState(FightEnum.FINISHED);
    }

    private boolean checkTimeout(long mills) {
        long nowMills = TimeUtil.nowMills();
        if (nowMills - lastFightStatusChangeMills > mills) {
            return true;
        }
        return false;
    }

    private void changeFightState(FightEnum fightEnum) {
        this.fightEnum = fightEnum;
        this.lastFightStatusChangeMills = TimeUtil.nowMills();
    }

    private void smallRoundTick() {

        switch (smallRoundEnum) {
            case DETERMINE_NEXT_ACTION_UNIT -> {
                determineNextActionUnit();
            }
            case SMALL_ROUND_BEGIN -> {
                smallRoundBegin();
            }
            case WAIT_MANUAL -> {
                if (curActionHero.isAutoAction()) {
                    this.doActionArgs = curActionHero.runWithAI();
                    changeSmallRoundState(SmallRoundEnum.DO_ACTION);
                } else if (doActionArgs != null) {
                    changeSmallRoundState(SmallRoundEnum.DO_ACTION);
                } else if (checkTimeout(FightConst.ACTION_TIMEOUT_MILLS)) {
                    //超时了 AI代替出手
                    this.doActionArgs = curActionHero.runWithAI();
                    changeSmallRoundState(SmallRoundEnum.DO_ACTION);
                }
            }
            case DO_ACTION -> {

                if (this.doAction()) {
                    //成功出手
                    changeSmallRoundState(SmallRoundEnum.ACTION_UNIT_END);
                } else {
                    //错误处理
                }
            }
            case EXTRA_ACTION -> {
                if (doAction()) {
                    changeSmallRoundState(SmallRoundEnum.CHECK_EXTRA_ACTION);
                } else {
                    //错误处理
                }
            }
            case CHECK_EXTRA_ACTION -> {
                if (!this.extraActionList.isEmpty()) {
                    this.doActionArgs = extraActionList.removeFirst();
                    changeSmallRoundState(SmallRoundEnum.EXTRA_ACTION);
                } else {
                    changeSmallRoundState(SmallRoundEnum.SMALL_ROUND_END);
                }
            }
            case SKIP_ACTION -> {
                //暂时没有
            }
            case ACTION_UNIT_END -> {
                unitActionEnd();
                changeSmallRoundState(SmallRoundEnum.CHECK_EXTRA_ACTION);
            }
            case SMALL_ROUND_END -> {
                smallRoundEnd();
            }
            case JUDGE -> {
                judge();
            }
        }
    }

    private void judge() {
        if (tryFootUp()) {
            changeFightState(FightEnum.FOOT_UP);
        } else {
            changeSmallRoundState(SmallRoundEnum.DETERMINE_NEXT_ACTION_UNIT);
        }
    }

    private boolean tryFootUp() {

        //只剩下一方的人了
        boolean finished = heroMap.values().stream().allMatch(FightHero::isAttacker) || heroMap.values().stream().noneMatch(FightHero::isAttacker);

        if (finished) {
            return true;
        }

        //防止死循环
        if (round > 1000) return true;

        return false;
    }


    private void smallRoundEnd() {
        fightContext.getFightEventBus().postEvent(new SmallRoundEndEvent());
        fightContext.getFightTimeMachine().roll();
        this.changeSmallRoundState(SmallRoundEnum.JUDGE);
    }

    private void unitActionEnd() {
        //单位行动后 暂时没啥事要做
    }

    private boolean doAction() {

        long playerId = doActionArgs.getPlayerId();
        long heroId = doActionArgs.getHeroId();
        int skillId = doActionArgs.getSkillId();
        List<Long> targetIds = doActionArgs.getTargetIds();

        if (curActionHero.getFightMember().getId() != playerId || curActionHero.getId() != heroId) {
            Log.battle.error("行动失败, 参数错误, uid 或 heroId错误");
            return false;
        }

        IFightSkill fightSkill = curActionHero.getSkillManager().getFightSkill(skillId);
        if (fightSkill == null) {
            Log.battle.error("行动失败, 参数错误, skillId={}", skillId);
            return false;
        }

        //手动选择需要校验

        //行动开始前事件
        BeforeActionEvent beforeActionEvent = new BeforeActionEvent();
        fightContext.getFightEventBus().postEvent(beforeActionEvent);
        fightContext.getFightTimeMachine().roll();

        if (!curActionHero.isCanDoAction()) {
            return false;
        }

        //技能消耗和冷却
        if (!fightSkill.consumeCost()) {
            return false;
        }

        //行动事件
        DoActionEvent actionEvent = new DoActionEvent(heroId, curActionHero.isAttacker(), skillId, targetIds);
        fightContext.getFightEventBus().postEvent(actionEvent);

        //释放技能!
        curActionHero.emitSkill(skillId, targetIds, this);
        fightContext.getFightTimeMachine().roll();

        fightContext.getFightEventBus().postEvent(new AfterActionEvent());
        fightContext.getFightTimeMachine().roll();

        return true;
    }

    private void smallRoundBegin() {

        this.round++;

        MoveBarUnit actionMoveBarUnit = actionMoveBar.getUnit(curActionHero.getId());
        actionMoveBarUnit.doAction();

        //推送当前状态
        pushFightMatchPb();

        //抛出小回合开始事件
        fightContext.getFightEventBus().postEvent(new SmallRoundBeginEvent(curActionHero.getId(), round));
        fightContext.getFightTimeMachine().roll();

        //
        changeSmallRoundState(SmallRoundEnum.WAIT_MANUAL);
    }

    private void determineNextActionUnit() {
        curActionHero = null;
        curActionHero = getFastestUnit();
        doActionArgs = null;
        changeSmallRoundState(SmallRoundEnum.SMALL_ROUND_BEGIN);
    }

    private FightHero getFastestUnit() {
        this.actionMoveBar.roll();
        MoveBarUnit fastestUnit = this.actionMoveBar.getFastestUnit();
        if (fastestUnit == null) {
            Log.battle.error("FightImpl, getFastestUnit()为空");
            return null;
        }
        long heroId = fastestUnit.getHeroId();
        FightHero fightHero = this.heroMap.get(heroId);

        if (fightHero == null) {
            Log.battle.error("FightImpl, fightHero为空, fastestUnit={}", fastestUnit);
            return null;
        }

        return fightHero;
    }

    private void changeSmallRoundState(SmallRoundEnum nextSmallRoundState) {
        this.smallRoundEnum = nextSmallRoundState;
    }

    public FightHero getFightHero(long heroId) {
        return this.heroMap.get(heroId);
    }

    public void unitDead(FightHero fightHero) {
        long heroId = fightHero.getId();
        heroMap.remove(heroId);
        actionMoveBar.removeUnit(heroId);
    }

    public List<FightHero> getTargetHeroList(boolean attacker) {
        return this.heroMap.values().stream().filter(e -> e.isAttacker() != attacker).collect(Collectors.toList());
    }

    public void finish() {
        fightContext.getFightLogManager().printLog();
        Log.battle.info("FightMatch.finish, matchId={}", this.id);
    }

    private void pushFightMatchPb() {

        FightMatchUpdatePush.Builder builder = FightMatchUpdatePush.newBuilder();
        builder.setFightMatchPb(buildFightMatchPb());

        //push
    }

    private FightMatchPb buildFightMatchPb() {
        FightMatchPb.Builder builder = FightMatchPb.newBuilder();
        builder.setMatchId(this.id);
        for (IFightMember member : this.memberMap.values()) {
            builder.addPlayerData(member.buildFightPlayerInfoPb());
        }
        return builder.build();
    }
}
