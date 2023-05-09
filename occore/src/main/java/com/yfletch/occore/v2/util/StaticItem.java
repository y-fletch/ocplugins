package com.yfletch.occore.v2.util;

import lombok.Getter;
import net.runelite.api.Item;
import net.runelite.api.Point;
import net.unethicalite.api.Interactable;
import net.unethicalite.api.events.MenuAutomated;

public class StaticItem implements Interactable
{
	@Getter
	private final Item item;

	@Getter
	private final int widgetId;

	public StaticItem(Item item)
	{
		this.item = item;
		widgetId = item.getWidgetId();
	}

	public void use()
	{
		final var currentWidgetId = item.getWidgetId();
		item.setWidgetId(widgetId);
		item.use();
		item.setWidgetId(currentWidgetId);
	}

	@Override
	public Point getClickPoint()
	{
		return item.getClickPoint();
	}

	@Override
	public String[] getActions()
	{
		item.setWidgetId(widgetId);
		return item.getActions();
	}

	@Override
	public int getActionOpcode(int action)
	{
		return item.getActionOpcode(action);
	}

	@Override
	public MenuAutomated getMenu(int actionIndex)
	{
		item.setWidgetId(widgetId);
		return item.getMenu(actionIndex);
	}

	@Override
	public MenuAutomated getMenu(int actionIndex, int opcode)
	{
		item.setWidgetId(widgetId);
		return item.getMenu(actionIndex, opcode);
	}

	@Override
	public void interact(int index)
	{
		item.interact(index);
	}

	@Override
	public void interact(int index, int opcode)
	{
		item.interact(index, opcode);
	}

	@Override
	public void interact(int identifier, int opcode, int param0, int param1)
	{
		item.interact(identifier, opcode, param0, param1);
	}
}
