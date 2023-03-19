package com.yfletch.rift.action.cycle.craft;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;

public class WorkAtWorkbench extends ObjectAction<RiftContext>
{
	private final int WORKBENCH = ObjectID.WORKBENCH_43754;

	public WorkAtWorkbench()
	{
		super("Work-at", "Workbench");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		boolean hasAcceptableFreeSlots =
			ctx.getFreeInventorySlots() > 3
				|| ctx.getFreeInventorySlots() > 0
				&& ctx.isNextTo(WORKBENCH);

		return !ctx.flag("mine-cycle")
			&& !ctx.hasRunes()
			&& ctx.getGameTime() > ctx.getExitMineTime()
			&& !ctx.isOutsideRift()
			&& hasAcceptableFreeSlots
			|| ctx.flag("crafting");

		// FYI "crafting" flag is turned off in
		// place cell, power up guardian, and fill pouch
		// so one of those has to be next (can't go
		// straight to an altar)
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		if (ctx.isPathingTo(WORKBENCH))
		{
			ctx.flag("crafting", true);
			return true;
		}

		return ctx.flag("crafting")
			&& ctx.isNextTo(WORKBENCH);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getFreeInventorySlots() == 0
			|| !ctx.hasItem(ItemID.GUARDIAN_FRAGMENTS);
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction(
			"Work-at",
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			WORKBENCH
		);

		ctx.flag("crafting", true);
	}
}
