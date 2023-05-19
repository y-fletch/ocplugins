package com.yfletch.ocsalamanders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

@Singleton
public class SalamandersContext extends CoreContext
{
	@Inject private Client client;

	public TileObject getNetTrap()
	{
		return TileObjects.getNearest(o -> o.getName().equals("Net trap") && o.hasAction("Check"));
	}

	public TileObject getYoungTree()
	{
		return TileObjects.getNearest(o -> o.getName().equals("Young tree") && o.hasAction("Set-trap"));
	}

	public boolean canPlaceTrap()
	{
		return Inventory.contains("Rope") && Inventory.contains("Small fishing net");
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();
		map.put("can-place-trap", "" + canPlaceTrap());

		return map;
	}
}
