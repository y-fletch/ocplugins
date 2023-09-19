package com.yfletch.occore.v2.overlay;

import com.yfletch.occore.v2.RunnerPlugin;
import com.yfletch.occore.v2.util.TextColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Slf4j
public class InteractionOverlay extends OverlayPanel
{
	private final RunnerPlugin<?> plugin;

	public InteractionOverlay(RunnerPlugin<?> plugin)
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

		if (plugin.areBreaksEnabled())
		{
			if (plugin.isInBreak())
			{
				panelComponent.getChildren().add(
					LineComponent.builder()
						.left("Break ends")
						.right(plugin.getTimeRemainingInBreak())
						.build()
				);
			}
			else
			{
				panelComponent.getChildren().add(
					LineComponent.builder()
						.left("Next break")
						.right(plugin.getTimeToNextBreak())
						.build()
				);
			}
		}

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

		var repeats = "";
		if (plugin.getRuleRepeatsLeft() > 1)
		{
			repeats = TextColor.GRAY + " (" + plugin.getRuleRepeatsLeft() + "x)";
		}
		else if (plugin.getRuleRepeatsLeft() == 0)
		{
			repeats = TextColor.GRAY + " (complete)";
		}

		panelComponent.getChildren().add(
			MenuEntryComponent.builder().text(interaction.getTooltip() + repeats).build()
		);
		if (plugin.isDelaying())
		{
			panelComponent.getChildren().add(
				MenuEntryComponent.builder().text(TextColor.GRAY + "Delaying...").build()
			);
		}

		return super.render(graphics);
	}
}
