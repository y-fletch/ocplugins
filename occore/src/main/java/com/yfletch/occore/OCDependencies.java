package com.yfletch.occore;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.ui.overlay.OverlayManager;

@Getter
@Singleton
public class OCDependencies
{
	@Inject private Client client;
	@Inject private ClientThread clientThread;
	@Inject private OverlayManager overlayManager;
}
