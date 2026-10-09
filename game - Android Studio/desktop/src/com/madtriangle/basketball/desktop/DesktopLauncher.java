package com.madtriangle.basketball.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import MainGame.BasketballGame;
import configuration.Configuration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = Configuration.GAME_NAME;
        config.height= (1080 / 2);
        config.width = (1920 / 2);
        new LwjglApplication(new BasketballGame(new ActionResolverDesktop()), config);
    }
}
