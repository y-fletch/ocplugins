package com.yfletch.summergarden;

import com.yfletch.summergarden.util.ObjectManager;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.NPC;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

@Singleton
public class State
{
	@Inject
	private Client client;

	@Inject
	private ObjectManager objectManager;

	@Inject
	private SummerGardenConfig config;

	@Getter
	@Setter
	private String usingItemName = null;

	private ItemContainer cachedBank;

	public int getContainerCount(InventoryID containerId, int itemId)
	{
		ItemContainer container = client.getItemContainer(containerId);
		if (container == null)
		{
			return 0;
		}

		return container.count(itemId);
	}

	public int getTotalCount(int itemId)
	{
		return getInventoryCount(itemId) + getBankCount(itemId);
	}

	public int getBankCount(int itemId)
	{
		ItemContainer bank = client.getItemContainer(InventoryID.BANK);
		if (bank != null)
		{
			cachedBank = bank;
		}

		return cachedBank != null ? cachedBank.count(itemId) : 0;
	}

	public int getInventoryCount(int itemId)
	{
		return getContainerCount(InventoryID.INVENTORY, itemId);
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

	public boolean isNpcVisible(int npcId)
	{
		NPC npc = new NPCQuery().idEquals(npcId).result(client).nearestTo(client.getLocalPlayer());
		return npc != null;
	}

	public boolean isAlmostAtGate()
	{
		return inCoords(2910, 5479, 2910, 5480);
	}

	public boolean isInGardenCenter()
	{
		return inCoords(2905, 5465, 2918, 5480);
	}

	public boolean isInSummerGarden()
	{
		return inCoords(2906, 5482, 2925, 5495);
	}

	public boolean isInGarden()
	{
		return inCoords(2800, 5400, 3000, 5500);
	}

	public boolean isInAlKharidPalace()
	{
		return inCoords(3289, 3160, 3296, 3166);
	}

	public boolean isInAlKharid()
	{
		return inCoords(3281, 3119, 3330, 3179);
	}

	public boolean isOnStartingTile()
	{
		return atCoords(2910, 5481);
	}

	public boolean isRunning()
	{
		int anim = client.getLocalPlayer().getPoseAnimation();

		return anim == 824 || // default
			anim == 1210 || // blowpipe/wand/trident
			anim == 1427 || // western banner
			anim == 1661 || // whip
			anim == 1664 || // gadderhammer
			anim == 4228 || // crossbow
			anim == 7043; // godsword/2h
	}

	public boolean isRunEnabled()
	{
		return client.getVarpValue(Consts.VAR_PLAYER_RUNNING) == 1;
	}

	public boolean isInHouse()
	{
		WorldPoint location = client.getLocalPlayer().getWorldLocation();
		// check if player is outside map
		return !isInGarden() && (location.getX() > 4000 || location.getY() > 4000);
	}

	public boolean needsStamina()
	{
		return client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 0 &&
			getEnergy() <= config.staminaThreshold() || getEnergy() <= config.extraStaminaThreshold();
	}

	public int getEnergy()
	{
		return client.getEnergy();
	}

	private boolean inCoords(int xStart, int yStart, int xEnd, int yEnd)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return false;
		}
		WorldPoint loc = player.getWorldLocation();
		return loc.getX() >= xStart && loc.getX() <= xEnd && loc.getY() >= yStart && loc.getY() <= yEnd;
	}

	public boolean atCoords(int x, int y)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return false;
		}
		WorldPoint loc = player.getWorldLocation();
		return loc.getX() == x && loc.getY() == y;
	}

	public boolean isBankOpen()
	{
		Widget bankContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
		return bankContainer != null && !bankContainer.isHidden();
	}

}
