package com.yfletch.occore.event;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
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

	private final int itemId = -1;

	private Consumer<MenuEntry> callback;

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

	public EventOverride onClick(Consumer<MenuEntry> callback)
	{
		this.callback = callback;
		return this;
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
			targetEvent.setItemId(getItemId());
			targetEvent.getMenuEntry().onClick(callback);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Consume (fail)");
			targetEvent.consume();
		}
	}
}
