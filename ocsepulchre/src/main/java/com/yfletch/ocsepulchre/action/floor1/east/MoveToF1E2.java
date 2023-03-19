package com.yfletch.ocsepulchre.action.floor1.east;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1E2 extends MoveAction<OCSepulchreContext>
{
	public MoveToF1E2()
	{
		super(Tiles.F1E2);
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isEastPath()
			&& ctx.getObstacles().getF1EWizard1().isSafe(ctx);
	}
}
