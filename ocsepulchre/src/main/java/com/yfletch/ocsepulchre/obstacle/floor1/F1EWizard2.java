package com.yfletch.ocsepulchre.obstacle.floor1;

import com.yfletch.occore.util.RegionPoint;
import com.yfletch.ocsepulchre.obstacle.Wizard;
import com.yfletch.ocsepulchre.obstacle.WizardCycle;
import net.runelite.api.coords.Direction;

public class F1EWizard2 extends Wizard
{
	public F1EWizard2()
	{
		super(
			new RegionPoint(9053, 54, 17, 2),
			Direction.WEST,
			new WizardCycle()
				.add(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, 5)
				.add(new int[]{1, 0, 1, 1}, new int[]{1, 0, 1, 0}, 3),
			8
		);
	}

	public boolean isSafe()
	{
		return isSynced()
			&& (getCurrentTick() >= 6);
	}
}
