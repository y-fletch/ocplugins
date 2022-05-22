package com.yfletch.summergarden.actions.restock.house;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;
import net.runelite.api.MenuAction;

public class TeleportMountedGlory extends ObjectAction
{
	public TeleportMountedGlory()
	{
		super("Amulet of Glory", Consts.MOUNTED_AL_KHARID);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInHouse()
			&& context.getObjectManager().isVisible(Consts.MOUNTED_GLORY);
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.objectAction(Consts.MOUNTED_AL_KHARID, MenuAction.GAME_OBJECT_FOURTH_OPTION, Consts.MOUNTED_GLORY);
	}

	@Override
	public void done(ActionContext context)
	{
		context.setFlag("in-house", false);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().isRunning();
	}
}
