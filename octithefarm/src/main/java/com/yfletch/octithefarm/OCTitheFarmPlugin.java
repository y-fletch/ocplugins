package com.yfletch.octithefarm;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.yfletch.occore.v2.RunnerPlugin;
import static com.yfletch.occore.v2.interaction.Entities.item;
import static com.yfletch.occore.v2.interaction.Entities.of;
import static com.yfletch.occore.v2.interaction.Entities.spell;
import static com.yfletch.occore.v2.util.Util.containing;
import static com.yfletch.occore.v2.util.Util.nameContaining;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.unethicalite.api.items.Inventory;
import static net.unethicalite.api.magic.SpellBook.Lunar.HUMIDIFY;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "OC Tithe Farm",
	description = "One-click Tithe Farm",
	enabledByDefault = false
)
public class OCTitheFarmPlugin extends RunnerPlugin<TitheFarmContext>
{
	@Inject private Client client;
	@Inject private TitheFarmContext context;

	@Inject
	public void init(TitheFarmConfig config, TitheFarmContext context)
	{
		setConfig(config);
		setContext(context);
		setConfigGroup(TitheFarmConfig.GROUP_NAME);
	}

	@Override
	public void setup()
	{
		final var seed = new String[]{"Golovanova seed", "Bologano seed", "Logavano seed"};

		requirements()
			.mustHaveInInventory(nameContaining("Watering can"), "Watering cans")
			.mustHaveInInventory("Seed dibber", "Spade")
			.mustHaveAnyInInventory(seed)
			.mustBeAbleToCast(HUMIDIFY);

		action().name("Cast humidify")
			.when(c -> !Inventory.contains(nameContaining("Watering can("))
				&& HUMIDIFY.canCast())
			.then(c -> spell(HUMIDIFY).cast())
			.delay(2);

		action().name("Harvest/clear plant")
			.when(c -> c.getNextHarvestable() != null)
			.then(c -> of(c.getNextHarvestable()).interact("Harvest", "Clear"))
			.delay(1)
			.oncePerTick()
			.onClick(c -> c.flag("harvested", true));

		action().name("Water plant")
			.when(c -> c.getNextWaterable() != null)
			.then(c -> item(containing("Watering can(")).useOn(of(c.getNextWaterable())))
			.oncePerTick();

		action().name("Plant seed")
			.when(c -> c.getNextPatch() != null)
			.then(c -> item(seed).useOn(of(c.getNextPatch())))
			.delay(c -> c.flag("harvested") ? 0 : 1)
			.onClick(c -> c.clear("harvested"));
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		if (event.getActor().equals(client.getLocalPlayer()))
		{
			if (client.getLocalPlayer().getAnimation() == 2293)
			{
				context.next();
			}
		}
	}

	@Provides
	TitheFarmConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TitheFarmConfig.class);
	}
}
