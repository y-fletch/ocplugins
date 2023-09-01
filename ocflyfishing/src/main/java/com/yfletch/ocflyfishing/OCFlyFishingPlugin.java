package com.yfletch.ocflyfishing;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.npc;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.widget;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Fly Fishing",
	description = "One-click fly fishing and cooking",
	enabledByDefault = false
)
public class OCFlyFishingPlugin extends RunnerPlugin<FlyFishingContext>
{
	@Inject private FlyFishingConfig config;
	@Inject private FlyFishingContext context;

	@Inject
	public void init(FlyFishingConfig config, FlyFishingContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(FlyFishingConfig.GROUP_NAME);
	}

	@Override
	public void setup()
	{
		requirements().mustHave("Feather", "Fly fishing rod");

		action().name("Drop fish")
			.when(
				c -> Inventory.contains("Trout", "Salmon", "Burnt fish")
					&& !Inventory.contains("Raw trout", "Raw salmon"))
			.then(c -> item("Trout", "Salmon", "Burnt fish").drop())
			.until(c -> !Inventory.contains("Trout", "Salmon", "Burnt fish"))
			.many()
			.onClick(c -> c.clear("cooking"));

		action().name("Lure fishing spot")
			.when(c -> Inventory.getFreeSlots() > 0 && !c.isFishing() && !c.isCooking())
			.then(c -> npc("Rod Fishing spot").interact("Lure"));

		action().name("Cook trout")
			.when(c -> widget("Trout").exists())
			.then(c -> widget("Trout").interact("Cook"))
			.until(c -> !Inventory.contains("Raw trout"));

		action().name("Cook salmon")
			.when(c -> widget("Raw salmon").exists())
			.then(c -> widget("Raw salmon").interact("Cook"))
			.until(c -> !Inventory.contains("Raw salmon"));

		action().name("Cook fish")
			.when(c -> Inventory.isFull() && !c.isCooking())
			.then(c -> object("Fire").interact("Cook"));
	}

	@Provides
	FlyFishingConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FlyFishingConfig.class);
	}
}
