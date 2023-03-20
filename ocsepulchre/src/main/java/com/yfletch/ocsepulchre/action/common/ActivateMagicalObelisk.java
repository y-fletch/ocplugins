package com.yfletch.ocsepulchre.action.common;

import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;
import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;

public class ActivateMagicalObelisk extends ObjectAction<OCSepulchreContext>
{
	public ActivateMagicalObelisk()
	{
		super("Activate", "Magical Obelisk");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isInFloor1Centre();
	}

	@Override
	public boolean isWorking(OCSepulchreContext ctx)
	{
		return ctx.isPathingTo(Const.MAGICAL_OBELISK);
	}

	@Override
	public boolean isDone(OCSepulchreContext ctx)
	{
		// TODO: ctx.getRunEnergy() == 100;
		return ctx.isInLobby();
	}

	@Override
	public void run(OCSepulchreContext ctx, WrappedEvent event)
	{
		event.builder().object()
			.setObject(Const.MAGICAL_OBELISK)
			.setOption("Quick-exit", 3)
			// TODO: .setOption("Activate", 1)
			.override();
	}
}
