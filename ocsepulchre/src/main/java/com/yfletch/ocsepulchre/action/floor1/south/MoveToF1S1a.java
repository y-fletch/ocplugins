package com.yfletch.ocsepulchre.action.floor1.south;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1S1a extends MoveAction<OCSepulchreContext>
{
	public MoveToF1S1a()
	{
		super(Tiles.F1S1a);
		setMarkerName("1S1a");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isSouthPath()
			&& ctx.isAt(Tiles.F1S1)
			&& ctx.getObstacles().getF1SKnight().isNookSafe(ctx);
	}
}
