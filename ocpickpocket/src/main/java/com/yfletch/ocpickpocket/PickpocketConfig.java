package com.yfletch.ocpickpocket;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(PickpocketConfig.GROUP_NAME)
public interface PickpocketConfig extends CoreConfig
{
	String GROUP_NAME = "oc-pickpocket";

	@ConfigSection(
		name = "Pickpocket",
		description = "Plugin settings",
		position = 2
	)
	String pickpocket = "pickpocket";

	@ConfigItem(
		keyName = "target",
		name = "Target NPC",
		description = "Name of NPC to pickpocket. Accepts multiple, comma separated.",
		section = pickpocket,
		position = 1
	)
	default String target()
	{
		return "Knight of Ardougne";
	}

	@ConfigItem(
		keyName = "food",
		name = "Food",
		description = "Name of food to withdraw and eat. Accepts multiple, comma separated.",
		section = pickpocket,
		position = 2
	)
	default String food()
	{
		return "Monkfish";
	}

	@ConfigItem(
		keyName = "healthMin",
		name = "Min health",
		description = "Start eating under this health",
		section = pickpocket,
		position = 3
	)
	default int minHealth()
	{
		return 40;
	}

	@ConfigItem(
		keyName = "dodgyNecklaceAmount",
		name = "Dodgy Necklaces",
		description = "Amount of dodgy necklaces to withdraw per inventory. Set to 0 to disable.",
		section = pickpocket,
		position = 5
	)
	default int dodgyNecklaceAmount()
	{
		return 8;
	}

	@ConfigItem(
		keyName = "lowValueItems",
		name = "Low value items",
		description = "Items to drop immediately. Accepts multiple, comma separated.",
		section = pickpocket,
		position = 6
	)
	default String lowValueItems()
	{
		return "";
	}

	@ConfigItem(
		keyName = "highValueItems",
		name = "High value items",
		description = "Items to keep, and when on the floor, food/necklaces will be dropped to pick it up. Accepts multiple, comma separated.",
		section = pickpocket,
		position = 7
	)
	default String highValueItems()
	{
		return "";
	}

	@ConfigItem(
		keyName = "presetElves",
		name = "Load Elf preset",
		description = "Load low/high value items for pickpocketing Elves",
		section = pickpocket,
		position = 8
	)
	default Button presetElves()
	{
		return new Button();
	}

	@ConfigItem(
		keyName = "presetVyres",
		name = "Load Vyre preset",
		description = "Load low/high value items for pickpocketing Vyres",
		section = pickpocket,
		position = 9
	)
	default Button presetVyres()
	{
		return new Button();
	}
}
