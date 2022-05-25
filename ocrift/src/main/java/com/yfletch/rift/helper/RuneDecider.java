package com.yfletch.rift.helper;

import com.yfletch.rift.Rune;
import com.yfletch.rift.RiftContext;

public class RuneDecider
{
	private RiftContext context;

	public RuneDecider(RiftContext context)
	{
		this.context = context;
	}

	public Rune pick()
	{
		Rune elemental = context.getElementalRune();
		Rune catalytic = context.getCatalyticRune();

		if (elemental != null && elemental.getRequiredLevel() > context.getRunecraftLevel())
		{
			elemental = null;
		}

		if (catalytic != null && catalytic.getRequiredLevel() > context.getRunecraftLevel())
		{
			catalytic = null;
		}

		if (elemental == null)
		{
			return catalytic;
		}

		if (catalytic == null)
		{
			return elemental;
		}

		// "better than" is strictly GT. If they're equal, it'll
		// use the false path
		if (context.getConfig().preferCatalytic())
		{
			return elemental.isBetterThan(catalytic) ? elemental : catalytic;
		}
		else
		{
			return catalytic.isBetterThan(elemental) ? catalytic : elemental;
		}
	}
}
