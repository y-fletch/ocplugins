package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import com.yfletch.occore.event.WrappedEvent;
import com.yfletch.occore.util.RegionPoint;
import java.awt.Color;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.LineComponent;

public class MoveAction<T extends ActionContext> extends Action<T>
{
	@Getter
	protected RegionPoint target;

	@Setter
	private String markerName;

	public MoveAction(RegionPoint regionPoint)
	{
		target = regionPoint;
	}

	@Override
	public LineComponent getDisplayLine(ActionContext context)
	{
		WorldPoint worldPoint = target.toWorld();
		String text = "???";

		if (worldPoint != null)
		{
			text = markerName != null
				? markerName + " " + worldPoint.getX() + "," + worldPoint.getY() + "," + worldPoint.getPlane()
				: worldPoint.getX() + "," + worldPoint.getY() + "," + worldPoint.getPlane();
		}

		return LineComponent.builder()
			.left("Move to").leftColor(Color.WHITE)
			.right(text).rightColor(Color.ORANGE)
			.build();
	}

	@Override
	public boolean isWorking(T ctx)
	{
		return ctx.isPathingTo(target.toWorld());
	}

	@Override
	public boolean isDone(T ctx)
	{
		return ctx.isAt(target.toWorld());
	}

	@Override
	public void run(T ctx, WrappedEvent event)
	{
		event.builder().move()
			.to(target.toWorld())
			.override();
	}
}
