package com.yfletch.occore.v2.test;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.widgets.Dialog;
import net.unethicalite.api.widgets.Widgets;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Core Tester",
	description = "Test plugin for the OCCore framework. For developers only."
)
public class TestPlugin extends RunnerPlugin<TestContext>
{
	@Inject TestContext context;

	@Inject
	public void init(TestConfig config, TestContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(TestConfig.GROUP_NAME);
	}

	@Override
	public void setup()
	{
		requirements().name("House requirements")
			.when(c -> !Bank.isOpen())
			.mustHaveOnPerson(ItemID.FIRE_RUNE, ItemID.BUNNY_TOP, ItemID.TELEPORT_TO_HOUSE, ItemID.TEAK_LOGS)
			.must(TestContext::isInHouse, "Must be in house");

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 0)
			.then(c -> object(containing("pool of")).interact("Drink"))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 1)
			.then(c -> item("Fire rune").useOn(object("Door")))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 2 && Inventory.contains(ItemID.BUNNY_TOP))
			.then(c -> item("Bunny top").equip())
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 3)
			.then(c -> equipment("Bunny top").remove())
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 4)
			.then(c -> item("Fire rune").useOn(npc(containing("butler"))))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 5)
			.then(c -> object(containing("nexus")).interact("Teleport Menu"))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 6)
			.then(c -> item("Teleport to house").interact("Break"))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 7)
			.then(c -> item("Teak logs").drop())
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 8)
			.then(c -> spell(SpellBook.Standard.ARDOUGNE_TELEPORT).cast())
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 9)
			.then(c -> spell(SpellBook.Standard.MAGIC_DART).castOn(npc(containing("butler"))))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 10 && !Dialog.isOpen())
			.then(c -> npc(containing("butler")).interact("Talk-to"));

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 10 && Dialog.isOpen()
				&& Dialog.hasOption("Something else..."))
			.then(c -> dialog("Something else...").interact());

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 10
				&& Dialog.isOpen() && Dialog.hasOption("Go to the bank..."))
			.then(c -> dialog(containing("bank")).interact())
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 11)
			.then(c -> widget("Toggle Run").interact())
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 12)
			.then(c -> object("Portal").interact("Lock"))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 13)
			.until(c -> Inventory.contains(ItemID.PET_ROCK))
			.then(c -> entity("Pet rock").interact("Pick-up"));

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 13
				&& Inventory.contains(ItemID.PET_ROCK))
			.until(c -> Dialog.isOpen())
			.then(c -> item("Pet rock").interact("Interact"));

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 13
				&& Inventory.contains(ItemID.PET_ROCK)
				&& Dialog.isOpen() && Dialog.hasOption("Talk"))
			.then(c -> dialog("Talk").interact());

		action()
			.many()
			.when(c -> c.isInHouse() && c.getHouseActive() == 13
				&& Inventory.contains(ItemID.PET_ROCK)
				&& Dialog.isOpen() && Dialog.canContinue())
			.then(c -> continueDialog().interact())
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 14
				&& Inventory.contains(ItemID.PET_ROCK))
			.then(c -> item("Pet rock").useOn(object("Mahogany house")))
			.onClick(TestContext::next);

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 15)
			.until(c -> Widgets.get(WidgetInfo.JEWELLERY_BOX_DUEL_RING) != null)
			.then(c -> object(containing("jewellery box")).interact("Teleport Menu"));

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 15)
			.then(c -> widget(WidgetID.JEWELLERY_BOX_GROUP_ID, "Castle Wars").interact())
			.onClick(TestContext::next);
//
//		requirements()
//			.when(c -> Bank.isOpen())
//			.mustHaveBanked(ItemID.PURE_ESSENCE)
//			.mustBeNear("Castle Wars bank", new WorldArea(
//				new WorldPoint(2438, 3083, 0), 6, 10
//			));
//
//		action()
//			.when(c -> Bank.isOpen() && c.getBankActive() == 0)
//			.then(c -> interact().click(WidgetInfo.BANK_DEPOSIT_INVENTORY))
//			.maxDelay(10);
//
//		action()
//			.when(c -> Bank.isOpen() && c.getBankActive() == 1
//				&& Bank.contains(ItemID.PURE_ESSENCE))
//			.then(c -> interact()
//				.withdraw(1, ItemID.PURE_ESSENCE)
//				.repeat(10)
//				.then(() -> log.info("Withdrew 1 pure essence"))
//				.after(() -> log.info("All done :)"))
//			);
//
//		action()
//			.when(c -> Bank.isOpen() && c.getBankActive() == 2
//				&& Inventory.contains(ItemID.PURE_ESSENCE))
//			.then(c -> interact().deposit(1, ItemID.PURE_ESSENCE));
//
//		action()
//			.when(c -> Bank.isOpen() && c.getBankActive() == 3
//				&& Bank.contains(ItemID.WATER_RUNE))
//			.then(c -> interact().withdrawX(ItemID.WATER_RUNE));
//
//		action()
//			.when(c -> Bank.isOpen() && c.getBankActive() == 4
//				&& Inventory.contains(ItemID.WATER_RUNE))
//			.then(c -> interact().depositAll(ItemID.WATER_RUNE));
	}

	@Subscribe
	public void onConfigButtonClicked(ConfigButtonClicked event)
	{
		if (event.getGroup().equals(TestConfig.GROUP_NAME)
			&& event.getKey().equals("next"))
		{
			context.next();
		}
	}

	@Provides
	TestConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TestConfig.class);
	}
}
