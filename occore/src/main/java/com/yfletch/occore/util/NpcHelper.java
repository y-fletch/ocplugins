package com.yfletch.occore.util;

import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.NPCQuery;
import net.unethicalite.client.Static;

@Slf4j
@Singleton
public class NpcHelper
{
	private final Client client;

	@Inject
	public NpcHelper(Client client)
	{
		this.client = client;
	}

	public static NpcHelper instance()
	{
		return new NpcHelper(Static.getClient());
	}

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

	/**
	 * Get nearest NPC matching the predicate
	 */
	public NPC getNearest(Predicate<NPC> predicate, Locatable locatable)
	{
		return new NPCQuery().filter(predicate).result(client).nearestTo(locatable);
	}

	/**
	 * Get nearest NPC matching the predicate to the player
	 */
	public NPC getNearest(Predicate<NPC> predicate)
	{
		return getNearest(predicate, client.getLocalPlayer());
	}

	/**
	 * Determine whether a point (the player) is beside an object, taking
	 * the object's size into account. Used to check if the player is pathing
	 * to/already beside a game object.
	 */
	public boolean isBeside(WorldPoint player, NPC npc)
	{
//		Point minScene = object.getSceneMinLocation();
//		Point maxScene = object.getSceneMaxLocation();
//
//		return WorldPoint.isInZone(
//				WorldPoint.fromScene(client, minScene.getX() - 1, minScene.getY() - 1, object.getPlane()),
//				WorldPoint.fromScene(client, maxScene.getX() + 1, maxScene.getY() + 1, object.getPlane()),
//				player
//		) && !WorldPoint.isInZone(
//				WorldPoint.fromScene(client, minScene.getX(), minScene.getY(), object.getPlane()),
//				WorldPoint.fromScene(client, maxScene.getX(), maxScene.getY(), object.getPlane()),
//				player
//		);
		
		Point x = npc.getWorldLocation().

		return false;
	}
}
