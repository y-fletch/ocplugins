package com.yfletch.ocbloods.overlay;

import com.yfletch.ocbloods.OCBloodsConfig;
import com.yfletch.ocbloods.util.RSNumberFormat;
import com.yfletch.ocbloods.util.Statistics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class StatisticsOverlay extends OverlayPanel
{
	public static final String ACTION_RESET_STATISTICS = "Reset";
	private static final String PANEL_NAME = "OC Bloods statistics";

	@Inject
	private OCBloodsConfig config;

	@Inject
	private Statistics statistics;
	
	public StatisticsOverlay()
	{
		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, ACTION_RESET_STATISTICS, PANEL_NAME)
		);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showStatisticsOverlay())
		{
			return null;
		}

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("OC Bloods").leftColor(Color.CYAN)
				.right(statistics.getTimeElapsed())
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Blood runes")
				.right(statistics.getRunesCrafted())
				.rightColor(RSNumberFormat.getColor(statistics.getRunesCrafted()))
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Runes/hr")
				.right(statistics.getRunesPerHour())
				.rightColor(RSNumberFormat.getColor(statistics.getRunesPerHour()))
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Total GP")
				.right(statistics.getTotalGp())
				.rightColor(RSNumberFormat.getColor(statistics.getTotalGp()))
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("GP/hr")
				.right(statistics.getGpPerHour())
				.rightColor(RSNumberFormat.getColor(statistics.getGpPerHour()))
				.build()
		);

		if (config.showAdvancedStatistics())
		{
			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Trips completed")
					.right("" + statistics.getTripsCompleted())
					.build()
			);

			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Fastest trip")
					.right(statistics.getFastestTripTime())
					.rightColor(statistics.getFastestTripTimeColor())
					.build()
			);

			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Average trip")
					.right(statistics.getAverageTripTime())
					.rightColor(statistics.getAverageTripTimeColor())
					.build()
			);

			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Trips/hr")
					.right(statistics.getTripsPerHour())
					.rightColor(statistics.getTripsPerHourColor())
					.build()
			);
		}

		return super.render(graphics);
	}
}
