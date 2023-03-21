package com.yfletch.ocbarbfishing.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Method
{
	TAR_DROP("Tar/drop"),
	CUT_EAT("Cut/eat"),
	TAR_THEN_CUT_EAT("Tar then cut/eat");

	private final String name;
}
