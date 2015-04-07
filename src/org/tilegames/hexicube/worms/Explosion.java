package org.tilegames.hexicube.worms;

public abstract class Explosion
{
	public abstract int getDamageDealt(double distance);
	public abstract float getKnockback(double distance);
	public abstract int getMapDamageRadius();
}