package com.t13max.fight.log;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 17:00 2024/4/22
 */
@Data
public class RoundLogEntity implements Serializable {

    private int round;

    private long actionHeroId;

    private List<Long> targetIds;

    private int skillId;

    private boolean attacker;

    private List<Long> dueToDeathList;

    private Map<Long, AttrUpdateLog> attrUpdateLogs = new HashMap<>();


    @Override
    public String toString() {
        String attackerStr = attacker ? LogConst.ATTACKER : LogConst.DEFENDER;
        AttrUpdateLog attrUpdateLog = attrUpdateLogs.get(actionHeroId);
        String dueTo = "";
        if (attrUpdateLog != null) {
            dueTo = "导致:" + attrUpdateLog + ", 造成" + dueToDeathList + "死亡";
        }
        return "第" + round + "回合, " + attackerStr + actionHeroId + "对" + targetIds + "发动了" + skillId + dueTo + "attrUpdate=" + attrUpdateLogs;
    }
}
