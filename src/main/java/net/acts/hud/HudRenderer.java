package net.acts.hud;

import net.acts.modules.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class HudRenderer {

    private final ModuleManager moduleManager;

    public HudRenderer(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void render(DrawContext context, float tickDelta) {
        if (MinecraftClient.getInstance().player == null) {
            return;
        }
        moduleManager.onRender(context, tickDelta);
    }
}
