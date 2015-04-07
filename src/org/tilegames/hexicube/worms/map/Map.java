package org.tilegames.hexicube.worms.map;

import java.util.ArrayList;

import org.tilegames.hexicube.worms.Game;
import org.tilegames.hexicube.worms.Team;
import org.tilegames.hexicube.worms.entity.*;
import org.tilegames.hexicube.worms.weapons.Explosion;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map
{
	public ArrayList<Entity> entities;
	public Team[] teams;
	
	public int[][] mapData;
	private Texture tex;
	
	public Map(MapGenerator generator, int width, int height)
	{
		generator.generate(width, height, Game.rand);
		mapData = generator.getMapData();
		entities = generator.getPlacedObjects();
		teams = new Team[0];
	}
	
	public void tick()
	{
		int size = entities.size();
		for(int a = 0; a < size; a++)
		{
			Entity e = entities.get(a);
			if(e.isRemoved())
			{
				entities.remove(a);
				a--;
				size--;
			}
			else e.tick();
		}
		for(Team team : teams)
		{
			size = team.worms.size();
			for(int a = 0; a < size; a++)
			{
				Entity e = team.worms.get(a);
				if(e.isRemoved())
				{
					team.worms.remove(a);
					a--;
					size--;
				}
				else e.tick();
			}
		}
		//explosion(rand.nextInt(400), rand.nextInt(400), new ExplosionTest());
	}
	
	private Pixmap pm;
	public void render(SpriteBatch batch)
	{
		if(tex == null) redraw(0, 0, 0, 0);
		batch.draw(tex, 0, mapData[0].length-tex.getHeight());
	}
	
	private void redraw(int x, int y, int w, int h)
	{
		if(x < 0)
		{
			w += x;
			x = 0;
		}
		if(y < 0)
		{
			h += y;
			y = 0;
		}
		if(x+w > mapData.length) w = mapData.length - x;
		if(y+h > mapData[0].length) h = mapData[0].length - y;
		if(tex == null)
		{
			pm = new Pixmap(Game.nextPowerTwo(mapData.length), Game.nextPowerTwo(mapData[0].length), Format.RGBA8888);
			for(x = 0; x < mapData.length; x++)
			{
				for(y = 0; y < mapData[0].length; y++)
				{
					pm.drawPixel(x, y, mapData[x][y]);
				}
			}
			tex = new Texture(pm);
			pm.dispose();
		}
		else
		{
			pm = new Pixmap(w, h, Format.RGBA8888);
			for(int x2 = 0; x2 < w; x2++)
			{
				for(int y2 = 0; y2 < h; y2++)
				{
					try
					{
						pm.drawPixel(x2, y2, mapData[x+x2][y+y2]);
					}
					catch(IndexOutOfBoundsException e){}
				}
			}
			tex.draw(pm, x, y);
			pm.dispose();
		}
	}
	
	public void explosion(float x, float y, Explosion e)
	{
		int r = e.getMapDamageRadius();
		for(int x2 = -r-3; x2 <= r+3; x2++)
		{
			for(int y2 = -r-3; y2 <= r+3; y2++)
			{
				int dist = (int)Math.sqrt(x2*x2 + y2*y2) - r;
				try
				{
					if(dist <= 0) mapData[(int)(x+0.5f)+x2][(int)(y+0.5f)+y2] = 0x00000000;
					else if(dist <= 2)
					{
						if(mapData[(int)(x+0.5f)+x2][(int)(y+0.5f)+y2] != 0x00000000) mapData[(int)(x+0.5f)+x2][(int)(y+0.5f)+y2] = 0x000000FF;
					}
				}
				catch(IndexOutOfBoundsException e2){}
			}
		}
		redraw((int)(x+0.5f)-r-4, (int)(y+0.5f)-r-4, r*2+8, r*2+8);
		for(Entity ent : entities)
		{
			float xDist = ent.x - x;
			float yDist = ent.y - y;
			double dist = Math.sqrt(xDist*xDist + yDist*yDist);
			float knockback = e.getKnockback(dist);
			if(ent instanceof EntityLiving)
			{
				((EntityLiving)ent).damage(e.getDamageDealt(dist));
			}
			if(knockback > 0)
			{
				double angle = Math.atan2(yDist, xDist); //TODO: check if works
				ent.velX += Math.cos(angle) * knockback;
				ent.velY += Math.sin(angle) * knockback;
			}
		}
		for(Team team : teams)
		{
			for(EntityWorm ent : team.worms)
			{
				float xDist = ent.x - x;
				float yDist = ent.y - y;
				double dist = Math.sqrt(xDist*xDist + yDist*yDist);
				ent.damage(e.getDamageDealt(dist));
				float knockback = e.getKnockback(dist);
				if(knockback > 0)
				{
					double angle = Math.atan2(yDist, xDist); //TODO: check if works
					ent.velX += Math.cos(angle) * knockback;
					ent.velY += Math.sin(angle) * knockback;
				}
			}
		}
	}
}