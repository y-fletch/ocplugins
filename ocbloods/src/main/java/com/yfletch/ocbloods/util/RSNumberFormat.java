package com.yfletch.ocbloods.util;

import java.awt.Color;

public class RSNumberFormat
{
	public static String format(int number)
	{
		if (number < 100000)
		{
			return String.format("%,d", number);
		}

		if (number < 10000000)
		{
			return String.format("%,dK", number / 1000);
		}

		return String.format("%,dM", number / 1000000);
	}

	public static String format(double number)
	{
		return format((int) number);
	}

	public static Color getColor(int number)
	{
		return getColor(format(number));
	}

	public static Color getColor(String formatted)
	{
		if (formatted.contains("M"))
		{
			return Color.GREEN;
		}

		if (formatted.contains("K"))
		{
			return Color.WHITE;
		}

		return Color.YELLOW;
	}
}
