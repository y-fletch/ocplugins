package com.yfletch.ocpickpocket;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.banked;
import static com.yfletch.occore.v2.interaction.Entities.entity;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.npc;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.of;
import static com.yfletch.occore.v2.interaction.Entities.tileItem;
import static com.yfletch.occore.v2.interaction.Walking.walk;
import static com.yfletch.occore.v2.util.Util.containing;
import static com.yfletch.occore.v2.util.Util.nameMatching;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.XpDropEvent;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Equipment;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Pickpocket",
	description = "One-click pickpocketing for any NPC",
	enabledByDefault = false
)
public class OCPickpocketPlugin extends RunnerPlugin<PickpocketContext>
{
	@Inject private Client client;
	@Inject private ConfigManager configManager;
	@Inject private PickpocketConfig config;
	@Inject private PickpocketContext context;

	private final static int SEPULCHRE_EXIT_STAIRS = ObjectID.STAIRS_38601;
	private final static WorldPoint ARDOUGNE_KEY_WORLDPOINT = new WorldPoint(2643, 3299, 0);

	private Item[] previousInventory;

	@Inject
	public void init(PickpocketContext context, PickpocketConfig config)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(PickpocketConfig.GROUP_NAME);
		refreshOnConfigChange(true);
	}

	@Override
	public void setup()
	{
		statistics.addDisplays("XP", "GP", "Success", "Fail");
		statistics.addPerHourDisplays("XP", "GP");
		statistics.addPercentageDisplay("Success", List.of("Success", "Fail"));
		statistics.addPercentageDisplay("Fail", List.of("Success", "Fail"));

		final var target = split(config.target());
		final var food = split(config.food());
		final var lowValueItems = split(config.lowValueItems());
		final var highValueItems = split(config.highValueItems());

		// drop low value
		action().name("Drop low value items")
			.when(c -> Inventory.contains(lowValueItems))
			.then(c -> item(lowValueItems).drop())
			.many();

		// pick up high value
		action().name("Eat food for high value item")
			.when(c -> tileItem(highValueItems).exists()
				&& Inventory.isFull()
				&& Inventory.contains(food))
			.then(c -> item(food).interact("Eat", "Drink"));

		action().name("Drop dodgy necklace for high value item")
			.when(c -> tileItem(highValueItems).exists()
				&& Inventory.isFull()
				&& Inventory.contains("Dodgy necklace"))
			.then(c -> item("Dodgy necklace").drop());

		action().name("Pick up high value item")
			.when(c -> tileItem(highValueItems).exists())
			.then(c -> tileItem(highValueItems).interact("Take"))
			.many();

		// pop coin pouches
		action().name("Open coin pouches")
			.when(c ->
					  // have coin pouches and need to bank
					  Inventory.contains("Coin pouch")
						  && (!Inventory.contains("Dodgy necklace")
						  || !Inventory.contains(food))
						  // have max amount of coin pouches
						  || Inventory.getCount(true, "Coin pouch") >= 28)
			.then(c -> item("Coin pouch").interact("Open-all"))
			// can sometimes get stuck after a stun
			.oncePerTick();

		// equip dodgy necklace
		action().name("Equip dodgy necklace")
			.when(c -> !Equipment.contains("Dodgy necklace")
				&& Inventory.contains("Dodgy necklace"))
			.then(c -> item("Dodgy necklace").equip());

		// eat
		action().name("Eat food")
			.when(PickpocketContext::shouldEat)
			.then(c -> item(food).interact("Eat", "Drink"))
			.many()
			// delay during stun cooldown
			.delay(4, 6);

		// path to bank
		group(
			(c) -> !Inventory.contains("Dodgy necklace")
				|| !Inventory.contains(food),
			() -> {

				// pre-bank - darkmeyer
				action().name("Open doors towards mausoleum")
					.when(c -> !c.isBankBoothInRange()
						&& c.getNextDoorOnPathTo(object("Mausoleum Door").unwrap()) != null)
					.then(c -> of(c.getNextDoorOnPathTo(object("Mausoleum Door").unwrap())).interact("Open"))
					// delay in case stunned
					.delay(4);

				action().name("Enter mausoleum")
					.when(c -> !c.isBankBoothInRange()
						&& object("Mausoleum Door").exists())
					.then(c -> object("Mausoleum Door").interact("Enter"))
					// delay in case stunned
					.delay(4);

				action().name("Walk to ardougne key point")
					.when(c -> !c.isBankBoothInRange()
						&& c.isInArdougne())
					.then(c -> walk(ARDOUGNE_KEY_WORLDPOINT))
					.delay(4);

				// open bank
				action().name("Open bank")
					.when(PickpocketContext::isBankBoothInRange)
					.until(c -> Bank.isOpen())
					.then(c -> entity(containing("bank")).interact("Use", "Bank"))
					// delay in case stunned
					.delay(4);
			}
		);

		action().name("Deposit everything but coins")
			.when(c -> Bank.isOpen()
				&& Inventory.getFreeSlots() < 27
				&& !c.flag("withdrawing"))
			.then(c -> item(c.getNonCoinInventoryItems()).depositAll())
			.many();

		action().name("Withdraw dodgy necklaces")
			.when(c -> Bank.isOpen()
				&& Inventory.getCount("Dodgy necklace") < config.dodgyNecklaceAmount())
			.then(c -> banked("Dodgy necklace").withdraw(1))
			.onClick(c -> c.flag("withdrawing", true))
			.repeat(config.dodgyNecklaceAmount());

		action().name("Withdraw food")
			.when(c -> Bank.isOpen()
				&& !Inventory.contains(food))
			.onClick(c -> c.flag("withdrawing", true))
			.then(c -> banked(food).withdrawAll());

		action().name("Make space for coins and coin pouch")
			.when(c -> Bank.isOpen()
				&& Inventory.contains(food)
				&& (!Inventory.contains("Coins") || !Inventory.contains("Coin pouch"))
				&& Inventory.getFreeSlots() < 2)
			.then(c -> item(food).deposit(1))
			.oncePerTick()
			.many();

		// post bank
		group(
			c -> !npc(target).exists(),
			() -> {
				// darkmeyer
				action().name("Climb up sepulchre stairs")
					.when(c -> object(SEPULCHRE_EXIT_STAIRS).exists())
					.then(c -> object(SEPULCHRE_EXIT_STAIRS).interact("Climb-up"));
			}
		);

		action().name("Open doors to target")
			.when(c -> npc(target).exists()
				&& c.getNextDoorOnPathTo(npc(target).unwrap()) != null)
			.then(c -> of(c.getNextDoorOnPathTo(npc(target).unwrap())).interact("Open"))
			.many();

		// pickpocket
		action().name("Pickpocket NPC")
			.then(c -> npc(target).interact("Pickpocket"))
			.onClick(c -> c.flag("withdrawing", false))
			.oncePerTick();
	}

	private String[] split(String input)
	{
		if (Strings.isNullOrEmpty(input))
		{
			return new String[]{};
		}

		return Arrays.stream(input.split(","))
			.map(String::trim)
			.filter(s -> !Strings.isNullOrEmpty(s))
			.toArray(String[]::new);
	}

	@Subscribe
	public void onConfigButtonClicked(ConfigButtonClicked event)
	{
		if (event.getGroup().equals(PickpocketConfig.GROUP_NAME))
		{
			switch (event.getKey())
			{
				case "presetElves":
					configManager.setConfiguration(
						PickpocketConfig.GROUP_NAME,
						"lowValueItems",
						"Jug of wine,Gold ore"
					);
					configManager.setConfiguration(
						PickpocketConfig.GROUP_NAME,
						"highValueItems",
						"Crystal shard,Enhanced crystal teleport seed"
					);
					break;
				case "presetVyres":
					configManager.setConfiguration(
						PickpocketConfig.GROUP_NAME,
						"target",
						"Vallessia von Pitt,Vlad Bechstein,Diphylla Bechstein"
					);
					configManager.setConfiguration(
						PickpocketConfig.GROUP_NAME,
						"lowValueItems",
						"Blood pint,Cooked mystery meat"
					);
					configManager.setConfiguration(
						PickpocketConfig.GROUP_NAME,
						"highValueItems",
						"Blood shard"
					);
					break;
			}
		}
	}

	@Subscribe
	public void onXpDrop(XpDropEvent event)
	{
		if (config.enabled()
			&& event.getSkill() == Skill.THIEVING
			&& event.getExp() > 0)
		{
			statistics.add("XP", event.getExp());
			statistics.add("Success", 1);
		}
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event)
	{
		if (config.enabled() && event.getActor() == client.getLocalPlayer())
		{
			statistics.add("Fail", 1);
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.INVENTORY.getId())
		{
			return;
		}

		if (!config.enabled() || previousInventory == null)
		{
			previousInventory = event.getItemContainer().getItems();
			return;
		}

		final var items = event.getItemContainer().getItems();

		final var oldCoinStack = Arrays.stream(previousInventory)
			.filter(nameMatching("Coins"))
			.findFirst().orElse(null);

		final var newCoinStack = Arrays.stream(items)
			.filter(nameMatching("Coins"))
			.findFirst().orElse(null);

		previousInventory = items;

		if (newCoinStack == null)
		{
			return;
		}

		if (oldCoinStack == null)
		{
			statistics.add("GP", newCoinStack.getQuantity());
			return;
		}

		statistics.add("GP", newCoinStack.getQuantity() - oldCoinStack.getQuantity());
	}

	@Provides
	public PickpocketConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PickpocketConfig.class);
	}
}
