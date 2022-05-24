package com.yfletch.rift.action.firstcycle;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;

public class MineLargeRemains extends ObjectAction<RiftContext>
{
	public MineLargeRemains()
	{
		super("Mine", "Large guardian remains");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() >= 0
			&& ctx.getGameTime() < ctx.getExitMineTime()
			&& ctx.isInLargeMine();
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isMining();
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getGameTime() >= ctx.getExitMineTime();
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction(
			"Mine",
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			ObjectID.LARGE_GUARDIAN_REMAINS
		);
	}
}
