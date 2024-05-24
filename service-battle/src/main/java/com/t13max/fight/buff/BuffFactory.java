package com.t13max.fight.buff;

import com.t13max.fight.FightHero;
import com.t13max.fight.buff.condition.Condition_0_Active;
import com.t13max.fight.buff.condition.Condition_2_Element;
import com.t13max.fight.buff.condition.IEventCondition;
import com.t13max.fight.buff.effect.AbstractEffect;
import com.t13max.fight.buff.effect.BuffEffectEnum;
import com.t13max.fight.buff.effect.IBuffEffect;
import com.t13max.template.helper.BuffHelper;
import com.t13max.template.helper.SkillHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateBuff;
import com.t13max.template.util.ParseUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 11:42 2024/4/23
 */
@UtilityClass
@Log4j2
public class BuffFactory {

    private final static Map<Integer, IEventCondition> CONDITION_MAP = new HashMap<>();

    static {
        //先不扫包了 手动创建吧
        CONDITION_MAP.put(0, new Condition_0_Active());
        CONDITION_MAP.put(2, new Condition_2_Element());

    }

    public static IEventCondition getCondition(int id) {
        return CONDITION_MAP.get(id);
    }

    public BuffBoxImpl createBuffBoxImpl(FightHero owner, String param) {
        return createBuffBoxImpl(owner, ParseUtil.getIntList(param));
    }

    public BuffBoxImpl createBuffBoxImpl(FightHero owner, List<Integer> buffIds) {

        BuffBoxImpl buffBox = new BuffBoxImpl();
        buffBox.setOwner(owner);

        for (Integer buffId : buffIds) {
            BuffHelper buffHelper = TemplateManager.inst().helper(BuffHelper.class);
            TemplateBuff template = buffHelper.getTemplate(buffId);
            if (template == null) {
                log.error("createBuffBoxImpl, buff不存在, buffId={}", buffId);
                continue;
            }
            IBuffEffect buffEffect = createBuffEffect(template);
            buffBox.getBuffEffects().add(buffEffect);
        }

        buffBox.onCreate();
        return buffBox;
    }

    public IBuffEffect createBuffEffect(int effectId,String param) {
        BuffEffectEnum buffEffectEnum = BuffEffectEnum.getEffect(effectId);
        if (buffEffectEnum == null) {
            return null;
        }

        AbstractEffect result = null;
        try {
            Constructor<? extends IBuffEffect> constructor = buffEffectEnum.getClazz().getConstructor();
            result = (AbstractEffect) constructor.newInstance();
        } catch (Exception exception) {
            log.error("action创建出错");
            exception.printStackTrace();
            return null;
        }
        result.setParam(param);
        result.getActiveConditions().add(getCondition(Integer.parseInt(template.getActiveCondition())));
        result.getDisposedConditions().add(getCondition(Integer.parseInt(template.getDisposedCondition())));

        return result;
    }

}
