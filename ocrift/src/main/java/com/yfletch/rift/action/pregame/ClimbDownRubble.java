package com.yfletch.rift.action.pregame;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ObjectAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;
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

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideObjectAction(
			"Climb",
			MenuAction.GAME_OBJECT_FIRST_OPTION,
			ObjectID.RUBBLE_43724
		);
	}
}
