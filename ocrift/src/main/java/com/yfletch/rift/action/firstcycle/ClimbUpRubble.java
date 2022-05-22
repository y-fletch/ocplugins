package com.yfletch.rift.action.firstcycle;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import net.runelite.api.ObjectID;

public class ClimbUpRubble extends ObjectAction<RiftContext>
{
	public ClimbUpRubble()
	{
		super("Climb", "Rubble");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.isInLargeMine()
			&& ctx.getGameTime() > 85;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ObjectID.RUBBLE_43726);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.isInLargeMine();
	}
}
