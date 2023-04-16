package com.yfletch.ocherblore;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.unethicalite.client.Static;

@Slf4j
@Singleton
public class OCHerbloreRunner extends ActionRunner<OCHerbloreContext>
{
	@Inject
	public OCHerbloreRunner(OCHerbloreContext context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup()
	{
		// can only get item compositions on client thread
		if (!Static.getClient().isClientThread())
		{
			return;
		}

		final var primary = getContext().getPrimaryItem();
		final var secondary = getContext().getSecondaryItem();

		final var nearestBank = getContext().getNearestBank();
		final var nearestNPC = getContext().getNearestBankNPC();

		add(builder().prep().withConditions(ctx -> Map.of(
			"Primary item not set", primary != null,
			"Secondary item not set", secondary != null,
			"Primary item not in bank", primary == null || !ctx.isBankOpen() || ctx.hasBanked(primary.getId()),
			"Secondary item not in bank", secondary == null || !ctx.isBankOpen() || ctx.hasBanked(secondary.getId()),
			"Bank must be nearby", nearestBank != null || nearestNPC != null
		)));

		if (primary == null || secondary == null ||
			nearestBank == null && nearestNPC == null)
		{
			return;
		}

		final var distToBankObject = nearestBank == null
			? Integer.MAX_VALUE
			: nearestBank.getWorldLocation()
			.distanceTo(getContext().getPlayerLocation());
		final var distToBankNPC = nearestNPC == null
			? Integer.MAX_VALUE
			: nearestNPC.getWorldLocation()
			.distanceTo(getContext().getPlayerLocation());

		if (nearestBank != null && distToBankObject <= distToBankNPC)
		{
			final var bankIndex = nearestBank.getActionIndex("Bank");
			final var useIndex = nearestBank.getActionIndex("Use");
			add(builder().object(bankIndex >= 0 ? "Bank" : "Use", nearestBank.getName())
					.readyIf(ctx -> !ctx.hasItem(ctx.getPrimaryItemId())
						&& !ctx.isBankOpen())
					.doneIf(ActionContext::isBankOpen)
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
					.readyIf(ctx -> !ctx.hasItem(ctx.getPrimaryItemId())
						&& !ctx.isBankOpen()
					)
					.doneIf(ActionContext::isBankOpen)
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
					&& !ctx.hasItem(ctx.getPrimaryItemId())
					&& ctx.getFreeInventorySlots() < 28)
				.onceUntil(ctx -> ctx.getFreeInventorySlots() == 28)
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Deposit inventory", 1)
						.setWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY)
						.override()
				)
		);

		add(builder().item("Withdraw-14", primary.getName())
				.readyIf(ctx -> ctx.isBankOpen()
					&& !ctx.hasItem(primary.getId())
					&& ctx.getFreeInventorySlots() == 28)
				.blockExtraClicks()
				.onceUntil(ctx -> ctx.hasItem(primary.getId()))
				.onRun(
					(ctx, event) -> event.builder().item()
						.withdraw()
						.setOption("Withdraw-14", 5)
						.setItem(primary.getId())
						.setLowPriority()
						.override()
				)
		);

		add(builder().item("Withdraw-14", secondary.getName())
				.readyIf(ctx -> ctx.isBankOpen()
					&& ctx.getFreeInventorySlots() == 14)
				.blockExtraClicks()
				.onceUntil(ctx -> ctx.hasItem(secondary.getId()))
				.onRun(
					(ctx, event) -> event.builder().item()
						.withdraw()
						.setOption("Withdraw-14", 5)
						.setItem(secondary.getId())
						.setLowPriority()
						.override()
				)
		);

		add(builder().widget("Close")
				.readyIf(ctx -> ctx.isBankOpen()
					&& ctx.getFreeInventorySlots() == 0)
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
					&& ctx.hasItem(primary.getId())
					&& ctx.hasItem(secondary.getId())
					&& !ctx.isMixing()
					&& !ctx.isSkillInterfaceOpen())
				.doneIf(ctx -> ctx.isSkillInterfaceOpen()
					|| ctx.isMixing())
				.onRun(
					(ctx, event) -> event.builder().item()
						.use(primary.getId())
						.on(secondary.getId())
						.override()
				)
		);

		add(builder().widget("Make", "Potion")
				.readyIf(OCHerbloreContext::isSkillInterfaceOpen)
				.doneIf(ctx -> !ctx.hasItem(ctx.getPrimaryItemId()))
				.blockExtraClicks()
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Make", 1)
						.setWidget(WidgetID.MULTISKILL_MENU_GROUP_ID, 17694734)
						.override()
				)
		);

		add(builder().consume("Waiting"));
	}
}
