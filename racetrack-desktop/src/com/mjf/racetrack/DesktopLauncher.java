package com.mjf.racetrack;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "racetrack";
		cfg.useGL20 = false;
		cfg.width = Racetrack.gameWidth;
		cfg.height = Racetrack.gameHeight;

		new LwjglApplication(new Racetrack(), cfg);
	}
}
