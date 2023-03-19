package com.yfletch.ocsepulchre.action.floor1.south;

import com.yfletch.occore.action.MoveAction;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;

public class MoveToF1S1 extends MoveAction<OCSepulchreContext>
{
	public MoveToF1S1()
	{
		super(Tiles.F1S1);
		setMarkerName("1S1");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isAtEntrance()
			&& ctx.isSouthPath();
	}
}
