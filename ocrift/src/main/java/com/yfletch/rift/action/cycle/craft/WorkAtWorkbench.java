package com.yfletch.rift.action.cycle.craft;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;

public class WorkAtWorkbench extends ObjectAction<RiftContext>
{
	public WorkAtWorkbench()
	{
		super("Work-at", "Workbench");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return !ctx.flag("mine-cycle")
			&& !ctx.hasRunes()
			&& ctx.getGameTime() > ctx.getExitMineTime()
			&& !ctx.isOutsideRift()
			&& ctx.getFreeInventorySlots() > 3
			|| ctx.flag("crafting");
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		if (ctx.isPathingTo(ObjectID.WORKBENCH_43754))
		{
			ctx.flag("crafting", true);
			return true;
		}

		return ctx.flag("crafting")
			&& ctx.isNextTo(ObjectID.WORKBENCH_43754);
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
			ObjectID.WORKBENCH_43754
		);

		ctx.flag("crafting", true);
	}
}
