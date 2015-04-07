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
		float amount = Math.abs(velX/Game.DELTA_PER_TICK);
		float sign = velX > 0 ? 1 : -1;
		
		while (amount > 0) {
			x += sign;
			amount--;
			if (collide(x, y)) {
				velX *= -bounce;
				x -= sign;
				break;
			}
		}
	}

	protected void resolveMovementY() {
		float amount = Math.abs(velY/Game.DELTA_PER_TICK);
		float sign = velY > 0 ? 1 : -1;

		while (amount > 0) {
			y += sign;
			amount--;
			if (collide(x, y)) {
				velY *= -bounce;
				y -= sign;
				break;
			}
		}
	}

	public void damage(int amount) {}

	public abstract void update();
	public abstract boolean isRemoved();
	
}
