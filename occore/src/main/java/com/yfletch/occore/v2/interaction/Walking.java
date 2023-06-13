package com.yfletch.occore.v2.interaction;

import net.runelite.api.coords.WorldPoint;
import net.unethicalite.client.Static;

public class Walking
{
	public static DeferredWalkInteraction walk(WorldPoint target)
	{
		return new DeferredWalkInteraction(target);
	}

	public static DeferredWalkInteraction walk(int x, int y)
	{
		final var current = Static.getClient().getLocalPlayer().getWorldLocation();
		return new DeferredWalkInteraction(offset(current, x, y));
	}

	public static WorldPoint offset(WorldPoint origin, int x, int y)
	{
		return new WorldPoint(origin.getX() + x, origin.getY() + y, origin.getPlane());
	}
}
