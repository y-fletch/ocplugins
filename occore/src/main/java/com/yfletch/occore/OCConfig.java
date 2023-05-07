package com.yfletch.occore;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

public interface OCConfig extends Config
{
	@ConfigSection(
		name = "One-click",
		description = "Default settings",
		position = 1
	)
	String oneClick = "oneClick";

	@ConfigItem(
		keyName = "ocEnabled",
		name = "One click enabled",
		description = "Enables one-click functionality",
		section = oneClick,
		position = 1
	)
	default boolean ocEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "quickToggleKeybind",
		name = "Quick toggle",
		description = "Set a keybind to toggle OC",
		section = oneClick,
		position = 2
	)
	default Keybind quickToggleKeybind()
	{
		return null;
	}

	@ConfigSection(
		name = "Overlays",
		description = "Configure overlays",
		position = 98
	)
	String overlays = "overlays";

	@ConfigItem(
		keyName = "showActionOverlay",
		name = "Show action overlay",
		description = "Show the current action and status on screen",
		section = overlays,
		position = 1
	)
	default boolean showActionOverlay()
	{
		return true;
	}

	@ConfigSection(
		name = "Debug",
		description = "Configure overlays",
		position = 99
	)
	String debug = "debug";

	@ConfigItem(
		keyName = "showDebugOverlay",
		name = "Show debug overlay",
		description = "Show debugging information on screen",
		section = debug,
		position = 0
	)
	default boolean showDebugOverlay()
	{
		return false;
	}

	@ConfigItem(
		keyName = "debugMenuEntries",
		name = "Debug menu entries",
		description = "Log menu entries to the game chatbox",
		section = debug,
		position = 1
	)
	default boolean debugMenuEntries()
	{
		return false;
	}
}
