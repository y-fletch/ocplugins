package com.yfletch.summergarden.actions.garden;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class CrushSqirkFruit extends ItemAction
{
	public CrushSqirkFruit()
	{
		super("Summer sq'irk", Consts.USE);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return "Pestle and mortar".equals(context.getState().getUsingItemName());
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.itemAction(0, MenuAction.WIDGET_TARGET_ON_WIDGET, Consts.SUMMER_SQIRK);
		context.setFlag("pestle-clicked", false);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().getInventoryCount(Consts.SUMMER_SQIRK) == 0;
	}
}
