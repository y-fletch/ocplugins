package com.yfletch.summergarden;

import lombok.Setter;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class ActionOverlay extends OverlayPanel
{
	@Setter
	private LineComponent line;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (line != null)
		{
			panelComponent.getChildren().add(line);
		}

		return super.render(graphics);
	}
}
