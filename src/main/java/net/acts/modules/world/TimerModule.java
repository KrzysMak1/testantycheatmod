package net.acts.modules.world;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class TimerModule extends BaseModule {

    private float timerMultiplier = 1.0f;
    private boolean pulse = false;
    private int pulseInterval = 40;

    public TimerModule() {
        super("Timer", "Adjusts client tick speed for testing", Category.WORLD);
        setDisplayColor(0xFFCC55);
    }

    @Override
    public void onEnable() {
        System.out.println("[Timer] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Timer] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client == null) {
            return;
        }
        if (pulse && client.world != null && client.world.getTime() % pulseInterval == 0) {
            System.out.println("[Timer] Pulse tick x" + timerMultiplier);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Timer: x" + timerMultiplier,
            5,
            17,
            0xFFCC55,
            true
        );
    }

    public void setTimerMultiplier(float timerMultiplier) {
        this.timerMultiplier = Math.max(0.5f, Math.min(2.0f, timerMultiplier));
    }

    public void setPulse(boolean pulse) {
        this.pulse = pulse;
    }

    public void setPulseInterval(int pulseInterval) {
        this.pulseInterval = Math.max(10, Math.min(200, pulseInterval));
    }
}
