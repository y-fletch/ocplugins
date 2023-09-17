package com.yfletch.ocpickpocket;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import static com.yfletch.occore.v2.interaction.Entities.entity;
import static com.yfletch.occore.v2.util.Util.containing;
import static com.yfletch.occore.v2.util.Util.nameMatching;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.movement.pathfinder.Walker;

@Slf4j
@Singleton
public class PickpocketContext extends CoreContext
{
	@Inject private Client client;
	@Inject private PickpocketConfig config;

	public String[] getNonCoinInventoryItems()
	{
		final var all = Inventory.getAll();
		return all.stream()
			.map(Item::getName)
			.filter(name -> !name.equals("Coins"))
			.toArray(String[]::new);
	}

	public boolean shouldEat()
	{
		return client.getBoostedSkillLevel(Skill.HITPOINTS) < config.minHealth();
	}

	public TileObject getNextDoorOnPathTo(TileObject tileObject)
	{
		if (tileObject == null)
		{
			return null;
		}

		return getNextDoorOnPath(tileObject.getWorldLocation());
	}

	public TileObject getNextDoorOnPathTo(NPC npc)
	{
		if (npc == null)
		{
			return null;
		}

		return getNextDoorOnPath(npc.getWorldLocation());
	}

	public TileObject getNextDoorOnPath(WorldPoint destination)
	{
		final var path = Walker.buildPath(destination);
		for (var point : path)
		{
			final var door = TileObjects.getFirstAt(point, nameMatching("Door"));
			if (door != null && door.hasAction("Open"))
			{
				return door;
			}
		}

		return null;
	}

	public boolean isBankBoothInRange()
	{
		final var bank = entity(containing("bank")).unwrap();
		if (bank == null)
		{
			return false;
		}

		if (bank instanceof NPC)
		{
			return ((NPC) bank).distanceTo(client.getLocalPlayer().getWorldLocation()) < 20;
		}

		if (bank instanceof GameObject)
		{
			return ((GameObject) bank).distanceTo(client.getLocalPlayer().getWorldLocation()) < 20;
		}

		return false;
	}

	public boolean isInArdougne()
	{
		return client.getLocalPlayer().getWorldLocation().isInArea(
			new WorldArea(
				2620, 3264, 100, 100, 0
			)
		);
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();

		map.put("Coin pouches", "" + Inventory.getCount(true, "Coin pouch"));
		map.put("In Ardougne", "" + isInArdougne());

		return map;
	}
}
