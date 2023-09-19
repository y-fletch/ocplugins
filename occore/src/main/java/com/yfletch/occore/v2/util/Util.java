package com.yfletch.occore.v2.util;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.function.Predicate;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.util.Text;
import net.unethicalite.api.EntityNameable;
import net.unethicalite.api.Interactable;
import org.apache.commons.lang3.ArrayUtils;

public class Util
{
	/**
	 * Match a string containing any of the given fragments
	 */
	public static Predicate<String> containing(String... fragments)
	{
		return s -> !Strings.isNullOrEmpty(s)
			&& Arrays.stream(fragments).anyMatch(t -> Text.removeTags(s).toLowerCase().contains(t.toLowerCase()));
	}

	/**
	 * Match a string not containing any of the given fragments
	 */
	public static Predicate<String> notContaining(String... fragments)
	{
		return s -> !Strings.isNullOrEmpty(s) && !containing(fragments).test(s);
	}

	/**
	 * Match an entity name containing any of the given fragments
	 */
	public static <T extends EntityNameable> Predicate<T> nameContaining(String... fragments)
	{
		return e -> e != null && containing(fragments).test(e.getName());
	}

	/**
	 * Match an entity name not containing any of the given fragments
	 */
	public static <T extends EntityNameable> Predicate<T> nameNotContaining(String... fragments)
	{
		return e -> e != null && notContaining(fragments).test(e.getName());
	}

	/**
	 * Match a string equal to any of the given fragments
	 */
	public static Predicate<String> matching(String... fragments)
	{
		return s -> !Strings.isNullOrEmpty(s)
			&& Arrays.stream(fragments).anyMatch(t -> Text.removeTags(s).equalsIgnoreCase(t));
	}

	/**
	 * Match a string equal to any of the given fragments
	 */
	public static Predicate<String> notMatching(String... fragments)
	{
		return s -> !Strings.isNullOrEmpty(s) && !matching(fragments).test(s);
	}

	/**
	 * Match an entity name equal to any of the given fragments
	 */
	public static <T extends EntityNameable> Predicate<T> nameMatching(String... names)
	{
		return e -> e != null && matching(names).test(e.getName());
	}

	/**
	 * Match an entity name equal to any of the given fragments
	 */
	public static <T extends EntityNameable> Predicate<T> nameNotMatching(String... names)
	{
		return e -> e != null && !matching(names).test(e.getName());
	}

	/**
	 * Match an entity with any of the specified actions
	 */
	public static <T extends Interactable> Predicate<T> withAction(String... actions)
	{
		return e -> e != null && e.hasAction(actions);
	}

	public static String[] join(String[] a, String[] b)
	{
		return ArrayUtils.addAll(a, b);
	}

	public static String[] parseList(String input)
	{
		if (Strings.isNullOrEmpty(input))
		{
			return new String[0];
		}

		return Arrays.stream(input.split("\\s*,\\s*"))
			.filter(s -> !Strings.isNullOrEmpty(s))
			.toArray(String[]::new);
	}

	public static WorldPoint offset(WorldPoint origin, int x, int y)
	{
		return new WorldPoint(origin.getX() + x, origin.getY() + y, origin.getPlane());
	}

	public static WorldArea generateArea(WorldPoint origin, int radius)
	{
		return new WorldArea(
			origin.getX() - radius,
			origin.getY() - radius,
			radius * 2 + 1,
			radius * 2 + 1,
			origin.getPlane()
		);
	}

	public static String formatTickTime(int ticks)
	{
		final var seconds = (int) (ticks * 0.6d);
		final var minutes = (int) (seconds / 60d);
		final var hours = (int) (minutes / 60d);

		return hours > 0
			? String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
			: String.format("%02d:%02d", minutes % 60, seconds % 60);
	}
}
