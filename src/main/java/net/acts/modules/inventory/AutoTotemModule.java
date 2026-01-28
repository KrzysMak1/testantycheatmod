package net.acts.modules.inventory;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;

public class AutoTotemModule extends BaseModule {

    private boolean swapToOffhand = true;

    public AutoTotemModule() {
        super("AutoTotem", "Keeps a totem in offhand", Category.INVENTORY);
    }

    @Override
    public void onEnable() {
        System.out.println("[AutoTotem] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[AutoTotem] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        boolean hasTotem = client.player.getInventory().contains(Items.TOTEM_OF_UNDYING.getDefaultStack());
        if (swapToOffhand && hasTotem) {
            System.out.println("[AutoTotem] Totem available");
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "AutoTotem: " + (swapToOffhand ? "Offhand" : "Notify"),
            5,
            5,
            0xFFD700,
            true
        );
    }

    public void setSwapToOffhand(boolean swapToOffhand) {
        this.swapToOffhand = swapToOffhand;
    }
}
