package com.yfletch.occore.v2.rule;

import com.yfletch.occore.v2.CoreContext;
import com.yfletch.occore.v2.interaction.DeferredInteraction;
import java.util.function.Consumer;
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

	@Getter
	private Predicate<TContext> when;
	private Predicate<TContext> consumeWhile;
	private Predicate<TContext> until;
	private Function<TContext, DeferredInteraction> then;

	private int repeat = 1;

	@Setter(AccessLevel.MODULE)
	private Function<TContext, Integer> repeatProvider;

	@Setter(AccessLevel.MODULE)
	private int repeatsLeft = 1;

	private int maxDelay = 0;
	private int minDelay = 0;

	@Setter(AccessLevel.MODULE)
	private boolean many = false;
	private boolean once = false;

	@Getter
	private boolean resetsOnTick = false;

	private Consumer<TContext> onClick;
	private Consumer<TContext> onComplete;

	/**
	 * Only execute this rule once, before immediately
	 * moving control to the next rule.
	 * <p>
	 * This rule becomes eligible again as soon as the
	 * next rule executes. To chain rules together,
	 * use oncePerTick instead.
	 */
	public DynamicRule<TContext> once()
	{
		return once(true);
	}

	/**
	 * Only execute this rule once per tick, before immediately
	 * moving control to the next rule. This rule will not
	 * be eligible again until the next tick, where the rule
	 * is completely reset (including repeats).
	 */
	public DynamicRule<TContext> oncePerTick()
	{
		return once(true).resetsOnTick(true);
	}

	/**
	 * Allow this action to be performed multiple times
	 * per tick
	 */
	public DynamicRule<TContext> many()
	{
		return many(true);
	}

	public DynamicRule<TContext> delay(int min, int max)
	{
		return minDelay(min).maxDelay(max);
	}

	/**
	 * Forcibly delay this rule's action
	 */
	public DynamicRule<TContext> delay(int delay)
	{
		return minDelay(delay).maxDelay(delay);
	}

	public DynamicRule<TContext> repeat(Function<TContext, Integer> provider)
	{
		return repeatProvider(provider);
	}

	public DynamicRule<TContext> repeat(int repeat)
	{
		this.repeat = repeat;
		return this;
	}

	@Override
	public boolean passes(TContext ctx)
	{
		if ((once || resetsOnTick) && repeatsLeft == 0)
		{
			return false;
		}

		return when == null || when.test(ctx);
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
	public DeferredInteraction run(TContext ctx)
	{
		return then == null ? null : then.apply(ctx);
	}

	@Override
	public int maxDelay()
	{
		return maxDelay > 0 ? maxDelay : Rule.super.maxDelay();
	}

	@Override
	public int minDelay()
	{
		return minDelay > 0 ? minDelay : Rule.super.minDelay();
	}

	@Override
	public void reset(TContext context)
	{
		if (repeatProvider != null)
		{
			repeatsLeft = repeatProvider.apply(context);
			return;
		}
		repeatsLeft = repeat;
	}

	@Override
	public void useRepeat()
	{
		if (!many)
		{
			repeatsLeft--;
		}
	}

	@Override
	public boolean canExecute()
	{
		return repeatsLeft > 0;
	}

	@Override
	public void callback(TContext context)
	{
		if (onClick != null)
		{
			onClick.accept(context);
		}
	}

	@Override
	public void completeCallback(TContext context)
	{
		if (onComplete != null)
		{
			onComplete.accept(context);
		}
	}

	@Override
	public int repeatsLeft()
	{
		return repeatsLeft;
	}
}
