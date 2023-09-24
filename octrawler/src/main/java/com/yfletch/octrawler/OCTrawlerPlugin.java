package com.yfletch.octrawler;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.of;
import static com.yfletch.occore.v2.interaction.Entities.widget;
import static com.yfletch.occore.v2.interaction.Walking.walk;
import com.yfletch.occore.v2.util.TextColor;
import static com.yfletch.occore.v2.util.Util.containing;
import static com.yfletch.occore.v2.util.Util.offset;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Trawler",
	description = "One-click Fishing Trawler",
	enabledByDefault = false
)
public class OCTrawlerPlugin extends RunnerPlugin<TrawlerContext>
{
	private static final int TRAWLER_CATCH_WIDGET = 367;

	@Inject private TrawlerConfig config;
	@Inject private TrawlerContext context;

	@Inject
	public void init(TrawlerConfig config, TrawlerContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(TrawlerConfig.GROUP_NAME);
	}

	@Override
	public void setup()
	{
		statistics.addDisplays("Completions");
		statistics.addPerHourDisplays("Completions");

		requirements()
			.mustHaveInInventory("Swamp paste", "Bucket");

		action().name("Check net")
			.when(c -> c.isInPortKhazard() && c.flag("has-rewards")
				&& !widget(WidgetID.FISHING_TRAWLER_REWARD_GROUP_ID, "Bank-all").exists())
			.then(c -> object("Trawler net").interact("Inspect"))
			.onClick(c -> statistics.add("Completions", 1));

		action().name("Take Angler piece")
			.when(c -> config.takeAnglerPieces()
				&& widget(WidgetID.FISHING_TRAWLER_REWARD_GROUP_ID, containing("Angler")).exists())
			.then(c -> widget(WidgetID.FISHING_TRAWLER_REWARD_GROUP_ID, containing("Angler")).interact("Withdraw-1"));

		action().name("Bank-all")
			.when(c -> widget(WidgetID.FISHING_TRAWLER_REWARD_GROUP_ID, "Bank-all").exists())
			.then(c -> widget(WidgetID.FISHING_TRAWLER_REWARD_GROUP_ID, "Bank-all").interact("Bank-all"))
			.onClick(c -> c.clear("has-rewards"));

		action().name("Cross gangplank")
			.when(c -> c.isInPortKhazard() && !c.isOnShip())
			.then(c -> of(c.getGangplank()).interact("Cross"));

		action().name("Walk to side")
			.when(c -> !c.isOnWall() && !c.isOnShip())
			.then(c -> walk(offset(c.getPlayerLocation(), 0, 1)))
			.many();

		action().name("Fill leak")
			.when(c -> c.isLeakInRange() && c.getActivity() < 255)
			.then(c -> object("Leak").interact("Fill"))
			.many()
			.onClick(c -> c.flag("has-rewards", true));

		action().name("Waiting for game")
			.when(TrawlerContext::isOnShip)
			.message(TextColor.WHITE + "Waiting for game to start...")
			.noop();

		action().name("Waiting for leak")
			.when(c -> c.getActivity() < 255)
			.message(TextColor.WHITE + "Waiting for leak...")
			.noop();

		action().name("Waiting for game to end")
			.message(TextColor.WHITE + "Waiting for game to end...")
			.noop();
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarbitId() == Varbits.FISHING_TRAWLER_ACTIVITY)
		{
			context.setActivity(event.getValue());
		}
	}

	@Provides
	TrawlerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TrawlerConfig.class);
	}
}
