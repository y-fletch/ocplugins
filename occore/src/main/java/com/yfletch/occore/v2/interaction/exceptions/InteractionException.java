package com.yfletch.occore.v2.interaction.exceptions;

import com.yfletch.occore.v2.interaction.Interaction;

public abstract class InteractionException extends IllegalStateException
{
	protected final Interaction interaction;

	protected InteractionException(Interaction interaction, String message)
	{
		super(message);
		this.interaction = interaction;
	}
}
