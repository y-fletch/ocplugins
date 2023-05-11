package com.yfletch.occore.v2.rule;

import com.yfletch.occore.v2.CoreContext;
import com.yfletch.occore.v2.interaction.Interaction;
import java.util.List;

public interface Rule<TContext extends CoreContext>
{
	/**
	 * Maximum time the runner can delay execution of
	 * this rule's interaction after it passes (in ticks).
	 * <p>
	 * Set >= 1 to make actions much less suspicious
	 */
	default int maxDelay()
	{
		return 0;
	}

	/**
	 * Whether this rule should be activated
	 */
	boolean passes(TContext ctx);

	/**
	 * Whether this rule should prevent other rules
	 * from being activated, and should consume any
	 * extra mouse clicks (one-click mode)
	 */
	default boolean consumes(TContext ctx)
	{
		return false;
	}

	/**
	 * Whether this rule is completed, and control
	 * can continue to the next candidate rule
	 */
	default boolean continues(TContext ctx)
	{
		return false;
	}

	/**
	 * Action to run when this rule is activated or
	 * triggered
	 */
	default Interaction run(TContext ctx)
	{
		return null;
	}

	/**
	 * Messages to display in the overlay, if
	 * no interaction is possible
	 */
	default List<String> messages(TContext ctx)
	{
		return null;
	}
}
