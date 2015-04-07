package org.tilegames.hexicube.worms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.badlogic.gdx.Input.Keys;

public class KeyHandler
{
	public enum Key
	{
		LEFT("Left", Keys.A), RIGHT("Right", Keys.D), UP("Up", Keys.W), DOWN("Down", Keys.S),
		
		FIRE("Fire", Keys.SPACE), FIRESEC("Secondary Fire", Keys.TAB), INV("Inventory", Keys.E),
		
		PAUSE("Pause", Keys.ESCAPE);
		
		private int keyID, defaultValue;
		private String name;
		private static int numKeys;
		
		public static Key getKey(int key)
		{
			Key[] keys = values();
			for(int a = 0; a < keys.length; a++)
			{
				if(Game.keys.getKeyBind(keys[a]) == key) return keys[a];
			}
			return null;
		}
		
		private Key(String name, int defaultVal)
		{
			this.keyID = Key.nextID();
			this.name = name;
			defaultValue = defaultVal;
		}
		
		private static int nextID()
		{
			return numKeys++;
		}
		
		public int getID()
		{
			return keyID;
		}
		
		public int getDefaultValue()
		{
			return defaultValue;
		}
		
		public String getName()
		{
			return name;
		}
		
		public static int numKeys()
		{
			return numKeys;
		}
	}
	
	private int[] keyValues;
	private boolean[] keyPressed = new boolean[256];
	private boolean[] keyHeld = new boolean[256];
	
	private File config;
	private boolean needsSaving;
	
	public KeyHandler(File config)
	{
		this.config = config;
		keyValues = new int[Key.numKeys()];
		for(Key k : Key.values())
		{
			keyValues[k.getID()] = k.getDefaultValue();
		}
		if(config != null)
		{
			if(!config.exists() || !config.isFile())
			{
				needsSaving = true;
				saveKeys();
			}
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(config));
				while(reader.ready())
				{
					String line = reader.readLine();
					String[] data = line.split(":");
					if(data.length != 2)
					{
						System.err.println("Invalid key config line (ignoring it):");
						System.err.println("    "+line);
						continue;
					}
					for(Key k : Key.values())
					{
						if(k.getName().equalsIgnoreCase(data[0]))
						{
							try
							{
								keyValues[k.getID()] = Integer.parseInt(data[1]);
							}
							catch(NumberFormatException e)
							{
								System.err.println("Invalid key config line (ignoring it):");
								System.err.println("    "+line);
							}
						}
					}
				}
				reader.close();
				needsSaving = true;
				saveKeys();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void setKeyBind(Key key, int ID)
	{
		keyValues[key.getID()] = ID;
		needsSaving = true;
	}
	
	public int getKeyBind(Key key)
	{
		return keyValues[key.getID()];
	}
	
	public void saveKeys()
	{
		if(needsSaving)
		{
			needsSaving = false;
			if(config != null)
			{
				try
				{
					PrintWriter writer = new PrintWriter(config);
					for(Key k : Key.values())
					{
						writer.println(k.getName()+":"+keyValues[k.getID()]);
					}
					writer.flush();
					writer.close();
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void keyPress(int key)
	{
		if(key == 0) return;
		try
		{
			keyPressed[key] = true;
			keyHeld[key] = true;
		}
		catch(IndexOutOfBoundsException e) {}
	}
	
	public void keyRelease(int key)
	{
		try
		{
			keyHeld[key] = false;
		}
		catch(IndexOutOfBoundsException e) {}
	}
	
	public boolean isKeyPressed(int key)
	{
		try
		{
			return keyPressed[keyValues[key]];
		}
		catch(IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public boolean isKeyHeld(int key)
	{
		try
		{
			return keyHeld[keyValues[key]];
		}
		catch(IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public boolean isKeyPressed(Key key)
	{
		try
		{
			return keyPressed[keyValues[key.getID()]];
		}
		catch(IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public boolean isKeyHeld(Key key)
	{
		try
		{
			return keyHeld[keyValues[key.getID()]];
		}
		catch(IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public static String getProperName(int key)
	{
		return Keys.toString(key);
	}
	
	public String getProperName(Key k)
	{
		return getProperName(getKeyBind(k));
	}
	
	public void tick()
	{
		for(int a = 0; a < keyPressed.length; a++)
		{
			keyPressed[a] = false;
		}
	}
}