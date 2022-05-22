package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;
import net.runelite.api.MenuAction;

public class UseShortcut extends ObjectAction
{
	public UseShortcut()
	{
		super("Big window", "Climb");
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getConfig().usePalaceShortcut()
			&& context.getState().isInAlKharidPalace()
			&& context.getObjectManager().isVisible(Consts.PALACE_SHORTCUT);
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.objectAction("Climb", MenuAction.GAME_OBJECT_FIRST_OPTION, Consts.PALACE_SHORTCUT);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return !context.getState().isInAlKharidPalace();
	}
}
