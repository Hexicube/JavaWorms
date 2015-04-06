package org.tilegames.hexicube.worms.gui;

import org.tilegames.hexicube.worms.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GuiElement
{
	protected int x, y;
	protected float xAlign, yAlign;
	
	public GuiElement(int x, int y, float xAlign, float yAlign)
	{
		this.x = x;
		this.y = y;
		this.xAlign = xAlign;
		this.yAlign = yAlign;
	}
	
	protected int[] getPosition()
	{
		return new int[]{x+(int)(Game.width*xAlign), y+(int)(Game.height*yAlign)};
	}
	
	public abstract void tick();
	public abstract void render(SpriteBatch batch);
}