package com.yfletch.rift.util;

import com.yfletch.rift.Guardian;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.ObjectID;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.GameObjectQuery;

@Singleton
public class ObjectHelper
{
	@Inject
	private Client client;

	private final Map<Integer, Point> sizes = new HashMap<>();

	ObjectHelper()
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

	public TileObject getNearest(int objectId)
	{
		return new GameObjectQuery().idEquals(objectId).result(client).nearestTo(client.getLocalPlayer());
	}

	/**
	 * Get the static size of the object
	 */
	public Point getSize(int objectId)
	{
		return sizes.containsKey(objectId) ? sizes.get(objectId) : new Point(1, 1);
	}

	/**
	 * Determine whether a point (the player) is beside an object, taking
	 * the object's size into account. Used to check if the player is pathing
	 * to/already beside a game object.
	 */
	public boolean isBeside(WorldPoint player, TileObject object)
	{
		Point size = getSize(object.getId());
		WorldPoint objectLocation = object.getWorldLocation();

		int leftOffset = (-1 - size.getX()) / 2;
		int bottomOffset = (-1 - size.getY()) / 2;

		int rightOffset = size.getX() / 2 + 1;
		int topOffset = size.getY() / 2 + 1;

		// top side
		for (int i = 0; i < size.getX(); i++)
		{
			if (player.getY() == objectLocation.getY() + topOffset
				&& player.getX() == objectLocation.getX() + i + leftOffset / 2)
			{
				return true;
			}
		}

		// bottom side
		for (int i = 0; i < size.getX(); i++)
		{
			if (player.getY() == objectLocation.getY() + bottomOffset
				&& player.getX() == objectLocation.getX() + i + leftOffset / 2)
			{
				return true;
			}
		}

		// left side
		for (int j = 0; j < size.getY(); j++)
		{
			if (player.getY() == objectLocation.getY() + j + bottomOffset / 2
				&& player.getX() == objectLocation.getX() + leftOffset)
			{
				return true;
			}
		}

		// right side
		for (int j = 0; j < size.getY(); j++)
		{
			if (player.getY() == objectLocation.getY() + j + bottomOffset / 2
				&& player.getX() == objectLocation.getX() + rightOffset)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Get the center of the object, offset from the bottom-left.
	 * The negative of this can be used to get the bottom-left tile
	 * of the object - which is what's clicked when interacting with
	 * it.
	 */
	public Point getCenterOffset(int objectId)
	{
		Point size = getSize(objectId);
		return new Point((size.getX() - 1) / 2, (size.getY() - 1) / 2);
	}
}
