package com.yfletch.occore.v2.interaction.exceptions;

import com.yfletch.occore.v2.interaction.Interaction;

public class InvalidTargetException extends InteractionException
{
	public InvalidTargetException(Interaction interaction, String message)
	{
		super(interaction, message);
	}

	@Override
	public String getMessage()
	{
		return "Invalid target: " + super.getMessage();
	}
}
