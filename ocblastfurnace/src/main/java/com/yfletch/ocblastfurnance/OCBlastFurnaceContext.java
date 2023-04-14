package com.yfletch.ocblastfurnance;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.ocblastfurnance.util.FurnaceState;
import com.yfletch.ocblastfurnance.util.Method;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.ObjectHelper;
import java.util.Arrays;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class OCBlastFurnaceContext extends ActionContext
{
	private static final int[] BLAST_FURNACE_WORLDS = new int[]{
		352, 355, 356, 357, 358, 386, 387, 395, 424, 466, 494, 495, 496, 515, 516
	};

	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	@Inject
	private OCBlastFurnaceConfig config;

	@Inject
	private FurnaceState furnaceState;

	@Override
	public void tick()
	{
		furnaceState.tick();
	}

	public Method getMethod()
	{
		return config.method();
	}

	public boolean isAtConveyorBelt()
	{
		return isNear(new WorldPoint(1942, 4967, 0), 2);
	}

	public boolean isNearBarDispenser()
	{
		return isInZone(
			new WorldPoint(1939, 4962, 0),
			new WorldPoint(1941, 4964, 0)
		);
	}

	public boolean hasStaminaEffect()
	{
		return client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0;
	}

	public boolean requiresStamina()
	{
		final var energy = client.getEnergy() / 100.0;

		if (energy < config.xLowStaminaThreshold())
		{
			return true;
		}

		return energy < config.lowStaminaThreshold() && !hasStaminaEffect();
	}

	public boolean isOnBlastFurnaceWorld()
	{
		return Arrays.stream(BLAST_FURNACE_WORLDS).anyMatch(world -> world == client.getWorld());
	}

	public TileObject getBarDispenser()
	{
		return objectHelper.getNearest("Bar dispenser");
	}

	public boolean furnaceHasBars()
	{
		return furnaceState.has(
			ItemID.GOLD_BAR, ItemID.IRON_BAR,
			ItemID.STEEL_BAR, ItemID.MITHRIL_BAR,
			ItemID.ADAMANTITE_BAR, ItemID.RUNITE_BAR
		);
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

	public boolean hasFreeInventorySlot()
	{
		return getFreeInventorySlots() > 0;
	}

	public boolean isTakeInterfaceOpen()
	{
		final var widget = client.getWidget(17694723);
		return widget != null;
	}
}
