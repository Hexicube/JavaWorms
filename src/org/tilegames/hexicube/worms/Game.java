package org.tilegames.hexicube.worms;

import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.tilegames.hexicube.worms.KeyHandler.Key;
import org.tilegames.hexicube.worms.gui.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputProcessor;

public class Game implements ApplicationListener, InputProcessor
{
	public static final String gameName = "Worms";
	public static final String versionText = "Proto 1";
	
	private static SpriteBatch spriteBatch;
	
	public static int volume;
	
	public static boolean gameActive;
	
	private static float currentDeltaPassed;
	
	public static KeyHandler keys;
	public static SoundHandler sounds;
	
	public static Random rand;
	
	private static boolean paused = false;
	
	
	public static int width, height;
	
	public static GuiElementTextInput currentlyTyping;
	public static GuiElementDraggable currentlyDragging;
	
	public static GuiManager menu;
	
	
	public static Map map;
	
	public static void setMenu(GuiManager newMenu)
	{
		if(newMenu == null)
		{
			if(menu != null) menu = menu.parent;
		}
		else
		{
			newMenu.parent = menu;
			menu = newMenu;
		}
	}
	
	@Override
	public void create()
	{
		rand = new Random();
		
		spriteBatch = new SpriteBatch();
		volume = 100;
		
		Gdx.input.setInputProcessor(this);
		Gdx.graphics.setVSync(true);
		
		FontHolder.prep();
		
		Gdx.graphics.setTitle(gameName+" - "+versionText);
		
		currentDeltaPassed = 0;
		
		keys = new KeyHandler(new File("keys.txt"));
		sounds = new SoundHandler(new File("sounds.txt"));
		
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(loadIcon("icon_16x16"));
		icons.add(loadIcon("icon_32x32"));
		icons.add(loadIcon("icon_64x64"));
		for(Window w : Window.getWindows())
		{
			System.out.println(w);
			w.setIconImages(icons);
		}
		
		menu = new GuiManagerMainMenu();
	}
	
	@Override
	public void dispose()
	{
	}
	
	@Override
	public void pause()
	{
		paused = true;
	}
	
	public static final float DELTA_PER_TICK = .2f;
	@Override
	public void render()
	{
		currentDeltaPassed += Gdx.graphics.getDeltaTime();
		if(currentDeltaPassed > 1f) currentDeltaPassed = 1f;
		while(currentDeltaPassed >= DELTA_PER_TICK)
		{
			currentDeltaPassed -= DELTA_PER_TICK;
			tick();
		}
		Gdx.graphics.getGLCommon().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGLCommon().glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		int screenW = Gdx.graphics.getWidth();
		int screenH = Gdx.graphics.getHeight();
		
		spriteBatch.begin();
		
		if(menu != null) menu.render(spriteBatch);
		//TODO: render
	
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		Game.width = width;
		Game.height = height;
		spriteBatch = new SpriteBatch(); 
	}
	
	@Override
	public void resume()
	{
		paused = false;
	}
	
	@Override
	public boolean keyDown(int key)
	{
		Key k = Key.getKey(key);
		if(k == Key.PAUSE)
		{
			if(menu == null || !menu.pausesGame()) setMenu(new GuiManagerPauseMenu());
			else setMenu(null);
		}
		else if(menu == null || !menu.keyPress(key)) keys.keyPress(key);
		return false;
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		if(currentlyTyping != null) currentlyTyping.keyType(character);
		return false;
	}

	@Override
	public boolean keyUp(int key)
	{
		keys.keyRelease(key);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		if(currentlyDragging != null) currentlyDragging.handleRelease();
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		if(menu != null)
		{
			Game.currentlyDragging = null;
			Game.currentlyTyping = null;
			menu.mousePress(x, height-y-1, pointer);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		if(currentlyDragging != null) currentlyDragging.handleDrag(x, height-y-1, pointer);
		return false;
	}
	
	@Override
	public boolean touchMoved(int x, int y)
	{
		if(menu != null) menu.mouseMove(x, height-y-1);
		return false;
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		//TODO: scrolling stuff
		return false;
	}
	
	public static Image loadIcon(String name)
	{
		name = "images/" + name;
		if(!File.separator.equals("/")) name.replace("/", File.separator);
		FileHandle fh = Gdx.files.internal(name + ".png");
		try
		{
			return ImageIO.read(fh.read());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static Texture loadImage(String name)
	{
		name = "images/" + name;
		if(!File.separator.equals("/")) name.replace("/", File.separator);
		return new Texture(Gdx.files.internal(name + ".png"));
	}
	
	public static Sound loadSound(String name)
	{
		name = "sounds/" + name;
		if(!File.separator.equals("/")) name.replace("/", File.separator);
		return Gdx.audio.newSound(Gdx.files.internal(name + ".ogg"));
	}
	
	public static Music loadMusic(String name)
	{
		name = "sounds/" + name;
		if(!File.separator.equals("/")) name.replace("/", File.separator);
		return Gdx.audio.newMusic(Gdx.files.internal(name + ".ogg"));
	}
	
	public static void tick()
	{
		keys.tick();
		if(menu != null) menu.tick();
		if(paused) return;
		if(map != null) map.tick();
	}
	
	public static String numToStr(int num, int len, String filler)
	{
		String temp = String.valueOf(num);
		while(temp.length() < len) temp = filler + temp;
		String[] digits = temp.split("");
		String result = "";
		int mod = digits.length % 3;
		for(int a = 1; a < digits.length;)
		{
			result += digits[a];
			a++;
			if(a%3 == mod && a < digits.length) result += ",";
		}
		return result;
	}
	
	public static int rollDice(int sides, int amount)
	{
		int count = 0;
		for(int a = 0; a < amount; a++)
		{
			count += rand.nextInt(sides)+1;
		}
		return count;
	}
	
	public static int nextPowerTwo(int val)
	{
		return (int)Math.pow(2, Math.ceil(Math.log(val)/Math.log(2)));
	}
}