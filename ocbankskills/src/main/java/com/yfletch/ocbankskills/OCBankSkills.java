package com.yfletch.ocbankskills;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.OCPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Bank Skills",
	enabledByDefault = false
)
public class OCBankSkills extends OCPlugin
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
