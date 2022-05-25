package com.yfletch.rift;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oc-rift")
public interface RiftConfig extends Config
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
		keyName = "exitMineSeconds",
		name = "Exit mine at",
		description = "Exits the large mine when the game gets to the specified time",
		position = 2
	)
	default int exitMineSeconds()
	{
		return 35;
	}

	@ConfigItem(
		keyName = "preferCatalytic",
		name = "Prefer catalytic",
		description = "Prefer catalytic altars when both elemental & catalytic are the same tier. Toggle off to prefer elemental altars.",
		position = 2
	)
	default boolean preferCatalytic()
	{
		return true;
	}
}
