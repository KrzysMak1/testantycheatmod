package net.acts.modules.visual;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ESPModule extends BaseModule {

    private boolean showHealthBars = true;

    public ESPModule() {
        super("ESP", "Displays entity outlines and stats", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        System.out.println("[ESP] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[ESP] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.world == null) {
            return;
        }
        System.out.println("[ESP] Tracking entities: " + client.world.getEntities().size());
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "ESP: " + (showHealthBars ? "Health" : "Boxes"),
            5,
            17,
            0xFF99FF,
            true
        );
    }

    public void setShowHealthBars(boolean showHealthBars) {
        this.showHealthBars = showHealthBars;
    }
}
