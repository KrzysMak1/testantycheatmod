package net.acts.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class BaseModule {

    protected final String name;
    protected final String description;
    protected final Category category;
    protected boolean enabled;
    protected int displayColor = 0xFFFFFF;
    protected boolean showOnHud = true;

    public BaseModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        VISUAL("Visual"),
        WORLD("World"),
        INVENTORY("Inventory"),
        EXPLOIT("Exploit"),
        NETWORK("Network");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onTick(MinecraftClient client);

    public abstract void onRender(DrawContext context, float tickDelta);

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public int getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(int displayColor) {
        this.displayColor = displayColor;
    }

    public boolean isShowOnHud() {
        return showOnHud;
    }

    public void setShowOnHud(boolean showOnHud) {
        this.showOnHud = showOnHud;
    }
}
