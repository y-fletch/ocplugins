package com.yfletch.octithefarm;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(TitheFarmConfig.GROUP_NAME)
public interface TitheFarmConfig extends CoreConfig
{
	String GROUP_NAME = "oc-tithefarm";

	@ConfigSection(
		name = "Tithe Farm",
		description = "Plugin settings",
		position = 2
	)
	String titheFarm = "titheFarm";
}
