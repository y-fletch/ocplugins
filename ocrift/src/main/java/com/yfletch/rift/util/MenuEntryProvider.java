package com.yfletch.rift.util;

import com.yfletch.rift.lib.IMenuEntryProvider;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetInfo;

@Singleton
public class MenuEntryProvider implements IMenuEntryProvider
{
	@Inject
	private Client client;

	@Override
	public MenuEntry createInventoryItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		return null;
	}

	@Override
	public MenuEntry createDepositItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		return null;
	}

	@Override
	public MenuEntry createWithdrawItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		return null;
	}

	@Override
	public MenuEntry createObjectEntry(String action, MenuAction menuAction, int objectId)
	{
		return null;
	}

	@Override
	public MenuEntry createObjectEntry(String action, MenuAction menuAction, int objectId, int overrideX, int overrideY)
	{
		return null;
	}

	@Override
	public MenuEntry createNPCEntry(String action, MenuAction menuAction, int npcId)
	{
		return null;
	}

	@Override
	public MenuEntry createInterfaceEntry(String action, MenuAction menuAction, WidgetInfo widgetInfo)
	{
		return null;
	}
}
