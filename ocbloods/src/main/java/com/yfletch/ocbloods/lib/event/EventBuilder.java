package com.yfletch.ocbloods.lib.event;

import com.yfletch.ocbloods.lib.ObjectHelper;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;

@Setter
@Singleton
public class EventBuilder
{
	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	private MenuOptionClicked targetEvent;

	public ItemEvent item()
	{
		return (ItemEvent) new ItemEvent(client).setTargetEvent(targetEvent);
	}

	public ObjectEvent object()
	{
		return (ObjectEvent) new ObjectEvent(client, objectHelper).setTargetEvent(targetEvent);
	}

	public WidgetEvent widget()
	{
		return (WidgetEvent) new WidgetEvent(client).setTargetEvent(targetEvent);
	}
	
	public NpcEvent npc()
	{
		return (NpcEvent) new NpcEvent(client).setTargetEvent(targetEvent);
	}
}
