package com.yfletch.ocherbi;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import static com.yfletch.occore.v2.util.Util.withAction;
import java.util.List;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.herbiboars.HerbiboarPlugin;
import net.unethicalite.api.entities.TileObjects;

@Singleton
public class HerbiContext extends CoreContext
{
	@Inject private Client client;
	@Inject private HerbiboarPlugin herbiboar;

	private static final List<WorldPoint> END_LOCATIONS = List.of(
		new WorldPoint(3693, 3798, 0),
		new WorldPoint(3702, 3808, 0),
		new WorldPoint(3703, 3826, 0),
		new WorldPoint(3710, 3881, 0),
		new WorldPoint(3700, 3877, 0),
		new WorldPoint(3715, 3840, 0),
		new WorldPoint(3751, 3849, 0),
		new WorldPoint(3685, 3869, 0),
		new WorldPoint(3681, 3863, 0)
	);

	private static final List<WorldPoint> START_LOCATIONS = List.of(
		new WorldPoint(3686, 3870, 0),
		new WorldPoint(3751, 3850, 0),
		new WorldPoint(3695, 3800, 0),
		new WorldPoint(3704, 3810, 0),
		new WorldPoint(3705, 3830, 0)
	);

	public static final WorldPoint BANK_LOCATION = new WorldPoint(3737, 3804, 0);

	public int getRunEnergy()
	{
		return client.getEnergy() / 100;
	}

	public WorldPoint getNearestStartLocation()
	{
		final var player = client.getLocalPlayer().getWorldLocation();

		WorldPoint nearest = null;
		int nearestDist = Integer.MAX_VALUE;
		for (var point : START_LOCATIONS)
		{
			final var dist = player.distanceTo(point);
			if (dist < nearestDist)
			{
				nearest = point;
				nearestDist = dist;
			}
		}

		return nearest;
	}

	public TileObject getNextObject()
	{
		if (herbiboar.getFinishId() > 0 || herbiboar.getCurrentGroup() == null)
		{
			return null;
		}

		return TileObjects.getFirstAt(getNextLocation(), withAction("Inspect"));
	}

	public WorldPoint getNextLocation()
	{
		if (herbiboar.getFinishId() > 0 || herbiboar.getCurrentGroup() == null)
		{
			return null;
		}

		final var lastSpot = ((Enum<?>) Iterables.getLast(herbiboar.getCurrentPath())).toString();
		return HerbiboarSearchSpot.valueOf(lastSpot).getLocation();
	}

	public TileObject getTunnel()
	{
		if (herbiboar.getFinishId() <= 0)
		{
			return null;
		}

		return herbiboar.getTunnels().get(getTunnelLocation());
	}

	public WorldPoint getTunnelLocation()
	{
		if (herbiboar.getFinishId() <= 0)
		{
			return null;
		}

		return END_LOCATIONS.get(herbiboar.getFinishId() - 1);
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();

		try
		{
			final var nextLoc = getNextLocation();
			map.put("Next location", nextLoc == null ? "act. null" : nextLoc.getX() + ", " + nextLoc.getY());

			final var nextObj = getNextObject();
			map.put("Next object", ((Enum<?>) Iterables.getLast(herbiboar.getCurrentPath())).toString());

			final var tunnel = getTunnel();
			map.put(
				"Next tunnel",
				tunnel == null ? "act. null" : tunnel.getWorldLocation().getX() + ", " + tunnel.getWorldLocation()
					.getY()
			);

			map.put("Energy", client.getEnergy() + "");
		}
		catch (Exception ignored)
		{
		}

		return map;
	}
}
