package org.tilegames.hexicube.worms.gui;

public abstract class GuiElementDraggable extends GuiElementClickable
{
	public GuiElementDraggable(int x, int y, float xAlign, float yAlign)
	{
		super(x, y, xAlign, yAlign);
	}
	
	public abstract void handleDrag(int x, int y, int pointer);
	public abstract void handleRelease();
}