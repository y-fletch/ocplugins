package com.yfletch.occore.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
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
import net.unethicalite.client.Static;

@Slf4j
@Singleton
public class ObjectHelper
{
	private final Client client;

	@Inject
	public ObjectHelper(Client client)
	{
		this.client = client;
	}

	public static ObjectHelper instance()
	{
		return new ObjectHelper(Static.getClient());
	}

	public List<TileObject> where(Predicate<TileObject> predicate)
	{
		return new ObjectQuery()
			.filter(predicate)
			.result(client)
			.list;
	}

	public List<TileObject> where(int id, Predicate<TileObject> predicate)
	{
		return new ObjectQuery()
			.idEquals(id)
			.filter(predicate)
			.result(client)
			.list;
	}

	public LocatableQueryResults<TileObject> queryWhere(Predicate<TileObject> predicate)
	{
		return new ObjectQuery()
			.filter(predicate)
			.result(client);
	}

	public LocatableQueryResults<TileObject> queryWhere(int id, Predicate<TileObject> predicate)
	{
		return new ObjectQuery()
			.idEquals(id)
			.filter(predicate)
			.result(client);
	}

	/**
	 * Get the nearest object containing the search term to the current player (case ignored)
	 */
	public TileObject getNearest(String search)
	{
		return new ObjectQuery()
			.filter(object -> object.getName().toLowerCase().contains(search.toLowerCase()))
			.result(client)
			.nearestTo(client.getLocalPlayer());
	}

	/**
	 * Get the nearest object matching the predicate to the current player
	 */
	public TileObject getNearest(Predicate<TileObject> predicate)
	{
		return new ObjectQuery()
			.filter(predicate)
			.result(client)
			.nearestTo(client.getLocalPlayer());
	}

	/**
	 * Get nearest object with the matching ID to the locatable
	 */
	public TileObject getNearest(int objectId, Locatable locatable)
	{
		return new ObjectQuery()
			.idEquals(objectId)
			.result(client)
			.nearestTo(locatable);
	}

	/**
	 * Get nearest object with the matching ID to the current player
	 */
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

	/**
	 * Determine whether a point (the player) is beside an object, taking
	 * the object's size into account. Used to check if the player is pathing
	 * to/already beside a game object.
	 */
	public boolean isBeside(WorldPoint player, GameObject object)
	{
		Point minScene = object.getSceneMinLocation();
		Point maxScene = object.getSceneMaxLocation();

		return WorldPoint.isInZone(
			WorldPoint.fromScene(client, minScene.getX() - 1, minScene.getY() - 1, object.getPlane()),
			WorldPoint.fromScene(client, maxScene.getX() + 1, maxScene.getY() + 1, object.getPlane()),
			player
		) && !WorldPoint.isInZone(
			WorldPoint.fromScene(client, minScene.getX(), minScene.getY(), object.getPlane()),
			WorldPoint.fromScene(client, maxScene.getX(), maxScene.getY(), object.getPlane()),
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

	public boolean isBeside(Locatable locatable, TileObject object)
	{
		return isBeside(locatable.getWorldLocation(), object);
	}

	public boolean isBeside(Locatable locatable, GameObject object)
	{
		return isBeside(locatable.getWorldLocation(), object);
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
