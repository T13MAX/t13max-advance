package com.t13max.game.feature.quest.data;

import com.t13max.data.mongo.IData;
import com.t13max.game.feature.quest.enums.EQuestGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author t13max
 * @since 17:04 2024/9/4
 */
public class QuestDataMap implements IData {

    private final long roleId;

    private final Map<EQuestGroup, QuestData> questDataMap;


    public QuestDataMap(long roleId) {
        this.roleId = roleId;
        this.questDataMap = new HashMap<>();
    }

    public QuestDataMap(long roleId, Map<EQuestGroup, QuestData> questDataMap) {
        this.roleId = roleId;
        this.questDataMap = questDataMap;
    }

    @Override
    public long getId() {
        return roleId;
    }
}
