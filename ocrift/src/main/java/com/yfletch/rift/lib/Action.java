package com.yfletch.rift.lib;

import net.runelite.client.ui.overlay.components.LineComponent;

public class Action<T extends ActionContext>
{
	/**
	 * Get content to display in the UI overlay when this action
	 * is up next.
	 */
	public LineComponent getDisplayLine(T ctx)
	{
		return LineComponent.builder().left("Unknown").build();
	}

	/**
	 * Determines if this action is ready to run
	 */
	public boolean isReady(T ctx)
	{
		return false;
	}

	/**
	 * Determines if this action _is in the process of_ being completed.
	 * Used to prevent misclicks and starting the next action too early -
	 * if this is true while isReady is true, the event will be consumed.
	 * e.g. This action may click on an object, then be "done" as soon as
	 * the player character is moving.
	 */
	public boolean isWorking(T ctx)
	{
		return false;
	}

	/**
	 * Determines when this action is completed, and control can shift to
	 * the next action.
	 */
	public boolean isDone(T ctx)
	{
		return false;
	}

	/**
	 * Any code to run on each tick of this action.
	 */
	public void run(T ctx, WrappedEvent event)
	{
	}

	/**
	 * Logic to complete when this action has successfully been completed.
	 */
	public void done(T ctx)
	{
	}
}
