package com.yfletch.occore.v2.util;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.function.Predicate;
import net.runelite.api.util.Text;
import net.unethicalite.api.EntityNameable;
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
}
