package com.yfletch.rift;

import com.yfletch.rift.lib.ActionContext;
import com.yfletch.rift.lib.ObjectManager;
import com.yfletch.rift.util.ObjectHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

@Singleton
public class RiftContext extends ActionContext
{
	private static final int ELEMENTAL_RUNE_WIDGET_ID = 48889879;
	private static final int CATALYTIC_RUNE_WIDGET_ID = 48889876;

	@Inject
	private Client client;

	@Inject
	private ObjectManager objectManager;

	@Inject
	private ObjectHelper objectHelper;

	@Inject
	private RiftConfig config;

	@Getter
	@Setter
	private double gameTime = -60;

	@Getter
	private final Map<Pouch, Integer> pouchEssence = new HashMap<>();

	public int getExitMineTime()
	{
		return 120 - config.exitMineSeconds();
	}

	public WorldPoint getCurrentLocation()
	{
		return client.getLocalPlayer().getWorldLocation();
	}

	public WorldPoint getDestinationLocation()
	{
		LocalPoint location = client.getLocalDestinationLocation();
		return location != null ? WorldPoint.fromLocal(client, client.getLocalDestinationLocation()) : null;
	}

	private Guardian getGuardian(int widgetId)
	{
		Widget widget = client.getWidget(widgetId);
		if (widget == null)
		{
			return null;
		}

		int spriteId = widget.getSpriteId();

		return Arrays.stream(Guardian.values())
			.filter(g -> g.getSpriteId() == spriteId)
			.findFirst()
			.orElse(null);
	}

	public Guardian getElementalGuardian()
	{
		return getGuardian(ELEMENTAL_RUNE_WIDGET_ID);
	}

	public Guardian getCatalyticGuardian()
	{
		return getGuardian(CATALYTIC_RUNE_WIDGET_ID);
	}

	public int getItemCount(int itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
		return container != null ? container.count(itemId) : 0;
	}

	public boolean isAt(Point location)
	{
		WorldPoint currentLocation = getCurrentLocation();
		return currentLocation.getX() == location.getX()
			&& currentLocation.getY() == location.getY();
	}

	public boolean isNextTo(int objectId)
	{
		TileObject object = objectManager.get(objectId);
		WorldPoint location = getCurrentLocation();
		return object != null
			&& location != null
			&& objectHelper.isBeside(location, object);
	}

	public boolean isPathingTo(int objectId)
	{
		TileObject object = objectManager.get(objectId);
		WorldPoint dest = getDestinationLocation();
		return object != null
			&& dest != null
			&& objectHelper.isBeside(dest, object);
	}

	public boolean isInLargeMine()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3637, 9500, 0),
			new WorldPoint(3642, 9507, 0),
			getCurrentLocation()
		);
	}

	public int getSpecialEnergy()
	{
		return client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT.getId()) / 10;
	}

	public boolean isMining()
	{
		return client.getLocalPlayer().getAnimation() == 7139;
	}

	public boolean isCraftingEssence()
	{
		return client.getLocalPlayer().getAnimation() == 9365;
	}

	public boolean isFull(Pouch pouch)
	{
		int capacity = hasItem(pouch.getItemId()) ? pouch.getCapacity() : pouch.getDegradedCapacity();
		return pouchEssence.getOrDefault(pouch, 0) == capacity;
	}

	public void fillPouch(Pouch pouch)
	{
		int capacity = hasItem(pouch.getItemId()) ? pouch.getCapacity() : pouch.getDegradedCapacity();
		pouchEssence.put(pouch, Math.min(getItemCount(ItemID.GUARDIAN_ESSENCE) + getItemCount(ItemID.PURE_ESSENCE), capacity));
	}

	public void emptyPouch(Pouch pouch)
	{
		int freeSlots = getFreeInventorySlots();
		int previousEssence = pouchEssence.getOrDefault(pouch, 0);
		pouchEssence.put(pouch, previousEssence - Math.min(freeSlots, previousEssence));
	}

	public int getPouchCapacity()
	{
		int total = 0;

		for (Pouch pouch : Pouch.values())
		{
			if (getItemCount(pouch.getItemId()) > 0)
			{
				total += pouch.getCapacity();
			}
			else if (getItemCount(pouch.getDegradedItemId()) > 0)
			{
				total += pouch.getDegradedCapacity();
			}
		}

		return total;
	}

	public boolean hasItem(int itemId)
	{
		return getItemCount(itemId) > 0;
	}

	/**
	 * Check if there is a higher tier pouch than the
	 * pouch provided in the inventory. Used to stop
	 * the filling-pouches actions.
	 */
	public boolean hasHigherTierPouch(Pouch pouch)
	{
		for (Pouch test : Pouch.values())
		{
			// skip lower-tier pouches
			if (test.getCapacity() <= pouch.getCapacity())
			{
				continue;
			}

			if (hasItem(test.getItemId()) || hasItem(test.getDegradedItemId()))
			{
				return true;
			}
		}

		return false;
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
}
