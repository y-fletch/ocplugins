package enoki.occonstruction;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.OCPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Construction",
	enabledByDefault = false
)
public class OCConstructionPlugin extends OCPlugin
{
	@Inject
	protected void setup(Context context, Runner runner, Config config)
	{
		super.setup(context, runner, config);
		setConfigGroup(Config.GROUP_NAME);
		refreshOnConfigChange();
	}

	@Subscribe
	public void OnMenuEntryClicked(MenuOptionClicked event)
	{
		log.info("BEFORE: " + event.getMenuEntry().toString());
		super.onMenuOptionClicked(event);
		log.info("AFTER: " + event.getMenuEntry().toString());
	}

	@Provides
	Config provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}
}
