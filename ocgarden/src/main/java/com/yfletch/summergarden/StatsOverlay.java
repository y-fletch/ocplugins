package com.yfletch.summergarden;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class StatsOverlay extends OverlayPanel
{
	@Inject
	private Stats stats;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(LineComponent.builder().left("Summer garden").leftColor(Color.CYAN).leftFont(FontManager.getRunescapeBoldFont()).build());
		panelComponent.getChildren().add(LineComponent.builder().left("Fruit picked").right("" + stats.getFruitPicked()).build());
		panelComponent.getChildren().add(LineComponent.builder().left("XP gained").right(stats.xpGained()).build());
		panelComponent.getChildren().add(LineComponent.builder().left("XP/hr").right(stats.xpHr()).build());

		if (stats.canSeeTotalJuice())
		{
			panelComponent.getChildren().add(LineComponent.builder().left("Total juice").leftColor(Color.GREEN).right("" + stats.getTotalJuice()).build());
			panelComponent.getChildren().add(LineComponent.builder().left("Total XP").leftColor(Color.GREEN).right(stats.xpBanked()).build());
		}

		panelComponent.getChildren().add(LineComponent.builder().left("Fails").right("" + stats.getFails()).build());
		panelComponent.getChildren().add(LineComponent.builder().left("Failure rate").right(stats.failRate()).build());

		return super.render(graphics);
	}
}
