package com.yfletch.ocsepulchre.action;

import com.yfletch.occore.action.Action;
import com.yfletch.occore.event.WrappedEvent;
import com.yfletch.ocsepulchre.OCSepulchreContext;
import net.runelite.client.ui.overlay.components.LineComponent;

public class Wait extends Action<OCSepulchreContext>
{
	@Override
	public String getName()
	{
		return "Waiting";
	}

	@Override
	public LineComponent getDisplayLine(OCSepulchreContext ctx)
	{
		return LineComponent.builder()
			.left("Waiting")
			.build();
	}

	@Override
	public boolean isReady(OCSepulchreContext ctx)
	{
		return true;
	}

	@Override
	public void run(OCSepulchreContext ctx, WrappedEvent event)
	{
		event.consume();
	}
}
