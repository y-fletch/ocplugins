package com.yfletch.ocbloods.lib;

import java.awt.Color;
import lombok.AllArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

@AllArgsConstructor
public class ObjectAction<T extends ActionContext> extends Action<T>
{
	private String action;
	private String objectName;

	@Override
	public LineComponent getDisplayLine(T ctx)
	{
		return LineComponent.builder()
			.left(action).leftColor(Color.WHITE)
			.right(objectName).rightColor(Color.CYAN)
			.build();
	}
}
