package com.yfletch.ocsepulchre.obstacle;

import com.yfletch.occore.util.ObjectHelper;
import com.yfletch.occore.util.RegionPoint;
import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.Direction;

public abstract class Wizard implements Obstacle, DrawableObstacle
{
	/**
	 * First point within the set of wizard statues
	 */
	private final RegionPoint referencePoint;
	private final Direction direction;
	private final WizardCycle cycle;

	public Wizard(RegionPoint referencePoint, Direction direction, WizardCycle cycle)
	{
		this.referencePoint = referencePoint;
		this.direction = direction;
		this.cycle = cycle;
	}

	public int getCurrentTick()
	{
		return cycle.getCurrentTick();
	}

	private List<TileObject> getFires()
	{
		ObjectHelper objectHelper = ObjectHelper.instance();

		return objectHelper.where(Const.FIRE_OBJECT, obj -> {
			RegionPoint objRegionPoint = RegionPoint.fromWorldInstance(obj.getWorldLocation());

			if (direction == Direction.NORTH || direction == Direction.SOUTH)
			{
				return objRegionPoint.getX() == referencePoint.getX();
			}
			else
			{
				return objRegionPoint.getY() == referencePoint.getY();
			}
		});
	}

	private void trySync()
	{
		if (cycle.isSynced()) return;

		List<Boolean> activeLeft = new ArrayList<>();
		List<Boolean> activeRight = new ArrayList<>();

		int i = 0;
		for (TileObject fire : getFires())
		{
			List<Boolean> arr = i % 2 == 0 ? activeLeft : activeRight;
			int animId = ((DynamicObject) ((GameObject) fire).getRenderable()).getAnimationID();
			arr.add(animId == Const.FIRE_ANIM_ON || animId == Const.FIRE_ANIM_START);

			i++;
		}

		cycle.trySync(activeLeft, activeRight);
	}

	@Override
	public void tick(OCSepulchreContext ctx)
	{
		trySync();

		cycle.tick();
	}

	@Override
	public boolean tickIf(OCSepulchreContext ctx)
	{
		return true;
	}

	@Override
	public String getDebugText()
	{

		return (cycle.getCurrentStepIndex() + " " + cycle.getCurrentTick() + "/" + cycle.getCycleTime() + " " + cycle.getLeftActive() + " / " + cycle.getRightActive())
			.replace("true", "1")
			.replace("false", "0");
	}

	@Override
	public String getDebugTextLine2()
	{
		List<Boolean> activeLeft = new ArrayList<>();
		List<Boolean> activeRight = new ArrayList<>();

		int i = 0;
		for (TileObject fire : getFires())
		{
			List<Boolean> arr = i % 2 == 0 ? activeLeft : activeRight;
			int animId = ((DynamicObject) ((GameObject) fire).getRenderable()).getAnimationID();
			arr.add(animId == Const.FIRE_ANIM_ON || animId == Const.FIRE_ANIM_START);

			i++;
		}

		return (activeLeft + " / " + activeRight)
			.replace("true", "1")
			.replace("false", "0")
			+ " o: " + cycle.getPossibleOffsets() + " s:" + (cycle.isSynced() ? "true" : "false");
	}
}
