package com.yfletch.ocbankskills;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import java.util.Map;
import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.client.Static;

@Singleton
public class Runner extends ActionRunner<Context>
{
	@Inject private Client client;

	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
		// can only get item compositions on client thread
		if (!Static.getClient().isClientThread())
		{
			return;
		}

		final var primary = context.getPrimaryItem();
		final var secondary = context.getSecondaryItem();
		final var primaryName = primary == null ? "primary item" : primary.getName();
		final var secondaryName = secondary == null ? "secondary item" : secondary.getName();

		final var nearestBank = context.getNearestBank();
		final var nearestNPC = context.getNearestBankNPC();

		add(builder().prep().withConditions(ctx -> Map.of(
			"Primary item not set", primary != null,
			"Secondary item not set", secondary != null,
			"Missing " + primaryName,
			primary == null
				|| !ctx.isBankOpen()
				|| ctx.hasBanked(primary.getId())
				|| ctx.hasItem(primary.getId()),
			"Missing " + secondaryName,
			secondary == null
				|| !ctx.isBankOpen()
				|| ctx.hasBanked(secondary.getId())
				|| ctx.hasItem(secondary.getId()),
			"Bank must be nearby", nearestBank != null || nearestNPC != null,
			"Make option is invalid", ctx.getMakeOption() > 0
		)));

		if (primary == null || secondary == null ||
			nearestBank == null && nearestNPC == null)
		{
			return;
		}

		addBankStep(context, nearestBank, nearestNPC);

		add(builder().widget("Deposit inventory")
				.readyIf(ctx -> ctx.isBankOpen()
					&& (!ctx.hasPrimary() || !ctx.hasSecondary()))
				.onceUntil(ctx -> ctx.hasPrimary() && ctx.hasSecondary())
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Deposit inventory", 1)
						.setWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY)
						.override()
				)
		);

		// withdraw primary
		add(builder().item("Withdraw-N", primary.getName())
				.readyIf(ctx -> ctx.isBankOpen()
					&& !ctx.hasPrimary())
				.onceUntil(Context::hasPrimary)
				.onRun(
					(ctx, event) -> event.builder().item()
						.withdraw()
						.setOption("Withdraw-N", 5)
						.setItem(primary.getId())
						.setLowPriority()
						.override()
				)
		);

		// withdraw secondary
		add(builder().item("Withdraw-N", secondary.getName())
				.readyIf(ctx -> ctx.isBankOpen()
					&& !ctx.hasSecondary())
				.onceUntil(Context::hasSecondary)
				.onRun(
					(ctx, event) -> event.builder().item()
						.withdraw()
						.setOption("Withdraw-N", 5)
						.setItem(secondary.getId())
						.setLowPriority()
						.override()
				)
		);

		add(builder().widget("Close")
				.readyIf(ctx -> ctx.isBankOpen()
					&& ctx.hasPrimary() && ctx.hasSecondary())
				.onceUntil(ctx -> !ctx.isBankOpen())
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Close", 1)
						.setWidget(WidgetID.BANK_GROUP_ID, 2, 11)
						.override()
				)
		);

		add(builder().item("Use " + primary.getName() + " ->", secondary.getName())
				.readyIf(ctx -> !ctx.isBankOpen()
					&& ctx.hasPrimary() && ctx.hasSecondary()
					&& !ctx.isAnimating()
					&& !ctx.isSkillInterfaceOpen()
					&& !ctx.flag("just-clicked-make"))
				.doneIf(ctx -> ctx.isSkillInterfaceOpen()
					|| ctx.isAnimating())
				.onRun(
					(ctx, event) -> event.builder().item()
						.use(primary.getId())
						.on(secondary.getId())
						.override()
				)
		);

		add(builder().widget("Make", Context::getMakeTarget)
				.readyIf(Context::isSkillInterfaceOpen)
				.doneIf(ctx -> !ctx.hasPrimary() || !ctx.hasSecondary())
				.blockExtraClicks()
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Make", 1)
						.setWidget(ctx.getMakeButton())
						.onClick(menuEntry -> ctx.flag("just-clicked-make", true, 3))
						.override()
				)
		);

		add(builder().consume("Nothing").readyIf(ctx -> true));
	}

	private void addBankStep(Context context, TileObject nearestBank, NPC nearestNPC)
	{
		final var distToBankObject = nearestBank == null
			? Integer.MAX_VALUE
			: nearestBank.getWorldLocation().distanceTo(context.getPlayerLocation());
		final var distToBankNPC = nearestNPC == null
			? Integer.MAX_VALUE
			: nearestNPC.getWorldLocation().distanceTo(context.getPlayerLocation());

		final Predicate<Context> ready = ctx -> (!ctx.hasItem(ctx.getPrimaryItemId())
			|| !ctx.hasItem(ctx.getSecondaryItemId()))
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
	}
}
