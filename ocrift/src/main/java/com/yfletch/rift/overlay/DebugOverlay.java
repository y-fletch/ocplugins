package com.yfletch.rift.overlay;

import com.yfletch.rift.RiftConfig;
import com.yfletch.rift.RiftContext;
import com.yfletch.rift.enums.Pouch;
import com.yfletch.rift.helper.PouchSolver;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ItemID;
import net.runelite.api.ObjectID;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;

@Singleton
public class DebugOverlay extends OverlayPanel
{
	@Inject
	private RiftContext context;

	@Inject
	private RiftConfig config;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showDebugOverlay())
		{
			return null;
		}

		Map<String, String> debugs = new HashMap<>();
		debugs.put("Game time", "" + new DecimalFormat("0.00").format(context.getGameTime()));
//		debugs.put("Pouch capacity", "" + context.getPouchCapacity());
		context.getPouchEssence().forEach((pouch, qty) -> debugs.put(pouch.getItemName(), "" + qty));
		context.getFlags().forEach((flag, value) -> debugs.put(flag, "" + value));
		debugs.put("Free slots", "" + context.getFreeInventorySlots());
		debugs.put("Opt. free slots", "" + context.getOptimisticFreeSlots());
		debugs.put("Ess", "" + context.getItemCount(ItemID.PURE_ESSENCE));
		debugs.put("Opt. ess", "" + context.getOptimisticEssenceCount());
		Pouch unf = new PouchSolver(context).getNextUnfilledPouch();
		debugs.put("Next unf", "" + (unf != null ? unf.getItemName() : "null"));
		Pouch fil = new PouchSolver(context).getNextFilledPouch();
		debugs.put("Next fil", "" + (fil != null ? fil.getItemName() : "null"));

		debugs.forEach((name, value) ->
			panelComponent.getChildren().add(LineComponent.builder().left(name).right(value).build())
		);

		return super.render(graphics);
	}
}
