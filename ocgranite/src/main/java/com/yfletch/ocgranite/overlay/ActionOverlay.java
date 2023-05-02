package com.yfletch.ocgranite.overlay;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yfletch.ocgranite.OCGraniteRunner;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class ActionOverlay extends OverlayPanel
{
	@Inject
	private OCGraniteRunner runner;

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

