package it.enok.ocnightmarezone;

import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.event.EventBuilder;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;

import java.util.Map;

@Singleton
public class Runner extends ActionRunner<Context>
{
	final int[] absorptionPotions = new int[] {
			ItemID.ABSORPTION_1,
			ItemID.ABSORPTION_2,
			ItemID.ABSORPTION_3,
			ItemID.ABSORPTION_4
	};

	final int[] superCombatPotions = new int[] {
			ItemID.SUPER_COMBAT_POTION1,
			ItemID.SUPER_COMBAT_POTION2,
			ItemID.SUPER_COMBAT_POTION3,
			ItemID.SUPER_COMBAT_POTION4
	};

	@Inject
	public Runner(Context context, EventBuilder eventBuilder)
	{
		super(context, eventBuilder);
	}

	@Override
	public void setup(Context context)
	{
		add(builder().prep().withConditions(ctx -> Map.of(
				"Need damaging item", ctx.hasItem(ctx.getDamageItemId()) || ctx.getDamageItemId() == -1,
				"Not in NMZ", ctx.inInstancedRegion()
		)));

		add(builder().item("Guzzle", "Rock Cake")
				.readyIf(ctx -> ctx.inInstancedRegion()
						&& ctx.usingRockCake()
						&& ctx.getCurrentPlayerHealth() >= 2)
				.doneIf(ctx -> ctx.getCurrentPlayerHealth() == 1)
				.onRun(
						(ctx, event) -> event.builder().item()
								.setOption("Guzzle", 4)
								.setItem(ItemID.DWARVEN_ROCK_CAKE_7510)
								.setType(MenuAction.CC_OP)
								.onClick(e -> ctx.flag("eat", true, 3))
								.override()
				)
		);

		add(builder().item("Feel", "Locator Orb")
				.readyIf(ctx -> ctx.inInstancedRegion()
						&& ctx.usingLocatorOrb()
						&& ctx.getCurrentPlayerHealth() >= 2)
				.doneIf(ctx -> ctx.getCurrentPlayerHealth() == 1)
				.onRun(
						(ctx, event) -> event.builder().item()
								.setOption("Feel", 8) // TODO: Probably not 8...
								.setItem(ItemID.LOCATOR_ORB)
								.setType(MenuAction.CC_OP) // TODO: Probably not this either!
								.onClick(e -> ctx.flag("eat", true, 3))
								.override()
				)
		);

		add(builder().item("Drink", "Absorption")
				.readyIf(ctx -> ctx.drinkAbsorptionPotion() && ctx.hasItem(absorptionPotions))
				.doneIf(ctx -> !ctx.drinkAbsorptionPotion())
				.onRun(
						(ctx, event) -> event.builder().item()
								.setOption("Drink", 2)
								.setItems(Ints.asList(absorptionPotions))
								.setType(MenuAction.CC_OP)
								.onClick(e -> ctx.flag("drinkAbsorption", true, 3))
								.override()

				)
		);

		add(builder().item("Drink", "Combat Potion")
				.readyIf(ctx -> ctx.drinkCombatPotion() && ctx.hasItem(superCombatPotions))
				.doneIf(ctx -> !ctx.drinkCombatPotion())
				.onRun(
						(ctx, event) -> event.builder().item()
								.setOption("Drink", 2)
								.setItems(Ints.asList(superCombatPotions))
								.setType(MenuAction.CC_OP)
								.onClick(e -> ctx.flag("combatPotion", true, 3))
								.override()
				)
		);

		add(builder().consume("Wait").readyIf(ctx -> true));
	}
}
