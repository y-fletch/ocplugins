package com.yfletch.occlicker;


import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;

@Singleton
@Extension
@Slf4j
@PluginDescriptor(
	name = "OC Clicker",
	enabledByDefault = false,
	description = "Auto-clicker for OC plugins"
)
public class OCClickerPlugin extends Plugin
{
	@Inject
	private OCClickerConfig config;

	@Inject
	private OCClickerOverlay overlay;

	@Inject
	private Client client;

	@Inject
	private KeyManager keyManager;

	@Inject
	private OverlayManager overlayManager;

	@Getter
	private boolean enabled = false;

	@Getter
	private Point point;

	private static final Executor CLICK_EXECUTOR = Executors.newSingleThreadExecutor();

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggle())
	{
		@Override
		public void hotkeyPressed()
		{
			enabled = !enabled;
			point = client.getMouseCanvasPosition();
		}
	};

	private final Random random = new Random();

	@Override
	protected void startUp()
	{
		keyManager.registerKeyListener(hotkeyListener);
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(hotkeyListener);
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (enabled)
		{
			CLICK_EXECUTOR.execute(() -> {
				var intervals = generateIntervals(config.clicksPerTick());

				try
				{
					for (var interval : intervals)
					{
						click(point);
						Thread.sleep((long) (interval * 1000));
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			});
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		String menuOption = (event.getMenuOption());

		if ("Walk here".equals(menuOption) && enabled && config.consumeYellowClicks())
		{
			event.consume();
		}
	}

	private void click(Point point)
	{
		if (client.isStretchedEnabled())
		{
			final Dimension stretched = client.getStretchedDimensions();
			final Dimension real = client.getRealDimensions();
			final double width = (stretched.width / real.getWidth());
			final double height = (stretched.height / real.getHeight());
			final Point scaledPoint = new Point((int) (point.getX() * width), (int) (point.getY() * height));

			mouseEvent(501, scaledPoint);
			mouseEvent(502, scaledPoint);
			mouseEvent(503, scaledPoint);

			return;
		}

		mouseEvent(501, point);
		mouseEvent(502, point);
		mouseEvent(503, point);
	}

	private void mouseEvent(int id, Point point)
	{
		client.getCanvas().dispatchEvent(
			new MouseEvent(
				client.getCanvas(), id,
				System.currentTimeMillis(),
				0, point.getX(), point.getY(),
				1, false, 1
			)
		);
	}

	private double[] generateIntervals(int n)
	{
		var target = 0.6d;
		var values = new double[n];
		var random = new Random();
		for (var i = 0; i < n - 1; i++)
		{
			values[i] = random.nextDouble() * target;
			target -= values[i];
		}
		// fill in rest
		values[n - 1] = target;

		// shuffle
		for (var i = n - 1; i > 0; i--)
		{
			var j = random.nextInt(i + 1);
			var tmp = values[i];
			values[i] = values[j];
			values[j] = tmp;
		}

		return values;
	}

	@Provides
	OCClickerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OCClickerConfig.class);
	}
}