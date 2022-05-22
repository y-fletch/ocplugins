package com.yfletch.rift.util;

import com.yfletch.rift.Guardian;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.ObjectID;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

public class ObjectSize
{
	private static final Map<Integer, Point> sizes = new HashMap<>();

	static
	{
		sizes.put(ObjectID.UNCHARGED_CELLS_43732, new Point(2, 1));
		sizes.put(ObjectID.WEAK_CELLS, new Point(2, 2));
		sizes.put(ObjectID.WORKBENCH_43754, new Point(2, 2));
		sizes.put(ObjectID.LARGE_GUARDIAN_REMAINS, new Point(3, 3));
		sizes.put(ObjectID.HUGE_GUARDIAN_REMAINS, new Point(3, 3));

		for (Guardian guardian : Guardian.values())
		{
			sizes.put(guardian.getObjectId(), new Point(2, 2));
		}
	}

	public static Point get(int objectId)
	{
		return sizes.containsKey(objectId) ? sizes.get(objectId) : new Point(1, 1);
	}

	public static boolean isBeside(WorldPoint player, TileObject object)
	{
		Point objectSize = get(object.getId());
		WorldPoint objectLocation = object.getWorldLocation();

		// top side
		for (int i = 0; i < objectSize.getX(); i++)
		{
			if (player.getY() == objectLocation.getY() + objectSize.getY()
				&& player.getX() == objectLocation.getX() + i)
			{
				return true;
			}
		}

		// bottom side
		for (int i = 0; i < objectSize.getX(); i++)
		{
			if (player.getY() == objectLocation.getY() - 1
				&& player.getX() == objectLocation.getX() + i)
			{
				return true;
			}
		}

		// left side
		for (int j = 0; j < objectSize.getY(); j++)
		{
			if (player.getY() == objectLocation.getY() + j
				&& player.getX() == objectLocation.getX() - 1)
			{
				return true;
			}
		}

		// right side
		for (int j = 0; j < objectSize.getY(); j++)
		{
			if (player.getY() == objectLocation.getY() + j
				&& player.getX() == objectLocation.getX() + objectSize.getX())
			{
				return true;
			}
		}

		return false;
	}
}
