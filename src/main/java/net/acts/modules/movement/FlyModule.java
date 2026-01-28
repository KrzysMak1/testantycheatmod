package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FlyModule extends BaseModule {

    private float flySpeed = 0.05f;
    private boolean airJump = false;
    private boolean glide = false;

    public FlyModule() {
        super("Fly", "Enables client-side flight controls", Category.MOVEMENT);
        setDisplayColor(0x55AAFF);
    }

    @Override
    public void onEnable() {
        System.out.println("[Fly] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Fly] Disabled");
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.getAbilities().allowFlying = false;
            client.player.getAbilities().flying = false;
        }
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        client.player.getAbilities().allowFlying = true;
        client.player.getAbilities().flySpeed = flySpeed;
        if (airJump && client.options.jumpKey.wasPressed()) {
            client.player.addVelocity(0.0, 0.2, 0.0);
        }
        if (glide && !client.player.isOnGround() && client.player.getVelocity().y < 0) {
            client.player.setVelocity(client.player.getVelocity().x, -0.02, client.player.getVelocity().z);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Fly: speed " + flySpeed,
            5,
            17,
            0x55AAFF,
            true
        );
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = Math.max(0.02f, Math.min(0.2f, flySpeed));
    }

    public void setAirJump(boolean airJump) {
        this.airJump = airJump;
    }

    public void setGlide(boolean glide) {
        this.glide = glide;
    }
}
