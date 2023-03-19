package com.yfletch.ocsepulchre;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("oc-sepulchre")
public interface OCSepulchreConfig extends Config
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

	@ConfigSection(
		name = "Overlays",
		description = "Configure overlays",
		position = 7
	)
	String overlays = "overlays";

	@ConfigItem(
		keyName = "showDebugOverlay",
		name = "Show debug overlay",
		description = "Pretty self explanatory I reckon",
		section = overlays,
		position = 4
	)
	default boolean showDebugOverlay()
	{
		return true;
	}
}
