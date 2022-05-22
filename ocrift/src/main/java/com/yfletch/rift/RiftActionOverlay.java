package com.yfletch.rift;

import com.yfletch.rift.lib.ActionRunner;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Singleton;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class RiftActionOverlay extends OverlayPanel
{
	private final ActionRunner<?> runner;

	public RiftActionOverlay(ActionRunner<?> runner)
	{
		this.runner = runner;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
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
			panelComponent.getChildren().add(LineComponent.builder().left("No action").build());
		}

		return super.render(graphics);
	}
}
