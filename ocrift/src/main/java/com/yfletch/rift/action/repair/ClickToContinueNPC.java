package com.yfletch.rift.action.repair;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.InterfaceAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.MenuAction;

public class ClickToContinueNPC extends InterfaceAction<RiftContext>
{
	private static final int CLICK_TO_CONTINUE = 15138821;

	public ClickToContinueNPC()
	{
		super("Continue", null);
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.dialogNpcTextContains("What do you want?");
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.dialogNpcTextContains("What do you want?");
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		event.overrideInterfaceAction("Continue", MenuAction.WIDGET_CONTINUE, CLICK_TO_CONTINUE);
	}
}
