package com.yfletch.occore.event;

import com.yfletch.occore.util.ObjectHelper;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;

public class ObjectEvent extends EventOverride
{
	private final Client client;
	private final ObjectHelper objectHelper;

	ObjectEvent(Client client, ObjectHelper objectHelper)
	{
		this.client = client;
		this.objectHelper = objectHelper;
		setForceLeftClick(true);
	}

	@Override
	protected void validate()
	{
		if (getIdentifier() < 0)
		{
			throw new RuntimeException("ObjectEvent: Object not set");
		}

		super.validate();
	}

	/**
	 * Target the specific tile object
	 */
	public ObjectEvent setObject(TileObject object)
	{
		LocalPoint localPoint = object.getLocalLocation();

		int sceneX = localPoint.getSceneX();
		int sceneY = localPoint.getSceneY();

		if (object instanceof GameObject)
		{
			Point sceneLocation = ((GameObject) object).getSceneMinLocation();
			sceneX = sceneLocation.getX();
			sceneY = sceneLocation.getY();
		}

		ObjectComposition comp = client.getObjectDefinition(object.getId());
		setTarget("<col=ffff>" + comp.getName());
		setIdentifier(object.getId());
		setParam0(sceneX);
		setParam1(sceneY);

		return this;
	}

	/**
	 * Target the nearest object to the player matching the given ID
	 */
	public ObjectEvent setObject(int objectId)
	{
		TileObject object = objectHelper.getNearest(objectId);
		return object != null ? setObject(object) : this;
	}
	
	/**
	 * Set option name and index (one-based)
	 */
	public ObjectEvent setOption(String option, int index)
	{
		setOption(option);
		switch (index)
		{
			case 1:
				setType(MenuAction.GAME_OBJECT_FIRST_OPTION);
				break;
			case 2:
				setType(MenuAction.GAME_OBJECT_SECOND_OPTION);
				break;
			case 3:
				setType(MenuAction.GAME_OBJECT_THIRD_OPTION);
				break;
			case 4:
				setType(MenuAction.GAME_OBJECT_FOURTH_OPTION);
				break;
			case 5:
				setType(MenuAction.GAME_OBJECT_FIFTH_OPTION);
				break;
		}
		return this;
	}
}
