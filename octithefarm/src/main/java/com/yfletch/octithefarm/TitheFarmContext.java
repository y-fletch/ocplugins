package com.yfletch.octithefarm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import static com.yfletch.occore.v2.util.Util.nameContaining;
import static com.yfletch.occore.v2.util.Util.offset;
import static com.yfletch.occore.v2.util.Util.withAction;
import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.TileObjects;

@Singleton
public class TitheFarmContext extends CoreContext
{
	@Inject private Client client;

	private final List<Point> patches = List.of(
		// first two
		new Point(44, 48),
		new Point(39, 48),
		new Point(44, 45),
		new Point(39, 45),
		new Point(44, 42),
		new Point(39, 42),
		new Point(44, 39),
		new Point(39, 39),
		// third
		new Point(44, 33),
		new Point(44, 30),
		new Point(44, 27),
		new Point(44, 24),
		// fourth
		new Point(39, 24),
		new Point(39, 27),
		new Point(39, 30),
		new Point(39, 33),
		// fifth
		new Point(34, 39),
		new Point(34, 42),
		new Point(34, 45),
		new Point(34, 48)
	);

	private int patchIndex = 0;

	private WorldPoint getWorldPoint(Point point)
	{
		final var player = client.getLocalPlayer().getWorldLocation();
		return WorldPoint.fromRegion(player.getRegionID(), point.x, point.y, player.getPlane());
	}

	public void next()
	{
		patchIndex++;
		if (patchIndex >= patches.size())
		{
			patchIndex = 0;
		}
	}

	private TileObject getNextObject(Predicate<TileObject> predicate)
	{
		final var wp = getWorldPoint(patches.get(patchIndex));
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				final var obj = TileObjects.getFirstAt(offset(wp, i, j), predicate);
				if (obj != null)
				{
					return obj;
				}
			}
		}
		return null;
	}

	public TileObject getNextPatch()
	{
		return getNextObject(nameContaining("Tithe patch"));
	}

	public TileObject getNextWaterable()
	{
		return getNextObject(withAction("Water"));
	}

	public TileObject getNextHarvestable()
	{
		return getNextObject(withAction("Harvest", "Clear"));
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();

		map.put("Patch", patchIndex + "");

		final var nearest = TileObjects.getNearest("Tithe patch").getWorldLocation();
		map.put(
			"Nearest patch",
			(nearest.getX() - 1) + ", " + (nearest.getY() - 1) + " / " + nearest.getRegionID()
		);
		final var next = getNextPatch();
		final var nextPoint = next == null ? null : next.getWorldLocation();
		map.put(
			"Next patch",
			nextPoint == null ? "null" : (nextPoint.getX() - 1) + ", " + (nextPoint.getY() - 1)
		);

		final var expected = getWorldPoint(patches.get(patchIndex));
		map.put(
			"Expected WP",
			expected.getX() + ", " + expected.getY() + " / " + expected.getRegionID()
		);

		return map;
	}
}
