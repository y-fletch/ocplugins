package com.yfletch.ocbloods.lib;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Locatable;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.NPCQuery;

@Slf4j
@Singleton
public class NPCHelper
{
	@Inject
	private Client client;

	public NPC getNearestTo(int npcId, Locatable locatable)
	{
		return getNearestTo(new int[]{npcId}, locatable);
	}

	public NPC getNearestTo(int[] npcIds, Locatable locatable)
	{
		return new NPCQuery().idEquals(npcIds).result(client).nearestTo(locatable);
	}

	public NPC getNearest(int npcId)
	{
		return getNearestTo(npcId, client.getLocalPlayer());
	}
}
