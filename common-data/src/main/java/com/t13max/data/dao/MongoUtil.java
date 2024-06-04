package com.t13max.data.dao;

import com.t13max.data.entity.IData;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;

/**
 * Mongodb操作工具类
 * 待完善
 *
 * @author: t13max
 * @since: 13:28 2024/5/29
 */
@UtilityClass
public class MongoUtil {

    public static <T extends IData> void save(T data) {

    }

    public static <T extends IData> void delete(long id, Class<T> clazz) {

    }

    public static <T extends IData> T findById(long id, Class<T> clazz) {
        return (T) null;
    }

    public static <T extends IData> List<T> findList(Class<T> clazz) {

        return Collections.emptyList();
    }

    public static <T extends IData> List<T> findList(long uuid, Class<T> clazz) {
        return Collections.emptyList();
    }

}
