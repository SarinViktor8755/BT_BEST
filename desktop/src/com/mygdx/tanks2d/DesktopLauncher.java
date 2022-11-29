package com.mygdx.tanks2d;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.tanks2d.MainGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.useVsync(false);
		config.setTitle("TanksPVP_2d");
		config.setWindowedMode(MainGame.WIDTH_SCREEN  ,MainGame.HEIGHT_SCREEN);
		new Lwjgl3Application(new MainGame(1), config);
	}
}
