package net.acts.modules.combat;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;

public class KillAuraModule extends BaseModule {

    private float range = 4.0f;
    private int attackDelayTicks = 10;
    private int lastAttackTick = 0;
    private boolean requireClick = true;
    private boolean requireLineOfSight = true;

    public KillAuraModule() {
        super("KillAura", "Targets nearby entities for test attacks", Category.COMBAT);
        setDisplayColor(0xFF6666);
    }

    @Override
    public void onEnable() {
        System.out.println("[KillAura] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[KillAura] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) {
            return;
        }
        if (requireClick && !client.options.attackKey.isPressed()) {
            return;
        }
        int tick = (int) (client.world.getTime() % Integer.MAX_VALUE);
        if (tick - lastAttackTick < attackDelayTicks) {
            return;
        }
        List<Entity> targets = client.world.getOtherEntities(client.player, getTargetBox(client));
        Entity target = targets.stream()
            .filter(entity -> entity instanceof LivingEntity)
            .filter(Entity::isAlive)
            .filter(entity -> !requireLineOfSight || client.player.canSee(entity))
            .min(Comparator.comparingDouble(entity -> client.player.squaredDistanceTo(entity)))
            .orElse(null);
        if (target == null) {
            return;
        }
        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
        lastAttackTick = tick;
    }

    private Box getTargetBox(MinecraftClient client) {
        return client.player.getBoundingBox().expand(range, range, range);
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "KillAura: " + range + "m",
            5,
            17,
            0xFF6666,
            true
        );
    }

    public void setRange(float range) {
        this.range = Math.max(2.0f, Math.min(6.0f, range));
    }

    public void setAttackDelayTicks(int attackDelayTicks) {
        this.attackDelayTicks = Math.max(1, Math.min(20, attackDelayTicks));
    }

    public void setRequireClick(boolean requireClick) {
        this.requireClick = requireClick;
    }

    public void setRequireLineOfSight(boolean requireLineOfSight) {
        this.requireLineOfSight = requireLineOfSight;
    }
}
