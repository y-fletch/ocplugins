package com.yfletch.ocblastfurnance;

import com.yfletch.ocblastfurnance.util.Method;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oc-blastfurnace")
public interface OCBlastFurnaceConfig extends Config
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
		return Method.GOLD_BARS;
	}

	@ConfigItem(
		keyName = "lowStaminaThreshold",
		name = "Low Stamina Threshold",
		description = "Drink a dose of stamina when under this run energy, and stamina is not already applied",
		position = 3
	)
	default int lowStaminaThreshold()
	{
		return 50;
	}

	@ConfigItem(
		keyName = "xLowStaminaThreshold",
		name = "X Low Stamina Threshold",
		description = "Drink an extra dose of stamina when under this run energy",
		position = 4
	)
	default int xLowStaminaThreshold()
	{
		return 20;
	}
}
