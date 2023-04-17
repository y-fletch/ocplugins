package com.yfletch.ocbwans;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import java.util.Map;
import java.util.function.Predicate;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;

@Singleton
public class Runner extends ActionRunner<Context>
{
	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
		final var nearestRange = context.getNearestRange();
		final var nearestBank = context.getNearestBank();
		final var nearestNPC = context.getNearestBankNPC();

		add(builder().prep().withConditions(ctx -> Map.of(
			"Bank must be nearby", nearestBank != null || nearestNPC != null,
			"Range/fire must be nearby", nearestRange != null,
			"Missing Karambwans",
			!ctx.isBankOpen() || ctx.hasBanked(ItemID.RAW_KARAMBWAN) || ctx.hasItem(ItemID.RAW_KARAMBWAN)
		)));

		if (nearestRange == null || nearestBank == null && nearestNPC == null)
		{
			return;
		}

		final var distToBankObject = nearestBank == null
			? Integer.MAX_VALUE
			: nearestBank.getWorldLocation().distanceTo(context.getPlayerLocation());
		final var distToBankNPC = nearestNPC == null
			? Integer.MAX_VALUE
			: nearestNPC.getWorldLocation().distanceTo(context.getPlayerLocation());

		final Predicate<Context> ready = ctx -> !ctx.hasItem(ItemID.RAW_KARAMBWAN)
			&& !ctx.isBankOpen();

		final Predicate<Context> done = ActionContext::isBankOpen;

		if (nearestBank != null && distToBankObject <= distToBankNPC)
		{
			final var bankIndex = nearestBank.getActionIndex("Bank");
			final var useIndex = nearestBank.getActionIndex("Use");
			add(builder().object(bankIndex >= 0 ? "Bank" : "Use", nearestBank.getName())
					.readyIf(ready)
					.doneIf(done)
					.onRun(
						(obj, event) -> event.builder().object()
							.setObject(nearestBank)
							.setOption(
								bankIndex >= 0 ? "Bank" : "Use",
								(bankIndex >= 0 ? bankIndex : useIndex) + 1
							)
							.override()
					)
			);
		}

		if (nearestNPC != null && distToBankObject > distToBankNPC)
		{
			final var bankIndex = nearestNPC.getActionIndex("Bank");
			add(builder().npc("Bank", nearestNPC.getName())
					.readyIf(ready)
					.doneIf(done)
					.onRun(
						(obj, event) -> event.builder().npc()
							.setNpc(nearestNPC)
							.setOption("Bank", bankIndex + 1)
							.override()
					)
			);
		}

		add(builder().widget("Deposit inventory")
				.readyIf(ctx -> ctx.isBankOpen()
					&& ctx.hasItem(ItemID.BURNT_KARAMBWAN, ItemID.COOKED_KARAMBWAN, ItemID.POISON_KARAMBWAN))
				.doneIf(
					ctx -> !ctx.hasItem(ItemID.BURNT_KARAMBWAN, ItemID.COOKED_KARAMBWAN, ItemID.POISON_KARAMBWAN)
				)
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Deposit inventory", 1)
						.setWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY)
						.override()
				)
		);

		// withdraw bwans
		add(builder().item("Withdraw-All", "Raw karambwan")
				.readyIf(ctx -> ctx.isBankOpen()
					&& !ctx.hasItem(ItemID.RAW_KARAMBWAN))
				.doneIf(ctx -> ctx.hasItem(ItemID.RAW_KARAMBWAN))
				.onRun(
					(ctx, event) -> event.builder().item()
						.withdraw()
						.setOption("Withdraw-All", 7)
						.setItem(ItemID.RAW_KARAMBWAN)
						.setLowPriority()
						.override()
				)
		);

		add(builder().widget("Close")
				.readyIf(ctx -> ctx.isBankOpen()
					&& ctx.hasItem(ItemID.RAW_KARAMBWAN))
				.doneIf(ctx -> !ctx.isBankOpen())
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Close", 1)
						.setWidget(WidgetID.BANK_GROUP_ID, 2, 11)
						.override()
				)
		);

		// use bwan
		add(builder().object("Use Raw karambwan ->", nearestRange.getName())
				.readyIf(ctx -> !ctx.isBankOpen()
					&& ctx.hasItem(ItemID.RAW_KARAMBWAN))
				.onceUntil(ctx -> !ctx.flag("bwan"))
				.onRun(
					(ctx, event) -> event.builder().object()
						.use(ItemID.RAW_KARAMBWAN)
						.on(nearestRange)
						.onClick(e -> ctx.flag("bwan", true, 0))
						.override()
				)
		);

		add(builder().consume("Wait").readyIf(ctx -> true));
	}

//	private void addBankSteps()
//	{}
//
//	private void addCookingSteps()
//	{}
}
