package com.yfletch.occore.overlay;

import com.google.inject.Singleton;
import com.yfletch.occore.ActionRunner;
import com.yfletch.occore.OCConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class ActionOverlay extends OverlayPanel
{
	private final String name;
	private final ActionRunner<?> runner;
	private final OCConfig config;

	public ActionOverlay(String name, ActionRunner<?> runner, OCConfig config)
	{
		this.name = name;
		this.runner = runner;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left(name).leftColor(Color.CYAN)
				.right(config.ocEnabled() ? "ON" : "OFF")
				.rightColor(config.ocEnabled() ? Color.GREEN : Color.YELLOW)
				.build()
		);

		if (runner.getDisplayLine() != null)
		{
			panelComponent.getChildren().add(runner.getDisplayLine());
			if (runner.isWorking())
			{
				panelComponent.getChildren().add(LineComponent.builder().left("Working...").build());
			}
		}
		else
		{
			panelComponent.getChildren().add(
				LineComponent.builder()
					.left("No action (" + runner.getActions().size() + " checked)")
					.build()
			);
		}

		return super.render(graphics);
	}
}
