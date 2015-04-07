package org.tilegames.hexicube.worms.entity;

import org.tilegames.hexicube.worms.Game;

public abstract class Entity {
	
	public float x, y;
	public float width, height;
	public float velX, velY;
	public float bounce;

	/**
	 * Check if an object collides with the map at the given position
	 */
	public boolean collide(float x, float y) {
		float oldX = x, oldY = y;
		// res = check collision
		x = oldX;
		y = oldY;
		return false;
	}

	protected void resolveMovementX() {
		float amount = Math.abs(velX);
		float sign = velX > 0 ? 1 : -1;
		
		while (amount >= 1) {
			x += sign;
			amount--;
			if (collide(x, y)) {
				velX *= -bounce;
				x -= sign;
				sign = -sign;
			}
		}
		amount *= sign;
		float residue = x % 1;
		x += amount;
		if(sign == -1)
		{
			if(residue + amount <= 0)
			{
				if (collide(x, y)) {
					velX *= -bounce;
					x -= amount;
				}
			}
		}
		else
		{
			if(residue + amount >= 1)
			{
				if (collide(x, y)) {
					velX *= -bounce;
					x -= amount;
				}
			}
		}
	}

	protected void resolveMovementY() {
		float amount = Math.abs(velY);
		float sign = velX > 0 ? 1 : -1;
		
		while (amount >= 1) {
			y += sign;
			amount--;
			if (collide(x, y)) {
				velY *= -bounce;
				y -= sign;
				sign = -sign;
			}
		}
		amount *= sign;
		float residue = y % 1;
		y += amount;
		if(sign == -1)
		{
			if(residue + amount <= 0)
			{
				if (collide(x, y)) {
					velY *= -bounce;
					y -= amount;
				}
			}
		}
		else
		{
			if(residue + amount >= 1)
			{
				if (collide(x, y)) {
					velY *= -bounce;
					y -= amount;
				}
			}
		}
	}

	public abstract void tick();
	public abstract boolean isRemoved();
	
	public boolean isAtRest()
	{
		return Math.abs(velX) < Game.MIN_SPEED && Math.abs(velY) < Game.MIN_SPEED;
	}
}
