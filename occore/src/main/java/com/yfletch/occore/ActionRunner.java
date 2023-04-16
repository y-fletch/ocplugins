package com.yfletch.occore;

import com.yfletch.occore.action.Action;
import com.yfletch.occore.action.ActionBuilder;
import com.yfletch.occore.event.EventBuilder;
import com.yfletch.occore.event.WrappedEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.ui.overlay.components.LineComponent;

public class ActionRunner<T extends ActionContext>
{
	@Getter
	private final T context;
	private final EventBuilder eventBuilder;

	@Getter
	private final List<Action<T>> actions = new ArrayList<>();

	@Getter
	@Accessors(fluent = true)
	private final ActionBuilder<T> builder = new ActionBuilder<>();

	@Getter
	private Action<T> current = null;

	/**
	 * Create a new ActionRunner. Requires an injected
	 * EventBuilder instance
	 */
	public ActionRunner(T context, EventBuilder eventBuilder)
	{
		this.context = context;
		this.eventBuilder = eventBuilder;

		setup();
	}

	/**
	 * Run after runner initialization, add your steps here
	 */
	public void setup()
	{
	}

	/**
	 * Clear all actions, and run setup again
	 */
	public void refresh()
	{
		actions.clear();
		setup();
	}

	/**
	 * Add an action to the runner. Order here is important - FIFO
	 */
	public void add(Action<T> action)
	{
		actions.add(action);
	}

	/**
	 * Attempt to move to the next action.
	 * Will move on if current action is done (or no current action).
	 * Next action will be the first action that is reporting as ready.
	 */
	public void tick()
	{
		// check to reset all actions
		for (final var action : actions)
		{
			if (action.shouldReset(context))
			{
				action.hasRun(false);
			}
		}

		if (current != null)
		{
			if (current.isDone(context))
			{
				// allow next action to run
				current.done(context);
			}
		}

		for (final var action : actions)
		{
			if (action.isReady(context) && !action.isDone(context))
			{
				current = action;
				return;
			}
		}
	}

	/**
	 * Run the current action. If the current action is in
	 * the working state, the event will be consumed.
	 * <p>
	 * Will also determine the currently used/selected item
	 * and save it into the context.
	 */
	public void run(MenuOptionClicked event)
	{
		context.setUsingItemName(null);

		// "use"
		if (event.getMenuAction() == MenuAction.WIDGET_TARGET)
		{
			var target = event.getMenuTarget();
			if (!target.contains("->"))
			{
				target = target.replaceAll("</?col(?:=\\w{6})?>", "");
				context.setUsingItemName(target);
			}
		}

		if (current == null)
		{
			return;
		}

		if (current.isWorking(context))
		{
			event.consume();
			return;
		}

		current.run(context, new WrappedEvent(event, eventBuilder));
		current.hasRun(true);
	}

	public boolean isWorking()
	{
		return current != null && current.isWorking(context);
	}

	/**
	 * Get overlay display line for the current action.
	 */
	public LineComponent getDisplayLine()
	{
		if (current == null)
		{
			return null;
		}

		return current.getDisplayLine(context);
	}
}
