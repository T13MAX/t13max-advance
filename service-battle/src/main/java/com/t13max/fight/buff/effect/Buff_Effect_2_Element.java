package com.t13max.fight.buff.effect;

import com.t13max.fight.buff.effect.element.ElementEnum;
import com.t13max.fight.event.FightEventEnum;
import com.t13max.fight.event.IFightEvent;
import com.t13max.fight.event.IFightEventListener;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * 属性改变效果
 *
 * @author: t13max
 * @since: 16:09 2024/4/10
 */
@Getter
@Log4j2
public class Buff_Effect_2_Element extends AbstractEffect {

    public final static double PER_SUB_AMOUNT = 0.5;

    //元素类型
    private ElementEnum elementEnum;

    //元素量
    private double amount;

    @Override
    protected void onInit() {

        //订阅小回合结束事件 每回合结束减少元素量
        subscribeEvent(FightEventEnum.SMALL_ROUND_END);

        String[] split = param.split(",");
        if (split.length != 2) {
            log.error("param error! param={}", param);
            return;
        }
        ElementEnum element = ElementEnum.getElement(Integer.parseInt(split[0]));
        if (element == null) {
            log.error("param error! param={}", param);
            return;
        }
        this.elementEnum = element;
        this.amount = Double.parseDouble(split[1]);

    }

    @Override
    protected void doOnEvent(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_END -> {
                this.amount -= PER_SUB_AMOUNT;
            }
            default -> {

            }
        }
    }
}
