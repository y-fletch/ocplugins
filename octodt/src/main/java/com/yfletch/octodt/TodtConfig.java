package com.yfletch.octodt;

import com.yfletch.occore.v2.CoreConfig;
import com.yfletch.octodt.config.Brazier;
import com.yfletch.octodt.config.Food;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(TodtConfig.GROUP_NAME)
public interface TodtConfig extends CoreConfig
{
	String GROUP_NAME = "oc-todt";

	@ConfigSection(
		name = "Wintertodt",
		description = "Plugin settings",
		position = 2
	)
	String wintertodt = "wintertodt";

	@ConfigItem(
		keyName = "fletch",
		name = "Fletch",
		description = "Why?",
		section = wintertodt,
		position = 0
	)
	default boolean fletch()
	{
		return false;
	}

	@ConfigItem(
		keyName = "preferredBrazier",
		name = "Preferred brazier",
		description = "Prefer this brazier, if available",
		section = wintertodt,
		position = 1
	)
	default Brazier preferredBrazier()
	{
		return Brazier.RANDOM;
	}

	@ConfigItem(
		keyName = "food",
		name = "Food",
		description = "",
		section = wintertodt,
		position = 2
	)
	default Food food()
	{
		return Food.MONKFISH;
	}

	@ConfigItem(
		keyName = "minFood",
		name = "Minimum food",
		description = "Minimum food required to start a game",
		section = wintertodt,
		position = 3
	)
	@Range(min = 2,
		   max = 26)
	default int minFood()
	{
		return 4;
	}

	@ConfigItem(
		keyName = "maxFood",
		name = "Maximum food",
		description = "Maximum food withdrawn from the bank",
		section = wintertodt,
		position = 4
	)
	@Range(min = 2,
		   max = 26)
	default int maxFood()
	{
		return 8;
	}

	@ConfigItem(
		keyName = "minHealth",
		name = "Minimum health",
		description = "Always stay over this amount of health",
		section = wintertodt,
		position = 5
	)
	@Range(max = 99)
	default int minHealth()
	{
		return 50;
	}
}
