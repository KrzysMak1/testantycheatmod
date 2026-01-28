package net.acts.modules.network;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class PacketRateMeterModule extends BaseModule {

    private long lastSampleTime = 0;
    private int packetsThisSecond = 0;
    private int lastRate = 0;
    private int sampleWindowMs = 1000;

    public PacketRateMeterModule() {
        super("PacketRateMeter", "Measures packets per second", Category.NETWORK);
        setDisplayColor(0x55AAFF);
    }

    @Override
    public void onEnable() {
        lastSampleTime = System.currentTimeMillis();
        packetsThisSecond = 0;
        System.out.println("[PacketRateMeter] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[PacketRateMeter] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        packetsThisSecond++;
        long now = System.currentTimeMillis();
        if (now - lastSampleTime >= sampleWindowMs) {
            lastRate = packetsThisSecond;
            packetsThisSecond = 0;
            lastSampleTime = now;
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "PacketRate: " + lastRate + "/s",
            5,
            17,
            0x55AAFF,
            true
        );
    }

    public void setSampleWindowMs(int sampleWindowMs) {
        this.sampleWindowMs = Math.max(250, Math.min(5000, sampleWindowMs));
    }
}
