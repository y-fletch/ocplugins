package com.yfletch.ocsepulchre.obstacle.floor1;

import com.yfletch.occore.util.RegionPoint;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.Tiles;
import com.yfletch.ocsepulchre.obstacle.Wizard;
import com.yfletch.ocsepulchre.obstacle.WizardCycle;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.WorldPoint;

public class F1EWizard1 extends Wizard
{
	public F1EWizard1()
	{
		super(
			new RegionPoint(9053, 59, 52, 2),
			Direction.SOUTH,
			new WizardCycle()
				.add(new int[]{0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0}, 5)
				.add(new int[]{0, 0, 1, 1, 1}, new int[]{1, 1, 1, 0, 0}, 3)
		);
	}

	public boolean isSafe(OCSepulchreContext ctx)
	{
		// 7 is safe tick, but go 1t earlier
		return getCurrentTick() == 6;
	}

	@Override
	public WorldPoint getDebugPosition()
	{
		return Tiles.F1E1.toWorld();
	}
}
