package com.idlestatus;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("idlestatus")
public interface IdleStatusConfig extends Config
{
	@ConfigItem(
			keyName = "showIdleStatus",
			name = "Show Idle Status",
			description = "Display whether you are idle or not"
	)
	default boolean showIdleStatus()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showIdleTimer",
			name = "Show Idle Timer",
			description = "Display the idle time in ticks"
	)
	default boolean showIdleTimer()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showIdlePercentage",
			name = "Show Idle Percentage",
			description = "Display the percentage of time spent idling over the login session"
	)
	default boolean showIdlePercentage()
	{
		return true;
	}
}
