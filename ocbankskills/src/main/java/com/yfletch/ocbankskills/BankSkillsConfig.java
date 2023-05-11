package com.yfletch.ocbankskills;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(BankSkillsConfig.GROUP_NAME)
public interface BankSkillsConfig extends CoreConfig
{
	String GROUP_NAME = "oc-bankskills";

	@ConfigSection(
		name = "Bank Skills",
		description = "Plugin settings",
		position = 2
	)
	String bankSkills = "bankSkills";

	@ConfigItem(
		keyName = "primary",
		name = "Primary item ID",
		description = "Primary item ID",
		section = bankSkills,
		position = 1
	)
	@Range(min = -1)
	default int primary()
	{
		return -1;
	}

	@ConfigItem(
		keyName = "secondary",
		name = "Secondary item ID",
		description = "Secondary item ID",
		section = bankSkills,
		position = 2
	)
	@Range(min = -1)
	default int secondary()
	{
		return -1;
	}

	@ConfigItem(
		keyName = "makeOption",
		name = "Make option",
		description = "Option to click in the skill interface (starting at 1)",
		section = bankSkills,
		position = 3
	)
	default int makeOption()
	{
		return 1;
	}
}
