package com.t13max.fight.log;

import com.t13max.fight.FightHero;
import com.t13max.fight.FightImpl;
import com.t13max.fight.event.*;
import lombok.extern.log4j.Log4j2;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 17:09 2024/4/15
 */
@Log4j2
public class FightLogManager extends AbstractEventListener {

    public static final DecimalFormat DF = new DecimalFormat("#.00");

    private List<RoundLogEntity> logList = new LinkedList<>();

    private RoundLogEntity curRoundLogEntity;

    private FootUpLog footUpLog;

    public FightLogManager() {
        subscribeEvent(FightEventEnum.values());
    }

    public void printLog() {
        log.info("战斗日志");
        for (RoundLogEntity roundLogEntity : this.logList) {
            log.info(roundLogEntity.toString());
        }
        log.info(footUpLog);
        log.info("战斗日志结束");
    }

    public void forcePrint() {
        this.logList.add(curRoundLogEntity);
        printLog();
    }

    @Override
    public void onEvent(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_BEGIN -> {
                SmallRoundBeginEvent smallRoundBeginEvent = (SmallRoundBeginEvent) event;
                this.curRoundLogEntity = new RoundLogEntity();
                this.curRoundLogEntity.setRound(smallRoundBeginEvent.getRound());
                this.curRoundLogEntity.setActionHeroId(smallRoundBeginEvent.getHeroId());
            }
            case DO_ACTION -> {
                DoActionEvent doActionEvent = (DoActionEvent) event;
                this.curRoundLogEntity.setSkillId(doActionEvent.getSkillId());
                this.curRoundLogEntity.setTargetIds(doActionEvent.getTargetIds());
                this.curRoundLogEntity.setAttacker(doActionEvent.isAttacker());
            }
            case ATTRIBUTE_UPDATE -> {
                AttributeUpdateEvent attributeUpdateEvent = (AttributeUpdateEvent) event;
                this.curRoundLogEntity.getAttrUpdateLogs().add(new AttrUpdateLog(attributeUpdateEvent));
            }
            case UNIT_DEAD -> {
                UnitDeadEvent unitDeadEvent = (UnitDeadEvent) event;
                this.curRoundLogEntity.setDueToDeathList(unitDeadEvent.getDeadList());
            }
            case SMALL_ROUND_END -> {
                log.debug(curRoundLogEntity);
                this.logList.add(curRoundLogEntity);
                this.curRoundLogEntity = null;
            }
            case FOOT_UP -> {
                FootUpEvent footUpEvent = (FootUpEvent) event;
                FightImpl fight = footUpEvent.getFight();
                Map<Long, FightHero> winMap = fight.getAttacker();
                if (winMap.isEmpty()) {
                    winMap = fight.getDefender();
                }
                this.footUpLog = new FootUpLog(winMap);
            }
        }
    }

}
