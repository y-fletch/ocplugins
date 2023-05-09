package com.yfletch.occore.v2.test;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.Button;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(TestConfig.GROUP_NAME)
public interface TestConfig extends CoreConfig
{
	String GROUP_NAME = "oc-core-test";

	@ConfigSection(
		name = "Core Tester",
		description = "Plugin settings",
		position = 2
	)
	String ocTest = "ocTest";

	@ConfigItem(
		keyName = "next",
		name = "Next",
		description = "Move to next rule",
		section = ocTest
	)
	default Button nextButton()
	{
		return new Button();
	}
}
