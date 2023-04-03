package com.yfletch.rift.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Locatable;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.queries.TileObjectQuery;

@Slf4j
@Singleton
public class ObjectHelper
{
	@Inject
	private Client client;

	public TileObject getNearest(String objectName)
	{
		return new ObjectQuery()
			.filter(object -> object.getName().contains(objectName))
			.result(client)
			.nearestTo(client.getLocalPlayer());
	}

	public TileObject getNearest(int objectId, Locatable locatable)
	{
		return new ObjectQuery()
			.idEquals(objectId)
			.result(client)
			.nearestTo(locatable);
	}

	public TileObject getNearest(int objectId)
	{
		return getNearest(objectId, client.getLocalPlayer());
	}

	public List<TileObject> getAll(int... objectId)
	{
		return new ObjectQuery()
			.idEquals(objectId)
			.result(client)
			.list;
	}

	public boolean isBeside(WorldPoint player, GameObject object)
	{
		Point minScene = object.getSceneMinLocation();
		Point maxScene = object.getSceneMaxLocation();

		WorldPoint min = WorldPoint.fromScene(client, minScene.getX() - 1, minScene.getY() - 1, 0);
		WorldPoint max = WorldPoint.fromScene(client, maxScene.getX() + 1, maxScene.getY() + 1, 0);

		return WorldPoint.isInZone(
			WorldPoint.fromScene(client, minScene.getX() - 1, minScene.getY() - 1, 0),
			WorldPoint.fromScene(client, maxScene.getX() + 1, maxScene.getY() + 1, 0),
			player
		) && !WorldPoint.isInZone(
			WorldPoint.fromScene(client, minScene.getX(), minScene.getY(), 0),
			WorldPoint.fromScene(client, maxScene.getX(), maxScene.getY(), 0),
			player
		);
	}

	/**
	 * Determine whether a point (the player) is beside an object, taking
	 * the object's size into account. Used to check if the player is pathing
	 * to/already beside a game object.
	 */
	public boolean isBeside(WorldPoint player, TileObject object)
	{
		if (object instanceof GameObject)
		{
			return isBeside(player, (GameObject) object);
		}

		WorldPoint pos = object.getWorldLocation();

		return WorldPoint.isInZone(
			new WorldPoint(pos.getX() - 1, pos.getY() - 1, 0),
			new WorldPoint(pos.getX() + 1, pos.getY() + 1, 0),
			player
		);
	}

	private static class ObjectQuery extends TileObjectQuery<TileObject, ObjectQuery>
	{
		@Override
		public LocatableQueryResults<TileObject> result(Client client)
		{
			return new LocatableQueryResults<>(getObjects(client).stream()
				.filter(Objects::nonNull)
				.filter(predicate)
				.distinct()
				.collect(Collectors.toList()));
		}

		public Collection<TileObject> getObjects(Client client)
		{
			Collection<TileObject> objects = new ArrayList<>();
			for (Tile tile : getTiles(client))
			{
				TileObject[] gameObjects = tile.getGameObjects();
				if (gameObjects != null)
				{
					objects.addAll(Arrays.asList(gameObjects));
				}

				objects.add(tile.getGroundObject());
				objects.add(tile.getWallObject());
			}
			return objects;
		}
	}
}
