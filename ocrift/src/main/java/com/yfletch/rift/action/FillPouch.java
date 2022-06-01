package com.yfletch.rift.action;

import com.yfletch.rift.enums.Pouch;
import com.yfletch.rift.RiftContext;
import com.yfletch.rift.lib.ItemAction;
import com.yfletch.rift.lib.WrappedEvent;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

public class FillPouch extends ItemAction<RiftContext>
{
	private final Pouch pouch;

	public FillPouch(Pouch pouch)
	{
		super("Fill", pouch.getItemName());
		this.pouch = pouch;
	}

	@Override
	public boolean isReady(RiftContext ctx)
	{
		if (pouch == Pouch.COLOSSAL)
		{
			return !ctx.isOutsideRift()
				&& !ctx.isFull(pouch)
				&& (ctx.hasItem(pouch.getItemId()) || ctx.hasItem(pouch.getDegradedItemId()))
				&& ctx.getFreeInventorySlots() == 0;
		}

		return !ctx.isOutsideRift()
			&& !ctx.isFull(pouch)
			&& (ctx.hasItem(pouch.getItemId()) || ctx.hasItem(pouch.getDegradedItemId()))
			&& (ctx.getItemCount(ItemID.GUARDIAN_ESSENCE) >= ctx.getPouchCapacity() || ctx.flag("filling-pouches"));
	}

	@Override
	public boolean isDone(RiftContext ctx)
	{
		return ctx.flag("p-fill-" + pouch.getItemId())
			|| ctx.isFull(pouch)
			|| !ctx.hasItem(ItemID.GUARDIAN_ESSENCE);
	}

	@Override
	public void run(RiftContext ctx, WrappedEvent event)
	{
		int itemId = ctx.hasItem(pouch.getItemId()) ? pouch.getItemId() : pouch.getDegradedItemId();
		event.overrideItemAction(2, MenuAction.CC_OP, itemId);

		// _Immediately_ set this pouch as clicked -
		// because the runner also ticks on menu events,
		// this will cause this action to finish immediately
		// after one click, letting the control move to the next
		// pouch.
		// i.e. multiple clicks in one tick.
		ctx.flag("p-fill-" + pouch.getItemId(), true);
	}

	@Override
	public void done(RiftContext ctx)
	{
		ctx.flag("filling-pouches", ctx.hasHigherTierPouch(pouch));
		ctx.flag("crafting", false);
		ctx.flag("mining", false);

		if (!ctx.flag("filling-pouches"))
		{
			// reset all clicked statuses
			ctx.flag("p-fill-" + Pouch.SMALL.getItemId(), false);
			ctx.flag("p-fill-" + Pouch.MEDIUM.getItemId(), false);
			ctx.flag("p-fill-" + Pouch.LARGE.getItemId(), false);
			ctx.flag("p-fill-" + Pouch.GIANT.getItemId(), false);
			ctx.flag("p-fill-" + Pouch.COLOSSAL.getItemId(), false);
		}
	}
}
