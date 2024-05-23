package com.t13max.fight;

import com.t13max.fight.enums.FightEnum;
import com.t13max.fight.enums.SmallRoundEnum;
import com.t13max.fight.event.*;
import com.t13max.fight.log.FightLogManager;
import com.t13max.fight.moveBar.ActionMoveBar;
import com.t13max.fight.moveBar.MoveBarUnit;
import com.t13max.fight.skill.FightSkill;
import com.t13max.template.temp.TemplateSkill;
import com.t13max.util.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 14:53 2024/4/10
 */
@Data
@Log4j2
@NoArgsConstructor
public class FightMatch {

    private long id;

    private FightEnum fightEnum;

    private SmallRoundEnum smallRoundEnum;

    private Map<Long, FightHero> attacker;

    private Map<Long, FightHero> defender;

    private ActionMoveBar actionMoveBar;

    private FightHero curActionHero;

    private ActionArgs actionArgs;

    private LinkedList<ActionArgs> extraActionList = new LinkedList<>();

    private FightTimeMachine fightTimeMachine;

    private FightLogManager fightLogManager;

    private FightEventBus fightEventBus;

    private int round;

    private long lastFightStatusChangeMills;

    public FightMatch(long id) {
        this.id = id;
        this.fightEnum = FightEnum.INIT;
        this.smallRoundEnum = SmallRoundEnum.DETERMINE_NEXT_ACTION_UNIT;
        this.round = 0;
        this.fightEventBus = new FightEventBus();
        this.fightTimeMachine = new FightTimeMachine(this);
        this.fightLogManager = new FightLogManager();
        this.fightEventBus.register(this.fightLogManager);
        this.lastFightStatusChangeMills = TimeUtil.nowMills();
    }

    public void tick() {

        log.debug("开始tick!");
        switch (this.fightEnum) {
            case INIT -> {
                //超时销毁
                if (checkTimeout(30 * 1000)) {
                    changeFightState(FightEnum.FINISHED);
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

        fightEventBus.postEvent(new FootUpEvent(this));

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
                if (true) {
                    this.actionArgs = curActionHero.runWithAI();
                    changeSmallRoundState(SmallRoundEnum.DO_ACTION);
                } else if (actionArgs != null) {
                    changeSmallRoundState(SmallRoundEnum.DO_ACTION);
                } else if (checkTimeout(10 * 1000)) {
                    //超时了 AI代替出手
                    this.actionArgs = curActionHero.runWithAI();
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
                    this.actionArgs = extraActionList.removeFirst();
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

        if (this.attacker.isEmpty()) {
            return true;
        }

        if (this.defender.isEmpty()) {
            return true;
        }

        //防止死循环
        if (round > 1000) return true;

        return false;
    }


    private void smallRoundEnd() {
        fightEventBus.postEvent(new SmallRoundEndEvent());
        fightTimeMachine.roll();
        this.changeSmallRoundState(SmallRoundEnum.JUDGE);
    }

    private void unitActionEnd() {
        //单位行动后 暂时没啥事要做
    }

    private boolean doAction() {

        long playerId = actionArgs.getPlayerId();
        long heroId = actionArgs.getHeroId();
        int skillId = actionArgs.getSkillId();
        List<Long> targetIds = actionArgs.getTargetIds();

        if (curActionHero.getFightMember().getUid() != playerId || curActionHero.getId() != heroId) {
            log.error("行动失败, 参数错误");
            return false;
        }

        FightSkill skill = curActionHero.getSkillManager().getSkill(skillId);
        if (skill == null) {
            log.error("行动失败, 参数错误");
            return false;
        }

        TemplateSkill template = TemplateSkill.getTemplate(skillId);
        if (template == null) {
            log.error("行动失败, 参数错误");
            return false;
        }

        //手动选择需要校验

        //行动开始前事件
        BeforeActionEvent beforeActionEvent = new BeforeActionEvent();
        fightEventBus.postEvent(beforeActionEvent);
        fightTimeMachine.roll();

        if (!curActionHero.isCanDoAction()) {
            return false;
        }

        //技能消耗和冷却
        skill.consumeCost();
        skill.increaseCoolDown();

        //行动事件
        DoActionEvent actionEvent = new DoActionEvent(heroId, curActionHero.isAttacker(), skillId, targetIds);
        fightEventBus.postEvent(actionEvent);

        //释放技能!
        curActionHero.emitSkill(skillId, targetIds, this);
        fightTimeMachine.roll();

        fightEventBus.postEvent(new AfterActionEvent());
        fightTimeMachine.roll();

        return true;
    }

    private void smallRoundBegin() {

        this.round++;

        MoveBarUnit actionMoveBarUnit = actionMoveBar.getUnit(curActionHero.getId());
        actionMoveBarUnit.doAction();

        //抛出小回合开始事件
        fightEventBus.postEvent(new SmallRoundBeginEvent(curActionHero.getId(), round));
        fightTimeMachine.roll();

        //

        changeSmallRoundState(SmallRoundEnum.WAIT_MANUAL);
    }

    private void determineNextActionUnit() {
        curActionHero = null;
        curActionHero = getFastestUnit();
        actionArgs = null;
        changeSmallRoundState(SmallRoundEnum.SMALL_ROUND_BEGIN);
    }

    private FightHero getFastestUnit() {
        this.actionMoveBar.roll();
        MoveBarUnit fastestUnit = this.actionMoveBar.getFastestUnit();
        if (fastestUnit == null) {
            log.error("FightImpl, getFastestUnit()为空");
            return null;
        }
        long heroId = fastestUnit.getHeroId();
        FightHero fightHero;
        if (fastestUnit.isAttacker()) {
            fightHero = attacker.get(heroId);
        } else {
            fightHero = defender.get(heroId);
        }

        if (fightHero == null) {
            log.error("FightImpl, fightHero为空, fastestUnit={}", fastestUnit);
            return null;
        }

        return fightHero;
    }

    private void changeSmallRoundState(SmallRoundEnum nextSmallRoundState) {
        this.smallRoundEnum = nextSmallRoundState;
    }

    public FightHero getFightHero(long heroId, boolean attacker) {
        return getHeroMap(attacker).get(heroId);
    }

    public void unitDead(FightHero fightHero) {
        long heroId = fightHero.getId();
        Map<Long, FightHero> heroMap = this.getHeroMap(fightHero.isAttacker());
        heroMap.remove(heroId);
        actionMoveBar.removeUnit(heroId);
    }

    private Map<Long, FightHero> getHeroMap(boolean attacker) {
        if (attacker) {
            return this.attacker;
        } else {
            return this.defender;
        }
    }

    public void finish() {
        fightLogManager.printLog();
    }
}
