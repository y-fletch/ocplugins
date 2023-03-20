package com.yfletch.ocsepulchre.action.floor1;

import com.yfletch.occore.action.ObjectAction;
import com.yfletch.occore.event.WrappedEvent;
import com.yfletch.ocsepulchre.Const;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import net.runelite.api.TileObject;

public class JumpPlatform extends ObjectAction<OCSepulchreContext>
{
	public JumpPlatform()
	{
		super("Jump", "Platform");
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return ctx.isInSubfloor1East();
	}

	@Override
	public boolean isWorking(OCSepulchreContext ctx)
	{
		return ctx.isPathingTo(Const.F1E_PLATFORM)
			|| ctx.isPathingTo(Const.F1W_PLATFORM);
	}

	@Override
	public boolean isDone(OCSepulchreContext ctx)
	{
		return ctx.isInFloor1Centre();
	}

	@Override
	public void run(OCSepulchreContext ctx, WrappedEvent event)
	{
		TileObject object = ctx.getNearestObject("Platform");
		event.builder().object()
			.setObject(object)
			.setOption("Jump", 1)
			.override();
	}
}
