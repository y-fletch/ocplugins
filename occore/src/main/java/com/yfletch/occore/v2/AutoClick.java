package com.yfletch.occore.v2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;

@Slf4j
@Singleton
public class AutoClick
{
	private static final Executor CLICK_EXECUTOR = Executors.newSingleThreadExecutor();

	@Inject private Client client;

	@Setter
	private int clicksPerTick;

	@Setter
	private Point point;

	public boolean ready()
	{
		return point != null && clicksPerTick != 0;
	}

	public void run()
	{
		CLICK_EXECUTOR.execute(() -> {
			final var intervals = generateIntervals(clicksPerTick);

			try
			{
				for (final var interval : intervals)
				{
					Thread.sleep((long) (interval * 500));
					click(point);
					Thread.sleep((long) (interval * 500));
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
	}

	private void click(Point point)
	{
		if (client.isStretchedEnabled())
		{
			final var stretched = client.getStretchedDimensions();
			final var real = client.getRealDimensions();
			final var width = (stretched.width / real.getWidth());
			final var height = (stretched.height / real.getHeight());
			final var scaledPoint = new Point((int) (point.getX() * width), (int) (point.getY() * height));

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
		// not a full tick - just slightly less to encourage
		// the threads not to overlap
		var target = 0.55d;
		final var values = new double[n];
		final var random = new Random();
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
}
