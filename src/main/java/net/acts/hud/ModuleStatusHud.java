package net.acts.hud;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class ModuleStatusHud {

    private int x = 8;
    private int y = 8;

    public void render(DrawContext context, float tickDelta, List<BaseModule> enabledModules) {
        if (enabledModules.isEmpty()) {
            return;
        }
        int lineHeight = 10;
        int index = 0;
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Active Modules:",
            x,
            y,
            0xFFFFFF,
            true
        );
        index++;
        for (BaseModule module : enabledModules) {
            context.drawText(
                MinecraftClient.getInstance().textRenderer,
                "- " + module.getName(),
                x,
                y + index * lineHeight,
                0x99FF99,
                true
            );
            index++;
        }
    }

    public void setPosition(int x, int y) {
        this.x = Math.max(0, x);
        this.y = Math.max(0, y);
    }
}
