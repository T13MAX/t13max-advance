package com.t13max;

import com.t13max.client.view.window.HeroDetailWindow;

import java.util.concurrent.locks.LockSupport;

/**
 * @author: t13max
 * @since: 18:50 2024/5/31
 */
public class Test {

    @org.junit.Test
    public void test(){
        HeroDetailWindow heroDetailWindow = new HeroDetailWindow();
        heroDetailWindow.openWindow();

        LockSupport.park();
    }
}
