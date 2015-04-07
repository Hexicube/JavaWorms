package org.tilegames.hexicube.worms.entity;

public abstract class EntityLiving extends Entity {
	public abstract void damage(int damage);
	public abstract boolean isDead();
}
