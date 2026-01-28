package net.acts.modules.visual;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class NameTagsModule extends BaseModule {

    private float scale = 1.2f;
    private boolean showDistance = false;

    public NameTagsModule() {
        super("NameTags", "Shows name tags through walls", Category.VISUAL);
        setDisplayColor(0xAAFFEE);
    }

    @Override
    public void onEnable() {
        System.out.println("[NameTags] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[NameTags] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.world == null) {
            return;
        }
        System.out.println("[NameTags] Rendering name tags x" + scale + (showDistance ? " with distance" : ""));
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "NameTags: x" + scale,
            5,
            41,
            0xAAFFEE,
            true
        );
    }

    public void setScale(float scale) {
        this.scale = Math.max(0.8f, Math.min(2.5f, scale));
    }

    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }
}
