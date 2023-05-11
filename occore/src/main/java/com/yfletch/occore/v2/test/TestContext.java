package com.yfletch.occore.v2.test;

import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import java.util.Map;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.client.Static;

@Singleton
public class TestContext extends CoreContext
{
	@Getter
	private int houseActive = 0;

	@Getter
	private int bankActive = 0;

	public void next()
	{
		if (isInHouse())
		{
			houseActive++;
			if (houseActive > 13)
			{
				houseActive = 0;
			}
		}

		if (Bank.isOpen())
		{
			bankActive++;
			if (bankActive > 8)
			{
				bankActive = 0;
			}
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
		map.put("house-active", "" + houseActive);
		map.put("bank-active", "" + bankActive);
		map.put("has-pure-essence", "" + Inventory.contains(ItemID.PURE_ESSENCE));

		return map;
	}
}
