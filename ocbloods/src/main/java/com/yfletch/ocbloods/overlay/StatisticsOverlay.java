package com.yfletch.ocbloods.overlay;

import com.yfletch.ocbloods.OCBloodsConfig;
import com.yfletch.ocbloods.util.Statistics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class StatisticsOverlay extends OverlayPanel
{
	@Inject
	private OCBloodsConfig config;

	@Inject
	private Statistics statistics;

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
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Runes/hr")
				.right(statistics.getRunesPerHour())
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Total GP")
				.right(statistics.getTotalGp())
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("GP/hr")
				.right(statistics.getGpPerHour())
				.build()
		);

		return super.render(graphics);
	}
}
