package com.yfletch.ocbwans;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.OCPlugin;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.input.Keyboard;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Bwans",
	enabledByDefault = false
)
public class OCBwansPlugin extends OCPlugin
{
	private final Executor HOLD_SPACE = Executors.newSingleThreadExecutor();

	private boolean isHoldingSpace = false;

	@Inject
	protected void setup(Context context, Runner runner, Config config)
	{
		super.setup(context, runner, config);
		setConfigGroup(Config.GROUP_NAME);
		refreshOnConfigChange();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(Config.GROUP_NAME) && event.getKey().equals("ocEnabled"))
		{
			isHoldingSpace = event.getNewValue().equals("true");

			if (isHoldingSpace)
			{
				HOLD_SPACE.execute(() -> {
					while (isHoldingSpace)
					{
						try
						{
							Thread.sleep(50);
							Keyboard.sendSpace();
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Provides
	Config provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Config.class);
	}
}
