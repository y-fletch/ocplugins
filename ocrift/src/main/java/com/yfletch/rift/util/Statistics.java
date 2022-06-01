package com.yfletch.rift.util;

import com.yfletch.rift.RiftConfig;
import com.yfletch.rift.enums.Rune;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemContainerChanged;

@Getter
@Singleton
public class Statistics
{
	@Inject
	private RiftConfig config;

	private int riftsClosed = 0;

	private int totalElementalEnergy = -1;
	private int totalCatalyticEnergy = -1;

	private int elementalEnergy = 0;
	private int catalyticEnergy = 0;
	private final Map<Rune, Integer> runesBanked = new HashMap<>();
	private Item[] previousInventory;

	private int ticks = 0;

	private final Pattern numberPattern = Pattern.compile(">(\\d+)<");

	public void reset()
	{
		ticks = 0;
		runesBanked.clear();
		elementalEnergy = 0;
		catalyticEnergy = 0;
		riftsClosed = 0;
	}

	public void tick()
	{
		ticks++;
	}

	public String getRiftsPerHour()
	{
		double hours = ticks * 0.6d / 3600d;
		return new DecimalFormat("0.0").format(riftsClosed / hours);
	}

	public String getElementalEnergyPerHour()
	{
		double hours = ticks * 0.6d / 3600d;
		return new DecimalFormat("0.0").format(elementalEnergy / hours);
	}

	public String getCatalyticEnergyPerHour()
	{
		double hours = ticks * 0.6d / 3600d;
		return new DecimalFormat("0.0").format(catalyticEnergy / hours);
	}

	public String getElementalEnergyPerGame()
	{
		return new DecimalFormat("0.0").format(elementalEnergy / (double) riftsClosed);
	}

	public String getCatalyticEnergyPerGame()
	{
		return new DecimalFormat("0.0").format(catalyticEnergy / (double) riftsClosed);
	}

	public void bankRune(Rune rune, int amt)
	{
		runesBanked.put(rune, runesBanked.getOrDefault(rune, 0) + amt);
	}

	public void onChatMessage(ChatMessage event)
	{
		String message = event.getMessage();
		if (message.contains("Elemental energy attuned:"))
		{
			riftsClosed++;

			Matcher matcher = numberPattern.matcher(message);
			if (matcher.find())
			{
				elementalEnergy += Integer.parseInt(matcher.group(1));
				matcher.find();
				catalyticEnergy += Integer.parseInt(matcher.group(1));
			}
		}
		else if (message.contains("Total elemental energy:"))
		{
			Matcher matcher = numberPattern.matcher(message);
			if (matcher.find())
			{
				totalElementalEnergy = Integer.parseInt(matcher.group(1));
				matcher.find();
				totalCatalyticEnergy = Integer.parseInt(matcher.group(1));
			}
		}
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
		Map<Rune, Integer> oldRunes = new HashMap<>();

		for (Item item : previousInventory)
		{
			if (config.dropRunes().contains(Rune.getByItemId(item.getId())))
			{
				continue;
			}

			Rune rune = Rune.getByItemId(item.getId());
			if (rune != null)
			{
				oldRunes.put(rune, item.getQuantity());
			}
		}

		oldRunes.forEach((rune, qty) -> {
			Item item = Arrays.stream(items)
				.filter(i -> i.getId() == rune.getItemId())
				.findFirst()
				.orElse(null);

			if (item == null || item.getQuantity() == 0)
			{
				bankRune(rune, qty);
			}
		});

		previousInventory = items;
	}
}
