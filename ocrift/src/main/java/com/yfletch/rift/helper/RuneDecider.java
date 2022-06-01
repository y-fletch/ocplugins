package com.yfletch.rift.helper;

import com.yfletch.rift.enums.Cell;
import com.yfletch.rift.enums.Rune;
import com.yfletch.rift.RiftContext;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RuneDecider
{
	private final RiftContext context;

	public RuneDecider(RiftContext context)
	{
		this.context = context;
	}

	public Rune pick()
	{
		List<Rune> possible = context.getPossibleGuardians().stream()
			.filter(rune -> rune != null && rune.getRequiredLevel() <= context.getRunecraftLevel())
			.collect(Collectors.toList());

		if (possible.isEmpty())
		{
			return null;
		}

		Cell best = null;
		for (Rune rune : possible)
		{
			if (best == null || rune.getCell().isBetterThan(best))
			{
				best = rune.getCell();
			}
		}

		if (best == null)
		{
			return null;
		}

		final Cell finalBest = best;

		List<Rune> bests = possible.stream()
			.filter(rune -> rune.getCell() == finalBest)
			.collect(Collectors.toList());

		if (bests.size() == 1)
		{
			return bests.get(0);
		}

		return bests.stream()
			.filter(rune -> rune.isCatalytic() == context.getConfig().preferCatalytic())
			.max(Comparator.comparingInt(Rune::getRequiredLevel))
			.orElse(null);
	}
}
