package com.yfletch.rift.action.repair;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.InterfaceAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;

public class ClickToContinuePlayer extends InterfaceAction<RiftContext>
{
	private static final int CLICK_TO_CONTINUE = 14221317;

	public ClickToContinuePlayer()
	{
		super("Continue", null);
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.dialogPlayerTextContains("Can you repair my pouches?");
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.dialogPlayerTextContains("Can you repair my pouches?");
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideInterfaceAction("Continue", MenuAction.WIDGET_CONTINUE, CLICK_TO_CONTINUE);
	}

	@Override
	public void done(RiftContext ctx)
	{
		ctx.repairPouches();
	}
}
