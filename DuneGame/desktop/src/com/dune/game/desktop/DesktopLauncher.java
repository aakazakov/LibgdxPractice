package com.dune.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.*;
import com.dune.game.DuneGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1280, 720);
//		config.foregroundFPS = 1;
		new Lwjgl3Application(new DuneGame(), config);
	}
}
