package com.yfletch.ocbloods.action.bank.repair;

import com.yfletch.ocbloods.OCBloodsContext;
import com.yfletch.occore.action.WidgetAction;
import com.yfletch.occore.event.WrappedEvent;

public class ClickToContinueNPC extends WidgetAction<OCBloodsContext>
{
	private static final int CLICK_TO_CONTINUE = 15138821;

	public ClickToContinueNPC()
	{
		super("Continue");
	}

	@Override
	public boolean isReady(OCBloodsContext ctx)
	{
		return ctx.dialogNpcTextContains("What do you want?");
	}

	@Override
	public boolean isDone(OCBloodsContext ctx)
	{
		return !ctx.dialogNpcTextContains("What do you want?");
	}

	@Override
	public void run(OCBloodsContext ctx, WrappedEvent event)
	{
		event.builder().widget()
			.setDialogOption(CLICK_TO_CONTINUE)
			.override();
	}
}
