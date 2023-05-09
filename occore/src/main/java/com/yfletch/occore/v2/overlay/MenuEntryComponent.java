package com.yfletch.occore.v2.overlay;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.util.Text;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.TextComponent;

@Setter
@Builder
public class MenuEntryComponent implements LayoutableRenderableEntity
{
	private String text;

	@Builder.Default
	private Point preferredLocation = new Point();

	@Builder.Default
	private Dimension preferredSize = new Dimension(ComponentConstants.STANDARD_WIDTH, 0);

	@Builder.Default
	@Getter
	private final Rectangle bounds = new Rectangle();

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final var metrics = graphics.getFontMetrics();
		final var fmHeight = metrics.getHeight();
		final var fullTextWidth = metrics.stringWidth(Text.removeTags(text));
		final int x = preferredLocation.x;
		final int baseY = preferredLocation.y + fmHeight;
		int y = baseY;
		final var textComponent = new TextComponent();

		if (preferredSize.width < fullTextWidth)
		{
			final var splitLines = lineBreakText(text, preferredSize.width, metrics);

			for (final String lineText : splitLines)
			{
				final var prevColor = graphics.getColor();
				final var tag = "<col=" + Integer.toHexString(prevColor.getRGB()).substring(2) + ">";
				textComponent.setPosition(new Point(x, y));
				textComponent.setText(tag + lineText);
				textComponent.render(graphics);

				y += fmHeight;
			}

			final Dimension dimension = new Dimension(preferredSize.width, y - baseY);
			bounds.setLocation(preferredLocation);
			bounds.setSize(dimension);
			return dimension;
		}

		textComponent.setPosition(new Point(x, y));
		textComponent.setText(text);
		textComponent.render(graphics);

		final Dimension dimension = new Dimension(preferredSize.width, fmHeight);
		bounds.setLocation(preferredLocation);
		bounds.setSize(dimension);
		return dimension;
	}

	private static String[] lineBreakText(String text, int maxWidth, FontMetrics metrics)
	{
		final String[] words = text.split(" ");

		if (words.length == 0)
		{
			return new String[0];
		}

		final StringBuilder wrapped = new StringBuilder(words[0]);
		int spaceLeft = maxWidth - metrics.stringWidth(Text.removeTags(wrapped.toString()));
		final int spaceWidth = metrics.stringWidth(" ");

		for (int i = 1; i < words.length; i++)
		{
			final String word = words[i];
			final int wordLen = metrics.stringWidth(Text.removeTags(word));

			if (wordLen + spaceWidth > spaceLeft)
			{
				wrapped.append('\n').append(word);
				spaceLeft = maxWidth - wordLen;
			}
			else
			{
				wrapped.append(' ').append(word);
				spaceLeft -= spaceWidth + wordLen;
			}
		}

		return wrapped.toString().split("\n");
	}
}
