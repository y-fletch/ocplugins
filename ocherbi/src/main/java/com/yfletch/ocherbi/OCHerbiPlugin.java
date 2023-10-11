package com.yfletch.ocherbi;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.banked;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.npc;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.of;
import static com.yfletch.occore.v2.interaction.Walking.walkPathTo;
import static com.yfletch.occore.v2.util.Util.containing;
import static com.yfletch.occore.v2.util.Util.nameContaining;
import static com.yfletch.occore.v2.util.Util.withAction;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.herbiboars.HerbiboarPlugin;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Herbi",
	description = "One-click Herbiboar",
	enabledByDefault = false
)
@PluginDependency(HerbiboarPlugin.class)
public class OCHerbiPlugin extends RunnerPlugin<HerbiContext>
{
	@Inject private HerbiConfig config;

	@Inject
	public void init(HerbiConfig config, HerbiContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(HerbiConfig.GROUP_NAME);
	}

	@Override
	public void setup()
	{
		statistics.addDisplays("Harvests");
		statistics.addPerHourDisplays("Harvests");

		action().name("Drink stamina")
			.when(c -> c.getRunEnergy() < 50)
			.then(c -> item(containing("Stamina potion")).interact("Drink"));

		action().name("Fill herb sack")
			.when(c -> Inventory.getFreeSlots() < 4 && Inventory.contains("Herb sack") && !c.flag("tried-herb-sack"))
			.then(c -> item("Herb sack").interact("Fill"))
			.onClick(c -> c.flag("tried-herb-sack", true));

		action().name("Go to bank")
			.when(c -> Inventory.getFreeSlots() < 4 && (c.flag("tried-herb-sack") || !Inventory.contains("Herb sack")))
			.then(c -> walkPathTo(HerbiContext.BANK_LOCATION, 8))
			.many().skipIfNull();

		action().name("Open bank")
			.when(c -> Inventory.getFreeSlots() < 4 && (c.flag("tried-herb-sack") || !Inventory.contains(
				"Herb sack")) && !Bank.isOpen())
			.then(c -> object("Bank chest").interact("Use"));

		action().name("Empty herb sack")
			.when(c -> Bank.isOpen() && Inventory.getFreeSlots() < 4 && Inventory.contains("Herb sack") && !c.flag(
				"herb-sack-emptied"))
			.then(c -> item("Herb sack").interact("Empty"))
			.onClick(c -> c.flag("herb-sack-emptied", true));

		action().name("Deposit items")
			.when(c -> Bank.isOpen() && Inventory.contains(nameContaining("Grimy", "Fossil")))
			.then(c -> item(containing("Grimy", "Fossil")).depositAll())
			.many();

		action().name("Withdraw staminas")
			.when(c -> Bank.isOpen() && Inventory.getCount(nameContaining("Stamina potion")) < config.staminas())
			.then(c -> banked(containing("Stamina potion")).withdraw(1))
			.many().oncePerTick();

		action().name("Drop vials")
			.when(c -> Inventory.contains("Vial"))
			.then(c -> item("Vial").drop())
			.many();

		action().name("Harvest herbi")
			.when(c -> NPCs.getNearest("Herbiboar") != null)
			.then(c -> npc("Herbiboar").interact("Harvest"))
			.onClick(c -> statistics.add("Harvests", 1));

		action().name("Attack tunnel")
			.when(c -> c.getTunnel() != null)
			.then(c -> of(c.getTunnel()).interact("Attack"));

		action().name("Go to tunnel")
			.when(c -> c.getTunnelLocation() != null)
			.then(c -> walkPathTo(c.getTunnelLocation(), 15))
			.many().skipIfNull();

		action().name("Inspect object")
			.when(c -> c.getNextObject() != null)
			.then(c -> of(c.getNextObject()).interact("Inspect"))
			.many();

		action().name("Go to next location")
			.when(c -> c.getNextLocation() != null)
			.then(c -> walkPathTo(c.getNextLocation(), 15))
			.many().skipIfNull();

		action().name("Inspect start object")
			.when(c -> c.getNextLocation() == null && c.getTunnelLocation() == null && TileObjects.getNearest(
				withAction("Check count")) != null)
			.then(c -> of(TileObjects.getNearest(withAction("Check count"))).interact("Inspect"))
			.onClick(c -> {
				c.clear("herb-sack-emptied");
				c.clear("tried-herb-sack");
			});

		action().name("Go to start")
			.when(
				c -> c.getNextLocation() == null && c.getTunnelLocation() == null && c.getNearestStartLocation() != null)
			.then(c -> walkPathTo(c.getNearestStartLocation(), 15))
			.many().skipIfNull();
	}

	@Provides
	HerbiConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HerbiConfig.class);
	}
}
