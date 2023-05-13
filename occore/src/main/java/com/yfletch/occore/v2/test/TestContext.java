package com.yfletch.occore.v2.test;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import java.util.Map;
import lombok.Getter;
import net.unethicalite.api.items.Bank;
import net.unethicalite.client.Static;

@Singleton
public class TestContext extends CoreContext
{
	@Inject TestConfig config;

	@Getter
	private int testId = 0;

	public void next()
	{
		testId++;

		if (testId >= config.testSuite().getRules())
		{
			testId = 0;
		}
	}

	public void previous()
	{
		testId--;

		if (testId < 0)
		{
			testId = config.testSuite().getRules() - 1;
		}
	}

	public boolean isInHouse()
	{
		return Static.getClient().isInInstancedRegion();
	}

	@Override
	public Map<String, String> getDebugMap()
	{
		final var map = super.getDebugMap();
		map.put("is-in-house", "" + isInHouse());
		map.put("bank-is-open", "" + Bank.isOpen());
		map.put("suite", config.testSuite().name().toLowerCase());
		map.put("test-id", "" + testId);

		return map;
	}

	public boolean nextOnClick()
	{
		return config.nextOnClick();
	}

	public boolean isHouseSuite()
	{
		return config.testSuite() == TestSuite.HOUSE;
	}

	public boolean isBankSuite()
	{
		return config.testSuite() == TestSuite.BANK;
	}
}
