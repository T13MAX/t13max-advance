package com.t13max.fight.event;

import battle.entity.FightEventPb;
import com.t13max.fight.context.FightContext;

import java.util.LinkedList;
import java.util.List;

/**
 * 小回合记录器
 *
 * @author: t13max
 * @since: 16:07 2024/6/5
 */
public class SmallRoundRecorder extends AbstractEventListener {

    private List<IFightEventPackager> eventPackagerList = new LinkedList<>();

    private FightContext fightContext;

    public SmallRoundRecorder(FightContext fightContext) {
        this.fightContext = fightContext;
        //监听所有
        subscribeEvent(FightEventEnum.values());
    }

    @Override
    public void onEvent(IFightEvent event) {
        if (event instanceof IFightEventPackager packager) {
            eventPackagerList.add(packager);
        }
    }

    public List<FightEventPb> buildEventList() {
        List<FightEventPb> result = new LinkedList<>();
        for (IFightEventPackager eventPackager : eventPackagerList) {
            FightEventPb pack = eventPackager.pack();
            result.add(pack);
        }
        eventPackagerList.clear();
        return result;
    }
}
