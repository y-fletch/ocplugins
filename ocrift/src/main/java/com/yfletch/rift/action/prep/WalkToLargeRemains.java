package com.yfletch.rift.action.prep;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import net.runelite.api.ObjectID;

public class WalkToLargeRemains extends ObjectAction<RiftContext>
{
	public WalkToLargeRemains()
	{
		super("Mine", "Large guardian remains");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() < 0
			&& ctx.isInLargeMine();
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ObjectID.LARGE_GUARDIAN_REMAINS);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.isNextTo(ObjectID.LARGE_GUARDIAN_REMAINS);
	}
}
