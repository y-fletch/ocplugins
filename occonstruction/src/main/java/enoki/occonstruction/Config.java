package enoki.occonstruction;

import com.yfletch.occore.OCConfig;
import net.runelite.client.config.ConfigGroup;

@ConfigGroup(Config.GROUP_NAME)
public interface Config extends OCConfig
{
	String GROUP_NAME = "oc-construction";
}
