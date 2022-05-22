package com.yfletch.summergarden;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("true-one-click-summer-garden")
public interface SummerGardenConfig extends Config
{
	@ConfigItem(
		keyName = "staminaThreshold",
		name = "Low Stamina Threshold",
		description = "What stamina level to use stamina potion",
		position = 1
	)
	default int staminaThreshold()
	{
		return 50;
	}

	@ConfigItem(
		keyName = "extraStaminaThreshold",
		name = "X Low Stamina Threshold",
		description = "What stamina level to use an extra stamina potion, if stamina is already applied",
		position = 2
	)
	default int extraStaminaThreshold()
	{
		return 8;
	}

	@ConfigItem(
		keyName = "withdrawStaminas",
		name = "Withdraw staminas",
		description = "Amount of stamina potions to withdraw at the bank",
		position = 3
	)
	default int withdrawStaminas()
	{
		return 3;
	}

	@ConfigItem(
		keyName = "usePalaceShortcut",
		name = "Palace shortcut",
		description = "Use palace shortcut (requires Desert hard diary)",
		position = 4
	)
	default boolean usePalaceShortcut()
	{
		return false;
	}
}
