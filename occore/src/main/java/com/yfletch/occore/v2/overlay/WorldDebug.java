package com.yfletch.occore.v2.overlay;

import java.util.List;
import lombok.Setter;
import net.runelite.api.GroundObject;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.Interactable;

public class WorldDebug
{
	@Setter private static WorldDebugOverlay worldOverlay;
	@Setter private static BankItemDebugOverlay bankItemDebugOverlay;
	@Setter private static InventoryItemDebugOverlay inventoryItemDebugOverlay;
	@Setter private static EquipmentItemDebugOverlay equipmentItemDebugOverlay;

	public static void clear()
	{
		// don't clear path - that's handled elsewhere
		worldOverlay.setTile(null);
		worldOverlay.setNpc(null);
		worldOverlay.setTileObject(null);
		worldOverlay.setGroundObject(null);
		worldOverlay.setTileItem(null);

		bankItemDebugOverlay.setItem(null);
		inventoryItemDebugOverlay.setItem(null);
		equipmentItemDebugOverlay.setItem(null);
	}

	public static void setTile(WorldPoint tile)
	{
		clear();
		worldOverlay.setTile(tile);
	}

	public static void setPath(List<WorldPoint> path)
	{
		worldOverlay.setPath(path);
	}

	public static void setNpc(NPC npc)
	{
		clear();
		worldOverlay.setNpc(npc);
	}

	public static void setTileObject(TileObject tileObject)
	{
		clear();
		worldOverlay.setTileObject(tileObject);
	}

	public static void setGroundObject(GroundObject groundObject)
	{
		clear();
		worldOverlay.setGroundObject(groundObject);
	}

	public static void setTileItem(TileItem tileItem)
	{
		clear();
		worldOverlay.setTileItem(tileItem);
	}

	public static void setItem(Item item)
	{
		clear();
		switch (item.getType())
		{
			case BANK:
				bankItemDebugOverlay.setItem(item);
				break;
			case INVENTORY:
			case BANK_INVENTORY:
			case TRADE_INVENTORY:
			case UNKNOWN:
				inventoryItemDebugOverlay.setItem(item);
				break;
			case EQUIPMENT:
				equipmentItemDebugOverlay.setItem(item);
				break;
		}
	}

	public static void setAny(Interactable interactable)
	{
		if (interactable instanceof NPC)
		{
			setNpc((NPC) interactable);
		}

		if (interactable instanceof TileObject)
		{
			setTileObject((TileObject) interactable);
		}

		if (interactable instanceof GroundObject)
		{
			setGroundObject((GroundObject) interactable);
		}

		if (interactable instanceof Item)
		{
			setItem((Item) interactable);
		}

		if (interactable instanceof TileItem)
		{
			setTileItem((TileItem) interactable);
		}

		// TODO: Widget
	}
}
