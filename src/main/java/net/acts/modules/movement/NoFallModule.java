package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class NoFallModule extends BaseModule {

    private boolean resetVelocity = false;

    public NoFallModule() {
        super("NoFall", "Clears fall distance to test fall checks", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        System.out.println("[NoFall] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[NoFall] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        client.player.fallDistance = 0.0f;
        if (resetVelocity && client.player.getVelocity().y < -0.5) {
            client.player.setVelocity(client.player.getVelocity().x, -0.1, client.player.getVelocity().z);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "NoFall: active",
            5,
            29,
            0xFFD055,
            true
        );
    }

    public void setResetVelocity(boolean resetVelocity) {
        this.resetVelocity = resetVelocity;
    }
}
