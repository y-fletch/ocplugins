package com.yfletch.ocgranite;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oc-granite")
public interface OCGraniteConfig extends Config
{
	@ConfigItem(
		keyName = "ocEnabled",
		name = "One click enabled",
		description = "Enables one-click functionality",
		position = 1
	)
	default boolean ocEnabled()
	{
		return false;
	}

	@ConfigItem(
		keyName = "humidify",
		name = "Humidify",
		description = "Use humidify to refill waterskins",
		position = 2
	)
	default boolean humidify()
	{
		return false;
	}
}
