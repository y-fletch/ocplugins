package com.yfletch.occore.v2.rule;

import com.yfletch.occore.v2.CoreContext;
import com.yfletch.occore.v2.interaction.DeferredInteraction;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true,
		   chain = true)
public final class DynamicRule<TContext extends CoreContext> implements Rule<TContext>
{
	@Getter
	private String name;

	private Predicate<TContext> when;
	private Predicate<TContext> consumeWhile;
	private Predicate<TContext> until;
	private Function<TContext, DeferredInteraction<?>> then;

	private int repeat = 1;

	@Setter(AccessLevel.MODULE)
	private int repeatsLeft = 1;

	private int maxDelay = 0;

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
		return until != null && until.test(ctx);
	}

	@Override
	public DeferredInteraction<?> run(TContext ctx)
	{
		return then == null ? null : then.apply(ctx);
	}

	@Override
	public int maxDelay()
	{
		return maxDelay > 1 ? maxDelay : Rule.super.maxDelay();
	}

	@Override
	public void reset()
	{
		repeatsLeft = repeat;
	}

	@Override
	public void useRepeat()
	{
		repeatsLeft--;
	}

	@Override
	public boolean canExecute()
	{
		return repeatsLeft > 0;
	}
}
