package com.yfletch.ocbankskills;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.occore.v2.CoreContext;
import lombok.Getter;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.unethicalite.api.widgets.Widgets;
import net.unethicalite.client.Static;

@Singleton
public class BankSkillsContext extends CoreContext
{
	@Getter
	@Inject private BankSkillsConfig config;

	public boolean isAnimating()
	{
		if (Static.getClient().getLocalPlayer().isAnimating())
		{
			flag("animating", true, 3);
			return true;
		}

		return flag("animating");
	}

	public Widget getBankCloseButton()
	{
		return Widgets.get(WidgetID.BANK_GROUP_ID, 2, 11);
	}

	public Widget getMakeButton()
	{
		return Widgets.get(WidgetID.MULTISKILL_MENU_GROUP_ID, 13 + config.makeOption());
	}
}
