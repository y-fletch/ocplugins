package com.yfletch.summergarden.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

@Singleton
public class MenuEntryProvider
{
	@Inject
	private Client client;

	@Inject
	private ObjectManager objectManager;

	private Widget getItem(Collection<Integer> ids)
	{
		return getItem(ids, WidgetInfo.INVENTORY);
	}

	private Widget getItem(Collection<Integer> ids, WidgetInfo widget)
	{
		List<Widget> matches = getItems(ids, widget);
		return matches.size() != 0 ? matches.get(0) : null;
	}

	private ArrayList<Widget> getItems(Collection<Integer> ids, WidgetInfo widget)
	{
		client.runScript(6009, 9764864, 28, 1, -1);
		Widget inventoryWidget = client.getWidget(widget);
		ArrayList<Widget> matchedItems = new ArrayList<>();

		if (inventoryWidget != null && inventoryWidget.getDynamicChildren() != null)
		{
			Widget[] items = inventoryWidget.getDynamicChildren();
			for (Widget item : items)
			{
				if (ids.contains(item.getItemId()))
				{
					matchedItems.add(item);
				}
			}
		}
		return matchedItems;
	}

	public MenuEntry depositItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		Widget item = getItem(itemIds, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		if (item == null)
		{
			return null;
		}

		return client.createMenuEntry(
			"",
			item.getName(),
			action,
			menuAction.getId(),
			item.getIndex(),
			WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId(),
			false
		);
	}

	public MenuEntry withdrawItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		Widget item = getItem(itemIds, WidgetInfo.BANK_ITEM_CONTAINER);
		if (item == null)
		{
			return null;
		}

		return client.createMenuEntry(
			"",
			item.getName(),
			action,
			menuAction.getId(),
			item.getIndex(),
			WidgetInfo.BANK_ITEM_CONTAINER.getId(),
			false
		);
	}

	public MenuEntry itemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
	{
		Widget item = getItem(itemIds);
		if (item == null)
		{
			return null;
		}

		return client.createMenuEntry(
			"",
			item.getName(),
			action,
			menuAction.getId(),
			item.getIndex(),
			WidgetInfo.INVENTORY.getId(),
			false
		);
	}

	public MenuEntry objectEntry(String action, MenuAction menuAction, int objectId, int x, int y)
	{
		TileObject object = objectManager.get(objectId);
		if (object == null)
		{
			return null;
		}

		ObjectComposition comp = client.getObjectDefinition(object.getId());

		return client.createMenuEntry(
			action,
			"<col=ffff>" + comp.getName(),
			object.getId(),
			menuAction.getId(),
			x,
			y,
			true
		);
	}

	public MenuEntry objectEntry(String action, MenuAction menuAction, int objectId, int offBy)
	{
		TileObject object = objectManager.get(objectId);
		if (object == null)
		{
			return null;
		}

		ObjectComposition comp = client.getObjectDefinition(object.getId());

		return client.createMenuEntry(
			action,
			"<col=ffff>" + comp.getName(),
			object.getId(),
			menuAction.getId(),
			object.getLocalLocation().getSceneX() - offBy,
			object.getLocalLocation().getSceneY() - offBy,
			true
		);
	}

	public MenuEntry npcEntry(String action, MenuAction menuAction, int npcId)
	{
		NPC npc = new NPCQuery().idEquals(npcId).result(client).nearestTo(client.getLocalPlayer());
		if (npc == null)
		{
			return null;
		}

		return client.createMenuEntry(
			action,
			npc.getName(),
			npc.getIndex(),
			menuAction.getId(),
			0,
			0,
			false
		);
	}

	public MenuEntry interfaceEntry(String action, WidgetInfo interfaceInfo)
	{
		Widget interf = client.getWidget(interfaceInfo);
		if (interf == null)
		{
			return null;
		}

		return client.createMenuEntry(
			action,
			"",
			1,
			MenuAction.CC_OP.getId(),
			-1,
			interf.getId(),
			false
		);
	}
}
