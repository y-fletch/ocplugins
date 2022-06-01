package com.yfletch.rift.overlay;

import com.yfletch.rift.RiftConfig;
import com.yfletch.rift.util.RSNumberFormat;
import com.yfletch.rift.util.Statistics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class StatisticsOverlay extends OverlayPanel
{
	public static final String CLEAR_STATISTICS = "Clear";

	@Inject
	private RiftConfig config;

	@Inject
	private Statistics statistics;

	public StatisticsOverlay()
	{
		getMenuEntries().add(
			new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, CLEAR_STATISTICS, "Rift statistics")
		);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showStatistics())
		{
			return null;
		}

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("OC Rift").leftColor(Color.CYAN)
				.right("E/C")
				.build()
		);

		if (statistics.getRiftsClosed() > 0)
		{
			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Rifts closed")
					.right(statistics.getRiftsClosed() + "")
					.build()
			);

			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Games/hr")
					.right(statistics.getRiftsPerHour())
					.build()
			);

			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Energy/game")
					.right(statistics.getElementalEnergyPerGame() + "/" + statistics.getCatalyticEnergyPerGame())
					.build()
			);
		}

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Energy gained")
				.right(statistics.getElementalEnergy() + "/" + statistics.getCatalyticEnergy())
				.build()
		);

		panelComponent.getChildren().add(
			LineComponent.builder()
				.left("Energy/hr")
				.right(statistics.getElementalEnergyPerHour() + "/" + statistics.getCatalyticEnergyPerHour())
				.build()
		);

		if (statistics.getTotalCatalyticEnergy() > 0)
		{
			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("Total energy")
					.leftColor(Color.GREEN)
					.right(statistics.getTotalElementalEnergy() + "/" + statistics.getTotalCatalyticEnergy())
					.rightColor(Color.GREEN)
					.build()
			);
		}

		if (config.showRunesBanked())
		{
			statistics.getRunesBanked().forEach((rune, amt) -> {
				panelComponent.getChildren().add(
					LineComponent.builder()
						.left(rune.getName() + " runes")
						.leftColor(ColorScheme.LIGHT_GRAY_COLOR)
						.right(RSNumberFormat.format(amt))
						.rightColor(ColorScheme.LIGHT_GRAY_COLOR)
						.build()
				);
			});
		}

		return super.render(graphics);
	}
}
