package com.yfletch.ocbloods.lib.event;

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

	public WidgetEvent setOption(String option, int optionId)
	{
		setOption(option);
		setIdentifier(optionId);

		return this;
	}

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

	public WidgetEvent setDialogOption(int dialogWidgetId)
	{
		setOption("Continue", 0);
		setType(MenuAction.WIDGET_CONTINUE);
		setWidget(dialogWidgetId);

		return this;
	}

	public WidgetEvent setWidget(Widget widget)
	{
		setParam0(widget.getIndex());
		setParam1(widget.getId());

		return this;
	}

	public WidgetEvent setWidget(WidgetInfo widgetInfo)
	{
		Widget widget = client.getWidget(widgetInfo);
		return widget != null ? setWidget(widget) : this;
	}

	public WidgetEvent setWidget(int widgetInfo)
	{
		Widget widget = client.getWidget(widgetInfo);
		return widget != null ? setWidget(widget) : this;
	}

	public WidgetEvent setWidget(int groupId, int widgetId)
	{
		Widget widget = client.getWidget(groupId << 16 | widgetId);
		return widget != null ? setWidget(widget) : this;
	}

	public WidgetEvent setChild(int childId)
	{
		setParam0(childId);
		return this;
	}
}
