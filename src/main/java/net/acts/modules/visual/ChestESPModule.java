package net.acts.modules.visual;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ChestESPModule extends BaseModule {

    private int scanRadius = 12;
    private boolean showTrapped = true;

    public ChestESPModule() {
        super("ChestESP", "Highlights chests in range", Category.VISUAL);
        setDisplayColor(0xFFCC88);
    }

    @Override
    public void onEnable() {
        System.out.println("[ChestESP] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[ChestESP] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        System.out.println("[ChestESP] Scanning radius " + scanRadius + (showTrapped ? " (incl. trapped)" : ""));
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "ChestESP: radius " + scanRadius,
            5,
            53,
            0xFFCC88,
            true
        );
    }

    public void setScanRadius(int scanRadius) {
        this.scanRadius = Math.max(4, Math.min(32, scanRadius));
    }

    public void setShowTrapped(boolean showTrapped) {
        this.showTrapped = showTrapped;
    }
}
