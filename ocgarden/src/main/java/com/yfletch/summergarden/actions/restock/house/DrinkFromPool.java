package com.yfletch.summergarden.actions.restock.house;

import com.yfletch.summergarden.Consts;
import com.yfletch.summergarden.State;
import com.yfletch.summergarden.util.ObjectManager;
import com.yfletch.summergarden.util.WrappedEvent;
import com.yfletch.summergarden.util.action.ActionContext;
import com.yfletch.summergarden.util.action.ObjectAction;
import javax.inject.Inject;

public class DrinkFromPool extends ObjectAction
{
	@Inject
	private State state;

	@Inject
	private ObjectManager objectManager;

	public DrinkFromPool()
	{
		super("Rejuvenation pool", "Drink");
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		if (!context.getState().isInHouse()
			|| context.getFlag("drank-from-pool"))
		{
			return false;
		}

		return context.getObjectManager().isVisible(Consts.POOL_1)
			|| context.getObjectManager().isVisible(Consts.POOL_2)
			|| context.getObjectManager().isVisible(Consts.POOL_3)
			|| context.getObjectManager().isVisible(Consts.POOL_4);
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		if (objectManager.isVisible(Consts.POOL_1))
		{
			event.objectAction("Drink", Consts.POOL_1, 1);
		}
		else if (objectManager.isVisible(Consts.POOL_2))
		{
			event.objectAction("Drink", Consts.POOL_2, 1);
		}
		else if (objectManager.isVisible(Consts.POOL_3))
		{
			event.objectAction("Drink", Consts.POOL_3, 1);
		}
		else if (objectManager.isVisible(Consts.POOL_4))
		{
			event.objectAction("Drink", Consts.POOL_4, 1);
		}
	}

	@Override
	public void done(ActionContext context)
	{
		if (state.getEnergy() == 100)
		{
			context.setFlag("drank-from-pool", true);
		}
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return context.getState().getEnergy() == 100;
	}
}
