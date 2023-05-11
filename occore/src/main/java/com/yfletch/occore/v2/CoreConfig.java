package com.yfletch.occore.v2;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

public interface CoreConfig extends Config
{
	@ConfigSection(
		name = "One-click",
		description = "Default settings",
		position = 1
	)
	String plugin = "oneClick";

	@ConfigItem(
		keyName = "enabled",
		name = "Plugin enabled",
		description = "Enables one-click functionality",
		section = plugin,
		position = 1
	)
	default boolean enabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "quickToggleKeybind",
		name = "Quick toggle",
		description = "Set a keybind to toggle this plugin",
		section = plugin,
		position = 2
	)
	default Keybind quickToggleKeybind()
	{
		return null;
	}

	@ConfigItem(
		keyName = "pluginApi",
		name = "Plugin API",
		description = "a.k.a. automation mode"
			+ "<br/>One-click will forward mutated mouse events, requiring external input."
			+ "<br/>Devious will use Devious client's automation API and does not require any external input.",
		section = plugin,
		position = 3
	)
	default PluginAPI pluginApi()
	{
		return PluginAPI.ONE_CLICK;
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
		keyName = "debugRawMenuEntries",
		name = "Debug raw menu entries",
		description = "Log menu entries to the game chatbox",
		section = debug,
		position = 1
	)
	default boolean debugRawMenuEntries()
	{
		return false;
	}

	@ConfigItem(
		keyName = "debugOCMenuEntries",
		name = "Debug OC menu entries",
		description = "Log OC overridden menu entries to the game chatbox",
		section = debug,
		position = 2
	)
	default boolean debugOCMenuEntries()
	{
		return false;
	}
}
