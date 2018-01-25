package com.planes.pc.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.planes.pc.Planes;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//1130 x 720
		config.width = 1130;
		config.height = 720;
		config.resizable = true;
		new LwjglApplication(new Planes(), config);
	}
}
