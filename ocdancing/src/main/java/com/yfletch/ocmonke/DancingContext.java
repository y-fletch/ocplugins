package com.yfletch.ocmonke;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.client.Static;

@Singleton
public class DancingContext extends CoreContext
{
	@Inject private DancingConfig config;

	public WorldPoint getPlayerLocation()
	{
		return Static.getClient().getLocalPlayer().getWorldLocation();
	}

	public WorldPoint getTile1()
	{
		return stringToWorldPoint(config.tile1());
	}

	public WorldPoint getTile2()
	{
		return stringToWorldPoint(config.tile2());
	}

	@Nullable
	private static WorldPoint stringToWorldPoint(String in)
	{
		try
		{
			var bits = Arrays.stream(in.split("\\s*,\\s*")).mapToInt(Integer::valueOf).toArray();
			return new WorldPoint(bits[0], bits[1], bits[2]);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static String worldPointToString(WorldPoint in)
	{
		return in.getX() + "," + in.getY() + "," + in.getPlane();
	}
}
