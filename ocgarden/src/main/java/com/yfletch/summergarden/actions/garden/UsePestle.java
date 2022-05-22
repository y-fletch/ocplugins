package com.yfletch.summergarden.actions.garden;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

import javax.inject.Singleton;

@Singleton
public class UsePestle extends ItemAction
{
	public UsePestle()
	{
		super("Pestle and mortar", Consts.USE);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().getInventoryCount(ItemID.SUMMER_SQIRK) > 1;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.itemAction(0, MenuAction.WIDGET_TARGET, Consts.PESTLE_AND_MORTAR);
		context.setFlag("pestle-clicked", true);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return "Pestle and mortar".equals(context.getState().getUsingItemName());
	}
}
