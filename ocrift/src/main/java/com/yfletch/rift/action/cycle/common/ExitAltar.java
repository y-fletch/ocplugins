package com.yfletch.rift.action.cycle.common;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

public class ExitAltar extends ObjectAction<RiftContext>
{
	public ExitAltar()
	{
		super("Use", "Portal");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.isOutsideRift()
			&& !ctx.hasItem(ItemID.GUARDIAN_ESSENCE)
			&& ctx.allPouchesAreEmpty();
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ctx.getObjectHelper().getNearest("Portal"));
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.isOutsideRift();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction(
			"Craft-run",
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			ctx.getObjectHelper().getNearest("Portal")
		);
	}

	@Override
	public void done(RiftContext ctx)
	{
		// toggle cycle
		ctx.flag("mine-cycle", !ctx.flag("mine-cycle"));
	}
}
