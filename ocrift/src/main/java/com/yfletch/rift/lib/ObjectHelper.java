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
import net.runelite.api.coords.LocalPoint;
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

	/**
	 * Determine whether a point (the player) is beside an object, taking
	 * the object's size into account. Used to check if the player is pathing
	 * to/already beside a game object.
	 */
	public boolean isBeside(WorldPoint player, TileObject object)
	{
		WorldPoint pos = object.getWorldLocation();
		int w = 1;
		int h = 1;

		if (object instanceof GameObject)
		{
			Point min = ((GameObject) object).getSceneMinLocation();
			Point max = ((GameObject) object).getSceneMaxLocation();
			pos = WorldPoint.fromScene(client, min.getX(), min.getY(), 0);

			w = max.getX() - min.getX() + 1;
			h = max.getY() - min.getY() + 1;
		}

		int x = pos.getX();
		int y = pos.getY();

		// left side
		for (int j = 0; j < h; j++)
		{
			if (player.getY() == y + j && player.getX() == x - 1)
			{
				return true;
			}
		}

		// right side
		for (int j = 0; j < h; j++)
		{
			if (player.getY() == y + j && player.getX() == x + w)
			{
				return true;
			}
		}

		// bottom side
		for (int i = 0; i < w; i++)
		{
			if (player.getY() == y - 1 && player.getX() == x + i)
			{
				return true;
			}
		}

		// top side
		for (int i = 0; i < w; i++)
		{
			if (player.getY() == y + h && player.getX() == x + i)
			{
				return true;
			}
		}

		return false;
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
