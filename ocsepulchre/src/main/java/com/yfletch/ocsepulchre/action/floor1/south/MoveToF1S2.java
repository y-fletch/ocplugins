package com.yfletch.ocsepulchre.action.floor1.south;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1S2 extends MoveAction<OCSepulchreContext>
{
	public MoveToF1S2()
	{
		super(Tiles.F1S2);
		setMarkerName("1S2");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isSouthPath()
			&& (ctx.isAt(Tiles.F1S1) || ctx.isAt(Tiles.F1S1a))
			&& ctx.getObstacles().getF1SKnight().isSafe(ctx);
	}
}
