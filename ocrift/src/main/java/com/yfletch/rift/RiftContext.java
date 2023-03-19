package com.yfletch.rift;

import com.yfletch.rift.enums.Cell;
import com.yfletch.rift.enums.Pouch;
import com.yfletch.rift.enums.Rune;
import com.yfletch.rift.lib.ActionContext;
import com.yfletch.rift.lib.NPCHelper;
import com.yfletch.rift.lib.ObjectHelper;
import com.yfletch.rift.util.Statistics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import net.runelite.api.widgets.WidgetInfo;

@Slf4j
@Singleton
public class RiftContext extends ActionContext
{
	private static final int ELEMENTAL_RUNE_WIDGET_ID = 48889876;
	private static final int CATALYTIC_RUNE_WIDGET_ID = 48889879;
	private static final int POUCH_USES_PER_GAME = 5;

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

	@Inject
	private Statistics statistics;

	@Getter
	@Setter
	private double gameTime = 120;

	@Getter
	private final Map<Pouch, Integer> pouchEssence = new HashMap<>();

	private final Map<Pouch, Integer> pouchUses = new HashMap<>();

	@Getter
	private int optimisticEssenceCount = 0;

	@Getter
	private int optimisticFreeSlots = 0;

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

	public void onInventoryChanged()
	{
		optimisticEssenceCount = getItemCount(ItemID.GUARDIAN_ESSENCE) + getItemCount(ItemID.PURE_ESSENCE);
		optimisticFreeSlots = getFreeInventorySlots();
	}

	public boolean isPregame()
	{
		return !isOutsideRift()
			&& (getGameTime() < 0
			|| (getGuardianPower() == 100 || getGuardianPower() == 0)
			&& !hasCell()
			&& !hasGuardianStones());
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

	public List<Rune> getPossibleGuardians()
	{
		List<Rune> guardians = new ArrayList<>();

		for (Rune rune : Rune.values())
		{
			if (hasItem(rune.getTalismanId()))
			{
				guardians.add(rune);
			}
		}

		guardians.add(getElementalRune());
		guardians.add(getCatalyticRune());
		return guardians;
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

	/**
	 * Includes 1 tile into the rift, to force state to reset
	 */
	public boolean isInLobbyArea()
	{
		return WorldPoint.isInZone(
			new WorldPoint(3600, 9471, 0),
			new WorldPoint(3630, 9484, 0),
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

	public int getTimesUsed(Pouch pouch)
	{
		return pouchUses.getOrDefault(pouch, 0);
	}

	public void fillPouch(Pouch pouch)
	{
		int capacity = hasItem(pouch.getItemId()) ? pouch.getCapacity() : pouch.getDegradedCapacity();
		int inPouch = pouchEssence.getOrDefault(pouch, 0);
		int capacityLeft = capacity - inPouch;
		int essenceAdded = Math.min(getOptimisticEssenceCount(), capacityLeft);

		if (essenceAdded > 0)
		{
			int newEssence = inPouch + essenceAdded;
			pouchEssence.put(pouch, newEssence);

			// only counts as a use when pouch is filled
			// (not exactly, but it's ok as the plugin should
			// always completely fill/empty colossal pouches)
			if (newEssence == capacity)
			{
				pouchUses.put(pouch, pouchUses.getOrDefault(pouch, 0) + 1);
			}

			optimisticFreeSlots += essenceAdded;
			optimisticEssenceCount -= essenceAdded;
		}
	}

	public void emptyPouch(Pouch pouch)
	{
		int freeSlots = getOptimisticFreeSlots();
		int previousEssence = pouchEssence.getOrDefault(pouch, 0);
		int diff = Math.min(freeSlots, previousEssence);
		pouchEssence.put(pouch, previousEssence - diff);

		optimisticFreeSlots -= diff;
		optimisticEssenceCount += diff;
	}

	public int getPouchCapacity()
	{
		int total = 0;

		for (Pouch pouch : Pouch.values())
		{
			if (hasItem(pouch.getItemId()))
			{
				total += pouch.getCapacity();
			}
			else if (hasItem(pouch.getDegradedItemId()))
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

	public boolean allPouchesAreEmpty()
	{
		return getEssenceInPouches() == 0;
	}

	public boolean allPouchesAreFull()
	{
		return getEssenceInPouches() == getPouchCapacity();
	}

	public boolean hasDegradedPouch()
	{
		for (Pouch pouch : Pouch.values())
		{
			if (hasItem(pouch.getDegradedItemId()))
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasNearlyDegradedPouch()
	{
		for (Pouch pouch : Pouch.values())
		{
			if (!hasItem(pouch.getItemId()) || pouch.getUses() < 0)
			{
				continue;
			}

			if (pouchUses.getOrDefault(pouch, 0) >= pouch.getUses() - POUCH_USES_PER_GAME)
			{
				return true;
			}
		}

		return false;
	}

	public void repairPouches()
	{
		for (Pouch pouch : Pouch.values())
		{
			if (hasItem(pouch.getItemId()))
			{
				pouchUses.put(pouch, 0);
			}
		}
	}

	public boolean hasItem(int itemId)
	{
		return getItemCount(itemId) > 0;
	}

	public boolean hasEqupped(int itemId)
	{
		ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);
		return container != null && container.count(itemId) > 0;
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

	public int getDroppableRune()
	{
		for (Rune rune : config.dropRunes())
		{
			if (hasItem(rune.getItemId()))
			{
				return rune.getItemId();
			}
		}

		return -1;
	}

	public boolean hasDroppableRunes()
	{
		return getDroppableRune() != -1;
	}

	public int getCell()
	{
		for (Cell cell : Cell.values())
		{
			if (cell == Cell.UNCHARGED)
			{
				continue;
			}

			if (hasItem(cell.getItemId()))
			{
				return cell.getItemId();
			}
		}

		return -1;
	}

	public boolean hasCell()
	{
		return getCell() != -1;
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

		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher matcher = pattern.matcher(widget.getText());
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
	}

	public boolean isNpcContacting()
	{
		return client.getLocalPlayer().getAnimation() == 4413;
	}

	public boolean isDialogOpen()
	{
		return client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT) != null
			|| client.getWidget(WidgetInfo.DIALOG_NPC_TEXT) != null
			|| client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS) != null;
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
		Widget dialogOptions = client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
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

	/**
	 * Check if player has less catalytic energy than elemental.
	 * Uses total energy instead if available.
	 * -1 = less catalytic
	 * 0 = equal
	 * 1 = more catalytic
	 */
	public int compareEnergies()
	{
		if (statistics.getTotalCatalyticEnergy() != statistics.getTotalElementalEnergy())
		{
			return statistics.getTotalCatalyticEnergy() < statistics.getTotalElementalEnergy()
				? -1 : 1;
		}

		if (statistics.getCatalyticEnergy() == statistics.getElementalEnergy())
		{
			return 0;
		}

		return statistics.getCatalyticEnergy() < statistics.getElementalEnergy()
			? -1 : 1;
	}
}
