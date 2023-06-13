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
		name = "Test suite",
		keyName = "testSuite",
		description = "Set of tests to run",
		section = ocTest,
		position = 0
	)
	default TestSuite testSuite()
	{
		return TestSuite.HOUSE;
	}

	@ConfigItem(
		name = "Next on click",
		keyName = "nextOnClick",
		description = "Move to the next test rule on click",
		section = ocTest,
		position = 1
	)
	default boolean nextOnClick()
	{
		return false;
	}

	@ConfigItem(
		keyName = "previous",
		name = "Previous",
		description = "Move to previous rule",
		section = ocTest,
		position = 2
	)
	default Button previousButton()
	{
		return new Button();
	}

	@ConfigItem(
		keyName = "next",
		name = "Next",
		description = "Move to next rule",
		section = ocTest,
		position = 3
	)
	default Button nextButton()
	{
		return new Button();
	}
}
