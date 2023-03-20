package com.yfletch.ocsepulchre.obstacle;

import com.yfletch.occore.util.ObjectHelper;
import com.yfletch.occore.util.RegionPoint;
import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.util.TileDebugInfo;
import java.util.ArrayList;
import java.util.Collection;
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
	private final int totalStatues;

	public Wizard(RegionPoint referencePoint, Direction direction, WizardCycle cycle, int totalStatues)
	{
		this.referencePoint = referencePoint;
		this.direction = direction;
		this.cycle = cycle;
		this.totalStatues = totalStatues;
	}

	public boolean isSynced()
	{
		return cycle.isSynced();
	}

	public int getCurrentTick()
	{
		return cycle.getCurrentTick();
	}

	private List<TileObject> getFires()
	{
		ObjectHelper objectHelper = ObjectHelper.instance();

		List<TileObject> fires = objectHelper.where(Const.FIRE_OBJECT, obj -> {
			RegionPoint objRegionPoint = RegionPoint.fromWorldInstance(obj.getWorldLocation());

			if (direction == Direction.NORTH || direction == Direction.SOUTH)
			{
				return objRegionPoint.getX() == referencePoint.getX()
					&& Math.abs(objRegionPoint.getY() - referencePoint.getY()) <= totalStatues;
			}
			else
			{
				return objRegionPoint.getY() == referencePoint.getY()
					&& Math.abs(objRegionPoint.getX() - referencePoint.getX()) <= totalStatues;
			}
		});

		// put in zig-zag order, beginning closest left of reference tile,
		// ending farthest right of reference tile
		fires.sort((a, b) -> {
			RegionPoint aRegion = RegionPoint.fromWorldInstance(a.getWorldLocation());
			RegionPoint bRegion = RegionPoint.fromWorldInstance(b.getWorldLocation());
			int aDist = direction == Direction.NORTH || direction == Direction.SOUTH
				? Math.abs(aRegion.getY() - referencePoint.getY())
				: Math.abs(aRegion.getX() - referencePoint.getX());
			int bDist = direction == Direction.NORTH || direction == Direction.SOUTH
				? Math.abs(bRegion.getY() - referencePoint.getY())
				: Math.abs(bRegion.getX() - referencePoint.getX());

			if (aDist == bDist)
			{
				// prefer left
				aDist -= 1;
			}

			return aDist - bDist;
		});

		return fires;
	}

	private void trySync()
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

		cycle.trySync(activeLeft, activeRight);
	}

	@Override
	public void tick(OCSepulchreContext ctx)
	{
		cycle.tick();
		trySync();
	}

	@Override
	public boolean tickIf(OCSepulchreContext ctx)
	{
		return true;
	}

	public String getDebugLine1()
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
			+ " o: " + cycle.getPossibleOffsets() + " s:" + (cycle.isSynced() ? "true" : "false")
			+ " f: " + getFires().size();
	}

	public String getDebugLine2()
	{

		return (cycle.getCurrentStepIndex() + " " + cycle.getCurrentTick() + "/" + cycle.getCycleTime() + " " + cycle.getLeftActive() + " / " + cycle.getRightActive())
			.replace("true", "1")
			.replace("false", "0");
	}

	@Override
	public Collection<TileDebugInfo> getTileDebug()
	{
		List<TileDebugInfo> debugs = new ArrayList<>();

		// main tile
		debugs.add(new TileDebugInfo(referencePoint, getDebugLine1(), getDebugLine2()));

		// obstacle orders
//		int i = 0;
//		for (TileObject fire : getFires())
//		{
//			debugs.add(new TileDebugInfo(
//				RegionPoint.fromWorldInstance(fire.getWorldLocation()),
//				i + ""
//			));
//			i++;
//		}

		return debugs;
	}
}
