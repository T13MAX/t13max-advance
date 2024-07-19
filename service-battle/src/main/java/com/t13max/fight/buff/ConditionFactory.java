package com.t13max.fight.buff;

import com.t13max.fight.buff.condition.ConditionEnum;
import com.t13max.fight.buff.condition.IEventCondition;
import com.t13max.game.util.Log;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;

/**
 * 条件工厂类
 *
 * @author: t13max
 * @since: 11:54 2024/4/23
 */
@UtilityClass
public class ConditionFactory {

    public List<IEventCondition> createEventConditionList(String conditionListStr) {

        List<IEventCondition> result = new LinkedList<>();

        String[] split = conditionListStr.split(";");
        for (String string : split) {
            IEventCondition eventCondition = createEventCondition(string);
            if (eventCondition == null) continue;
            result.add(eventCondition);
        }

        return result;
    }

    public IEventCondition createEventCondition(String conditionStr) {
        String[] split = conditionStr.split(",");
        int id = 0;
        String param = null;
        if (split.length > 0) {
            id = Integer.parseInt(split[0]);
        }
        //大于10的不处理 只处理基础类型
        if (id >= 10) {
            return null;
        }
        if (split.length > 1) {
            param = split[1];
        }
        ConditionEnum conditionEnum = ConditionEnum.getConditionEnum(id);
        if (conditionEnum == null) {
            Log.battle.error("ConditionEnum不存在, id={}", id);
            return null;
        }
        IEventCondition condition = null;
        try {
            condition = conditionEnum.createCondition(param);
        } catch (Exception e) {
            Log.battle.error("ConditionEnum创建失败, error={}", e.getMessage());
        }
        return condition;
    }

}
