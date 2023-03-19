package com.yfletch.ocsepulchre;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import com.yfletch.ocsepulchre.action.Wait;
import com.yfletch.ocsepulchre.action.bank.ClimbDownStairs;
import com.yfletch.ocsepulchre.action.floor1.east.MoveToF1E1;
import com.yfletch.ocsepulchre.action.floor1.east.MoveToF1E2;
import com.yfletch.ocsepulchre.action.floor1.north.MoveToF1N1;
import com.yfletch.ocsepulchre.action.floor1.south.MoveToF1S1;
import com.yfletch.ocsepulchre.action.floor1.south.MoveToF1S1a;
import com.yfletch.ocsepulchre.action.floor1.south.MoveToF1S2;
import com.yfletch.ocsepulchre.action.floor1.west.MoveToF1W1;

@Singleton
public class OCSepulchreRunnerFactory
{
	@Inject
	private OCSepulchreContext context;

	@Inject
	private EventBuilder eventBuilder;

	public ActionRunner<OCSepulchreContext> create()
	{
		ActionRunner<OCSepulchreContext> runner = new ActionRunner<>(context, eventBuilder);

		// bank
		runner.add(new ClimbDownStairs());

		// floor 1
		// north
		runner.add(new MoveToF1N1());

		// south
		runner.add(new MoveToF1S1());
		runner.add(new MoveToF1S2()); // prefer F1S2 over F1S1a
		runner.add(new MoveToF1S1a());

		// east
		runner.add(new MoveToF1E1());
		runner.add(new MoveToF1E2());

		// west
		runner.add(new MoveToF1W1());

		// consume waiting time
		runner.add(new Wait());

		return runner;
	}
}
