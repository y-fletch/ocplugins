package com.yfletch.occore.v2.interaction.exceptions;

import com.yfletch.occore.v2.interaction.Interaction;

public class InvalidOptionException extends InteractionException
{
	public InvalidOptionException(Interaction interaction, String message)
	{
		super(interaction, message);
	}

	@Override
	public String getMessage()
	{
		return "Invalid option: " + super.getMessage();
	}
}
