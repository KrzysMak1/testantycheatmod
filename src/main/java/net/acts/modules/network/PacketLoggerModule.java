package net.acts.modules.network;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class PacketLoggerModule extends BaseModule {

    private int logLimit = 50;
    private boolean logInbound = true;
    private boolean logOutbound = true;

    public PacketLoggerModule() {
        super("PacketLogger", "Logs recent packets for audit", Category.NETWORK);
        setDisplayColor(0xAAAAFF);
    }

    @Override
    public void onEnable() {
        System.out.println("[PacketLogger] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[PacketLogger] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.getNetworkHandler() == null) {
            return;
        }
        System.out.println("[PacketLogger] Logging up to " + logLimit + " packets (inbound "
            + logInbound + ", outbound " + logOutbound + ")");
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "PacketLogger: limit " + logLimit,
            5,
            5,
            0xAAAAFF,
            true
        );
    }

    public void setLogLimit(int logLimit) {
        this.logLimit = Math.max(10, Math.min(500, logLimit));
    }

    public void setLogInbound(boolean logInbound) {
        this.logInbound = logInbound;
    }

    public void setLogOutbound(boolean logOutbound) {
        this.logOutbound = logOutbound;
    }
}
