package com.yfletch.occore.event;

import com.yfletch.occore.util.ObjectHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public class ObjectEvent extends EventOverride
{
	private final Client client;
	private final ObjectHelper objectHelper;

	private Widget usedItemWidget;

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

	public ObjectEvent use(int itemId)
	{
		usedItemWidget = getItem(Set.of(itemId));
		if (usedItemWidget == null)
		{
			throw new RuntimeException("Could not find use item in inventory: " + itemId);
		}

		setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT);

		return this;
	}

	private Widget getItem(Collection<Integer> ids)
	{
		List<Widget> matches = getItems(ids);
		return matches.size() != 0 ? matches.get(matches.size() - 1) : null;
	}

	private ArrayList<Widget> getItems(Collection<Integer> ids)
	{
		client.runScript(6009, 9764864, 28, 1, -1);
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		ArrayList<Widget> matchedItems = new ArrayList<>();

		if (inventoryWidget != null && inventoryWidget.getDynamicChildren() != null)
		{
			Widget[] items = inventoryWidget.getDynamicChildren();
			for (Widget item : items)
			{
				if (ids.contains(item.getItemId()))
				{
					matchedItems.add(item);
				}
			}
		}
		return matchedItems;
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

	public ObjectEvent on(TileObject object)
	{
		setOption("Use");
		setObject(object);
		return this;
	}

	public ObjectEvent on(int objectId)
	{
		setOption("Use");
		setObject(objectId);
		return this;
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

	@Override
	public void override()
	{
		if (usedItemWidget != null)
		{
			setTarget(usedItemWidget.getName() + "<col=ffffff> -> " + getTarget());

			client.setSelectedSpellWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedSpellChildIndex(usedItemWidget.getIndex());
			client.setSelectedSpellItemId(usedItemWidget.getItemId());
			client.setSpellSelected(true);
		}

		super.override();
	}
}
