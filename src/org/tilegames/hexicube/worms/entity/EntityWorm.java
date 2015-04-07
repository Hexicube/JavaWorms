package org.tilegames.hexicube.worms.entity;

public class EntityWorm extends EntityLiving {
	public int health = 100;
	
	public void tick() {
		//TODO: handle inputs if needed
		resolveMovementX();
		resolveMovementY();
	}

	protected void resolveMovementX() {
		float amount = Math.abs(velX);
		float sign = velX > 0 ? 1 : -1;
		
		while (amount >= 1) {
			x += sign;
			amount--;
			if (collide(x, y)) {
				if (!collide(x, y+2)) {
					y += 2;
				} else {
					velX = 0;
					x -= sign;
					sign = -sign;
				}
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
					if (!collide(x, y+2)) {
						y += 2;
					} else {
						velX = 0;
						x -= sign;
					}
				}
			}
		}
		else
		{
			if(residue + amount >= 1)
			{
				if (collide(x, y)) {
					if (!collide(x, y+2)) {
						y += 2;
					} else {
						velX = 0;
						x -= sign;
					}
				}
			}
		}
	}

	public boolean isRemoved() {
		return false;
	}


	@Override
	public void damage(int damage)
	{
		health -= damage;
	}


	@Override
	public boolean isDead()
	{
		return health <= 0;
	}
}
