package com.yfletch.rift.helper;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.enums.Pouch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.runelite.api.ItemID;

public class PouchSolver
{
	private final RiftContext ctx;

	public PouchSolver(RiftContext ctx)
	{
		this.ctx = ctx;
	}

	public Collection<Pouch> getPouchOrder()
	{
		List<Pouch> pouches = Arrays.stream(Pouch.values())
			.filter(pouch -> (ctx.hasItem(pouch.getItemId()) || ctx.hasItem(pouch.getDegradedItemId())))
			.sorted(Comparator.comparingInt(Pouch::getItemId))
			.collect(Collectors.toList());

		List<Pouch> reversed = new ArrayList<>(pouches);
		Collections.reverse(reversed);

		List<Pouch> ordered = new ArrayList<>();
		for (int i = 0; i < pouches.size() / 2; i++)
		{
			ordered.add(reversed.get(i));
			if (reversed.get(i) != pouches.get(i))
			{
				ordered.add(pouches.get(i));
			}
		}

		return ordered;
	}

	public Pouch getNextUnfilledPouch()
	{
		if (ctx.hasItem(ItemID.COLOSSAL_POUCH)
			|| ctx.hasItem(ItemID.COLOSSAL_POUCH_26786)
			|| ctx.hasItem(ItemID.COLOSSAL_POUCH_26906))
		{
			return Pouch.COLOSSAL;
		}

		Pouch pouch = getPouchOrder().stream()
			.filter(p -> !ctx.isFull(p))
			.findFirst()
			.orElse(null);

		if (pouch == null ||
			// has enough essence to fill
			(ctx.getOptimisticEssenceCount() < pouch.getCapacity()
				// or is colossal
				&& pouch != Pouch.COLOSSAL))
		{
			return null;
		}

		return pouch;
	}

	public Pouch getNextFilledPouch()
	{
		if (ctx.hasItem(ItemID.COLOSSAL_POUCH)
			|| ctx.hasItem(ItemID.COLOSSAL_POUCH_26786)
			|| ctx.hasItem(ItemID.COLOSSAL_POUCH_26906))
		{
			return Pouch.COLOSSAL;
		}

		Pouch pouch = getPouchOrder().stream()
			.filter(p -> !ctx.isEmpty(p))
			.findFirst()
			.orElse(null);

		if (pouch == null ||
			// has enough space to empty
			(ctx.getOptimisticFreeSlots() < pouch.getCapacity()
				// or is colossal
				&& pouch != Pouch.COLOSSAL))
		{
			return null;
		}

		return pouch;
	}
}
