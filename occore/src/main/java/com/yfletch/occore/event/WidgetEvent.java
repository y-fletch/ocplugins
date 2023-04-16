package com.yfletch.occore.event;

import java.util.Arrays;
import java.util.Optional;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public class WidgetEvent extends EventOverride
{
	private final Client client;

	WidgetEvent(Client client)
	{
		this.client = client;
		setTarget("");
		setType(MenuAction.CC_OP);
	}

	@Override
	protected void validate()
	{
		if (getParam1() < 0)
		{
			throw new RuntimeException("Widget event: No interface set");
		}

		super.validate();
	}

	/**
	 * Set option name and index (one-based)
	 */
	public WidgetEvent setOption(String option, int index)
	{
		setOption(option);
		setIdentifier(index);

		return this;
	}

	/**
	 * Click on a dialog option containing the given text
	 */
	public WidgetEvent setDialogOption(String optionName)
	{
		Widget dialogOptions = client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
		if (dialogOptions == null || dialogOptions.getChildren() == null)
		{
			return null;
		}

		Optional<Widget> option = Arrays.stream(dialogOptions.getChildren())
			.filter(widget -> widget.getText().contains(optionName))
			.findFirst();

		if (option.isEmpty())
		{
			return null;
		}

		setOption("Continue", 0);
		setType(MenuAction.WIDGET_CONTINUE);
		setParam0(option.get().getIndex());
		setParam1(option.get().getId());

		return this;
	}

	/**
	 * Click on a dialog option with the given widget ID
	 */
	public WidgetEvent setDialogOption(int dialogWidgetId)
	{
		setOption("Continue", 0);
		setType(MenuAction.WIDGET_CONTINUE);
		setWidget(dialogWidgetId);

		return this;
	}

	/**
	 * Target the widget
	 */
	public WidgetEvent setWidget(Widget widget)
	{
		setParam0(widget.getIndex());
		setParam1(widget.getId());

		return this;
	}

	/**
	 * Target the widget using its WidgetInfo
	 */
	public WidgetEvent setWidget(WidgetInfo widgetInfo)
	{
		Widget widget = client.getWidget(widgetInfo);
		return widget != null ? setWidget(widget) : this;
	}

	/**
	 * Target the widget by ID
	 */
	public WidgetEvent setWidget(int widgetInfo)
	{
		Widget widget = client.getWidget(widgetInfo);
		return widget != null ? setWidget(widget) : this;
	}


	/**
	 * Target the widget by group and widget ID.
	 * Useful if there is no enum value in WidgetInfo for
	 * the widget we wish to target.
	 */
	public WidgetEvent setWidget(int groupId, int widgetId)
	{
		final var widget = client.getWidget(groupId << 16 | widgetId);
		return widget != null ? setWidget(widget) : this;
	}


	/**
	 * Target the widget by group and widget ID.
	 * Useful if there is no enum value in WidgetInfo for
	 * the widget we wish to target.
	 */
	public WidgetEvent setWidget(int groupId, int widgetId, int childIndex)
	{
		final var widget = client.getWidget(groupId << 16 | widgetId);
		if (widget == null)
		{
			return null;
		}

		final var children = widget.getChildren();
		if (children == null || childIndex >= children.length)
		{
			return null;
		}

		return setWidget(children[childIndex]);
	}

	/**
	 * Target a child of the current widget (widget must also be set).
	 * Used to target children that all share the same widget ID.
	 */
	public WidgetEvent setChild(int childId)
	{
		setParam0(childId);
		return this;
	}
}
