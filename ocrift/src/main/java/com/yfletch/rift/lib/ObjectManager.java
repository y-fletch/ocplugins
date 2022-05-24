package com.yfletch.rift.lib;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

@Slf4j
@Singleton
public class ObjectManager
{
	@Inject
	private Client client;

	@Setter
	private TrackedObject[] trackedObjects;

	private final Map<Integer, Collection<SavedObject>> gameObjects = new HashMap<>();

	private boolean shouldTrack(TileObject object)
	{
		for (TrackedObject tracked : trackedObjects)
		{
			if (tracked.getId() == object.getId())
			{
				return tracked.getWorldPoint() == null || tracked.getWorldPoint().equals(object.getWorldLocation());
			}
		}

		return false;
	}

	private SavedObject getMatchingObject(TileObject object)
	{
		Collection<SavedObject> collection = gameObjects.getOrDefault(object.getId(), new ArrayList<>());

		for (SavedObject test : collection)
		{
			if (test.equals(object))
			{
				return test;
			}
		}

		return null;
	}

	private void validate()
	{
		for (Collection<SavedObject> collection : gameObjects.values())
		{
			collection.removeIf((savedObject -> !savedObject.isValid()));
		}
	}

	public void add(TileObject object)
	{
		if (!shouldTrack(object))
		{
			return;
		}

		validate();

		log.info(
			"Tracking object: " + object.getName()
				+ " (" + object.getId() + ") @"
				+ object.getWorldLocation().getX() + ","
				+ object.getWorldLocation().getY()
		);

		Collection<SavedObject> collection = gameObjects.getOrDefault(object.getId(), new ArrayList<>());
		SavedObject existing = getMatchingObject(object);
		if (existing != null)
		{
			log.info(
				"Replaced existing"
			);

			collection.remove(existing);
		}

		collection.add(new SavedObject(object));
		gameObjects.put(object.getId(), collection);
	}

	public Collection<TileObject> getAll()
	{
		validate();

		Collection<TileObject> result = new ArrayList<>();
		for (Collection<SavedObject> collection : gameObjects.values())
		{
			result.addAll(collection.stream().map((SavedObject::getObject)).collect(Collectors.toList()));
		}

		return result;
	}

	public void remove(TileObject object)
	{
		log.info(
			"Removing object: " + object.getName()
				+ " (" + object.getId() + ") @"
				+ object.getWorldLocation().getX() + ","
				+ object.getWorldLocation().getY()
		);

		Collection<SavedObject> collection = gameObjects.get(object.getId());
		if (collection != null)
		{
			collection.removeIf(test -> test.equals(object));
		}
	}

	/**
	 * Check if the object with the target ID is in memory
	 * (most objects don't unload / go invisible when the region
	 * changes *shrug*)
	 */
	public boolean isVisible(int objectId)
	{
		return !gameObjects.getOrDefault(objectId, new ArrayList<>()).isEmpty();
	}

	/**
	 * Get the closest object to the point, with the target ID
	 */
	public TileObject get(int id, WorldPoint point)
	{
		validate();

		Collection<SavedObject> collection = gameObjects.get(id);
		SavedObject closest = null;
		for (SavedObject object : collection)
		{
			if (closest == null || point.distanceTo(object.getInitialLocation()) < point.distanceTo(closest.getInitialLocation()))
			{
				closest = object;
			}
		}

		return closest != null ? closest.getObject() : null;
	}

	/**
	 * Get the closest object to the player with the target ID
	 */
	public TileObject get(int id)
	{
		return get(id, client.getLocalPlayer().getWorldLocation());
	}
}
