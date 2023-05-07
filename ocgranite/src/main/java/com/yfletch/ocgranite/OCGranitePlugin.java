package com.yfletch.ocgranite;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.OCPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.events.ExperienceGained;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Granite",
	description = "One-click 3t4g",
	enabledByDefault = false
)
public class OCGranitePlugin extends OCPlugin
{
	@Inject private Context context;

	@Inject
	protected void setup(
		Context context, Runner runner, Config config
	)
	{
		super.setup(context, runner, config);
		setConfigGroup(Config.GROUP_NAME);
	}

	@Subscribe
	public void onExperienceGained(ExperienceGained event)
	{
		if (event.getSkill() == Skill.MINING)
		{
			context.onGraniteMined();
		}
	}

	@Provides
	Config provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}
}
