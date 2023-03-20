package com.yfletch.ocsepulchre.obstacle.floor1;

import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;
import com.yfletch.ocsepulchre.obstacle.Knight;
import com.yfletch.ocsepulchre.obstacle.SwordDirection;
import net.runelite.api.coords.Direction;

public class F1SKnight extends Knight
{
	public F1SKnight()
	{
		super(Const.F1_KNIGHT_SWORD, Direction.WEST);
		setYPlane(0);
	}

	public boolean isSafe(OCSepulchreContext ctx)
	{
		if (ctx.isAt(Tiles.F1S1a))
		{
			return getRegionX() > 31
				|| getSwordDirection() == SwordDirection.AWAY
				&& getRegionX() > 20;
		}

		return getSwordDirection() == SwordDirection.AWAY
			&& getRegionX() > 24
			&& getRegionX() < 40;
	}

	public boolean isNookSafe(OCSepulchreContext ctx)
	{
		return (getSwordDirection() == SwordDirection.AWAY
			&& getRegionX() < 40)
			|| getSwordDirection() == SwordDirection.TOWARD
			&& getRegionX() < 23;
	}

	@Override
	public boolean tickIf(OCSepulchreContext ctx)
	{
		return ctx.isFloor(1)
			&& ctx.isSouthPath();
	}
}
