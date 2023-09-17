package com.yfletch.occore.v2.overlay;

import com.yfletch.occore.v2.RunnerPlugin;
import com.yfletch.occore.v2.StatisticTracker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

public class CoreStatisticsOverlay extends OverlayPanel
{
	public static final String CLEAR_STATISTICS = "Clear";

	private final RunnerPlugin<?> plugin;
	private final StatisticTracker statistics;

	public CoreStatisticsOverlay(RunnerPlugin<?> plugin, StatisticTracker statistics)
	{
		this.plugin = plugin;
		this.statistics = statistics;

		getMenuEntries().add(
			new OverlayMenuEntry(
				MenuAction.RUNELITE_OVERLAY,
				CLEAR_STATISTICS,
				plugin.getName() + " statistics"
			)
		);
	}

	private Color getColor(String value)
	{
		if (value.endsWith("K"))
		{
			return Color.WHITE;
		}

		if (value.endsWith("M"))
		{
			return Color.GREEN;
		}

		return Color.YELLOW;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left(plugin.getName() + " Statistics")
				.leftColor(Color.CYAN)
				.build()
		);

		for (var entry : statistics.collect().entrySet())
		{
			panelComponent.getChildren().add(
				LineComponent.builder()
					.left(entry.getKey())
					.right(entry.getValue())
					.rightColor(getColor(entry.getValue()))
					.build()
			);
		}

		return super.render(graphics);
	}
}
