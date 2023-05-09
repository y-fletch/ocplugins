package com.yfletch.occore.v2;

public interface Rule<TContext extends CoreContext>
{
	/**
	 * Maximum time the runner can delay activation
	 * of this rule after it passes (in ticks).
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
	boolean consumes(TContext ctx);

	/**
	 * Whether this rule is completed, and control
	 * can continue to the next candidate rule
	 */
	boolean continues(TContext ctx);

	/**
	 * Action to run when this rule is activated or
	 * triggered
	 */
	Interaction run(TContext ctx);
}
