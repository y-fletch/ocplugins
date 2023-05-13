package com.yfletch.occore.v2.util;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.MenuEntry;
import net.unethicalite.client.Static;

@Slf4j
public class RunnerUtil
{
	/**
	 * Log event/menu entry to game chat and output log
	 */
	public static void log(String prefix, MenuEntry event)
	{
		final var debug = "option=" + event.getOption()
			+ " target=" + event.getTarget()
			+ " id=" + event.getIdentifier()
			+ " action=" + event.getMenuAction()
			+ " p0=" + event.getParam0()
			+ " p1=" + event.getParam1();
		log(prefix, debug);
	}

	public static void log(String prefix, String text)
	{
		log.info("[" + prefix + "] " + text);
		Static.getClient().addChatMessage(
			ChatMessageType.GAMEMESSAGE,
			"Bob",
			"[" + prefix + "] " + text,
			null
		);
	}
}
