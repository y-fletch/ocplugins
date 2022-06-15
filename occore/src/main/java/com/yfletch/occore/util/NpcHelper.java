package com.yfletch.occore.util;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Locatable;
import net.runelite.api.NPC;
import net.runelite.api.queries.NPCQuery;

@Slf4j
@Singleton
public class NpcHelper
{
	@Inject
	private Client client;

	/**
	 * Get nearest NPC with the given ID to the locatable
	 */
	public NPC getNearestTo(int npcId, Locatable locatable)
	{
		return getNearestTo(new int[]{npcId}, locatable);
	}

	/**
	 * Get nearest NPC with any of the given IDs to the locatable
	 */
	public NPC getNearestTo(int[] npcIds, Locatable locatable)
	{
		return new NPCQuery().idEquals(npcIds).result(client).nearestTo(locatable);
	}
	
	/**
	 * Get nearest NPC with the given ID to the player
	 */
	public NPC getNearest(int npcId)
	{
		return getNearestTo(npcId, client.getLocalPlayer());
	}
}
