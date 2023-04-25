package it.enok.ocnightmarezone;

import com.yfletch.occore.OCConfig;
import it.enok.ocnightmarezone.config.ItemOption;
import it.enok.ocnightmarezone.config.PotionOption;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(Config.GROUP_NAME)
public interface Config extends OCConfig
{
	String GROUP_NAME = "oc-nightmarezone";

	@ConfigItem(
			keyName = "itemOption",
			name = "Damage Item",
			description = "Select which item hurts the player",
			section = "oc-nightmarezone",
			position = 2
	)
	default ItemOption itemOption()
	{
		return ItemOption.ROCK_CAKE;
	}

	@ConfigItem(
			keyName = "useAbsorptionPotion",
			name = "Use Absorbs",
			description = "Toggle the use of absorption potions",
			section = "oc-nightmarezone",
			position = 4
	)
	default boolean useAbsorptionPotion()
	{
		return true;
	}

	@ConfigItem(
			keyName = "absorptionThreshold",
			name = "Absorption Min",
			description = "Minimum value you want absorption to be",
			section = "oc-nightmarezone",
			position = 5
	)
	@Range(max = 1000, min = 25)
	default int absorptionThreshold()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "potionOption",
			name = "Sip Option",
			description = "Select which potion to sip",
			section = "oc-nightmarezone",
			position = 6
	)
	default PotionOption potionOption()
	{
		return PotionOption.SUPER_COMBAT_POTION;
	}

	@ConfigItem(
			keyName = "combatThreshold",
			name = "Combat Boost Min",
			description = "Minimum value for combat boosts before drinking",
			section = "oc-nightmarezone",
			position = 7
	)
	@Range(max = 15)
	default int combatThreshold()
	{
		return 0;
	}
}
