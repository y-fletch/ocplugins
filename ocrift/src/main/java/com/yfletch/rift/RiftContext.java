package com.yfletch.rift;

import com.yfletch.rift.lib.ActionContext;
import com.yfletch.rift.lib.NPCHelper;
import com.yfletch.rift.lib.ObjectHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.Point;
import net.runelite.api.Skill;
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
	@Getter
	private ObjectHelper objectHelper;

	@Inject
	@Getter
	private NPCHelper npcHelper;

	@Inject
	@Getter
	private RiftConfig config;

	@Getter
	@Setter
	private double gameTime = 120;

	@Getter
	private final Map<Pouch, Integer> pouchEssence = new HashMap<>();

	public void clearPouches()
	{
		pouchEssence.clear();
	}

	public void reset()
	{
		clearFlags();
		gameTime = -60;
		clearPouches();
	}

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
		return location != null ? WorldPoint.fromLocal(client, location) : null;
	}

	private Rune getGuardian(int widgetId)
	{
		Widget widget = client.getWidget(widgetId);
		if (widget == null)
		{
			return null;
		}

		int spriteId = widget.getSpriteId();

		return Arrays.stream(Rune.values())
			.filter(g -> g.getSpriteId() == spriteId)
			.findFirst()
			.orElse(null);
	}

	public Rune getElementalRune()
	{
		return getGuardian(ELEMENTAL_RUNE_WIDGET_ID);
	}

	public Rune getCatalyticRune()
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

	public boolean isPathingToGreatGuardian()
	{
		if (getDestinationLocation() == null)
		{
			return false;
		}

		return WorldPoint.isInZone(
			new WorldPoint(3612, 9500, 0),
			new WorldPoint(3618, 9506, 0),
			getDestinationLocation()
		);
	}

	public boolean isInLargeMine()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3637, 9500, 0),
			new WorldPoint(3642, 9507, 0),
			getCurrentLocation()
		);
	}

	public boolean isInHugeMine()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3588, 9500, 0),
			new WorldPoint(3593, 9507, 0),
			getCurrentLocation()
		);
	}

	/**
	 * Rift is the entire minigame area - including guardian
	 * mines and excluding runecrafting altars
	 */
	public boolean isOutsideRift()
	{
		return !WorldPoint.isInZone(
			new WorldPoint(3588, 9483, 0),
			new WorldPoint(3642, 9520, 0),
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

	public boolean isFull(Pouch pouch)
	{
		int capacity = hasItem(pouch.getItemId()) ? pouch.getCapacity() : pouch.getDegradedCapacity();
		return pouchEssence.getOrDefault(pouch, 0) == capacity;
	}

	public boolean isEmpty(Pouch pouch)
	{
		return pouchEssence.getOrDefault(pouch, 0) == 0;
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

	public int getEssenceInPouches()
	{
		int total = 0;
		for (int value : pouchEssence.values())
		{
			total += value;
		}

		return total;
	}

	public boolean areAllPouchesEmpty()
	{
		return getEssenceInPouches() == 0;
	}

	public boolean areAllPouchesFull()
	{
		return getEssenceInPouches() == getPouchCapacity();
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

	public TileObject getHugeEssencePortal()
	{
		return objectHelper.getNearest(ObjectID.PORTAL_43729);
	}

	public boolean hasGuardianStones()
	{
		return hasItem(ItemID.ELEMENTAL_GUARDIAN_STONE)
			|| hasItem(ItemID.CATALYTIC_GUARDIAN_STONE)
			|| hasItem(ItemID.POLYELEMENTAL_GUARDIAN_STONE);
	}

	public boolean hasRunes()
	{
		for (Rune rune : Rune.values())
		{
			if (hasItem(rune.getItemId()))
			{
				return true;
			}
		}

		return hasItem(ItemID.STEAM_RUNE)
			|| hasItem(ItemID.MIST_RUNE)
			|| hasItem(ItemID.DUST_RUNE)
			|| hasItem(ItemID.SMOKE_RUNE)
			|| hasItem(ItemID.MUD_RUNE)
			|| hasItem(ItemID.LAVA_RUNE);
	}

	public boolean hasCells()
	{
		return hasItem(Cell.WEAK.getItemId())
			|| hasItem(Cell.MEDIUM.getItemId())
			|| hasItem(Cell.STRONG.getItemId())
			|| hasItem(Cell.OVERCHARGED.getItemId());
	}

	public int getRunecraftLevel()
	{
		return client.getRealSkillLevel(Skill.RUNECRAFT);
	}

	public int getGuardianPower()
	{
		Widget widget = client.getWidget(48889874);
		if (widget == null || widget.getText() == null)
		{
			return 0;
		}

		Pattern pattern = Pattern.compile("\\d{2}");
		Matcher matcher = pattern.matcher(widget.getText());
		return matcher.matches() ? Integer.parseInt(matcher.group(0)) : 0;
	}
}
