package com.yfletch.ocsalamanders;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.of;
import static com.yfletch.occore.v2.interaction.Entities.tileItem;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Salamanders",
	description = "One-click black salamanders",
	enabledByDefault = false
)
public class OCSalamandersPlugin extends RunnerPlugin<SalamandersContext>
{
	@Inject private SalamandersConfig config;
	@Inject private SalamandersContext context;

	@Inject
	public void init(SalamandersConfig config, SalamandersContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(SalamandersConfig.GROUP_NAME);
		refreshOnConfigChange(true);
	}

	@Override
	public void setup()
	{
		requirements().mustBeNear(() -> object("Young tree"));

		action().name("Drop salamander")
			.when(c -> Inventory.contains("Black salamander"))
			.then(c -> item("Black salamander").interact("Release"))
			.many();

		action().name("Pick up items")
			.when(c -> tileItem("Rope", "Small fishing net").exists())
			.then(c -> tileItem("Rope", "Small fishing net").interact("Take"))
			.many();

		action().name("Set net trap")
			.when(c -> c.getYoungTree() != null && c.canPlaceTrap())
			.then(c -> of(c.getYoungTree()).interact("Set-trap"))
			.many();

		action().name("Check net trap")
			.when(c -> c.getNetTrap() != null)
			.then(c -> of(c.getNetTrap()).interact("Check"))
			.delay(2);
	}


	@Provides
	public SalamandersConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SalamandersConfig.class);
	}
}
