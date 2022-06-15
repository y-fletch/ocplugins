package com.yfletch.ocbloods.lib.event;

import net.runelite.api.events.MenuOptionClicked;

/**
 * Easily mutable event with helper methods.
 */
public class WrappedEvent
{
	private final MenuOptionClicked event;
	private final EventBuilder eventBuilder;

	public WrappedEvent(MenuOptionClicked event, EventBuilder eventBuilder)
	{
		this.event = event;
		this.eventBuilder = eventBuilder;
	}

	public EventBuilder builder()
	{
		eventBuilder.setTargetEvent(event);
		return eventBuilder;
	}

	public void consume()
	{
		event.consume();
	}
}
