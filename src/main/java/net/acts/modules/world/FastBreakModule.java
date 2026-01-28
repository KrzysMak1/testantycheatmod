package net.acts.modules.world;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FastBreakModule extends BaseModule {

    private float breakMultiplier = 1.5f;
    private boolean onlyOnGround = false;

    public FastBreakModule() {
        super("FastBreak", "Accelerates block breaking for testing", Category.WORLD);
        setDisplayColor(0xFFAA55);
    }

    @Override
    public void onEnable() {
        System.out.println("[FastBreak] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[FastBreak] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) {
            return;
        }
        if (client.player.isBreakingBlock() && (!onlyOnGround || client.player.isOnGround())) {
            client.interactionManager.setBlockBreakingCooldown(Math.max(0, (int) (4 / breakMultiplier)));
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "FastBreak: x" + breakMultiplier,
            5,
            5,
            0xFFAA55,
            true
        );
    }

    public void setBreakMultiplier(float breakMultiplier) {
        this.breakMultiplier = Math.max(1.0f, Math.min(5.0f, breakMultiplier));
    }

    public void setOnlyOnGround(boolean onlyOnGround) {
        this.onlyOnGround = onlyOnGround;
    }
}
