package com.yfletch.ocbwans;

import com.yfletch.occore.v2.CoreConfig;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(BwansConfig.GROUP_NAME)
public interface BwansConfig extends CoreConfig
{
	String GROUP_NAME = "oc-bwans";
}
