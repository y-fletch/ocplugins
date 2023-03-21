package com.yfletch.ocbarbfishing;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oc-barbfishing")
public interface OCBarbFishingConfig extends Config
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
		keyName = "method",
		name = "Method",
		description = "",
		position = 2
	)
	default Method method()
	{
		return Method.CUT_EAT;
	}
}
