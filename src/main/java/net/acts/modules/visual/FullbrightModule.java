package net.acts.modules.visual;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FullbrightModule extends BaseModule {

    private double previousGamma = 1.0;

    public FullbrightModule() {
        super("Fullbright", "Boosts gamma for full brightness", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        System.out.println("[Fullbright] Enabled");
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            previousGamma = client.options.getGamma().getValue();
            client.options.getGamma().setValue(16.0);
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
            client.options.getGamma().setValue(16.0);
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
}
