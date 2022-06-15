package com.yfletch.occore.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;

@Getter
@Setter
@Accessors(chain = true)
public abstract class EventOverride
{
	private MenuOptionClicked targetEvent;

	private String option;
	private String target;
	private int identifier = -1;
	private MenuAction type;
	private int param0 = -1;
	private int param1 = -1;
	private boolean forceLeftClick = false;

	protected void validate()
	{
		if (getTarget() == null)
		{
			throw new RuntimeException("EventOverride: Missing target");
		}

		if (getType() == null)
		{
			throw new RuntimeException("EventOverride: Missing type/menuAction");
		}
	}

	/**
	 * Override the current event with this override's options
	 */
	public void override()
	{
		validate();

		try
		{
			targetEvent.setMenuOption(getOption());
			targetEvent.setMenuTarget(getTarget());
			targetEvent.setId(getIdentifier());
			targetEvent.setMenuAction(getType());
			targetEvent.setParam0(getParam0());
			targetEvent.setParam1(getParam1());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Consume (fail)");
			targetEvent.consume();
		}
	}
}
