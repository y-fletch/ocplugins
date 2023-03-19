package com.yfletch.ocsepulchre.action.floor1.north;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1N1 extends MoveAction<OCSepulchreContext>
{
	public MoveToF1N1()
	{
		super(Tiles.F1N1);
		setMarkerName("1N1");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isAtEntrance()
			&& ctx.isNorthPath();
	}
}
