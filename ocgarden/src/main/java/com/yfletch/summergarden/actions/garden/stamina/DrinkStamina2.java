package com.yfletch.summergarden.actions.garden.stamina;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ItemAction;
import net.runelite.api.MenuAction;

public class DrinkStamina2 extends ItemAction
{
	public DrinkStamina2()
	{
		super("Stamina potion (2)", Consts.DRINK);
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		return context.getState().isInGardenCenter()
			&& context.getState().needsStamina()
			&& context.getState().getInventoryCount(Consts.STAMINA_2) > 0;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		event.itemAction(2, MenuAction.CC_OP, Consts.STAMINA_2);
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return !context.getState().needsStamina();
	}
}