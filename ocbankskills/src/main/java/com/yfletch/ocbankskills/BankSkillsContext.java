package com.yfletch.ocbankskills;

import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import net.unethicalite.client.Static;

@Singleton
public class BankSkillsContext extends CoreContext
{
	public boolean isAnimating()
	{
		if (Static.getClient().getLocalPlayer().isAnimating())
		{
			flag("animating", true, 3);
			return true;
		}

		return flag("animating");
	}
}
