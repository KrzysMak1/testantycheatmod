package net.acts.modules.inventory;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class InventoryManagerModule extends BaseModule {

    private boolean autoSort = true;
    private boolean autoDropJunk = false;

    public InventoryManagerModule() {
        super("InventoryManager", "Sorts and cleans inventory", Category.INVENTORY);
        setDisplayColor(0x88CCFF);
    }

    @Override
    public void onEnable() {
        System.out.println("[InventoryManager] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[InventoryManager] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (autoSort || autoDropJunk) {
            System.out.println("[InventoryManager] Auto-sort " + autoSort + ", auto-drop " + autoDropJunk);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "InventoryManager: " + (autoSort ? "Auto" : "Manual"),
            5,
            65,
            0x88CCFF,
            true
        );
    }

    public void setAutoSort(boolean autoSort) {
        this.autoSort = autoSort;
    }

    public void setAutoDropJunk(boolean autoDropJunk) {
        this.autoDropJunk = autoDropJunk;
    }
}
