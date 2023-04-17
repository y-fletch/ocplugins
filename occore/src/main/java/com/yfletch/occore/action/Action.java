package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import com.yfletch.occore.event.WrappedEvent;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.client.ui.overlay.components.LineComponent;

public abstract class Action<T extends ActionContext>
{
	private Predicate<T> isReady;
	private Predicate<T> isWorking;
	private Predicate<T> isDone;
	private Predicate<T> shouldReset;
	private BiConsumer<T, WrappedEvent> run;
	private Consumer<T> done;

	@Getter
	@Setter
	@Accessors(fluent = true)
	private boolean hasRun = false;

	public abstract String getName();

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
		if (isReady != null)
		{
			return isReady.test(ctx);
		}

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
		if (isWorking != null)
		{
			return isWorking.test(ctx);
		}

		return false;
	}

	/**
	 * Determines when this action is completed, and control can shift to
	 * the next action.
	 */
	public boolean isDone(T ctx)
	{
		if (isDone != null)
		{
			return isDone.test(ctx);
		}

		return false;
	}

	/**
	 * Determines when the `hasRun` value should be reset.
	 * Only relevant for once-off actions.
	 */
	public boolean shouldReset(T ctx)
	{
		if (shouldReset != null)
		{
			return shouldReset.test(ctx);
		}

		return false;
	}

	/**
	 * Code to run when a MenuOption is clicked while this action is active.
	 * Use this to override the event.
	 */
	public void run(T ctx, WrappedEvent event)
	{
		if (run != null)
		{
			run.accept(ctx, event);
		}
	}

	/**
	 * Extra code to run when this action has successfully been completed.
	 */
	public void done(T ctx)
	{
		if (done != null)
		{
			done.accept(ctx);
		}
	}

	public Action<T> readyIf(Predicate<T> isReady)
	{
		this.isReady = isReady;
		return this;
	}

	public Action<T> workingIf(Predicate<T> isWorking)
	{
		this.isWorking = isWorking;
		return this;
	}

	public Action<T> doneIf(Predicate<T> isDone)
	{
		if (shouldReset != null)
		{
			throw new IllegalArgumentException("Do not set an isDone condition for once-off actions");
		}

		this.isDone = isDone;
		return this;
	}

	public Action<T> onRun(BiConsumer<T, WrappedEvent> run)
	{
		this.run = run;
		return this;
	}

	public Action<T> onDone(Consumer<T> done)
	{
		this.done = done;
		return this;
	}

	/**
	 * Ensure this action is only run once, and can only run again
	 * once the reset condition has been met.
	 */
	public Action<T> onceUntil(Predicate<T> resetCondition)
	{
		shouldReset = resetCondition;
		isDone = ctx -> hasRun();
		return this;
	}

	/**
	 * Block any extra clicks after this action is run once.
	 */
	public Action<T> blockExtraClicks()
	{
		shouldReset = isDone;
		isWorking = ctx -> hasRun();
		return this;
	}
}
