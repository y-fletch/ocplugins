package com.yfletch.occore.v2.interaction;

import net.runelite.api.MenuEntry;

public interface DeferredInteraction
{
	String getTooltip();

	/**
	 * Execute the interaction with Devious API
	 */
	void execute();

	/**
	 * Run anything that needs to be done before the
	 * menu entry is clicked (OC only)
	 */
	default void prepare()
	{
	}

	/**
	 * Code to run when this interaction is "queued" or
	 * ready to run
	 */
	default void onActive()
	{
	}

	/**
	 * Create and return a menu entry for this interaction
	 */
	MenuEntry createMenuEntry();
}
