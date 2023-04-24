package it.enok.ocnightmarezone;

import com.yfletch.occore.OCConfig;
import it.enok.ocnightmarezone.config.EatOption;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(Config.GROUP_NAME)
public interface Config extends OCConfig
{
	String GROUP_NAME = "oc-nightmarezone";

	@ConfigItem(
			keyName = "damageItem",
			name = "Eat",
			description = "Select which item hurts the player",
			section = "oc-nightmarezone",
			position = 1
	)
	default EatOption damageItem()
	{
		return EatOption.ROCK_CAKE;
	}

	@ConfigItem(
			keyName = "absorptionThreshold",
			name = "Absorption Mininum",
			description = "Minimum value you want absorption to be",
			section = "oc-nightmarezone",
			position = 2
	)
	@Range(
			max = 1000,
			min = 25
	)
	default int absorptionThreshold()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "combatThreshold",
			name = "Combat Boost Mininum",
			description = "Minimum value for combat boosts before drinking",
			section = "oc-nightmarezone",
			position = 2
	)
	@Range(
			max = 16,
			min = 0
	)
	default int combatThreshold()
	{
		return 0;
	}
}
