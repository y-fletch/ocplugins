package com.yfletch.ocblastfurnance;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.ocblastfurnance.util.Method;
import com.yfletch.occore.ActionContext;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;

@Slf4j
@Singleton
public class OCBlastFurnaceRunner extends ActionRunner<OCBlastFurnaceContext>
{
	private final int[] BANKABLE_ITEMS = new int[]{
		ItemID.GOLD_BAR,
		ItemID.IRON_BAR,
		ItemID.STEEL_BAR,
		ItemID.MITHRIL_BAR,
		ItemID.ADAMANTITE_BAR,
		ItemID.RUNITE_BAR,
		ItemID.VIAL,
		ItemID.STAMINA_POTION1,
		ItemID.STAMINA_POTION2,
		ItemID.STAMINA_POTION3,
		ItemID.STAMINA_POTION4
	};

	@Inject
	public OCBlastFurnaceRunner(OCBlastFurnaceContext context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);

		add(builder().prep()
				.withConditions(ctx -> {
					final var map = new HashMap<String, Boolean>();

					map.put("Not on Blast Furnace world", ctx.isOnBlastFurnaceWorld());

					if (ctx.getMethod() == Method.GOLD_BARS)
					{
						map.put(
							"Missing Goldsmith gauntlets",
							ctx.hasItem(ItemID.GOLDSMITH_GAUNTLETS) || ctx.hasEquipped(ItemID.GOLDSMITH_GAUNTLETS)
						);
						map.put(
							"Missing Ice gloves",
							ctx.hasItem(ItemID.ICE_GLOVES) || ctx.hasEquipped(ItemID.ICE_GLOVES)
						);
					}
					else
					{
						map.put("No method selected", false);
					}

					return map;
				})
		);

		final var staminas = Map.of(
			"Stamina potion(1)", ItemID.STAMINA_POTION1,
			"Stamina potion(2)", ItemID.STAMINA_POTION2,
			"Stamina potion(3)", ItemID.STAMINA_POTION3,
			"Stamina potion(4)", ItemID.STAMINA_POTION4
		);

		// withdraw & drink stamina
		for (final var entry : staminas.entrySet())
		{
			add(builder().item("Withdraw-1", entry.getKey())
					.readyIf(ctx -> ctx.isBankOpen()
						&& ctx.requiresStamina()
						&& !ctx.hasItem(
						ItemID.STAMINA_POTION1, ItemID.STAMINA_POTION2,
						ItemID.STAMINA_POTION3, ItemID.STAMINA_POTION4
					)
						&& ctx.hasBanked(entry.getValue())
						&& ctx.hasFreeInventorySlot())
					.doneIf(ctx -> ctx.hasItem(entry.getValue()))
					.onRun(
						(ctx, event) -> event.builder().item()
							.withdraw()
							.setOption("Withdraw-1", 1)
							.setItem(entry.getValue())
							.override()
					)
			);

			add(builder().item("Drink", entry.getKey())
					.readyIf(ctx -> ctx.isBankOpen()
						&& ctx.requiresStamina()
						&& ctx.hasItem(entry.getValue()))
					.doneIf(ctx -> !ctx.requiresStamina())
					.onRun(
						(ctx, event) -> event.builder().item()
							.deposit()
							.setOption("Drink", 9)
							.setItem(entry.getValue())
							.setLowPriority()
							.override()
					)
			);
		}

