package com.yfletch.ocgranite;

import com.yfletch.occore.OCConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(Config.GROUP_NAME)
public interface Config extends OCConfig
{
	String GROUP_NAME = "oc-granite";

	@ConfigSection(
		name = "OC Granite",
		description = "Plugin settings",
		position = 2
	)
	String ocGranite = "ocGranite";

	@ConfigItem(
		keyName = "useHumidify",
		name = "Humidify",
		description = "Use humidify to refill waterskins",
		position = 0,
		section = ocGranite
	)
	default boolean useHumidify()
	{
		return false;
	}
}
