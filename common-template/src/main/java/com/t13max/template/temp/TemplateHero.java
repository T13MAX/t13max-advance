package com.t13max.template.temp;

import com.t13max.template.ITemplate;
import com.t13max.template.util.JsonUtils;
import lombok.Data;

import java.util.*;

/**
 * @author: t13max
 * @since: 14:02 2024/4/11
 */
@Data
public class TemplateHero implements ITemplate<TemplateHero> {

    private final static String FILE_NAME = "hero.json";

    private static final Map<Integer, TemplateHero> DATA_MAP = new HashMap<>();

    private int id;
    private String name;
    private int atk;
    private int def;
    private int hp;
    private int speed;
    private int element;
    private int mastery;
    private int skill1;
    private int skill2;

    public static boolean load() {
        List<TemplateHero> iTemplates = JsonUtils.readCommodityTxt(FILE_NAME, TemplateHero.class);
        if (iTemplates == null || iTemplates.isEmpty()) {
            return false;
        }
        iTemplates.forEach(e -> DATA_MAP.put(e.getId(), e));
        return true;
    }

    public static TemplateHero getTemplate(int id) {
        return DATA_MAP.get(id);
    }

    public static Collection<TemplateHero> getAll() {
        return DATA_MAP.values();
    }


}