package com.yfletch.ocpickpocket;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import static com.yfletch.occore.v2.interaction.Entities.entity;
import static com.yfletch.occore.v2.interaction.Entities.npc;
import static com.yfletch.occore.v2.util.Util.nameContaining;
import static com.yfletch.occore.v2.util.Util.parseList;
import java.util.HashSet;
import java.util.Map;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.unethicalite.api.items.Inventory;

@Slf4j
@Singleton
public class PickpocketContext extends CoreContext
{
	@Inject private Client client;
	@Inject private PickpocketConfig config;

	private WorldPoint previousTargetLocation;

	@Setter private boolean shadowVeilActive = false;
	@Setter private boolean shadowVeilOnCooldown = false;

	public String[] getBankableItems()
	{
		final var all = Inventory.getAll();

		final var ignoreItems = new HashSet<>();
		ignoreItems.add("Coins");

		if (config.useShadowVeil())
		{
			ignoreItems.add("Cosmic rune");
			ignoreItems.add("Earth rune");
			ignoreItems.add("Fire rune");
			ignoreItems.add("Rune pouch");
			ignoreItems.add("Divine rune pouch");
		}

		return all.stream()
			.map(Item::getName)
			.filter(name -> !ignoreItems.contains(name))
			.toArray(String[]::new);
	}

	public boolean shouldEat()
	{
		return client.getBoostedSkillLevel(Skill.HITPOINTS) < config.minHealth();
	}

	public boolean isBankBoothInRange()
	{
		final var bank = entity(nameContaining("bank")).unwrap();
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

	public boolean hasTargetRespawned(String[] target)
	{
		final var npc = npc(target).unwrap();
		if (npc == null)
		{
			return false;
		}

		if (previousTargetLocation == null)
		{
			previousTargetLocation = npc.getWorldLocation();
			return false;
		}

		final var result = previousTargetLocation.distanceTo(npc) > 5;
		previousTargetLocation = npc.getWorldLocation();
		return result;
	}

	public boolean canCastShadowVeil()
	{
		return !shadowVeilActive && !shadowVeilOnCooldown;
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();

		map.put("Coin pouches", "" + Inventory.getCount(true, "Coin pouch"));
		map.put("In Ardougne", "" + isInArdougne());
		map.put("Target moved", "" + hasTargetRespawned(parseList(config.target())));

		return map;
	}
}
