package net.acts.modules.inventory;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class AutoPotModule extends BaseModule {

    private float healthThreshold = 12.0f;
    private int useCooldownTicks = 20;

    public AutoPotModule() {
        super("AutoPot", "Auto-uses splash or drinkable potions", Category.INVENTORY);
        setDisplayColor(0xFF77AA);
    }

    @Override
    public void onEnable() {
        System.out.println("[AutoPot] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[AutoPot] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (client.player.getHealth() <= healthThreshold) {
            System.out.println("[AutoPot] Health low, would use potion (cooldown " + useCooldownTicks + ")");
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "AutoPot: " + healthThreshold + "hp",
            5,
            29,
            0xFF77AA,
            true
        );
    }

    public void setHealthThreshold(float healthThreshold) {
        this.healthThreshold = Math.max(1.0f, Math.min(20.0f, healthThreshold));
    }

    public void setUseCooldownTicks(int useCooldownTicks) {
        this.useCooldownTicks = Math.max(0, Math.min(100, useCooldownTicks));
    }
}