		// deposit all
		add(builder().widget("Deposit inventory")
				.readyIf(ctx -> ctx.isBankOpen()
					&& ctx.hasItem(BANKABLE_ITEMS))
				.doneIf(ctx -> !ctx.hasItem(BANKABLE_ITEMS))
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Deposit inventory", 1)
						.setWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY)
						.override()
				)
		);

		// withdraw ore
		// TODO - other methods
		add(builder().item("Withdraw-All", "Gold ore")
				.readyIf(ctx -> ctx.isBankOpen()
					&& !ctx.hasItem(ItemID.GOLD_ORE))
				.doneIf(ctx -> ctx.hasItem(ItemID.GOLD_ORE))
				.onRun(
					(ctx, event) -> event.builder().item()
						.withdraw()
						.setOption("Withdraw-All", 7)
						.setItem(ItemID.GOLD_ORE)
						.setLowPriority()
						.override()
				)
		);

		// deposit ore on belt
		add(builder().object("Put-ore-on", "Conveyor belt")
				.readyIf(ctx -> ctx.hasItem(ItemID.GOLD_ORE))
				.workingIf(ctx -> ctx.isPathingTo(ObjectID.CONVEYOR_BELT))
				.doneIf(ctx -> !ctx.hasItem(ItemID.GOLD_ORE)
					&& ctx.isAtConveyorBelt())
				.onRun(
					(ctx, event) -> event.builder().object()
						.setOption("Put-ore-on", 1)
						.setObject(ObjectID.CONVEYOR_BELT)
						.override()
				)
		);

		// equip gold gauntlets
		add(builder().item("Wear", "Goldsmith gauntlets")
				.readyIf(ctx -> ctx.isAtConveyorBelt()
					&& !ctx.hasEquipped(ItemID.GOLDSMITH_GAUNTLETS))
				.doneIf(ctx -> ctx.hasEquipped(ItemID.GOLDSMITH_GAUNTLETS))
				.onRun(
					(ctx, event) -> event.builder().item()
						.setOption("Wear", 3)
						.setItem(ItemID.GOLDSMITH_GAUNTLETS)
						.override()
				)
		);

		// move to bar dispenser
		add(builder().object("Check", "Bar dispenser")
				.readyIf(ctx -> ctx.isAtConveyorBelt()
					&& ctx.hasEquipped(ItemID.GOLDSMITH_GAUNTLETS))
				.workingIf(ctx -> ctx.isPathingTo(ctx.getBarDispenser()))
				.doneIf(OCBlastFurnaceContext::isNearBarDispenser)
				.onRun(
					(ctx, event) -> event.builder().object()
						.setOption("Check", 1)
						.setObject(ctx.getBarDispenser())
						.override()
				)
		);

		// equip ice gloves
		add(builder().item("Wear", "Ice gloves")
				.readyIf(ctx -> ctx.isNearBarDispenser()
					&& ctx.hasEquipped(ItemID.GOLDSMITH_GAUNTLETS)
					&& ctx.furnaceHasBars())
				.doneIf(ctx -> ctx.hasEquipped(ItemID.ICE_GLOVES))
				.onRun(
					(ctx, event) -> event.builder().item()
						.setOption("Wear", 3)
						.setItem(ItemID.ICE_GLOVES)
						.override()
				)
		);

		// open take interface
		add(builder().object("Take", "Bar dispenser")
				.readyIf(ctx -> ctx.isNearBarDispenser()
					&& ctx.hasEquipped(ItemID.ICE_GLOVES)
					&& !ctx.hasItem(ItemID.GOLD_BAR))
				.doneIf(OCBlastFurnaceContext::isTakeInterfaceOpen)
				.onRun(
					(ctx, event) -> event.builder().object()
						.setOption("Take", 1)
						.setObject(ctx.getBarDispenser())
						.override()
				)
		);

		// click interface
		add(builder().widget("Take", "Gold bar")
				.readyIf(OCBlastFurnaceContext::isTakeInterfaceOpen)
				.doneIf(ctx -> ctx.hasItem(ItemID.GOLD_BAR))
				.onRun(
					(ctx, event) -> event.builder().widget()
						.setOption("Take", 1)
						.setWidget(WidgetID.MULTISKILL_MENU_GROUP_ID, 17694734)
						.override()
				)
		);

		// open bank
		add(builder().object("Use", "Bank chest")
				.readyIf(ctx -> ctx.isNearBarDispenser()
					&& ctx.hasItem(ItemID.GOLD_BAR))
				.workingIf(ctx -> ctx.isPathingTo(ObjectID.BANK_CHEST_26707))
				.doneIf(ActionContext::isBankOpen)
				.onRun(
					(ctx, event) -> event.builder().object()
						.setOption("Use", 1)
						.setObject(ObjectID.BANK_CHEST_26707)
						.override()
				)
		);
	}
}
