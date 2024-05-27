package com.idlestatus;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Arrays;
import java.util.List;

@PluginDescriptor(
		name = "Idle Status",
		description = "Shows current idle status, how long you've idled (in ticks), and percentage over the login session.",
		tags = {"idle", "status", "timer", "tracking"},
		enabledByDefault = true
)
public class IdleStatusPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private IdleStatusOverlay overlay;

	@Inject
	private IdleStatusConfig config;

	private boolean isIdle = false;
	private int idleTicks = 0;
	private int totalIdleTicks = 0;
	private int totalTicks = 0;

	private static final List<Integer> idlePoses = Arrays.asList(808, 813, 3418, 10075);

	@Provides
	IdleStatusConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(IdleStatusConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Player player = client.getLocalPlayer();
		if (player == null) {
			return;
		}

		boolean wasIdle = isIdle;
		isIdle = player.getAnimation() == -1 && idlePoses.contains(player.getPoseAnimation());

		if (isIdle) {
			idleTicks++;
			totalIdleTicks++;
		} else if (wasIdle) {
			idleTicks = 0;
		}
		totalTicks++;

		overlay.updateIdleStatus(isIdle);
		overlay.updateIdleTime(idleTicks);
		overlay.updateIdlePercentage((int)((double) totalIdleTicks / totalTicks * 100));
	}
}
