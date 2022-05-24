package com.yfletch.rift.util;

import com.yfletch.rift.lib.IMenuEntryProvider;
import com.yfletch.rift.lib.ObjectManager;
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
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

@Singleton
public class MenuEntryProvider implements IMenuEntryProvider
{
	@Inject
	private ObjectManager objectManager;

	@Inject
	private ObjectHelper objectHelper;

	@Inject
	private Client client;

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

	@Override
	public MenuEntry createInventoryItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
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

	@Override
	public MenuEntry createDepositItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
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

	@Override
	public MenuEntry createWithdrawItemEntry(int action, MenuAction menuAction, Collection<Integer> itemIds)
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

	@Override
	public MenuEntry createObjectEntry(String action, MenuAction menuAction, int objectId)
	{
		TileObject object = objectManager.get(objectId);
		if (object == null)
		{
			return null;
		}

		Point offset = objectHelper.getCenterOffset(objectId);
		LocalPoint localPoint = LocalPoint.fromWorld(client, object.getWorldLocation());
		if (localPoint == null)
		{
			return null;
		}

		return createObjectEntry(
			action,
			menuAction,
			objectId,
			localPoint.getSceneX() - offset.getX(),
			localPoint.getSceneY() - offset.getY()
		);
	}

	@Override
	public MenuEntry createObjectEntry(String action, MenuAction menuAction, int objectId, int overrideX, int overrideY)
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
			overrideX,
			overrideY,
			true
		);
	}

	@Override
	public MenuEntry createNPCEntry(String action, MenuAction menuAction, int npcId)
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

	@Override
	public MenuEntry createInterfaceEntry(String action, MenuAction menuAction, WidgetInfo widgetInfo)
	{
		Widget inter = client.getWidget(widgetInfo);
		if (inter == null)
		{
			return null;
		}

		return client.createMenuEntry(
			action,
			"",
			1,
			MenuAction.CC_OP.getId(),
			-1,
			inter.getId(),
			false
		);
	}
}
