package com.yfletch.ocflyfishing;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(FlyFishingConfig.GROUP_NAME)
public interface FlyFishingConfig extends CoreConfig
{
	String GROUP_NAME = "oc-flyfishing";

	@ConfigSection(
		name = "Fly Fishing",
		description = "Plugin settings",
		position = 2
	)
	String flyFishing = "flyFishing";
}
