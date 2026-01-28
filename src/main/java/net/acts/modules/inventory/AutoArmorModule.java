package net.acts.modules.inventory;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class AutoArmorModule extends BaseModule {

    private boolean prioritizeProtection = true;

    public AutoArmorModule() {
        super("AutoArmor", "Equips best armor automatically", Category.INVENTORY);
    }

    @Override
    public void onEnable() {
        System.out.println("[AutoArmor] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[AutoArmor] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        System.out.println("[AutoArmor] Scanning armor, prioritize: " + prioritizeProtection);
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "AutoArmor: " + (prioritizeProtection ? "Protection" : "Durability"),
            5,
            17,
            0xCCCCCC,
            true
        );
    }

    public void setPrioritizeProtection(boolean prioritizeProtection) {
        this.prioritizeProtection = prioritizeProtection;
    }
}
