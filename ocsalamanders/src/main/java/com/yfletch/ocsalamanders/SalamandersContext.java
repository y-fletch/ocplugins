package com.yfletch.ocsalamanders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import java.util.Map;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;

@Singleton
public class SalamandersContext extends CoreContext
{
	@Inject private Client client;

	@Getter
	private TileObject primaryTree = null;

	public TileObject getNetTrap()
	{
		WorldPoint primaryTreeLocation = primaryTree.getWorldLocation();
		return TileObjects.getNearest(
				primaryTreeLocation,
				o -> o.getName().equals("Net trap")
						&& o.hasAction("Check")
						&& o.distanceTo(primaryTreeLocation) < 10
		);
	}

	public TileObject getYoungTree()
	{
		WorldPoint primaryTreeLocation = primaryTree.getWorldLocation();
		return TileObjects.getNearest(
				primaryTreeLocation,
				o -> o.getName().equals("Young tree")
						&& o.hasAction("Set-trap")
						&& o.distanceTo(primaryTreeLocation) < 10
		);
	}

	public void setPrimaryTree(int x, int y)
	{
		this.primaryTree = TileObjects.getFirstAt(x, y, client.getLocalPlayer().getPlane(), "Young tree");
	}

	public void clearPrimaryTree()
	{
		this.primaryTree = null;
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
