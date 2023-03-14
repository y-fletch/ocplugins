package com.yfletch.ocbloods;

import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.ObjectHelper;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

@Singleton
public class OCBloodsContext extends ActionContext
{
	private final static int POUCH_CAPACITY = 40;
	public final static int POUCH_DURABILITY = 320;

	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	@Inject
	@Getter
	private OCBloodsConfig config;

	// only colossal pouch is supported
	/**
	 * Amount of optimistic essence in the Colossal pouch.
	 */
	@Getter
	private int pouchEssence = 0;

	/**
	 * NOT amount of fills - amount of essence that have
	 * gone through the pouch.
	 */
	@Getter
	private int pouchUses = -1;

	@Getter
	private int optimisticEssenceCount = 0;

	@Getter
	private int optimisticFreeSlots = 0;

	public int getItemCount(int itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
		return container != null ? container.count(itemId) : 0;
	}

	public boolean hasItem(int itemId)
	{
		return getItemCount(itemId) > 0;
	}

	public int getEquipmentItemCount(int itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);
		return container != null ? container.count(itemId) : 0;
	}

	public boolean hasEquipped(int itemId)
	{
		return getEquipmentItemCount(itemId) > 0;
	}

	public boolean hasItemOrHasEquipped(int itemId)
	{
		return hasItem(itemId) || hasEquipped(itemId);
	}

	public int getBankItemCount(int itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.BANK);
		return container != null ? container.count(itemId) : 0;
	}

	public boolean hasBanked(int itemId)
	{
		return getBankItemCount(itemId) > 0;
	}

	public int getFreeInventorySlots()
	{
		ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
		if (container == null)
		{
			return 0;
		}

		int freeSlots = 28;
		for (Item item : container.getItems())
		{
			if (item.getQuantity() > 0)
			{
				freeSlots--;
			}
		}
		return freeSlots;
	}

	public void onInventoryChanged()
	{
		optimisticEssenceCount = getItemCount(ItemID.PURE_ESSENCE);
		optimisticFreeSlots = getFreeInventorySlots();
	}

	public void fillPouch()
	{
		int added = Math.min(getItemCount(ItemID.PURE_ESSENCE), POUCH_CAPACITY - pouchEssence);

		if (added > 0)
		{
			pouchEssence += added;
			pouchUses += added;

			optimisticFreeSlots += added;
			optimisticEssenceCount -= added;
		}
	}

	public void emptyPouch()
	{
		int diff = Math.min(getOptimisticFreeSlots(), pouchEssence);
		pouchEssence -= diff;

		optimisticFreeSlots -= diff;
		optimisticEssenceCount += diff;
	}

	public boolean pouchIsEmpty()
	{
		return pouchEssence == 0;
	}

	public boolean pouchIsFull()
	{
		return pouchEssence >= POUCH_CAPACITY;
	}

	public boolean pouchNeedsRepair()
	{
		return pouchUses < 0 || pouchUses + POUCH_CAPACITY >= POUCH_DURABILITY;
	}

	public boolean isNpcContacting()
	{
		return client.getLocalPlayer().getAnimation() == 4413;
	}

	public boolean dialogNpcTextContains(String test)
	{
		Widget npcText = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
		if (npcText == null)
		{
			return false;
		}

		return npcText.getText().contains(test);
	}

	public boolean dialogPlayerTextContains(String test)
	{
		Widget playerText = client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT);
		if (playerText == null)
		{
			return false;
		}

		return playerText.getText().contains(test);
	}

	public int getDialogOptionIndex(String test)
	{
		Widget dialogOptions = client.getWidget(WidgetInfo.DIALOG_OPTION_OPTION1);
		if (dialogOptions == null || dialogOptions.getChildren() == null)
		{
			return -1;
		}

		for (Widget option : dialogOptions.getChildren())
		{
			if (option.getText().contains(test))
			{
				return option.getIndex();
			}
		}
		return -1;
	}

	public void repairPouch()
	{
		pouchUses = 0;
	}

	public WorldPoint getCurrentLocation()
	{
		return client.getLocalPlayer().getWorldLocation();
	}

	public WorldPoint getDestinationLocation()
	{
		LocalPoint location = client.getLocalDestinationLocation();
		return location != null ? WorldPoint.fromLocal(client, location) : null;
	}

	public boolean isInBloodAltar()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3224, 4825, 0),
			new WorldPoint(3240, 4840, 0),
			getCurrentLocation()
		);
	}

	public boolean isInCastleWars()
	{
		return WorldPoint.isInZone(
			new WorldPoint(2438, 3083, 0),
			new WorldPoint(2444, 3097, 0),
			getCurrentLocation()
		);
	}

	public boolean isBankOpen()
	{
		Widget bankContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
		return bankContainer != null && !bankContainer.isHidden();
	}

	public boolean isNextTo(int objectId)
	{
		return isNextTo(objectHelper.getNearest(objectId));
	}

	public boolean isNextTo(TileObject object)
	{
		WorldPoint location = getCurrentLocation();
		return object != null
			&& location != null
			&& objectHelper.isBeside(location, object);
	}

	public boolean isPathingTo(int objectId)
	{
		return isPathingTo(objectHelper.getNearest(objectId));
	}

	public boolean isPathingTo(TileObject object)
	{
		WorldPoint dest = getDestinationLocation();
		return object != null
			&& dest != null
			&& objectHelper.isBeside(dest, object);
	}

	public boolean isInTunnel1()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3439, 9822, 0),
			new WorldPoint(3451, 9827, 0),
			getCurrentLocation()
		);
	}

	public boolean isInTunnel2()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3460, 9810, 0),
			new WorldPoint(3472, 9821, 0),
			getCurrentLocation()
		);
	}

	public boolean isInTunnel3()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3481, 9804, 0),
			new WorldPoint(3504, 9836, 0),
			getCurrentLocation()
		);
	}

	public boolean isInTunnel4()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3534, 9763, 0),
			new WorldPoint(3539, 9776, 0),
			getCurrentLocation()
		);
	}

	public boolean isInBloodCrypt()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3543, 9765, 0),
			new WorldPoint(3570, 9785, 0),
			getCurrentLocation()
		);
	}

	public boolean isInBankingArea()
	{
		return isInCastleWars();
	}

	public boolean isInInstance()
	{
		return client.isInInstancedRegion();
	}

	public int getAgilityLevel()
	{
		return client.getBoostedSkillLevel(Skill.AGILITY);
	}

	public int getRunEnergy()
	{
		return client.getEnergy() / 100;
	}

	public TileObject getPoolOfRejuvenation()
	{
		TileObject rejuvenationPool = objectHelper.getNearest("Rejuvenation");

		return rejuvenationPool != null
			? rejuvenationPool
			: objectHelper.getNearest("Revitalisation");
	}
}
