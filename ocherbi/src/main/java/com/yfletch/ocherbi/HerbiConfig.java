package com.yfletch.ocherbi;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(HerbiConfig.GROUP_NAME)
public interface HerbiConfig extends CoreConfig
{
	String GROUP_NAME = "oc-herbi";

	@ConfigSection(
		name = "Herbiboar",
		description = "Plugin settings",
		position = 2
	)
	String herbiboar = "herbiboar";

	@ConfigItem(
		name = "Staminas",
		keyName = "staminas",
		description = "Amount of staminas to withdraw from the bank (can be 0)",
		section = herbiboar,
		position = 1
	)
	default int staminas()
	{
		return 5;
	}
}
