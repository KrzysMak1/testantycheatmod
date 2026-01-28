package net.acts.modules.visual;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class TracersModule extends BaseModule {

    private boolean showPlayersOnly = true;
    private boolean showThroughWalls = true;

    public TracersModule() {
        super("Tracers", "Draws tracers to entities", Category.VISUAL);
        setDisplayColor(0xAA88FF);
    }

    @Override
    public void onEnable() {
        System.out.println("[Tracers] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Tracers] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.world == null) {
            return;
        }
        System.out.println("[Tracers] Active " + (showPlayersOnly ? "Players" : "All")
            + (showThroughWalls ? " (through walls)" : ""));
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Tracers: " + (showPlayersOnly ? "Players" : "All"),
            5,
            65,
            0xAA88FF,
            true
        );
    }

    public void setShowPlayersOnly(boolean showPlayersOnly) {
        this.showPlayersOnly = showPlayersOnly;
    }

    public void setShowThroughWalls(boolean showThroughWalls) {
        this.showThroughWalls = showThroughWalls;
    }
}
