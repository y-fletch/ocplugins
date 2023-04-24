package it.enok.ocnightmarezone;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.OCPlugin;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "OC Nightmare Zone",
	enabledByDefault = false,
	description = "Stay in the Nightmare Zone... forever",
	tags = {"enoki", "enok", "one-click", "oneclick", "one", "click", "nightmare", "zone", "nmz"}
)
public class OCNightmareZonePlugin extends OCPlugin
{
	@Inject
	protected void setup(Context context, Runner runner, Config config)
	{
		super.setup(context, runner, config);
		setConfigGroup(Config.GROUP_NAME);
		refreshOnConfigChange();
	}

	@Provides
	Config provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}
}
