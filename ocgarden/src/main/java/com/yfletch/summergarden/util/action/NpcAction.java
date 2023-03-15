package com.yfletch.summergarden.util.action;

import com.yfletch.summergarden.util.WrappedEvent;
import java.awt.Color;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;
import org.apache.commons.lang3.NotImplementedException;

@AllArgsConstructor
public class NpcAction implements Action
{
	private String npcName;

	/**
	 * AKA Menu option
	 */
	private String actionName;

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		return LineComponent.builder()
			.left(actionName).leftColor(Color.WHITE)
			.right(npcName).rightColor(Color.YELLOW)
			.build();
	}

	@Override
	public boolean isReady(ActionContext context)
	{
		throw new NotImplementedException("Missing isReady implementation");
	}

	@Override
	public boolean isDone(ActionContext context)
	{
		return false;
	}

	@Override
	public void run(ActionContext context, WrappedEvent event)
	{
		throw new NotImplementedException("Missing run implementation");
	}

	@Override
	public void done(ActionContext context)
	{

	}
}
