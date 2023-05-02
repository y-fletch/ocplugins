package com.yfletch.ocgranite.overlay;

import com.yfletch.ocgranite.OCGraniteContext;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.ui.overlay.Overlay;

@Singleton
public class HighlightOverlay extends Overlay
{
	@Inject
	private OCGraniteContext context;

	@Override
	public Dimension render(Graphics2D graphics2D)
	{
		var rock = context.getNextRock();
		if (rock == null)
		{
			return null;
		}

		var clickbox = rock.getClickbox();
		graphics2D.setColor(Color.CYAN);
		graphics2D.draw(clickbox);

		var prev = context.getPreviousRock();
		if (prev == null)
		{
			return null;
		}

		clickbox = prev.getClickbox();
		graphics2D.setColor(Color.RED);
		graphics2D.draw(clickbox);

		return null;
	}
}
