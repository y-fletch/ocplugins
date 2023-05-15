package com.yfletch.occore.v2.test;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.*;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.magic.SpellBook;
import net.unethicalite.api.movement.Movement;
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
			.when(TestContext::isHouseSuite)
			.mustHaveOnPerson(ItemID.FIRE_RUNE, ItemID.BUNNY_TOP, ItemID.TELEPORT_TO_HOUSE)
			.must(TestContext::isInHouse, "Must be in house");

		final Consumer<TestContext> next = c -> {
			if (c.nextOnClick()) c.next();
		};

		action().name("Drink from pool")
			.when(c -> c.isHouseSuite() && c.getTestId() == 0)
			.then(c -> object(containing("pool of")).interact("Drink"))
			.delay(5, 10)
			.onClick(next);

		action().name("Use rune on door")
			.when(c -> c.isHouseSuite() && c.getTestId() == 1)
			.then(c -> item("Fire rune").useOn(object("Door")))
			.onClick(next);

		action().name("Equip bunny top")
			.when(c -> c.isHouseSuite() && c.getTestId() == 2 && Inventory.contains(ItemID.BUNNY_TOP))
			.then(c -> item("Bunny top").equip())
			.onClick(next);

		action().name("Unequip bunny top")
			.when(c -> c.isHouseSuite() && c.getTestId() == 3)
			.then(c -> equipment("Bunny top").remove())
			.onClick(next);

		action().name("Rune on butler")
			.when(c -> c.isHouseSuite() && c.getTestId() == 4)
			.then(c -> item("Fire rune").useOn(npc(containing("butler"))))
			.onClick(next);

		action().name("Teleport nexus")
			.when(c -> c.isHouseSuite() && c.getTestId() == 5)
			.then(c -> object(containing("nexus")).interact("Teleport Menu"))
			.onClick(next);

		action().name("Break tab")
			.when(c -> c.isHouseSuite() && c.getTestId() == 6)
			.then(c -> item("Teleport to house").interact("Break"))
			.onClick(next);

		action().name("Drop all logs")
			.when(c -> c.isHouseSuite() && c.getTestId() == 7)
			.then(c -> item("Teak logs").drop())
			.until(c -> !Inventory.contains("Teak logs"))
			.many();

		action().name("Pick up logs")
			.when(c -> c.isHouseSuite() && c.getTestId() == 7)
			.then(c -> tileItem("Teak logs").interact("Take"))
			.many()
			.maxDelay(5)
			.onClick(next);

		action().name("Cast Ardy tele")
			.when(c -> c.isHouseSuite() && c.getTestId() == 8)
			.then(c -> spell(SpellBook.Standard.ARDOUGNE_TELEPORT).cast())
			.onClick(next);

		action().name("Cast magic dart on butler")
			.when(c -> c.isHouseSuite() && c.getTestId() == 9)
			.then(c -> spell(SpellBook.Standard.MAGIC_DART).castOn(npc(containing("butler"))))
			.onClick(next);

		action().name("Talk to butler")
			.when(c -> c.isHouseSuite() && c.getTestId() == 10 && !Dialog.isOpen())
			.then(c -> npc(containing("butler")).interact("Talk-to"));

		action().name("Click something else")
			.when(c -> c.isHouseSuite() && c.getTestId() == 10 && Dialog.isOpen()
				&& Dialog.hasOption("Something else..."))
			.then(c -> dialog("Something else...").interact());

		action().name("Click go to bank")
			.when(c -> c.isHouseSuite() && c.getTestId() == 10
				&& Dialog.isOpen() && Dialog.hasOption("Go to the bank..."))
			.then(c -> dialog(containing("bank")).interact())
			.onClick(next);

		action().name("Turn run off")
			.when(c -> c.isHouseSuite() && c.getTestId() == 11 && Movement.isRunEnabled())
			.then(c -> widget("Toggle Run").interact());

		action().name("Turn run on")
			.when(c -> c.isHouseSuite() && c.getTestId() == 11 && !Movement.isRunEnabled())
			.then(c -> widget("Toggle Run").interact())
			.maxDelay(5)
			.onClick(next);

		action().name("Lock portal")
			.when(c -> c.isHouseSuite() && c.getTestId() == 12)
			.then(c -> object("Portal").interact("Lock"))
			.onClick(next);

		action().name("Pick up pet rock")
			.when(c -> c.isHouseSuite() && c.getTestId() == 13)
			.until(c -> Inventory.contains("Pet rock"))
			.then(c -> entity("Pet rock").interact("Pick-up"));

		action().name("Interact with pet rock")
			.when(c -> c.isHouseSuite() && c.getTestId() == 13
				&& Inventory.contains("Pet rock"))
			.until(c -> Dialog.isOpen())
			.then(c -> item("Pet rock").interact("Interact"));

		action().name("Talk to pet rock")
			.when(c -> c.isHouseSuite() && c.getTestId() == 13
				&& Inventory.contains("Pet rock")
				&& Dialog.isOpen() && Dialog.hasOption("Talk"))
			.then(c -> dialog("Talk").interact());

		action().name("Keep talking")
			.many()
			.when(c -> c.isHouseSuite() && c.getTestId() == 13
				&& Inventory.contains("Pet rock")
				&& Dialog.isOpen() && Dialog.canContinue())
			.then(c -> continueDialog().interact())
			.onClick(next);

		action().name("Put pet rock back")
			.when(c -> c.isHouseSuite() && c.getTestId() == 14
				&& Inventory.contains("Pet rock"))
			.then(c -> item("Pet rock").useOn(object("Mahogany house")))
			.onClick(next);

		action().name("Use jewellery box")
			.when(c -> c.isHouseSuite() && c.getTestId() == 15)
			.until(c -> Widgets.get(WidgetInfo.JEWELLERY_BOX_DUEL_RING) != null)
			.then(c -> object(containing("jewellery box")).interact("Teleport Menu"));

		action().name("Teleport to castle wars")
			.when(c -> c.isHouseSuite() && c.getTestId() == 15)
			.then(c -> widget(WidgetID.JEWELLERY_BOX_GROUP_ID, "Castle Wars").interact())
			.delay(1)
			.onClick(next);

		requirements()
			.when(TestContext::isBankSuite)
			.mustHaveBanked(ItemID.PURE_ESSENCE)
			.mustBeNear("Castle Wars bank", new WorldArea(
				new WorldPoint(2438, 3083, 0), 6, 10
			));

		action().name("Deposit inventory")
			.when(c -> Bank.isOpen() && c.getTestId() == 0)
			.then(c -> widget("Deposit inventory").interact())
			.onComplete(next);

		action().name("Withdraw 7 pure essence")
			.when(c -> Bank.isOpen() && c.getTestId() == 1
				&& Bank.contains("Pure essence"))
			.then(c -> banked("Pure essence").withdraw(1))
			.repeat(7)
			.onComplete(next);

		action().name("Drop 4 pure essence")
			.when(c -> !Bank.isOpen() && c.getTestId() == 1
				&& Inventory.contains("Pure essence"))
			.then(c -> item("Pure essence").drop())
			.repeat(4)
			.onComplete(next);

		action().name("Deposit 7 pure essence")
			.when(c -> Bank.isOpen() && c.getTestId() == 2
				&& Inventory.contains("Pure essence"))
			.then(c -> item("Pure essence").deposit(1))
			.repeat(7)
			.onComplete(next);

		action().name("Withdraw x water runes")
			.when(c -> Bank.isOpen() && c.getTestId() == 3
				&& !Inventory.contains("Water rune"))
			.then(c -> banked("Water rune").withdrawX());

		action().name("Deposit x water runes")
			.when(c -> Bank.isOpen() && c.getTestId() == 3
				&& Inventory.contains("Water rune"))
			.then(c -> item("Water rune").depositX())
			.delay(1, 4)
			.onComplete(next);

		action().name("Withdraw all monkfish")
			.when(c -> Bank.isOpen() && c.getTestId() == 4
				&& !Inventory.contains("Monkfish"))
			.then(c -> banked("Monkfish").withdrawAll());

		action().name("Deposit all monkfish")
			.when(c -> Bank.isOpen() && c.getTestId() == 4
				&& Inventory.contains("Monkfish"))
			.then(c -> item("Monkfish").depositAll())
			.delay(1, 4)
			.onComplete(next);

		action().name("Withdraw barrows gloves")
			.when(c -> Bank.isOpen() && c.getTestId() == 5
				&& !Inventory.contains("Barrows gloves"))
			.then(c -> banked("Barrows gloves").withdraw(1));

		action().name("Equip barrows gloves from bank")
			.when(c -> Bank.isOpen() && c.getTestId() == 5
				&& Inventory.contains("Barrows gloves"))
			.then(c -> item("Barrows gloves").equip())
			.onComplete(next);
	}

	@Subscribe
	public void onConfigButtonClicked(ConfigButtonClicked event)
	{
		if (event.getGroup().equals(TestConfig.GROUP_NAME)
		)
		{
			if (event.getKey().equals("next"))
			{
				context.next();
			}

			if (event.getKey().equals("previous"))
			{
				context.previous();
			}
		}
	}

	@Provides
	TestConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TestConfig.class);
	}
}
