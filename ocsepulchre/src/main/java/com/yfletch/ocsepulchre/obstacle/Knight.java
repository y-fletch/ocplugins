package com.yfletch.ocsepulchre.obstacle;

import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.RegionPoint;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import com.yfletch.ocsepulchre.util.TileDebugInfo;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.WorldPoint;
import org.jetbrains.annotations.NotNull;

public abstract class Knight implements Obstacle, DrawableObstacle
{
	private final int npcId;

	/**
	 * Direction the statue is facing
	 */
	private final Direction facing;

	/**
	 * Only match swords on this x plane
	 */
	@Setter
	private int xPlane = -1;

	/**
	 * Only match swords on this y plane
	 */
	@Setter
	private int yPlane = -1;

	@Nullable
	@Getter
	private WorldPoint location;

	@Nullable
	@Getter
	private WorldPoint previousLocation;

	@Nullable
	@Getter
	private SwordDirection swordDirection;

	public Knight(int npcId, @NotNull Direction facing)
	{
		this.npcId = npcId;
		this.facing = facing;
	}

	@Override
	public void tick(OCSepulchreContext ctx)
	{
		NPC npc = NpcHelper.instance().getNearest(npcId);
		if (npc == null || npc.getWorldLocation() == null)
		{
			return;
		}

		// ensure correct NPC has been selected
		RegionPoint regionPoint = RegionPoint.fromWorldInstance(npc.getWorldLocation());
		if ((xPlane > -1 && regionPoint.getX() != xPlane)
			|| (yPlane > -1 && regionPoint.getY() != yPlane))
		{
			return;
		}

		previousLocation = location;
		location = npc.getWorldLocation();

		if (location == null || previousLocation == null)
		{
			return;
		}

		if (location.getX() > previousLocation.getX())
		{
			swordDirection = facing == Direction.WEST
				? SwordDirection.TOWARD : SwordDirection.AWAY;
		}
		else if (location.getX() < previousLocation.getX())
		{
			swordDirection = facing == Direction.EAST
				? SwordDirection.TOWARD : SwordDirection.AWAY;
		}
		else if (location.getY() > previousLocation.getY())
		{
			swordDirection = facing == Direction.SOUTH
				? SwordDirection.TOWARD : SwordDirection.AWAY;
		}
		else if (location.getY() < previousLocation.getY())
		{
			swordDirection = facing == Direction.NORTH
				? SwordDirection.TOWARD : SwordDirection.AWAY;
		}
	}

	public int getRegionX()
	{
		if (location == null) return -1;
		return RegionPoint.fromWorldInstance(location).getX();
	}

	public int getRegionY()
	{
		if (location == null) return -1;
		return RegionPoint.fromWorldInstance(location).getY();
	}

	@Override
	public Collection<TileDebugInfo> getTileDebug()
	{
		if (location == null) return null;

		return List.of(
			new TileDebugInfo(
				RegionPoint.fromWorldInstance(location),
				facing + " / " + swordDirection + " / " + RegionPoint.fromWorldInstance(location)
			)
		);
	}
}
