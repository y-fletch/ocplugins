package com.yfletch.occore.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PluginAPI
{
	ONE_CLICK("One-click", true),
	ONE_CLICK_CONSUME("One-click (consume)", true),
	ONE_CLICK_AUTO("One-click (auto)", true),
	DEVIOUS("Devious", false);

	private final String name;
	@Getter
	private final boolean isOneClick;

	public String toString()
	{
		return name;
	}
}
