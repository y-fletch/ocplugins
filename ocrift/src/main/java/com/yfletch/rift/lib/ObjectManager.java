package com.yfletch.rift.lib;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import lombok.Setter;
import net.runelite.api.TileObject;

@Singleton
public class ObjectManager
{
	@Setter
	private TrackedObject[] trackedObjects;

	private final Map<Integer, TileObject> visible = new HashMap<>();
	private final Map<Integer, TileObject> gameObjects = new HashMap<>();

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

	public void add(TileObject object)
	{
		if (visible.containsKey(object.getId()) || !shouldTrack(object))
		{
			return;
		}

		System.out.println("ObjectManager: register " + object.getId());

		visible.put(object.getId(), object);
		gameObjects.put(object.getId(), object);
	}

	public void hide(TileObject object)
	{
		visible.remove(object.getId());
	}

	public boolean isVisible(int object)
	{
		return visible.containsKey(object);
	}

	public TileObject get(int id)
	{
		return gameObjects.get(id);
	}
}
