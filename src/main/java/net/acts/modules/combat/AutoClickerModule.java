package net.acts.modules.combat;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoClickerModule extends BaseModule {

    private int cps = 10;
    private long lastClick = 0;
    private Mode mode = Mode.CONSTANT;
    private boolean onlyOnEntity = false;
    private int jitterMs = 0;

    public AutoClickerModule() {
        super("AutoClicker", "Performs timed clicks for CPS testing", Category.COMBAT);
        setDisplayColor(0xFFFFFF);
    }

    public enum Mode {
        CONSTANT,
        RANDOM,
        BURST
    }

    @Override
    public void onEnable() {
        System.out.println("[AutoClicker] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[AutoClicker] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) {
            return;
        }
        if (!client.options.attackKey.isPressed()) {
            return;
        }
        long now = System.currentTimeMillis();
        long interval = Math.max(1, 1000 / getCurrentCps());
        if (jitterMs > 0) {
            interval += (int) (Math.random() * jitterMs);
        }
        if (now - lastClick < interval) {
            return;
        }
        HitResult hit = client.crosshairTarget;
        if (onlyOnEntity && !(hit instanceof EntityHitResult)) {
            return;
        }
        if (hit instanceof EntityHitResult entityHit) {
            client.interactionManager.attackEntity(client.player, entityHit.getEntity());
        }
        client.player.swingHand(Hand.MAIN_HAND);
        lastClick = now;
    }

    private int getCurrentCps() {
        switch (mode) {
            case RANDOM:
                return Math.max(1, cps + (int) (Math.random() * 4) - 2);
            case BURST:
                return (System.currentTimeMillis() / 250) % 2 == 0 ? cps * 2 : Math.max(1, cps / 2);
            case CONSTANT:
            default:
                return cps;
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "AutoClicker: " + getCurrentCps() + " CPS",
            5,
            5,
            0xFFFFFF,
            true
        );
    }

    public void setCps(int cps) {
        this.cps = Math.max(1, Math.min(20, cps));
    }

    public void setMode(Mode mode) {
        if (mode != null) {
            this.mode = mode;
        }
    }

    public void setOnlyOnEntity(boolean onlyOnEntity) {
        this.onlyOnEntity = onlyOnEntity;
    }

    public void setJitterMs(int jitterMs) {
        this.jitterMs = Math.max(0, Math.min(30, jitterMs));
    }
}
