package com.yfletch.ocsepulchre.action.floor1.west;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1W1 extends MoveAction<OCSepulchreContext>
{
	public MoveToF1W1()
	{
		super(Tiles.F1W1);
		setMarkerName("1W1");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isAtEntrance()
			&& ctx.isWestPath();
	}
}
