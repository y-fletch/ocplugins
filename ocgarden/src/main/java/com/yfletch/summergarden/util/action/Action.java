package com.yfletch.summergarden.util.action;

import com.yfletch.summergarden.util.WrappedEvent;
import net.runelite.client.ui.overlay.components.LineComponent;

public interface Action
{
	/**
	 * Get instruction line text for overlays
	 */
	public LineComponent getDisplayLine(ActionContext context);

	/**
	 * Test if this action is ready to be run
	 */
	public boolean isReady(ActionContext context);

	public boolean isDone(ActionContext context);

	/**
	 * Apply highlighting or show other indicator of this action
	 */
	public void run(ActionContext context, WrappedEvent event);

	/**
	 * Apply highlighting or show other indicator of this action
	 */
	public void done(ActionContext context);
}
