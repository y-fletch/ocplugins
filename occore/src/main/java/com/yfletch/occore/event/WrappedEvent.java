package com.yfletch.occore.event;

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

	/**
	 * Get the builder instance to begin overriding the event
	 */
	public EventBuilder builder()
	{
		eventBuilder.setTargetEvent(event);
		return eventBuilder;
	}

	/**
	 * Consume the event, preventing anything from happening
	 */
	public void consume()
	{
		event.consume();
	}
}
