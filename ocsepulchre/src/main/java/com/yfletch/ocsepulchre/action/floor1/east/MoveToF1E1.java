package com.yfletch.ocsepulchre.action.floor1.east;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1E1 extends MoveAction<OCSepulchreContext>
{
	public MoveToF1E1()
	{
		super(Tiles.F1E1);
		setMarkerName("1E1");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isAtEntrance()
			&& ctx.isEastPath();
	}
}
