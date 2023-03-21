package com.yfletch.ocbarbfishing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.ocbarbfishing.action.EatRoeOrCaviar;
import com.yfletch.ocbarbfishing.action.IsPrepared;
import com.yfletch.ocbarbfishing.action.UseKnifeOnFish;
import com.yfletch.ocbarbfishing.action.UseRodFishingSpot;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;

@Singleton
public class OCBarbFishingRunnerFactory
{
	@Inject
	private OCBarbFishingContext context;

	@Inject
	private EventBuilder eventBuilder;

	public ActionRunner<OCBarbFishingContext> create()
	{
		ActionRunner<OCBarbFishingContext> runner = new ActionRunner<>(context, eventBuilder);

		runner.add(new IsPrepared());
		runner.add(new UseRodFishingSpot());
		runner.add(new UseKnifeOnFish());
		runner.add(new EatRoeOrCaviar());

		return runner;
	}
}
