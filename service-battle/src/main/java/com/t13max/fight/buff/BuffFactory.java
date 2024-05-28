package com.t13max.fight.buff;

import com.t13max.fight.FightContext;
import com.t13max.fight.buff.condition.IEventCondition;
import com.t13max.fight.buff.effect.AbstractEffect;
import com.t13max.fight.buff.effect.BuffEffectEnum;
import com.t13max.fight.buff.effect.IBuffEffect;
import com.t13max.util.Log;
import com.t13max.util.UuidUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * buff盒子和效果工厂
 *
 * @author: t13max
 * @since: 11:42 2024/4/23
 */
@UtilityClass
@Log4j2
public class BuffFactory {

    public BuffBoxImpl createBuffBoxImpl(FightContext fightContext, long ownerId, String param) {
        return createBuffBoxImpl(fightContext, ownerId, Integer.parseInt(param));
    }

    public BuffBoxImpl createBuffBoxImpl(FightContext fightContext, long ownerId, int buffId) {

        BuffBoxImpl buffBox = new BuffBoxImpl();
        buffBox.setId(UuidUtil.getNextId());
        buffBox.setFightContext(fightContext);
        buffBox.setOwnerId(ownerId);
        buffBox.setBuffId(buffId);

        try {

            buffBox.onCreate();
        } catch (Exception e) {
            Log.battle.error("createBuffBoxImpl, error={}", e.getMessage());
            return null;
        }
        return buffBox;
    }

    public IBuffEffect createBuffEffect(IBuffBox buffBox, int effectId, String param, String activeCondition, String disposedCondition) {
        BuffEffectEnum buffEffectEnum = BuffEffectEnum.getEffect(effectId);
        if (buffEffectEnum == null) {
            return null;
        }

        AbstractEffect result = null;
        try {
            Constructor<? extends IBuffEffect> constructor = buffEffectEnum.getClazz().getConstructor();
            result = (AbstractEffect) constructor.newInstance();
        } catch (Exception exception) {
            Log.battle.error("buffEffect创建失败");
            exception.printStackTrace();
            return null;
        }
        result.setParam(param);
        result.setBuffBox(buffBox);

        //填充 常规生效消散条件 (还有一些特殊条件是自己实现的 不在通用部分) 目前没有复杂的条件处理 只是多个或关系的用;分隔 一个条件用,分隔id和参数 后续优化
        List<IEventCondition> activeConditionList = ConditionFactory.createEventConditionList(activeCondition);
        result.getActiveConditions().addAll(activeConditionList);
        List<IEventCondition> dispossedConditionList = ConditionFactory.createEventConditionList(disposedCondition);
        result.getDisposedConditions().addAll(dispossedConditionList);


        return result;
    }


}


/**
 * private final static Map<Integer, IEventCondition> CONDITION_MAP = new HashMap<>();
 * <p>
 * static {
 * Set<Class<?>> classSet = PackageUtil.scan("com.t13max.fight.buff.condition");
 * for (Class<?> clazz : classSet) {
 * if (!IEventCondition.class.isAssignableFrom(clazz)) {
 * continue;
 * }
 * <p>
 * if (clazz.isInterface()) {
 * continue;
 * }
 * <p>
 * IEventCondition condition = null;
 * try {
 * Constructor<?> constructor = clazz.getConstructor();
 * condition = (IEventCondition) constructor.newInstance();
 * } catch (Exception e) {
 * throw new BattleException(e);
 * }
 * <p>
 * CONDITION_MAP.put(condition.getConditionEnum().getId(), condition);
 * }
 * }
 * <p>
 * public static IEventCondition getCondition(int id) {
 * return CONDITION_MAP.get(id);
 * }
 */
