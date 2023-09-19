package com.yfletch.occore.v2.overlay;

import com.google.inject.Singleton;
import lombok.Setter;

@Setter
@Singleton
public class InventoryItemDebugOverlay extends ItemDebugOverlay
{
	public InventoryItemDebugOverlay()
	{
		showOnInventory();
	}
}
