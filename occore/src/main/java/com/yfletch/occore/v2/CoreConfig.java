package com.yfletch.occore.v2;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Units;

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
		description = "Set the execution API/mode of the plugin."
			+ "<br/>\"One-click\" is the original OC behaviour - each manual click within the game will be mutated to perform the correct action."
			+ "<br/>\"One-click (consume)\" is identical to One-click, but any extra clicks are consumed (blocked)."
			+ "<br/>\"One-click (auto)\" is a fully automatic version using the One-click API"
			+ "<br/>\"Devious\" will use Devious client's interaction manager. This allows the plugin to work without manual input.",
		section = plugin,
		position = 3
	)
	default PluginAPI pluginApi()
	{
		return PluginAPI.ONE_CLICK_CONSUME;
	}

	@ConfigItem(
		keyName = "clicksPerTick",
		name = "Clicks per tick (auto)",
		description = "Amount of clicks to send per tick. Only applies to the \"One-click (auto)\" API.",
		section = plugin,
		position = 4
	)
	default int clicksPerTick()
	{
		return 2;
	}

	@ConfigSection(
		name = "Breaks",
		description = "Configure breaks",
		position = 90
	)
	String breaks = "breaks";

	@ConfigItem(
		keyName = "enableBreaks",
		name = "Take breaks",
		description = "Have a break. Have a KitKat.",
		section = breaks,
		position = 1
	)
	default boolean enableBreaks()
	{
		return false;
	}

	@ConfigItem(
		keyName = "breakInterval",
		name = "Break interval",
		description = "Average break interval",
		section = breaks,
		position = 2
	)
	@Units(Units.MINUTES)
	default int breakInterval()
	{
		return 45;
	}

	@ConfigItem(
		keyName = "breakDuration",
		name = "Break duration",
		description = "Average break duration",
		section = breaks,
		position = 3
	)
	@Units(Units.MINUTES)
	default int breakDuration()
	{
		return 7;
	}

	@ConfigSection(
		name = "Safety [Coming soon]",
		description = "Configure safety features",
		position = 91
	)
	String safety = "safety";

	@ConfigItem(
		keyName = "logoutIfStuck",
		name = "Logout if stuck",
		description = "Logout and show an alert if no action has been performed in a set amount of time.",
		section = safety,
		position = 1
	)
	default boolean logoutIfStuck()
	{
		return true;
	}

	@ConfigItem(
		keyName = "logoutStuckTime",
		name = "Stuck time",
		description = "Amount of time needed after the last successful action to consider the plugin \"stuck\" (seconds)",
		section = safety,
		position = 2
	)
	@Units(Units.SECONDS)
	default int logoutStuckTime()
	{
		return 120;
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

	@ConfigItem(
		keyName = "showStatisticsOverlay",
		name = "Show statistics overlay",
		description = "Show statistics for the plugin",
		section = overlays,
		position = 2
	)
	default boolean showStatisticsOverlay()
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
		keyName = "showWorldDebugOverlay",
		name = "Show world debug overlay",
		description = "Render outlines on top of objects, tiles, etc",
		section = debug,
		position = 1
	)
	default boolean showWorldDebugOverlay()
	{
		return false;
	}

	@ConfigItem(
		keyName = "debugRawMenuEntries",
		name = "Debug raw menu entries",
		description = "Log menu entries to the game chatbox",
		section = debug,
		position = 2
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
		position = 3
	)
	default boolean debugOCMenuEntries()
	{
		return false;
	}
}
