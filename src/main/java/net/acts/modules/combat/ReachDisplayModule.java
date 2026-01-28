package net.acts.modules.combat;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class ReachDisplayModule extends BaseModule {

    private double lastReach = 0.0;
    private boolean warnOnReach = true;
    private double warningThreshold = 3.2;

    public ReachDisplayModule() {
        super("ReachDisplay", "Displays current reach distance and warnings", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        System.out.println("[ReachDisplay] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[ReachDisplay] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        HitResult hit = client.crosshairTarget;
        if (hit instanceof EntityHitResult entityHit) {
            lastReach = client.player.getPos().distanceTo(entityHit.getEntity().getPos());
            if (warnOnReach && lastReach > warningThreshold) {
                System.out.println("[ReachDisplay] Reach warning: " + lastReach);
            }
        } else {
            lastReach = 0.0;
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        String text = lastReach > 0.0 ? String.format("Reach: %.2f", lastReach) : "Reach: --";
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            text,
            5,
            17,
            lastReach > warningThreshold ? 0xFF5555 : 0xFFFFFF,
            true
        );
    }

    public void setWarningThreshold(double warningThreshold) {
        this.warningThreshold = Math.max(2.5, warningThreshold);
    }

    public void setWarnOnReach(boolean warnOnReach) {
        this.warnOnReach = warnOnReach;
    }
}
