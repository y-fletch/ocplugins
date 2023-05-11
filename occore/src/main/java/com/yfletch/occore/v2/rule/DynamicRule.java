package com.yfletch.occore.v2.rule;

import com.yfletch.occore.v2.CoreContext;
import com.yfletch.occore.v2.interaction.Interaction;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true,
		   chain = true)
public final class DynamicRule<TContext extends CoreContext> implements Rule<TContext>
{
	private Predicate<TContext> when;
	private Predicate<TContext> consumeWhile;
	private Predicate<TContext> continueWhen;
	private Function<TContext, Interaction> then;
	private int maxDelay = -1;

	@Override
	public boolean passes(TContext ctx)
	{
		return when != null && when.test(ctx);
	}

	@Override
	public boolean consumes(TContext ctx)
	{
		return consumeWhile != null && consumeWhile.test(ctx);
	}

	@Override
	public boolean continues(TContext ctx)
	{
		return continueWhen != null && continueWhen.test(ctx);
	}

	@Override
	public Interaction run(TContext ctx)
	{
		return then == null ? null : then.apply(ctx);
	}

	@Override
	public int maxDelay()
	{
		return maxDelay > 1 ? maxDelay : Rule.super.maxDelay();
	}
}
