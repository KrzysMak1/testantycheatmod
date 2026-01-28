package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

public class SpiderModule extends BaseModule {

    private double climbSpeed = 0.2;

    public SpiderModule() {
        super("Spider", "Climbs walls when colliding", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        System.out.println("[Spider] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Spider] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (client.player.horizontalCollision) {
            Vec3d velocity = client.player.getVelocity();
            client.player.setVelocity(velocity.x, climbSpeed, velocity.z);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Spider: " + climbSpeed,
            5,
            65,
            0xAAFFAA,
            true
        );
    }

    public void setClimbSpeed(double climbSpeed) {
        this.climbSpeed = Math.max(0.05, Math.min(0.5, climbSpeed));
    }
}
