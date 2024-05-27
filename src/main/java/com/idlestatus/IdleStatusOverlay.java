package com.idlestatus;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IdleStatusOverlay extends Overlay
{
    private final Client client;
    private final IdleStatusConfig config;

    private boolean isIdle = false;
    private int idleTicks = 0;
    private int idlePercentage = 0;

    private final PanelComponent panelComponent = new PanelComponent();
    private final List<String> texts = new ArrayList<>();

    @Inject
    public IdleStatusOverlay(Client client, IdleStatusConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    public void updateIdleStatus(boolean isIdle)
    {
        this.isIdle = isIdle;
    }

    public void updateIdleTime(int idleTicks)
    {
        this.idleTicks = idleTicks;
    }

    public void updateIdlePercentage(int idlePercentage)
    {
        this.idlePercentage = idlePercentage;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();
        texts.clear();

        if (!config.showIdleStatus() && !config.showIdleTimer() && !config.showIdlePercentage())
        {
            return null;
        }

        if (config.showIdleStatus())
        {
            String text = "Idle: " + (isIdle ? "Yes" : "No");
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(text)
                    .color(Color.WHITE)
                    .build());
            texts.add(text);
        }

        if (config.showIdleTimer())
        {
            String text = "Idle time: " + idleTicks;
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(text)
                    .color(Color.WHITE)
                    .build());
            texts.add(text);
        }

        if (config.showIdlePercentage())
        {
            String text = "Idle %: " + idlePercentage;
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(text)
                    .color(Color.WHITE)
                    .build());
            texts.add(text);
        }

        // Manually calculate the preferred size based on the longest text width
        Dimension preferredSize = calculatePreferredSize(graphics);
        panelComponent.setPreferredSize(preferredSize);

        return panelComponent.render(graphics);
    }

    private Dimension calculatePreferredSize(Graphics2D graphics)
    {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int width = 0;
        int height = 0;

        for (String text : texts)
        {
            width = Math.max(width, fontMetrics.stringWidth(text));
            height += fontMetrics.getHeight();
        }

        // Adding some padding around the text
        width += 10;
        height += 10;

        return new Dimension(width, height);
    }
}
