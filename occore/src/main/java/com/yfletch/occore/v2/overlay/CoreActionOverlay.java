package com.yfletch.occore.v2.overlay;

import com.yfletch.occore.v2.RunnerPlugin;
import com.yfletch.occore.v2.interaction.exceptions.InteractionException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Slf4j
public class CoreActionOverlay extends OverlayPanel
{
	private final RunnerPlugin<?> plugin;

	public CoreActionOverlay(RunnerPlugin<?> plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left(plugin.getName()).leftColor(Color.CYAN)
				.right(plugin.enabled() ? "ON" : "OFF")
				.rightColor(plugin.enabled() ? Color.GREEN : Color.YELLOW)
				.build()
		);

		final var interaction = plugin.getNextInteraction();
		if (interaction == null)
		{
			final var messages = plugin.getMessages();
			if (messages == null)
			{
				panelComponent.getChildren().add(
					LineComponent.builder()
						.left("Nothing to do (" + plugin.getRules().size() + " rules checked)")
						.build()
				);

				return super.render(graphics);
			}

			for (final var message : messages)
			{
				panelComponent.getChildren().add(
					MenuEntryComponent.builder()
						.text(message)
						.build()
				);
			}

			return super.render(graphics);
		}

		var text = "<col=ff0000>Errored getting action text";
		try
		{
			text = "<col=ffffff>" + interaction.getOption() + " " + interaction.getTarget();
		}
		catch (InteractionException ignored)
		{
		}

		panelComponent.getChildren().add(
			MenuEntryComponent.builder().text(text).build()
		);
		if (plugin.isConsuming())
		{
			panelComponent.getChildren().add(
				LineComponent.builder().left("Working...").build()
			);
		}

		return super.render(graphics);
	}
}
