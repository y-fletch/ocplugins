package com.yfletch.rift.action.cycle.mine;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;

public class MineHugeRemains extends ObjectAction<RiftContext>
{
	public MineHugeRemains()
	{
		super("Mine", "Huge guardian remains");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.isInHugeMine()
			&& ctx.getFreeInventorySlots() > 0;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		if (ctx.isPathingTo(ObjectID.HUGE_GUARDIAN_REMAINS))
		{
			ctx.flag("mining", true);
			return true;
		}

		return ctx.flag("mining")
			&& ctx.isNextTo(ObjectID.HUGE_GUARDIAN_REMAINS);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getFreeInventorySlots() == 0;
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction("Mine", MenuAction.GAME_OBJECT_FIRST_OPTION, ObjectID.HUGE_GUARDIAN_REMAINS);

		ctx.flag("mining", true);
	}
}
