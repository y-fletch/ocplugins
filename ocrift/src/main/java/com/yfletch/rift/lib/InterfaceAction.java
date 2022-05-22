package com.yfletch.rift.lib;

import java.awt.Color;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.runelite.client.ui.overlay.components.LineComponent;

@RequiredArgsConstructor
@AllArgsConstructor
public class InterfaceAction<T extends ActionContext> extends Action<T>
{
	private String action;
	@Nullable
	private String interfaceName;

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		LineComponent.LineComponentBuilder builder = LineComponent.builder()
			.left(action).leftColor(Color.WHITE);

		if (interfaceName != null)
		{
			builder
				.right(interfaceName)
				// green for magic spells
				.rightColor(action.equals("Cast") ? Color.GREEN : Color.ORANGE);
		}

		return builder.build();
	}

}
