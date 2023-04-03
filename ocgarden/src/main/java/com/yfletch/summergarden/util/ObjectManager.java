package com.yfletch.summergarden.util;

import com.google.inject.Singleton;
import com.yfletch.summergarden.Consts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

@Singleton
public class ObjectManager
{
	@Getter
	@AllArgsConstructor
	@RequiredArgsConstructor
	private class MonitoredObject
	{
		@NonNull
		private int id;
		private WorldPoint worldPoint;
	}

	private final MonitoredObject[] monitoredObjects = new MonitoredObject[]{
		new MonitoredObject(Consts.GATE),
		new MonitoredObject(Consts.SUMMER_SQIRK_TREE),
		new MonitoredObject(Consts.POOL_1),
		new MonitoredObject(Consts.POOL_2),
		new MonitoredObject(Consts.POOL_3),
		new MonitoredObject(Consts.POOL_4),
		new MonitoredObject(Consts.MOUNTED_GLORY),
		new MonitoredObject(Consts.PALACE_SHORTCUT),
		new MonitoredObject(Consts.PALACE_DOOR_CLOSED, new WorldPoint(3293, 3167, 0)),
		new MonitoredObject(Consts.BANK_BOOTH, new WorldPoint(3268, 3167, 0)),
		new MonitoredObject(Consts.APPRENTICE_DOOR_CLOSED, new WorldPoint(3321, 3142, 0)),
		new MonitoredObject(Consts.SHANTAY_BANK_CHEST, new WorldPoint(3309, 3120, 0))
	};

	private final Map<Integer, TileObject> visible = new HashMap<>();
	private final Map<Integer, TileObject> gameObjects = new HashMap<>();

	private boolean shouldMonitor(TileObject object)
	{
		for (MonitoredObject monitored : monitoredObjects)
		{
			if (monitored.getId() == object.getId())
			{
				return monitored.getWorldPoint() == null || monitored.getWorldPoint().equals(object.getWorldLocation());
			}
		}

		return false;
	}

	public void add(TileObject object)
	{
		if (visible.containsKey(object.getId()) || !shouldMonitor(object))
		{
			return;
		}

		visible.put(object.getId(), object);
		gameObjects.put(object.getId(), object);
	}

	public void hide(TileObject object)
	{
		visible.remove(object.getId());
	}

	public boolean isVisible(TileObject object)
	{
		return visible.containsValue(object);
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
