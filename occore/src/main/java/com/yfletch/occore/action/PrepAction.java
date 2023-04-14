package com.yfletch.occore.action;

import com.yfletch.occore.ActionContext;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.runelite.client.ui.overlay.components.LineComponent;
import org.apache.commons.lang3.NotImplementedException;

public class PrepAction<T extends ActionContext> extends Action<T>
{
	private Function<T, Map<String, Boolean>> getConditions;

	public PrepAction<T> withConditions(Function<T, Map<String, Boolean>> getConditions)
	{
		this.getConditions = getConditions;
		return this;
	}

	protected Map<String, Boolean> getConditions(T ctx)
	{
		if (getConditions != null)
		{
			return getConditions.apply(ctx);
		}

		throw new NotImplementedException("Missing conditions for prep action");
	}

	@Override
	public boolean isReady(T ctx)
	{
		return getConditions(ctx).values().stream().anyMatch(b -> b);
	}

	@Override
	public boolean isWorking(T ctx)
	{
		return false;
	}

	@Override
	public boolean isDone(T ctx)
	{
		return getConditions(ctx).values().stream().allMatch(b -> b);
	}

	@Override
	public LineComponent getDisplayLine(T ctx)
	{
		List<String> errors = new ArrayList<>();
		for (Map.Entry<String, Boolean> entry : getConditions(ctx).entrySet())
		{
			if (!entry.getValue())
			{
				errors.add(entry.getKey());
			}
		}

		return LineComponent.builder()
			.left("Failed requirements: " + String.join(", ", errors))
			.leftColor(Color.RED)
			.build();
	}
}
