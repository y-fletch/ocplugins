package com.yfletch.summergarden.actions.restock.bank;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.NpcAction;
import net.runelite.api.MenuAction;

public class TeleportApprentice extends NpcAction
{
	public TeleportApprentice()
	{
		super("Apprentice", Consts.APPRENTICE_TELEPORT);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInAlKharid()
			&& context.getState().getInventoryCount(Consts.EMPTY_BEER_GLASS) > 0
			&& context.getState().getFreeInventorySlots() == 2
			&& !context.getObjectManager().isVisible(Consts.APPRENTICE_DOOR_CLOSED)
			&& context.getState().isNpcVisible(Consts.APPRENTICE);
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.npcAction(Consts.APPRENTICE_TELEPORT, MenuAction.NPC_FOURTH_OPTION, Consts.APPRENTICE);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().isRunning();
	}
}
