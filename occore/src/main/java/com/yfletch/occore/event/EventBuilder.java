package com.yfletch.occore.event;

import com.yfletch.occore.util.NpcHelper;
import com.yfletch.occore.util.ObjectHelper;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;

@Setter(AccessLevel.PACKAGE)
@Singleton
public class EventBuilder
{
	@Inject
	private Client client;

	@Inject
	private ObjectHelper objectHelper;

	@Inject
	private NpcHelper npcHelper;

	private MenuOptionClicked targetEvent;

	/**
	 * Override an event on an inventory item (not just the player's inventory).
	 */
	public ItemEvent item()
	{
		return (ItemEvent) new ItemEvent(client).setTargetEvent(targetEvent);
	}

	/**
	 * Override an event on a game or tile object in the world.
	 */
	public ObjectEvent object()
	{
		return (ObjectEvent) new ObjectEvent(client, objectHelper).setTargetEvent(targetEvent);
	}

	/**
	 * Override an event on a widget/interface (including dialog).
	 */
	public WidgetEvent widget()
	{
		return (WidgetEvent) new WidgetEvent(client).setTargetEvent(targetEvent);
	}

	/**
	 * Override an event on an NPC in the world.
	 */
	public NpcEvent npc()
	{
		return (NpcEvent) new NpcEvent(npcHelper).setTargetEvent(targetEvent);
	}

	public MoveEvent move()
	{
		return (MoveEvent) new MoveEvent(client).setTargetEvent(targetEvent);
	}
}
