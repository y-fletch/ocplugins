package com.yfletch.occore.v2.util;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.function.Predicate;
import net.runelite.api.util.Text;
import org.apache.commons.lang3.ArrayUtils;

public class Util
{
	/**
	 * Shortcut for lowercase contains
	 */
	public static Predicate<String> containing(String text)
	{
		return s -> !Strings.isNullOrEmpty(s)
			&& Text.removeTags(s).toLowerCase().contains(text);
	}

	/**
	 * Match a string not containing the given fragment
	 */
	public static Predicate<String> notContaining(String text)
	{
		return s -> !Strings.isNullOrEmpty(s)
			&& !Text.removeTags(s).toLowerCase().contains(text);
	}

	/**
	 * Match a string not containing any of the given fragments
	 */
	public static Predicate<String> notContaining(String... text)
	{
		return s -> !Strings.isNullOrEmpty(s) && !matching(text).test(s);
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

	/**
	 * Match a string not containing any of the given fragments
	 */
	public static Predicate<String> matching(String... texts)
	{
		return s -> !Strings.
			isNullOrEmpty(s) && Arrays.stream(texts).anyMatch(t -> Text.removeTags(s).equals(t));
	}
}
