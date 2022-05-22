package com.yfletch.rift.action.prep;

import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.InterfaceAction;

public class UseSpecialAttack extends InterfaceAction<RiftContext>
{
	public UseSpecialAttack()
	{
		super("Use", "Special Attack");
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.getGameTime() < 0
			&& ctx.getGameTime() > -5
			&& ctx.isInLargeMine()
			&& ctx.getSpecialEnergy() == 100;
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.getSpecialEnergy() == 0;
	}
}
