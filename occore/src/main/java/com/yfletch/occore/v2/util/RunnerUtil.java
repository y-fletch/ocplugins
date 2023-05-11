package com.yfletch.occore.v2.util;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.MenuOptionClicked;
import net.unethicalite.client.Static;

@Slf4j
public class RunnerUtil
{
	/**
	 * Log event/menu entry to game chat and output log
	 */
	public static void logDebug(String prefix, MenuOptionClicked event)
	{
		final var debug = "[" + prefix + "] option=" + event.getMenuOption()
			+ " target=" + event.getMenuTarget()
			+ " id=" + event.getId()
			+ " action=" + event.getMenuAction()
			+ " p0=" + event.getParam0()
			+ " p1=" + event.getParam1();
		log.info(debug);
		Static.getClient().addChatMessage(
			ChatMessageType.GAMEMESSAGE,
			"Bob",
			debug,
			null
		);
	}
}
