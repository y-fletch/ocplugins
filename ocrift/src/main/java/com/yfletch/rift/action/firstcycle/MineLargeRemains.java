package com.yfletch.rift.action.firstcycle;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;

public class MineLargeRemains extends ObjectAction<RiftContext>
{
	public MineLargeRemains()
	{
		super("Mine", "Large guardian remains");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() == 0
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
		return ctx.getGameTime() > 85;
	}
}
