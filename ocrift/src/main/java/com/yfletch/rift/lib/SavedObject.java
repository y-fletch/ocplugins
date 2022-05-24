package com.yfletch.rift.lib;

import lombok.Getter;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

/**
 * TileObject wrapper because apparently TileObjects
 * can move *shrug*
 */
@Getter
public class SavedObject
{
	private TileObject object;
	private WorldPoint initialLocation;

	SavedObject(TileObject object)
	{
		this.object = object;
		this.initialLocation = new WorldPoint(
			object.getWorldLocation().getX(),
			object.getWorldLocation().getY(),
			object.getWorldLocation().getPlane()
		);
	}

	public boolean isValid()
	{
		if (!equalPoints(object.getWorldLocation(), initialLocation))
		{
			System.out.println("Invalid: " + object.getId());
		}

		return equalPoints(object.getWorldLocation(), initialLocation);
	}

	private boolean equalPoints(WorldPoint w1, WorldPoint w2)
	{
		return w1.getX() == w2.getX()
			&& w1.getY() == w2.getY()
			&& w1.getPlane() == w2.getPlane();
	}

	public boolean equals(TileObject other)
	{
		return object.equals(other)
			|| (
			object.getId() == other.getId() && (
				equalPoints(object.getWorldLocation(), other.getWorldLocation())
					|| equalPoints(initialLocation, other.getWorldLocation())
			)
		);
	}

	public boolean equals(SavedObject other)
	{
		return equals(other.getObject());
	}
}
