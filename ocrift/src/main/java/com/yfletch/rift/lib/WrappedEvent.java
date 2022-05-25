package com.yfletch.rift.lib;

import java.util.Set;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.TileObject;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;

/**
 * Easily mutable event with helper methods.
 */
public class WrappedEvent
{
	private MenuOptionClicked event;
	private IMenuEntryProvider menuEntryProvider;

	public WrappedEvent(MenuOptionClicked event, IMenuEntryProvider menuEntryProvider)
	{
		this.event = event;
		this.menuEntryProvider = menuEntryProvider;
	}

	private void apply(MenuEntry entry)
	{
		try
		{
			event.setMenuOption(entry.getOption());
			event.setMenuTarget(entry.getTarget());
			event.setId(entry.getIdentifier());
			event.setMenuAction(entry.getType());
			event.setParam0(entry.getParam0());
			event.setParam1(entry.getParam1());

//			System.out.println(
//				"Applied entry: " +
//					"\n\tOption: " + entry.getOption() +
//					"\n\tTarget: " + entry.getTarget() +
//					"\n\tIdentifier: " + entry.getIdentifier() +
//					"\n\tOpcode: " + entry.getOpcode() +
//					"\n\tParam0: " + entry.getParam0() +
//					"\n\tParam1: " + entry.getParam1()
//			);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Consume (fail)");
			event.consume();
		}
	}

	public void overrideItemAction(int action, MenuAction menuAction, int itemId)
	{
		apply(menuEntryProvider.createInventoryItemEntry(action, menuAction, Set.of(itemId)));
	}

	public void overrideDepositItemAction(int action, MenuAction menuAction, int itemId)
	{
		apply(menuEntryProvider.createDepositItemEntry(action, menuAction, Set.of(itemId)));
	}

	public void overrideWithdrawItemAction(int action, MenuAction menuAction, int itemId)
	{
		apply(menuEntryProvider.createWithdrawItemEntry(action, menuAction, Set.of(itemId)));
	}

	public void overrideObjectAction(String action, MenuAction menuAction, int objectId)
	{
		apply(menuEntryProvider.createObjectEntry(action, menuAction, objectId));
	}

	public void overrideObjectAction(String action, MenuAction menuAction, TileObject object)
	{
		apply(menuEntryProvider.createObjectEntry(action, menuAction, object));
	}

	public void overrideNPCAction(String action, MenuAction menuAction, int npcId)
	{
		apply(menuEntryProvider.createNPCEntry(action, menuAction, npcId));
	}

	public void overrideInterfaceAction(String action, MenuAction menuAction, WidgetInfo widgetInfo)
	{
		apply(menuEntryProvider.createInterfaceEntry(action, menuAction, widgetInfo));
	}

	public void consume()
	{
		event.consume();
	}
}
