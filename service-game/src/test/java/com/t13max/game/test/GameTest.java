package com.t13max.game.test;

import com.t13max.util.TimeUtil;
import org.junit.Test;

import java.time.DayOfWeek;

/**
 * @author: t13max
 * @since: 15:54 2024/6/6
 */
public class GameTest {

    @Test
    public void test(){
        System.out.println(TimeUtil.getSpecificDayTime(DayOfWeek.MONDAY));
    }
}
