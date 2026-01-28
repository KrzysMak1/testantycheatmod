package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

public class SpeedModule extends BaseModule {

    private float speedMultiplier = 1.4f;
    private boolean autoSprint = true;
    private boolean boostStrafe = false;

    public SpeedModule() {
        super("Speed", "Scales movement speed for bypass testing", Category.MOVEMENT);
        setDisplayColor(0x55FF55);
    }

    @Override
    public void onEnable() {
        System.out.println("[Speed] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Speed] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (autoSprint) {
            client.player.setSprinting(true);
        }
        Vec3d velocity = client.player.getVelocity();
        if (client.player.input != null && (client.player.input.movementForward != 0 || client.player.input.movementSideways != 0)) {
            float forwardMultiplier = speedMultiplier;
            float strafeMultiplier = boostStrafe && client.player.input.movementSideways != 0 ? speedMultiplier * 1.15f : speedMultiplier;
            client.player.setVelocity(velocity.x * strafeMultiplier, velocity.y, velocity.z * forwardMultiplier);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Speed: x" + speedMultiplier,
            5,
            5,
            0x55FF55,
            true
        );
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = Math.max(1.0f, Math.min(3.0f, speedMultiplier));
    }

    public void setAutoSprint(boolean autoSprint) {
        this.autoSprint = autoSprint;
    }

    public void setBoostStrafe(boolean boostStrafe) {
        this.boostStrafe = boostStrafe;
    }
}
