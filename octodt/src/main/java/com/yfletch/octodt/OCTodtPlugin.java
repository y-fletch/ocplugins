package com.yfletch.octodt;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.banked;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.of;
import static com.yfletch.occore.v2.interaction.Walking.offset;
import static com.yfletch.occore.v2.interaction.Walking.walk;
import com.yfletch.occore.v2.util.TextColor;
import static com.yfletch.occore.v2.util.Util.nameContaining;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ObjectID;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.commons.Rand;
import net.unethicalite.api.events.ExperienceGained;
import net.unethicalite.api.events.InventoryChanged;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Todt",
	description = "One-click mass world Wintertodt",
	enabledByDefault = false
)
public class OCTodtPlugin extends RunnerPlugin<TodtContext>
{
	@Inject private TodtConfig config;
	@Inject private TodtContext context;

	@Inject
	public void init(TodtContext context, TodtConfig config)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(TodtConfig.GROUP_NAME);
		actionsPerTick(8);
	}

	@Override
	public void setup()
	{
		requirements()
			.must(
				c -> WarmClothing.getCount() >= 4,
				"Must have 4 pieces of " + TextColor.ITEM + "warm clothing " + TextColor.WHITE + "equipped"
			)
			.mustHaveOnPerson(nameContaining(" axe"), "any axe");

		// bank

		group(
			c -> c.isRoundComplete() && !c.hasEnoughFood(),
			() -> {
				// exit wintertodt if not enough food left
				action().name("Exit wintertodt")
					.until(c -> !c.isInWintertodt())
					.then(c -> object("Doors of Dinh").interact("Enter"))
					.maxDelay(10);

				// open bank
				action().name("Open bank")
					.until(c -> Bank.isOpen())
					.then(c -> object("Bank chest").interact("Bank"))
					.delay(2);

				// deposit all boxes
				action().name("Deposit boxes")
					.until(c -> !Inventory.contains("Supply crate"))
					.then(c -> item("Supply crate").depositAll());

				// deposit empty vials
				action().name("Deposit vials")
					.until(c -> !Inventory.contains("Vial"))
					.then(c -> item("Vial").depositAll());

				// withdraw x food
				action().name("Withdraw food")
					.until(TodtContext::hasEnoughFood)
					.then(c -> banked(config.food().getIds()).withdraw(1))
					.repeat(TodtContext::getFoodRequired);
			}
		);

		action().name("Enter wintertodt")
			.until(TodtContext::isInWintertodt)
			.then(c -> object("Doors of Dinh").interact("Enter"));

		action().name("Collect hammer")
			.until(c -> Inventory.contains("Hammer"))
			.then(c -> object(ObjectID.CRATE_29316).interact("Take-hammer"));

		action().name("Collect knife")
			.when(c -> config.fletch())
			.until(c -> Inventory.contains("Knife"))
			.then(c -> object(ObjectID.CRATE_29317).interact("Take-knife"));

		action().name("Collect tinderbox")
			.until(c -> Inventory.contains("Tinderbox", "Bruma torch") || Equipment.contains("Bruma torch"))
			.then(c -> object(ObjectID.CRATE_29319).interact("Take-tinderbox"));

		// walk to brazier if game not started
		action().name("Walk to brazier")
			.when(c -> c.getWintertodtEnergy() == 0)
			.then(c -> walk(offset(c.getBrazier().getWorldLocation(), 0, -2)))
			.delay(4, 20);

		// game loop:

		action().name("Step to safety")
			.when(TodtContext::isOnDangerousTile)
			.until(c -> c.getSnowAttacks().isEmpty())
			.then(c -> walk(0, -Rand.nextInt(3, 5)));

		action().name("Eat food")
			.until(c -> !c.shouldEat())
			.then(c -> item(config.food().getIds()).interact("Eat", "Drink"))
			.many();

		action().name("Initial light brazier")
			.when(c -> c.flag("lighting")
				|| c.getWintertodtEnergy() == 100
				&& c.getBrazier().hasAction("Light"))
			.then(c -> of(c.getBrazier()).interact("Light"))
			.onClick(c -> c.flag("lighting", true, 2));

		action().name("Walk to root safespot")
			.when(c -> !c.flag("burning") && !c.flag("fletching"))
			.until(TodtContext::isInSafespot)
			.then(c -> walk(c.getSafespot()));

//		action().name("Special attack")
//			.when(c -> !c.flag("burning") && !c.flag("fletching"))
//			.until(c -> Static.getClient().getSpec)
//			.then(c -> object("Bruma roots").interact("Chop"));

		action().name("Chop bruma")
			.when(c -> !c.flag("burning") && !c.flag("fletching"))
			.until(c -> Inventory.isFull())
			.then(c -> object("Bruma roots").interact("Chop"));

		action().name("Fletch bruma")
			.when(c -> c.flag("fletching"))
			.then(c -> item("Knife").useOn(item("Bruma root")));

		action().name("Light brazier")
			.when(c -> c.flag("lighting") || c.flag("burning")
				&& c.getBrazier().hasAction("Light"))
			.then(c -> of(c.getBrazier()).interact("Light"))
			.onClick(c -> c.flag("lighting", true, 2));

		action().name("Fix brazier")
			.when(c -> c.flag("fixing") || c.flag("burning")
				&& c.getBrazier().hasAction("Fix"))
			.then(c -> of(c.getBrazier()).interact("Fix"))
			.delay(1)
			.onClick(c -> c.flag("fixing", true, 2));

		action().name("Feed brazier")
			.when(c -> c.flag("burning"))
			.then(c -> of(c.getBrazier()).interact("Feed"));
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event)
	{
		if (event.getActor() == Static.getClient().getLocalPlayer()
			&& getCurrentRule() != null)
		{
			// force current rule to click again
			log.info("Resetting rule");
			getCurrentRule().reset(context);
			log.info("Reset rule");
		}
	}

	// must run after devious
	@Subscribe
	public void onInventoryChanged(InventoryChanged event)
	{
		Static.getClientThread().invokeLater(() -> {
			if (Inventory.isFull())
			{
				context.clear("fletching");
				context.clear("burning");

				context.flag(
					config.fletch() && Inventory.contains("Bruma root")
						? "fletching" : "burning",
					true
				);
				return;
			}

			if (!Inventory.contains("Bruma root") && Inventory.contains("Bruma kindling"))
			{
				context.clear("fletching");
				context.flag("burning", true);
			}

			if (!Inventory.contains("Bruma kindling", "Bruma root"))
			{
				context.clear("burning");
			}
		});
	}

	@Subscribe
	public void onExperienceGained(ExperienceGained event)
	{
		context.clear("lighting");
	}

	@Provides
	TodtConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TodtConfig.class);
	}
}
