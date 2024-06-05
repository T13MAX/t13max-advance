package com.t13max.game.memory;

import com.t13max.game.feature.equip.EquipMemory;
import com.t13max.game.feature.item.ItemMemory;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author: t13max
 * @since: 20:16 2024/6/4
 */
@Getter
public enum MemoryTable {

    ITEM(MemoryId.ITEM, ItemMemory.class, ItemMemory::new),
    EQUIP(MemoryId.EQUIP, EquipMemory.class, EquipMemory::new),

    ;

    private byte id;

    private Class<? extends IMemory<?, ?>> memoryClazz;

    private Supplier<? extends IMemory<?, ?>> supplier;

    MemoryTable(byte id, Class<? extends IMemory<?, ?>> memoryClazz, Supplier<? extends IMemory<?, ?>> supplier) {
        this.id = id;
        this.memoryClazz = memoryClazz;
        this.supplier = supplier;
    }

    private static final Map<Class<? extends IMemory<?, ?>>, MemoryTable> DATA_MAP;

    static {
        DATA_MAP = new HashMap<>();
        for (MemoryTable memoryTable : values()) {
            DATA_MAP.put(memoryTable.getMemoryClazz(), memoryTable);
        }
    }

    public static byte getDataId(Class<? extends IMemory<?, ?>> memoryClazz) {
        MemoryTable memoryTable = DATA_MAP.get(memoryClazz);
        if (memoryTable == null) {
            return -1;
        }
        return memoryTable.getId();
    }

    public static MemoryTable getData(Class<? extends IMemory<?, ?>> memoryClazz) {
        return DATA_MAP.get(memoryClazz);
    }

    public static boolean isValid(byte id) {
        return id >= 0 && id < Long.SIZE;
    }
}
