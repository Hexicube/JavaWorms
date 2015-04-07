package org.tilegames.hexicube.worms;

public class ExplosionTest extends Explosion
{
	@Override
	public int getDamageDealt(double distance)
	{
		return 0;
	}
	
	@Override
	public float getKnockback(double distance)
	{
		return 0;
	}
	
	@Override
	public int getMapDamageRadius()
	{
		return 10;
	}
}