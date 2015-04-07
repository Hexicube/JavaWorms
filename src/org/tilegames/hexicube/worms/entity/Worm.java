package org.tilegames.hexicube.worms.entity;

import org.tilegames.hexicube.worms.Game;

public class Worm extends EntityLiving {

	public void update() {
		resolveMovementX();
		resolveMovementY();
	}


	protected void resolveMovementX() {
		float amount = Math.abs(velX/ Game.DELTA_PER_TICK);
		float sign = velX > 0 ? 1 : -1;

		while (amount > 0) {
			x += sign;
			amount--;
			if (collide(x, y)) {
				if (!collide(x, y+2)) {
					y += 2;
				} else {
					velX = 0;
					x -= sign;
					break;
				}
			}
		}
	}

	public boolean isRemoved() {
		return false;
	}
}
