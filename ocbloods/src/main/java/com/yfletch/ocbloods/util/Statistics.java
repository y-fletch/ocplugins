package com.yfletch.ocbloods.util;

import java.awt.Color;
import java.util.Arrays;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.ui.ColorScheme;

@Getter
@Singleton
public class Statistics
{
	private int ticks = 0;
	private int runesCrafted = 0;

	@Setter
	private int runePrice = 365;
	private Item[] previousInventory;

	private int lastDepositTicks = 0;
	private int fastestTripTicks = -1;

	@Getter
	private int tripsCompleted = 0;

	public void reset()
	{
		ticks = 0;
		runesCrafted = 0;
		lastDepositTicks = 0;
		fastestTripTicks = -1;
		tripsCompleted = 0;
	}

	public void tick()
	{
		ticks++;
	}

	public String getTimeElapsed()
	{
		int seconds = (int) (ticks * 0.6d) % 60;
		int minutes = (int) (ticks * 0.6d / 60) % 60;
		int hours = (int) (ticks * 0.6d / 3600) % 24;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public String getRunesCrafted()
	{
		return RSNumberFormat.format(runesCrafted);
	}

	public String getTotalGp()
	{
		return RSNumberFormat.format(runesCrafted * runePrice);
	}

	public String getRunesPerHour()
	{
		double hours = ticks * 0.6d / 3600d;
		return RSNumberFormat.format(runesCrafted / hours);
	}

	public String getGpPerHour()
	{
		double hours = ticks * 0.6d / 3600d;
		return RSNumberFormat.format((runesCrafted * runePrice) / hours);
	}

	public String getFastestTripTime()
	{
		if (fastestTripTicks < 0)
		{
			return "-";
		}

		return ((int) (fastestTripTicks * 0.6d)) + "s";
	}

	public Color getFastestTripTimeColor()
	{
		if (fastestTripTicks < 0)
		{
			return Color.WHITE;
		}

		if (fastestTripTicks <= 48 / 0.6d)
		{
			return ColorScheme.PROGRESS_COMPLETE_COLOR;
		}

		if (fastestTripTicks <= 60 / 0.6d)
		{
			return ColorScheme.PROGRESS_INPROGRESS_COLOR;
		}

		return ColorScheme.PROGRESS_ERROR_COLOR;

	}

	public String getAverageTripTime()
	{
		if (tripsCompleted == 0)
		{
			return "-";
		}

		return ((int) (ticks * 0.6d) / tripsCompleted) + "s";
	}

	public Color getAverageTripTimeColor()
	{
		if (tripsCompleted == 0)
		{
			return Color.WHITE;
		}

		int avg = (int) ((double) ticks / tripsCompleted);

		if (avg <= 48 / 0.6d)
		{
			return ColorScheme.PROGRESS_COMPLETE_COLOR;
		}

		if (avg <= 60 / 0.6d)
		{
			return ColorScheme.PROGRESS_INPROGRESS_COLOR;
		}

		return ColorScheme.PROGRESS_ERROR_COLOR;
	}

	public String getTripsPerHour()
	{
		double hours = ticks * 0.6d / 3600d;
		return String.valueOf(((int) (tripsCompleted / hours)));
	}

	public Color getTripsPerHourColor()
	{
		double hours = ticks * 0.6d / 3600d;
		int tph = (int) (tripsCompleted / hours);

		if (tph >= 70)
		{
			return ColorScheme.PROGRESS_COMPLETE_COLOR;
		}

		if (tph >= 60)
		{
			return ColorScheme.PROGRESS_INPROGRESS_COLOR;
		}

		return ColorScheme.PROGRESS_ERROR_COLOR;
	}

	private void depositRunes(int amt)
	{
		runesCrafted += amt;
		tripsCompleted++;

		int tripTicks = ticks - lastDepositTicks;
		if (tripTicks < fastestTripTicks || fastestTripTicks < 0)
		{
			fastestTripTicks = tripTicks;
		}

		lastDepositTicks = ticks;
	}

	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.INVENTORY.getId())
		{
			return;
		}

		if (previousInventory == null)
		{
			previousInventory = event.getItemContainer().getItems();
			return;
		}

		Item[] items = event.getItemContainer().getItems();
		int previousRunes = 0;

		for (Item item : previousInventory)
		{
			if (item.getId() == ItemID.BLOOD_RUNE)
			{
				previousRunes += item.getQuantity();
			}
		}

		Item item = Arrays.stream(items)
			.filter(i -> i.getId() == ItemID.BLOOD_RUNE)
			.findFirst()
			.orElse(null);

		if (previousRunes > 0 && (item == null || item.getQuantity() == 0))
		{
			depositRunes(previousRunes);
		}

		previousInventory = items;
	}
}
