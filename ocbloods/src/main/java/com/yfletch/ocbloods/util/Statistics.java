package com.yfletch.ocbloods.util;

import java.util.Arrays;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.events.ItemContainerChanged;

@Getter
@Singleton
public class Statistics
{
	private int ticks = 0;
	private int runesCrafted = 0;

	@Setter
	private int runePrice = 365;
	private Item[] previousInventory;
	private int previousRuneCount = 0;

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

	private void bankRune(int amt)
	{
		runesCrafted += amt;
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

		if (item == null || item.getQuantity() == 0)
		{
			bankRune(previousRunes);
		}

		previousInventory = items;
	}
}
