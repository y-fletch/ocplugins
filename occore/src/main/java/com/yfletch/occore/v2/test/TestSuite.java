package com.yfletch.occore.v2.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestSuite
{
	HOUSE(16), BANK(6), WALK(1);

	private final int rules;
}
