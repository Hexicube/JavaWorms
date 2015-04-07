package org.tilegames.hexicube.worms.map;

import java.util.ArrayList;
import java.util.Random;

import org.tilegames.hexicube.worms.entity.Entity;

public abstract class MapGenerator
{
	public abstract void generate(int width, int height, Random rand);
	
	public abstract int[][] getMapData();
	public abstract ArrayList<Entity> getPlacedObjects();
}