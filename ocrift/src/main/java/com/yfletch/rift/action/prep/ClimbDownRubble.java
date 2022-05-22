package com.yfletch.rift.action.prep;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import net.runelite.api.ObjectID;

public class ClimbDownRubble extends ObjectAction<RiftContext>
{
	public ClimbDownRubble()
	{
		super("Climb", "Rubble");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() < 0;
	}

	@Override
	public boolean isWorking(RiftContext ctx)
	{
		return ctx.isPathingTo(ObjectID.RUBBLE_43724);
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.isInLargeMine();
	}
}
