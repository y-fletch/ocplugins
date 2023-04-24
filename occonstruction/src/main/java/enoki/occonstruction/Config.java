package enoki.occonstruction;

import com.yfletch.occore.OCConfig;
import enoki.occonstruction.config.Buildable;
import enoki.occonstruction.config.Plank;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;


@ConfigGroup(Config.GROUP_NAME)
public interface Config extends OCConfig
{
	String GROUP_NAME = "oc-construction";

	@ConfigSection(
			name = "OC Construction",
			description = "Plugin settings",
			position = 2
	)
	String construction = "construction";

	@ConfigItem(
			keyName = "plankType",
			name = "Planks",
			description = "Use these planks to construct items",
			section = "construction",
			position = 1
	)
	default Plank plankType()
	{
		return Plank.OAK;
	}

	@ConfigItem(
			keyName = "buildableType",
			name = "Build",
			description = "Select what to build using your planks",
			section = "construction",
			position = 2
	)
	default Buildable buildableType()
	{
		return Buildable.LARDER;
	}

	@ConfigItem(
			keyName = "enableServant",
			name = "Demon Butler",
			description = "Use Demon Butler to fetch resources. Otherwise use Phials instead",
			section = "construction",
			position = 3
	)
	default boolean enableServant()
	{
		return false;
	}
}
