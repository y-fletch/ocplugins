package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import com.yfletch.occore.event.WrappedEvent;
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
	 * while the action is active and this is true, the event will be consumed.
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
	 * Code to run when a MenuOption is clicked while this action is active.
	 * Use this to override the event.
	 */
	public void run(T ctx, WrappedEvent event)
	{
	}

	/**
	 * Extra code to run when this action has successfully been completed.
	 */
	public void done(T ctx)
	{
	}
}
