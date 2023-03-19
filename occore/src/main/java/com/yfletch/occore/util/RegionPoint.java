package com.yfletch.occore.util;

import lombok.Value;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.client.Static;

/**
 * Coordinate based on game region, useful for
 * instances
 */
@Value
public class RegionPoint
{
	int regionId;
	int x;
	int y;
	int plane;

	public WorldPoint toWorld()
	{
		return WorldPoint.toLocalInstance(
			Static.getClient(),
			WorldPoint.fromRegion(regionId, x, y, plane)
		).stream().findFirst().orElse(null);
	}

	public LocalPoint toLocal()
	{
		return LocalPoint.fromWorld(Static.getClient(), toWorld());
	}

	public static RegionPoint fromWorld(WorldPoint worldPoint)
	{
		return new RegionPoint(
			worldPoint.getRegionID(),
			worldPoint.getRegionX(),
			worldPoint.getRegionY(),
			worldPoint.getPlane()
		);
	}

	public static RegionPoint fromWorldInstance(WorldPoint worldPoint)
	{
		LocalPoint localPoint = LocalPoint.fromWorld(Static.getClient(), worldPoint);
		return fromLocalInstance(localPoint);
	}

	public static RegionPoint fromLocalInstance(LocalPoint localPoint)
	{
		return fromWorld(WorldPoint.fromLocalInstance(Static.getClient(), localPoint));
	}

	public String toString()
	{
		return regionId + "/" + x + "," + y + "," + plane;
	}
}
