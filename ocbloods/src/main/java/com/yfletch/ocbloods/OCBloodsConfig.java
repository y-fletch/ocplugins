package com.yfletch.ocbloods;

import com.yfletch.ocbloods.enums.BankOption;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("oc-bloods")
public interface OCBloodsConfig extends Config
{
	@ConfigItem(
		keyName = "instructions",
		name = "Instructions",
		description = "",
		position = 0
	)
	default String instructions()
	{
		return "Crafts blood runes using the True Blood Altar."
			+ " Currently only supports using a Colossal pouch, and teleporting via"
			+ " a fairy ring in your POH (and getting there via house tab). Does not"
			+ " support using the long route (74 Agility), only the 93 Agility route."
			+ " If repairing pouches, runes for the repair must be in your rune pouch.";
	}

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
		keyName = "useSummerPies",
		name = "Use Summer pies",
		description = "Eat a summer pie if your Agility is less than 93",
		position = 2
	)
	default boolean useSummerPies()
	{
		return true;
	}

	@ConfigItem(
		keyName = "repairPouch",
		name = "Repair pouch",
		description = "Repair Colossal pouch with NPC Contact",
		position = 3
	)
	default boolean repairPouch()
	{
		return true;
	}

	@ConfigItem(
		keyName = "useBloodEssence",
		name = "Use Blood essence",
		description = "Use Blood essence for more runes",
		position = 4
	)
	default boolean useBloodEssence()
	{
		return false;
	}

	@ConfigItem(
		keyName = "bankingMethod",
		name = "Banking method",
		description = "Chosen banking method",
		position = 5
	)
	default BankOption bankMethod()
	{
		return BankOption.CASTLE_WARS;
	}

	@ConfigItem(
		keyName = "restoreRunThreshold",
		name = "Restore run threshold",
		description = "Restore run energy at your pool of rejuvenation when below this threshold",
		position = 6
	)
	default int restoreRunThreshold()
	{
		return 50;
	}

	@ConfigSection(
		name = "Overlays",
		description = "Configure overlays",
		position = 7
	)
	String overlays = "overlays";

	@ConfigItem(
		keyName = "showStatisticsOverlay",
		name = "Show statistics",
		description = "Show statistics panel",
		section = overlays,
		position = 1
	)
	default boolean showStatisticsOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPouchUsesOverlay",
		name = "Show pouch usage overlay",
		description = "Show pouch usage overlay",
		section = overlays,
		position = 2
	)
	default boolean showPouchUsesOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDebugOverlay",
		name = "Show debug overlay",
		description = "Pretty self explanatory I reckon",
		section = overlays,
		position = 3
	)
	default boolean showDebugOverlay()
	{
		return true;
	}
}
