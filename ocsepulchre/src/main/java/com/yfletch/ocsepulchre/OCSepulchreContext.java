package com.yfletch.ocsepulchre;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.ObjectHelper;
import com.yfletch.occore.util.RegionPoint;
import com.yfletch.ocsepulchre.obstacle.Obstacles;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class OCSepulchreContext extends ActionContext
{
	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	@Getter
	@Inject
	private Obstacles obstacles;

	private TileObject recentEntrance;

	@Setter
	@Getter
	private int floor = 1;

	@Getter
	private Direction floorPath = Direction.SOUTH;

	@Override
	public void tick()
	{
		super.tick();
		obstacles.tick();

		// reset path after finishing a run
		if (isInLobby() || floor == 0)
		{
			floor = 0;
			floorPath = null;
		}

		isAtEntrance();
	}

	public boolean isInLobby()
	{
		return WorldPoint.isInZone(
			new WorldPoint(2387, 5969, 0),
			new WorldPoint(2413, 5990, 0),
			getPlayerLocation()
		);
	}

	public RegionPoint getPlayerRegionLocation()
	{
		final WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, getPlayer().getLocalLocation());
		return RegionPoint.fromWorld(worldPoint);
	}

	/**
	 * Check if player is at an entrance. Internally stores extra
	 * information about the entrance and path direction
	 */
	public boolean isAtEntrance()
	{
		GameObject nearestStairs = (GameObject) objectHelper.getNearest(Const.EXIT_STAIRS);
		if (nearestStairs == null)
		{
			return false;
		}

		if (objectHelper.isBeside(getPlayer(), nearestStairs))
		{
			if (floor == 0)
			{
				floor = 1;
			}

			recentEntrance = nearestStairs;
			determineFloorPath();
			return true;
		}

		return false;
	}

	private void determineFloorPath()
	{
		WorldPoint entrance = recentEntrance.getWorldLocation();

		if (entrance.getX() > getPlayerLocation().getX())
		{
			floorPath = Direction.WEST;
		}
		else if (entrance.getX() < getPlayerLocation().getX())
		{
			floorPath = Direction.EAST;
		}
		else if (entrance.getY() > getPlayerLocation().getY())
		{
			floorPath = Direction.SOUTH;
		}
		else if (entrance.getY() < getPlayerLocation().getY())
		{
			floorPath = Direction.NORTH;
		}

		// flip directions on floor 1
		if (floor == 1)
		{
			switch (floorPath)
			{
				case NORTH:
					floorPath = Direction.SOUTH;
					return;
				case SOUTH:
					floorPath = Direction.NORTH;
					return;
				case WEST:
					floorPath = Direction.EAST;
					return;
				case EAST:
					floorPath = Direction.WEST;
					return;
			}
		}
	}

	public boolean isFloor(int testFloor)
	{
		return floor == testFloor;
	}

	public boolean isEastPath()
	{
		return floorPath == Direction.EAST;
	}

	public boolean isWestPath()
	{
		return floorPath == Direction.WEST;
	}

	public boolean isNorthPath()
	{
		return floorPath == Direction.NORTH;
	}

	public boolean isSouthPath()
	{
		return floorPath == Direction.SOUTH;
	}
}
