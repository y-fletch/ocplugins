package com.yfletch.ocmonke;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(DancingConfig.GROUP_NAME)
public interface DancingConfig extends CoreConfig
{
	String GROUP_NAME = "oc-dancing";

	@ConfigSection(
		name = "Dancing",
		description = "Plugin settings",
		position = 2
	)
	String dancing = "dancing";

	@ConfigItem(
		keyName = "tile1",
		name = "Tile 1",
		description = "Dancing tile 1. In the form x,y,plane",
		section = dancing,
		position = 1
	)
	default String tile1()
	{
		return null;
	}

	@ConfigItem(
		keyName = "tile2",
		name = "Tile 2",
		description = "Dancing tile 2. In the form x,y,plane",
		section = dancing,
		position = 2
	)
	default String tile2()
	{
		return null;
	}

	@ConfigItem(
		keyName = "danceInterval",
		name = "Dance interval",
		description = "Delay (ticks) between moving to the next dancing tile",
		section = dancing,
		position = 3
	)
	default int danceInterval()
	{
		return 2;
	}
}
