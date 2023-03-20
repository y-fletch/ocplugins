package com.yfletch.ocsepulchre.obstacle.floor1;

import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.obstacle.Knight;
import com.yfletch.ocsepulchre.obstacle.SwordDirection;
import net.runelite.api.coords.Direction;

public class F1EKnight extends Knight
{
	public F1EKnight()
	{
		super(Const.F1_KNIGHT_SWORD, Direction.SOUTH);
		setXPlane(0);
	}

	@Override
	public void tick(OCSepulchreContext ctx)
	{
		super.tick(ctx);
	}

	public boolean isSafe()
	{
		return getSwordDirection() == SwordDirection.AWAY
			? getRegionY() < 32 && getRegionY() > 22
			: getRegionY() >= 34;
	}

	@Override
	public boolean tickIf(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isEastPath();
	}
}
