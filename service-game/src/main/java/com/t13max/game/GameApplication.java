package com.t13max.game;


import com.t13max.game.manager.GameManager;

/**
 * @author: t13max
 * @since: 18:17 2024/4/9
 */
public class GameApplication {

    public static void main(String[] args) {

        try {
            GameManager gameManager = new GameManager();
            gameManager.quickStart();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private void print(){
        System.out.println(222);
    }
}
