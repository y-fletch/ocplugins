package com.yfletch.summergarden.util;

import java.util.Collection;
import java.util.Set;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;

public class WrappedEvent
{
	private MenuEntryProvider menuEntryProvider;
	private MenuOptionClicked event;

	public WrappedEvent(MenuEntryProvider menuEntryProvider, MenuOptionClicked event)
	{
		this.menuEntryProvider = menuEntryProvider;
		this.event = event;
	}

	public void apply(MenuEntry entry)
	{
		try
		{
			event.setMenuOption(entry.getOption());
			event.setMenuTarget(entry.getTarget());
			event.setId(entry.getIdentifier());
			event.setMenuAction(entry.getType());
			event.setParam0(entry.getParam0());
			event.setParam1(entry.getParam1());

//			System.out.println("Applied entry: \n\tOption: " + entry.getOption() + "\n\tTarget: " + entry.getTarget() + "\n\tIdentifier: " + entry.getIdentifier() + "\n\tOpcode: " + entry.getOpcode() + "\n\tParam0: " + entry.getParam0() + "\n\tParam1: " + entry.getParam1());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Consume (fail)");
			event.consume();
		}
	}

	public void itemAction(int action, MenuAction menuAction, int itemId)
	{
		itemAction(action, menuAction, Set.of(itemId));
	}

	public void itemAction(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		apply(menuEntryProvider.itemEntry(action, menuAction, itemIds));
	}

	public void depositItemAction(int action, MenuAction menuAction, int itemId)
	{
		depositItemAction(action, menuAction, Set.of(itemId));
	}

	public void depositItemAction(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		apply(menuEntryProvider.depositItemEntry(action, menuAction, itemIds));
	}

	public void withdrawItemAction(int action, MenuAction menuAction, int itemId)
	{
		withdrawItemAction(action, menuAction, Set.of(itemId));
	}

	public void withdrawItemAction(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		apply(menuEntryProvider.withdrawItemEntry(action, menuAction, itemIds));
	}

	public void objectAction(String action, int objectId)
	{
		objectAction(action, objectId, 0);
	}

	public void objectAction(String action, MenuAction menuAction, int objectId)
	{
		objectAction(action, menuAction, objectId, 0);
	}

	public void objectAction(String action, MenuAction menuAction, int objectId, int offBy)
	{
		apply(menuEntryProvider.objectEntry(action, menuAction, objectId, offBy));
	}

	public void objectAction(String action, int objectId, int offBy)
	{
		objectAction(action, MenuAction.GAME_OBJECT_FIRST_OPTION, objectId, offBy);
	}

	public void objectAction(String action, MenuAction menuAction, int objectId, int x, int y)
	{
		apply(menuEntryProvider.objectEntry(action, menuAction, objectId, x, y));
	}

	public void objectAction(String action, int objectId, int x, int y)
	{
		objectAction(action, MenuAction.GAME_OBJECT_FIRST_OPTION, objectId, x, y);
	}

	public void npcAction(String action, MenuAction menuAction, int npcId)
	{
		apply(menuEntryProvider.npcEntry(action, menuAction, npcId));
	}

	public void interfaceAction(String action, WidgetInfo widgetInfo)
	{
		apply(menuEntryProvider.interfaceEntry(action, widgetInfo));
	}

	public void consume()
	{
		event.consume();
	}
}
