package net.acts.hud;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class ModuleStatusHud {

    private int x = 8;
    private int y = 8;
    private int padding = 6;
    private int lineHeight = 11;
    private boolean showCategoryHeaders = true;
    private boolean showBackground = true;

    public void render(DrawContext context, float tickDelta, List<BaseModule> enabledModules) {
        List<BaseModule> hudModules = enabledModules.stream()
            .filter(BaseModule::isShowOnHud)
            .toList();
        if (hudModules.isEmpty()) {
            return;
        }
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        List<Line> lines = new java.util.ArrayList<>();
        lines.add(new Line("Active Modules", 0xFFFFFF));
        if (showCategoryHeaders) {
            BaseModule.Category current = null;
            for (BaseModule module : hudModules) {
                if (current != module.getCategory()) {
                    current = module.getCategory();
                    lines.add(new Line("[" + current.getDisplayName() + "]", 0xFFCC66));
                }
                lines.add(new Line("• " + module.getName(), module.getDisplayColor()));
            }
        } else {
            hudModules.forEach(module ->
                lines.add(new Line("• " + module.getName(), module.getDisplayColor()))
            );
        }
        int maxWidth = lines.stream().mapToInt(line -> textRenderer.getWidth(line.text)).max().orElse(0);
        int height = lines.size() * lineHeight + padding * 2;
        int width = maxWidth + padding * 2;
        if (showBackground) {
            context.fill(x - padding, y - padding, x - padding + width, y - padding + height, 0x88000000);
        }
        int lineY = y;
        for (Line line : lines) {
            context.drawText(textRenderer, line.text, x, lineY, line.color, true);
            lineY += lineHeight;
        }
    }

    public void setPosition(int x, int y) {
        this.x = Math.max(0, x);
        this.y = Math.max(0, y);
    }

    public void setPadding(int padding) {
        this.padding = Math.max(2, padding);
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = Math.max(8, lineHeight);
    }

    public void setShowCategoryHeaders(boolean showCategoryHeaders) {
        this.showCategoryHeaders = showCategoryHeaders;
    }

    public void setShowBackground(boolean showBackground) {
        this.showBackground = showBackground;
    }

    private record Line(String text, int color) {
    }
}
