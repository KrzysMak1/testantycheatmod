package net.acts.modules.inventory;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ChestStealerModule extends BaseModule {

    private int stealDelay = 2;
    private boolean randomizeOrder = false;

    public ChestStealerModule() {
        super("ChestStealer", "Quickly loots chest contents", Category.INVENTORY);
        setDisplayColor(0xCCCC55);
    }

    @Override
    public void onEnable() {
        System.out.println("[ChestStealer] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[ChestStealer] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        System.out.println("[ChestStealer] Steal delay " + stealDelay + " ticks"
            + (randomizeOrder ? " (random order)" : ""));
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "ChestStealer: " + stealDelay + "t",
            5,
            53,
            0xCCCC55,
            true
        );
    }

    public void setStealDelay(int stealDelay) {
        this.stealDelay = Math.max(0, Math.min(20, stealDelay));
    }

    public void setRandomizeOrder(boolean randomizeOrder) {
        this.randomizeOrder = randomizeOrder;
    }
}
