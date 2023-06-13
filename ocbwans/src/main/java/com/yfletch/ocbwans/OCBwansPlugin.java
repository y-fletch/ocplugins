package com.yfletch.ocbwans;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.banked;
import static com.yfletch.occore.v2.interaction.Entities.entity;
import static com.yfletch.occore.v2.interaction.Entities.lastItem;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.widget;
import static com.yfletch.occore.v2.util.Util.containing;
import static com.yfletch.occore.v2.util.Util.nameNotMatching;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Bwans",
	description = "One-click karambwans",
	enabledByDefault = false
)
public class OCBwansPlugin extends RunnerPlugin<BwansContext>
{
	@Inject
	protected void init(BwansContext context, BwansConfig config)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(BwansConfig.GROUP_NAME);
		actionsPerTick(2);
	}

	@Override
	public void setup()
	{
		requirements()
			.mustHave("Raw karambwan")
			.mustBeNear(() -> entity(containing("bank", "benedict")), "any bank")
			.mustBeNear(() -> object("Range", "Fire"), "any range/fire");

		action()
			.when(c -> !Inventory.contains("Raw karambwan"))
			.until(c -> Bank.isOpen())
			.then(c -> entity(containing("bank", "benedict")).interact("Use", "Bank"));

		action()
			.once()
			.when(c -> Bank.isOpen() && Inventory.contains(nameNotMatching("Raw karambwan")))
			.then(c -> widget("Deposit inventory").interact());

		action()
			.once()
			.when(c -> Bank.isOpen())
			.until(c -> Inventory.contains("Raw karambwan"))
			.then(c -> banked("Raw karambwan").withdrawAll());

		action()
			.when(c -> Bank.isOpen())
			.then(c -> widget(WidgetID.BANK_GROUP_ID, "Close").interact());

		action().name("Click cook option")
			.when(c -> !c.flag("bwan") && widget("Cooked karambwan").exists())
			.then(c -> widget("Cooked karambwan").interact("Cook"))
			// disable until the next tick
			.onClick(c -> c.flag("bwan", true, 0));

		action().name("Use karambwan on fire")
			.when(c -> true)
			.then(c -> lastItem("Raw karambwan").useOn(object("Range", "Fire")));
	}

	@Provides
	BwansConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BwansConfig.class);
	}
}
