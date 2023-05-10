package com.yfletch.occore.v2.interaction;

import lombok.Getter;
import net.runelite.api.Item;
import net.runelite.api.Point;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.events.MenuAutomated;

public class StaticItem implements Interactable
{
	@Getter
	private final Item wrapped;

	@Getter
	private final int widgetId;

	public StaticItem(Item wrapped)
	{
		this.wrapped = wrapped;
		widgetId = wrapped.getWidgetId();
	}

	public void use()
	{
		final var currentWidgetId = wrapped.getWidgetId();
		wrapped.setWidgetId(widgetId);
		wrapped.use();
		wrapped.setWidgetId(currentWidgetId);
	}

	@Override
	public Point getClickPoint()
	{
		return wrapped.getClickPoint();
	}

	@Override
	public String[] getActions()
	{
		wrapped.setWidgetId(widgetId);
		return wrapped.getActions();
	}

	@Override
	public int getActionOpcode(int action)
	{
		return wrapped.getActionOpcode(action);
	}

	@Override
	public MenuAutomated getMenu(int actionIndex)
	{
		wrapped.setWidgetId(widgetId);
		return wrapped.getMenu(actionIndex);
	}

	@Override
	public MenuAutomated getMenu(int actionIndex, int opcode)
	{
		wrapped.setWidgetId(widgetId);
		return wrapped.getMenu(actionIndex, opcode);
	}

	@Override
	public void interact(int index)
	{
		wrapped.interact(index);
	}

	@Override
	public void interact(int index, int opcode)
	{
		wrapped.interact(index, opcode);
	}

	@Override
	public void interact(int identifier, int opcode, int param0, int param1)
	{
		wrapped.interact(identifier, opcode, param0, param1);
	}
}
