package com.t13max.client.view.enums;

/**
 * @author: t13max
 * @since: 15:39 2024/5/29
 */
public interface Const {

    String MAIN_FRAME_TITLE = "Advance";

    int MAIN_WIDTH = 900;
    int MAIN_HEIGHT = 600;
    int SETTINGS_WIDTH = 250;
    int SETTINGS_HEIGHT = MAIN_HEIGHT;
    int LOG_WIDTH = 600;
    int LOG_HEIGHT = 600;
    int GAME_WIDTH = MAIN_WIDTH - SETTINGS_WIDTH;
    int GAME_HEIGHT = MAIN_HEIGHT;
    int HERO_WIDTH = 30;
    int MAX_PROCESS = 100;
    int HERO_DETAIL_WIDTH = 200;
    int HERO_DETAIL_HEIGHT = 200;

    String LOGIN = "login";

    String HP = " HP:";
    String ACTION = " ACTION:";
    String DEF = " DEF:";
    String ATK = " ATK:";
    String SKILL = " SKILL:";
    String BUFF = " BUFF:";
}
