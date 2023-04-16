package com.yfletch.ocherblore;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oc-herblore")
public interface OCHerbloreConfig extends Config
{
	@ConfigItem(
		keyName = "ocEnabled",
		name = "One click enabled",
		description = "Enables one-click functionality",
		position = 1
	)
	default boolean ocEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "primary",
		name = "Primary",
		description = "Primary item ID",
		position = 1
	)
	default int primary()
	{
		return -1;
	}

	@ConfigItem(
		keyName = "secondary",
		name = "Secondary",
		description = "Secondary item ID",
		position = 1
	)
	default int secondary()
	{
		return -1;
	}
}
