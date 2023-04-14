package com.yfletch.ocblastfurnance.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.runelite.api.Client;

@Singleton
public class FurnaceState
{
	@Inject
	private Client client;

	private final Map<Integer, Integer> previousQuantity = new HashMap<>();

	public void tick()
	{
		for (BarsOres varbit : BarsOres.values())
		{
			previousQuantity.put(varbit.getItemID(), getQuantity(varbit.getItemID()));
		}
	}

	public int getChange(int itemId)
	{
		return getQuantity(itemId) - previousQuantity.getOrDefault(itemId, 0);
	}

	public int getQuantity(int... itemIds)
	{
		var total = 0;

		for (int itemId : itemIds)
		{
			Optional<BarsOres> varbit = Arrays.stream(BarsOres.values())
				.filter(e -> e.getItemID() == itemId)
				.findFirst();
			assert varbit.isPresent();
			total += client.getVarbitValue(varbit.get().getVarbit());
		}

		return total;
	}

	public boolean has(int... itemIds)
	{
		return getQuantity(itemIds) > 0;
	}
}
