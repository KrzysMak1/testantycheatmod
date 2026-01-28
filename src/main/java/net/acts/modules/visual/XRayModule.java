package net.acts.modules.visual;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class XRayModule extends BaseModule {

    private int highlightRadius = 16;

    public XRayModule() {
        super("XRay", "Highlights ores for testing visibility", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        System.out.println("[XRay] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[XRay] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        System.out.println("[XRay] Scanning radius " + highlightRadius);
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "XRay: radius " + highlightRadius,
            5,
            5,
            0xAAFFEE,
            true
        );
    }

    public void setHighlightRadius(int highlightRadius) {
        this.highlightRadius = Math.max(4, Math.min(64, highlightRadius));
    }
}
