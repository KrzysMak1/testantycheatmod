package net.acts.modules.combat;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class AimAssistModule extends BaseModule {

    private float range = 4.5f;
    private float fov = 30.0f;
    private float strength = 0.35f;
    private boolean silent = false;
    private boolean requireClick = true;
    private boolean playersOnly = false;

    public AimAssistModule() {
        super("AimAssist", "Gently guides aim toward nearby targets", Category.COMBAT);
        setDisplayColor(0x66CCFF);
    }

    @Override
    public void onEnable() {
        System.out.println("[AimAssist] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[AimAssist] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }
        if (requireClick && !client.options.attackKey.isPressed()) {
            return;
        }

        Entity target = client.world.getEntities().stream()
            .filter(entity -> entity instanceof LivingEntity)
            .filter(entity -> entity != client.player)
            .filter(Entity::isAlive)
            .filter(entity -> !playersOnly || entity instanceof PlayerEntity)
            .filter(entity -> client.player.squaredDistanceTo(entity) <= range * range)
            .min(Comparator.comparingDouble(entity -> client.player.squaredDistanceTo(entity)))
            .orElse(null);

        if (target == null) {
            return;
        }

        Vec3d targetPos = target.getPos().add(0, target.getEyeHeight(target.getPose()) * 0.5, 0);
        Vec3d playerPos = client.player.getEyePos();
        Vec3d direction = targetPos.subtract(playerPos).normalize();

        double yaw = Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0;
        double pitch = -Math.toDegrees(Math.asin(direction.y));

        float currentYaw = client.player.getYaw();
        float currentPitch = client.player.getPitch();

        float yawDelta = wrapDegrees((float) (yaw - currentYaw));
        float pitchDelta = (float) (pitch - currentPitch);

        if (Math.abs(yawDelta) > fov) {
            return;
        }

        float newYaw = currentYaw + yawDelta * strength;
        float newPitch = currentPitch + pitchDelta * strength;

        if (!silent) {
            client.player.setYaw(newYaw);
            client.player.setPitch(newPitch);
        }
    }

    private float wrapDegrees(float degrees) {
        float value = degrees % 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "AimAssist: " + range + "m FOV " + fov,
            5,
            5,
            0x66CCFF,
            true
        );
    }

    public void setRange(float range) {
        this.range = Math.max(2.0f, Math.min(6.0f, range));
    }

    public void setFov(float fov) {
        this.fov = Math.max(5.0f, Math.min(180.0f, fov));
    }

    public void setStrength(float strength) {
        this.strength = Math.max(0.05f, Math.min(1.0f, strength));
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public void setRequireClick(boolean requireClick) {
        this.requireClick = requireClick;
    }

    public void setPlayersOnly(boolean playersOnly) {
        this.playersOnly = playersOnly;
    }
}
