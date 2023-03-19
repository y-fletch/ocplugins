package com.yfletch.ocsepulchre.action.bank;

import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;
import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;

public class ClimbDownStairs extends ObjectAction<OCSepulchreContext>
{
	public ClimbDownStairs()
	{
		super("Climb-down", "Stairs");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isInLobby();
	}

	@Override
	public boolean isWorking(OCSepulchreContext ctx)
	{
		return ctx.isPathingTo(Const.LOBBY_ENTRANCE_STAIRS);
	}

	@Override
	public boolean isDone(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1);
	}

	@Override
	public void run(OCSepulchreContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setObject(Const.LOBBY_ENTRANCE_STAIRS)
			.setOption("Climb-down", 1)
			.override();
	}
}
