package com.yfletch.rift.action.cycle.common;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

public class CraftRunes extends ObjectAction<RiftContext>
{
	public CraftRunes()
	{
		super("Craft-rune", "Altar");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.isOutsideRift()
			&& ctx.hasItem(ItemID.GUARDIAN_ESSENCE);
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ctx.getObjectHelper().getNearest("Altar"));
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.hasItem(ItemID.GUARDIAN_ESSENCE);
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction(
			"Craft-rune",
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			ctx.getObjectHelper().getNearest("Altar")
		);
	}
}
