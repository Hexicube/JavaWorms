package org.tilegames.hexicube.worms;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter
{
	public static LwjglApplicationConfiguration config;
	
	public static void main(String[] args)
	{
		config = new LwjglApplicationConfiguration();
		config.title = "Game loading...";
		config.width = 800;
		config.height = 600;
		Game.width = config.width;
		Game.height = config.height;
		config.useGL30 = false;
		config.resizable = false;
		config.addIcon("images/icon_16.png", Files.FileType.Internal);
		config.addIcon("images/icon_32.png", Files.FileType.Internal);
		config.addIcon("images/icon_64.png", Files.FileType.Internal);
		
		new LwjglApplication(new Game(), config);
	}
}