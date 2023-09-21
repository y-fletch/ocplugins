package com.yfletch.ocspells;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(SpellsConfig.GROUP_NAME)
public interface SpellsConfig extends CoreConfig
{
	String GROUP_NAME = "oc-spells";

	@ConfigSection(
		name = "Spells",
		description = "Plugin settings",
		position = 2
	)
	String spells = "spells";

	@ConfigItem(
		name = "Spell",
		keyName = "spell",
		description = "Spell to cast. Right click a spell in game to set this more easily.",
		section = spells,
		position = 1
	)
	default String spell()
	{
		return "Lvl-1 Enchant";
	}

	@ConfigItem(
		name = "Target item",
		keyName = "item",
		description = "Item to withdraw and cast spell on. Supports multiple, separated by a comma.",
		section = spells,
		position = 2
	)
	default String item()
	{
		return "Sapphire ring";
	}
}
