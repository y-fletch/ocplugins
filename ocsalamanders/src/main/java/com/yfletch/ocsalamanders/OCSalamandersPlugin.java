package com.yfletch.ocsalamanders;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.object;
import static com.yfletch.occore.v2.interaction.Entities.of;
import static com.yfletch.occore.v2.interaction.Entities.tileItem;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.unethicalite.api.items.Inventory;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Salamanders",
	description = "One-click black salamanders",
	enabledByDefault = false
)
public class OCSalamandersPlugin extends RunnerPlugin<SalamandersContext>
{
	@Inject private SalamandersConfig config;
	@Inject private SalamandersContext context;

	@Inject
	public void init(SalamandersConfig config, SalamandersContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(SalamandersConfig.GROUP_NAME);
		refreshOnConfigChange(true);
	}

	@Override
	protected void startUp()
	{
		super.startUp();
	}

	@Override
	protected void shutDown()
	{
		super.shutDown();
		context.clearPrimaryTree();
	}

	@Subscribe
	public void OnMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		if(context.getPrimaryTree() != null || !menuOptionClicked.getMenuTarget().contains("Young tree")) return;
		switch(menuOptionClicked.getMenuAction())
		{
			case WIDGET_TARGET_ON_GAME_OBJECT:
			case GAME_OBJECT_FIRST_OPTION:
			case GAME_OBJECT_SECOND_OPTION:
			case GAME_OBJECT_THIRD_OPTION:
			case GAME_OBJECT_FOURTH_OPTION:
			case GAME_OBJECT_FIFTH_OPTION:
			{
				int x = menuOptionClicked.getParam0();
				int y = menuOptionClicked.getParam1();
				context.setPrimaryTree(x, y);
				break;
			}
		}
	}

	@Override
	public void setup()
	{
		requirements()
				.mustBeNear(() -> object("Young tree"))
				.must((ctx) -> ctx.getPrimaryTree() != null, "Click on tree to start");

		action().name("Drop salamander")
			.when(c -> Inventory.contains(
					ItemID.BLACK_SALAMANDER,
					ItemID.RED_SALAMANDER,
					ItemID.ORANGE_SALAMANDER
			))
			.then(c -> item(
					"Black salamander",
					"Red salamander",
					"Orange salamander"
			).interact("Release"))
			.many();

		action().name("Pick up items")
			.when(c -> tileItem("Rope", "Small fishing net").exists())
			.then(c -> tileItem("Rope", "Small fishing net").interact("Take"))
			.many();

		action().name("Set net trap")
			.when(c -> c.getYoungTree() != null && c.canPlaceTrap())
			.then(c -> of(c.getYoungTree()).interact("Set-trap"))
			.many();

		action().name("Check net trap")
			.when(c -> c.getNetTrap() != null)
			.then(c -> of(c.getNetTrap()).interact("Check"))
			.delay(2);
	}


	@Provides
	public SalamandersConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SalamandersConfig.class);
	}
}
