package com.yfletch.occore;

import com.google.inject.Inject;
import com.yfletch.occore.util.ObjectHelper;
import com.yfletch.occore.util.RegionPoint;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

/**
 * Contains context (game state, configs) to pass to
 * the actions to help determine when they should run.
 * <p>
 * Extend this class and add your own getters.
 */
public class ActionContext
{
	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	private final Map<String, Boolean> flags = new HashMap<>();
	private final Map<String, Integer> ephemeral = new HashMap<>();

	@Setter(AccessLevel.PACKAGE)
	@Getter
	private String usingItemName;

	/**
	 * Persist a flag in context.
	 */
	public void flag(String key, Boolean value)
	{
		flags.put(key, value);
		ephemeral.remove(key);
	}

	/**
	 * Persist an ephemeral flag in context, that will be
	 * cleared after the set amount of ticks.
	 */
	public void flag(String key, Boolean value, int ticks)
	{
		flag(key, value);
		ephemeral.put(key, ticks);
	}

	/**
	 * Get a flag's value. Defaults to false if the flag
	 * isn't found.
	 */
	public Boolean flag(String key)
	{
		return flags.getOrDefault(key, false);
	}

	/**
	 * Decay ephemeral flags each tick.
	 */
	public void tick()
	{
		for (Map.Entry<String, Integer> entry : ephemeral.entrySet())
		{
			if (entry.getValue() < 1)
			{
				ephemeral.remove(entry.getKey());
				flags.remove(entry.getKey());
			}
			else
			{
				ephemeral.put(entry.getKey(), entry.getValue() - 1);
			}
		}
	}

	/**
	 * Clear a flag if it exists
	 */
	public void clearFlag(String key)
	{
		flags.remove(key);
		ephemeral.remove(key);
	}

	/**
	 * Clear all flags. Should only be necessary
	 * for plugin state resetting.
	 */
	public void clearFlags()
	{
		ephemeral.clear();
		flags.clear();
	}

	/**
	 * Get flags with their ephemeral tick time, for use
	 * in debug overlays
	 */
	public Map<String, String> getDebugFlags()
	{
		Map<String, String> debugFlags = new HashMap<>();
		for (Map.Entry<String, Boolean> entry : flags.entrySet())
		{
			String ticksLeft = "";
			if (ephemeral.containsKey(entry.getKey()))
			{
				ticksLeft = " (" + ephemeral.get(entry.getKey()) + ")";
			}
			debugFlags.put(entry.getKey(), entry.getValue() + ticksLeft);
		}

		return debugFlags;
	}

	/**
	 * Get current player
	 */
	public Player getPlayer()
	{
		return client.getLocalPlayer();
	}

	/**
	 * Get current player location
	 */
	public WorldPoint getPlayerLocation()
	{
		return getPlayer().getWorldLocation();
	}

	/**
	 * Get current player destination location
	 */
	public WorldPoint getDestinationLocation()
	{
		LocalPoint location = client.getLocalDestinationLocation();
		return location != null ? WorldPoint.fromLocal(client, location) : null;
	}

	/**
	 * Check if player is at position
	 */
	public boolean isAt(WorldPoint worldPoint)
	{
		return getPlayerLocation().equals(worldPoint);
	}

	/**
	 * Check if player is at position
	 */
	public boolean isAt(RegionPoint regionPoint)
	{
		return getPlayerLocation().equals(regionPoint.toWorld());
	}

	/**
	 * Check if player is near a position
	 */
	public boolean isNear(WorldPoint worldPoint, int maxDist)
	{
		return getPlayerLocation().distanceTo(worldPoint) < maxDist;
	}

	/**
	 * Check if player is near a position
	 */
	public boolean isNear(RegionPoint regionPoint, int maxDist)
	{
		return getPlayerLocation().distanceTo(regionPoint.toWorld()) < maxDist;
	}

	/**
	 * Check if player is in zone
	 */
	public boolean isInZone(WorldPoint lowerBound, WorldPoint upperBound)
	{
		return WorldPoint.isInZone(lowerBound, upperBound, getPlayerLocation());
	}

	/**
	 * Check if player is in zone
	 */
	public boolean isInZone(RegionPoint lowerBound, RegionPoint upperBound)
	{
		return isInZone(lowerBound.toWorld(), upperBound.toWorld());
	}

	/**
	 * Check if player is pathing to the nearest object
	 * matching the given id
	 */
	public boolean isPathingTo(int objectId)
	{
		return isPathingTo(objectHelper.getNearest(objectId));
	}

	/**
	 * Check if the player is pathing to the object
	 */
	public boolean isPathingTo(TileObject object)
	{
		WorldPoint dest = getDestinationLocation();
		return object != null
			&& dest != null
			&& objectHelper.isBeside(dest, object);
	}

	/**
	 * Check if the player is pathing to the given position
	 */
	public boolean isPathingTo(WorldPoint worldPoint)
	{
		WorldPoint destination = getDestinationLocation();
		return destination != null && destination.equals(worldPoint);
	}

	/**
	 * Check if bank interface is open
	 */
	public boolean isBankOpen()
	{
		Widget bankContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
		return bankContainer != null && !bankContainer.isHidden();
	}

	/**
	 * Get count of item in player inventory
	 */
	public int getItemCount(int... itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
		if (container == null)
		{
			return 0;
		}

		return Arrays.stream(itemId).map(container::count).sum();
	}

	/**
	 * Check if item is in player inventory
	 */
	public boolean hasItem(int... itemId)
	{
		return getItemCount(itemId) > 0;
	}

	/**
	 * Get count of item in player bank
	 * <p>
	 * Bank interface needs to be opened at least once
	 */
	public int getBankItemCount(int... itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.BANK);
		if (container == null)
		{
			return 0;
		}

		return Arrays.stream(itemId).map(container::count).sum();
	}

	/**
	 * Check if item is in player bank
	 * <p>
	 * Bank interface needs to be opened at least once
	 */
	public boolean hasBanked(int... itemId)
	{
		return getBankItemCount(itemId) > 0;
	}

	/**
	 * Get count of item in player equipment
	 */
	public int getEquipmentItemCount(int... itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);
		if (container == null)
		{
			return 0;
		}

		return Arrays.stream(itemId).map(container::count).sum();
	}

	/**
	 * Check if player has item equipped
	 */
	public boolean hasEquipped(int itemId)
	{
		return getEquipmentItemCount(itemId) > 0;
	}

	/**
	 * Get amount of free slots in player inventory
	 */
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

	public int getPlayerAnimation()
	{
		return client.getLocalPlayer().getAnimation();
	}
}
