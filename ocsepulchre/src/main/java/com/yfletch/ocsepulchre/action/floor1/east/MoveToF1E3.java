package com.yfletch.ocsepulchre.action.floor1.east;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1E3 extends MoveAction<OCSepulchreContext>
{
	public MoveToF1E3()
	{
		super(Tiles.F1E3);
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isEastPath()
			&& ctx.isAt(Tiles.F1E2)
			&& ctx.getObstacles().getF1EKnight().isSafe();
	}
}
