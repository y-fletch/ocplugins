package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import com.yfletch.occore.util.RegionPoint;

public class ActionBuilder<T extends ActionContext>
{
	public ItemAction<T> item(String action, String itemName)
	{
		return new ItemAction<>(action, itemName);
	}

	public MoveAction<T> move(RegionPoint regionPoint)
	{
		return new MoveAction<>(regionPoint);
	}

	public NpcAction<T> npc(String action, String npcName)
	{
		return new NpcAction<>(action, npcName);
	}

	public ObjectAction<T> object(String action, String objectName)
	{
		return new ObjectAction<>(action, objectName);
	}

	public PrepAction<T> prep()
	{
		return new PrepAction<>();
	}

	public WidgetAction<T> widget(String action, String widgetName)
	{
		return new WidgetAction<>(action, widgetName);
	}

	public WidgetAction<T> widget(String action)
	{
		return new WidgetAction<>(action, null);
	}
}
