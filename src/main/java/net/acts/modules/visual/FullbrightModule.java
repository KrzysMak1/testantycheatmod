package net.acts.modules.visual;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FullbrightModule extends BaseModule {

    private double previousGamma = 1.0;
    private double targetGamma = 16.0;

    public FullbrightModule() {
        super("Fullbright", "Boosts gamma for full brightness", Category.VISUAL);
        setDisplayColor(0xFFFFAA);
    }

    @Override
    public void onEnable() {
        System.out.println("[Fullbright] Enabled");
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            previousGamma = client.options.getGamma().getValue();
            client.options.getGamma().setValue(targetGamma);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[Fullbright] Disabled");
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            client.options.getGamma().setValue(previousGamma);
        }
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.options != null) {
            client.options.getGamma().setValue(targetGamma);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Fullbright: active",
            5,
            29,
            0xFFFFAA,
            true
        );
    }

    public void setTargetGamma(double targetGamma) {
        this.targetGamma = Math.max(1.0, Math.min(20.0, targetGamma));
    }
}
