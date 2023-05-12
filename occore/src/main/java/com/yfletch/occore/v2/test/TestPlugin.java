package com.yfletch.occore.v2.test;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.containing;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.object;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Bank;
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
			.mustHaveInInventory(ItemID.FIRE_RUNE, ItemID.BUNNY_TOP, ItemID.TELEPORT_TO_HOUSE, ItemID.TEAK_LOGS)
			.must(TestContext::isInHouse, "Must be in house");

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 0)
			.then(c -> object(containing("pool of")).interact("Drink"));

		action()
			.when(c -> c.isInHouse() && c.getHouseActive() == 1)
			.then(c -> item("Fire rune").useOn(object("Door")));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 2 && Inventory.contains(ItemID.BUNNY_TOP))
//			.then(c -> interact().equip(ItemID.BUNNY_TOP));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 3)
//			.then(c -> interact().remove(ItemID.BUNNY_TOP));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 4
//				&& NPCs.getNearest(NpcID.DEMON_BUTLER) != null)
//			.then(c -> interact().use(ItemID.FIRE_RUNE).onNPC(NpcID.DEMON_BUTLER));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 5)
//			.then(c -> interact().click("Teleport Menu").onObject(ObjectID.PORTAL_NEXUS_33373));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 6)
//			.then(c -> interact().click("Break").onItem(ItemID.TELEPORT_TO_HOUSE));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 7)
//			.then(c -> interact().drop(ItemID.TEAK_LOGS));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 8)
//			.then(c -> interact().cast(SpellBook.Lunar.HUMIDIFY));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 9
//				&& NPCs.getNearest(NpcID.DEMON_BUTLER) != null)
//			.then(c -> interact().cast(SpellBook.Lunar.MONSTER_EXAMINE).onNPC(NpcID.DEMON_BUTLER));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 10
//				&& !Dialog.isOpen() && NPCs.getNearest(NpcID.DEMON_BUTLER) != null)
//			.then(c -> interact().click("Talk-to").onNPC(NpcID.DEMON_BUTLER));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 10
//				&& Dialog.isOpen() && Dialog.hasOption("Something else..."))
//			.then(c -> interact().dialog(option -> option.contains("Something else...")));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 10
//				&& Dialog.isOpen() && Dialog.hasOption("Go to the bank..."))
//			.then(c -> interact().dialog("Go to the bank..."));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 11)
//			.then(c -> interact().click("Use").on(WidgetInfo.MINIMAP_SPEC_CLICKBOX));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 12)
//			.then(c -> interact().click("Lock").on("Portal"));
//
//		action()
//			.when(c -> c.isInHouse() && c.getHouseActive() == 13)
//			.then(c -> interact().click("Pick-up").on("Pet rock"));
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
