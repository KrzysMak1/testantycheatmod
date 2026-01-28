package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class NoSlowModule extends BaseModule {

    private float useItemMultiplier = 1.2f;

    public NoSlowModule() {
        super("NoSlow", "Offsets slowdown from items and effects", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        System.out.println("[NoSlow] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[NoSlow] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (client.player.isUsingItem() || client.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            Vec3d velocity = client.player.getVelocity();
            client.player.setVelocity(velocity.x * useItemMultiplier, velocity.y, velocity.z * useItemMultiplier);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "NoSlow: x" + useItemMultiplier,
            5,
            41,
            0x99FF55,
            true
        );
    }

    public void setUseItemMultiplier(float useItemMultiplier) {
        this.useItemMultiplier = Math.max(1.0f, Math.min(2.5f, useItemMultiplier));
    }
}
