package com.yfletch.octrawler;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(TrawlerConfig.GROUP_NAME)
public interface TrawlerConfig extends CoreConfig
{
	String GROUP_NAME = "oc-trawler";

	@ConfigSection(
		name = "Trawler",
		description = "Plugin settings",
		position = 2
	)
	String trawler = "trawler";

	@ConfigItem(
		keyName = "takeAnglerPieces",
		name = "Take Angler pieces",
		description = "Take Angler outfit pieces from the net",
		position = 1,
		section = trawler
	)
	default boolean takeAnglerPieces()
	{
		return true;
	}
}
