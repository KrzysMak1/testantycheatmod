package net.acts.modules.inventory;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class AutoSoupModule extends BaseModule {

    private float healthThreshold = 14.0f;

    public AutoSoupModule() {
        super("AutoSoup", "Auto-eats soup when health is low", Category.INVENTORY);
    }

    @Override
    public void onEnable() {
        System.out.println("[AutoSoup] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[AutoSoup] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (client.player.getHealth() <= healthThreshold) {
            System.out.println("[AutoSoup] Health low, would eat soup");
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "AutoSoup: " + healthThreshold + "hp",
            5,
            41,
            0xFFAA77,
            true
        );
    }

    public void setHealthThreshold(float healthThreshold) {
        this.healthThreshold = Math.max(1.0f, Math.min(20.0f, healthThreshold));
    }
}
