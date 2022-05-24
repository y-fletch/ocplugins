package com.yfletch.rift.action;

import com.yfletch.rift.Pouch;
import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ItemAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

public class EmptyPouch extends ItemAction<RiftContext>
{
	private final Pouch pouch;

	public EmptyPouch(Pouch pouch)
	{
		super("Empty", pouch.getItemName());
		this.pouch = pouch;
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		return ctx.isFull(pouch)
			&& (ctx.hasItem(pouch.getItemId()) || ctx.hasItem(pouch.getDegradedItemId()))
			&& (!ctx.hasItem(ItemID.GUARDIAN_ESSENCE) || ctx.flag("emptying-pouches"));
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return !ctx.isFull(pouch);
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		int itemId = ctx.hasItem(pouch.getItemId()) ? pouch.getItemId() : pouch.getDegradedItemId();
		event.overrideItemAction(3, MenuAction.CC_OP, itemId);
	}

	@Override
	public void done(RiftContext ctx)
	{
		ctx.flag("emptying-pouches", ctx.hasHigherTierPouch(pouch));
	}
}
