package org.tilegames.hexicube.worms;

import java.util.ArrayList;
import org.tilegames.hexicube.worms.entity.*;

public class Map
{
	public ArrayList<Entity> entities;
	public ArrayList<EntityWorm>[] teams;
	
	public int[][] mapData;
	
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
		for(ArrayList<EntityWorm> team : teams)
		{
			size = team.size();
			for(int a = 0; a < size; a++)
			{
				Entity e = team.get(a);
				if(e.isRemoved())
				{
					team.remove(a);
					a--;
					size--;
				}
				else e.tick();
			}
		}
	}
	
	public void explosion(float x, float y, Explosion e)
	{
		int r = e.getMapDamageRadius();
		for(int x2 = -r; x2 <= r; x2++)
		{
			for(int y2 = -r; y2 <= r; y2++)
			{
				int xDist = x2 * x2;
				int yDist = y2 * y2;
				int dist = (int)Math.sqrt(xDist*xDist + yDist*yDist) - r;
				if(dist <= 0) mapData[(int)(x+0.5f)+x2][(int)(y+0.5f)+y2] = 0;
				else if(dist <= 2) mapData[(int)(x+0.5f)+x2][(int)(y+0.5f)+y2] = 0x000000FF;
			}
		}
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
		for(ArrayList<EntityWorm> team : teams)
		{
			for(EntityWorm ent : team)
			{
				float xDist = ent.x - x;
				float yDist = ent.y - y;
				double dist = Math.sqrt(xDist*xDist + yDist*yDist);
				ent.danage(e.getDamageDealt(dist));
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