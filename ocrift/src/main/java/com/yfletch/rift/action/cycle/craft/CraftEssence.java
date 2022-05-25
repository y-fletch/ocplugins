package com.yfletch.rift.action.cycle.craft;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;

public class CraftEssence extends ObjectAction<RiftContext>
{
	public CraftEssence()
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
			&& ctx.getFreeInventorySlots() > 3;
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
		return ctx.getFreeInventorySlots() == 0;
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
