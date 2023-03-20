package com.yfletch.ocsepulchre.obstacle.floor1;

import com.yfletch.occore.util.RegionPoint;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.obstacle.Wizard;
import com.yfletch.ocsepulchre.obstacle.WizardCycle;
import net.runelite.api.coords.Direction;

public class F1EWizard1 extends Wizard
{
	public F1EWizard1()
	{
		super(
			new RegionPoint(9053, 59, 52, 2),
			Direction.SOUTH,
			new WizardCycle()
				.add(new int[]{0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0}, 5)
				.add(new int[]{0, 0, 1, 1, 1}, new int[]{1, 1, 1, 0, 0}, 3),
			10
		);
	}

	public boolean isSafe(OCSepulchreContext ctx)
	{
		return isSynced()
			&& getCurrentTick() == 7;
	}
}
