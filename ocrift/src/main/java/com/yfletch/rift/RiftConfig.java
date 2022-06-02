package com.yfletch.rift;

import com.yfletch.rift.enums.Rune;
import java.util.HashSet;
import java.util.Set;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

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

	@ConfigItem(
		keyName = "preferLowest",
		name = "Prefer lowest",
		description = "Prefer whichever energy is the lowest if enabled, falling back to prefer catalytic setting above.",
		position = 3
	)
	default boolean preferLowest()
	{
		return true;
	}

	@ConfigItem(
		keyName = "repairPouches",
		name = "Repair pouches",
		description = "Repair pouches before they degrade",
		position = 4
	)
	default boolean repairPouches()
	{
		return true;
	}

	@ConfigItem(
		keyName = "useSpecialTwice",
		name = "Use second special",
		description = "Use special attack if again available before 50% power",
		position = 5
	)
	default boolean useSpecialTwice()
	{
		return true;
	}

	@ConfigItem(
		keyName = "dropRunes",
		name = "Drop runes",
		description = "Select which runes to drop. Ctrl+click to select multiple",
		position = 6
	)
	default Set<Rune> dropRunes()
	{
		HashSet<Rune> set = new HashSet<>();
		set.add(Rune.AIR);
		set.add(Rune.WATER);
		set.add(Rune.EARTH);
		set.add(Rune.FIRE);
		set.add(Rune.MIND);
		set.add(Rune.BODY);

		return set;
	}

	@ConfigSection(
		name = "Overlays",
		description = "Configure overlays",
		position = 7
	)
	String overlays = "overlays";

	@ConfigItem(
		keyName = "showStatistics",
		name = "Show statistics",
		description = "Show statistics panel",
		section = overlays,
		position = 1
	)
	default boolean showStatistics()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showRunesBanked",
		name = "Show runes banked",
		description = "Show amount of each rune banked in the statistics panel",
		section = overlays,
		position = 2
	)
	default boolean showRunesBanked()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPouchUses",
		name = "Show pouch uses",
		description = "Show amount of times pouches have been used since repairing",
		section = overlays,
		position = 3
	)
	default boolean showPouchUses()
	{
		return true;
	}

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
