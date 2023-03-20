package com.yfletch.ocsepulchre.action.floor1.east;

import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;
import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1E4 extends ObjectAction<OCSepulchreContext>
{
	public MoveToF1E4()
	{
		super("Climb-down", "Stairs");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isEastPath()
			&& ctx.isAt(Tiles.F1E3)
			&& ctx.getObstacles().getF1EWizard2().isSafe();
	}

	@Override
	public boolean isWorking(OCSepulchreContext ctx)
	{
		return ctx.isPathingTo(Const.F1E_STAIRS);
	}

	@Override
	public boolean isDone(OCSepulchreContext ctx)
	{
		return super.isDone(ctx);
	}

	@Override
	public void run(OCSepulchreContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setObject(Const.F1E_STAIRS)
			.setOption("Climb-down", 1)
			.override();
	}
}
