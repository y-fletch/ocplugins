package com.yfletch.rift.action.cycle.common;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.enums.Rune;
import com.yfletch.rift.helper.RuneDecider;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

public class EnterAltar extends ObjectAction<RiftContext>
{
	public EnterAltar()
	{
		super("Enter", "Guardian");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return !ctx.isInHugeMine()
			&& ctx.getElementalRune() != null
			&& ctx.hasItem(ItemID.GUARDIAN_ESSENCE)
			&& ctx.areAllPouchesFull()
			&& !ctx.hasCell();
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		Rune rune = new RuneDecider(ctx).pick();
		if (rune == null)
		{
			return false;
		}

		return ctx.isPathingTo(
			ctx.getObjectHelper().getNearest(rune.getGuardianId())
		);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.isOutsideRift();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		Rune rune = new RuneDecider(ctx).pick();
		if (rune == null)
		{
			event.consume();
			return;
		}

		event.overrideObjectAction(
			"Enter",
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			ctx.getObjectHelper().getNearest(rune.getGuardianId())
		);
	}
}
