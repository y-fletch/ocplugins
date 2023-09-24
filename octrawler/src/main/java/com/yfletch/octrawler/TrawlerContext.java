package com.yfletch.octrawler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import static com.yfletch.occore.v2.interaction.Entities.object;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.TileObjects;

@Singleton
public class TrawlerContext extends CoreContext
{
	@Inject private Client client;

	@Getter
	@Setter
	private int activity;

	public WorldPoint getPlayerLocation()
	{
		return client.getLocalPlayer().getWorldLocation();
	}

	public boolean isInPortKhazard()
	{
		return getPlayerLocation()
			.isInArea(new WorldArea(
				2655, 3155,
				30, 30,
				0
			));
	}

	public boolean isOnShip()
	{
		return getPlayerLocation()
			.isInArea(new WorldArea(
				2668, 3167,
				6, 20,
				1
			));

		// 1886 4825
	}

	public boolean isLeakInRange()
	{
		final var leak = object("Leak");
		if (!leak.exists()) return false;

		return getPlayerLocation().distanceTo(leak.unwrap()) < 2;
	}

	public boolean isOnWall()
	{
		final var ladder = object("Winch");
		if (!ladder.exists()) return false;

		return getPlayerLocation().getY() == ladder.unwrap().getWorldY();
	}

	public TileObject getGangplank()
	{
		return TileObjects.getFirstAt(2675, 3170, 0, "Gangplank");
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();

		map.put("Contribution", "" + getActivity());
		map.put("On landing ship", "" + isOnShip());
		map.put("Against wall", "" + isOnWall());
		map.put("In port", "" + isInPortKhazard());

		return map;
	}
}
