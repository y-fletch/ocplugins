package com.yfletch.rift.action.cycle.common;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.NpcAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;
import net.runelite.api.NpcID;

public class PowerUpGuardian extends NpcAction<RiftContext>
{
	public PowerUpGuardian()
	{
		super("Power-up", "The Great Guardian");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		if (!ctx.hasGuardianStones() || ctx.isOutsideRift())
		{
			return false;
		}

		// always attempt to cash in at end of game
		if (ctx.getGuardianPower() >= 99)
		{
			return true;
		}

		return ctx.flag("mine-cycle")
			? !ctx.hasRunes()
			: ctx.getFreeInventorySlots() == 0;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingToGreatGuardian();
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.hasGuardianStones();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideNPCAction("Power-up", MenuAction.NPC_FIRST_OPTION, NpcID.THE_GREAT_GUARDIAN);
	}
}
