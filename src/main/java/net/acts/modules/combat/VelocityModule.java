package net.acts.modules.combat;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class VelocityModule extends BaseModule {

    private float horizontal = 0.6f;
    private float vertical = 0.8f;
    private boolean onlyWhenSprinting = false;

    public VelocityModule() {
        super("Velocity", "Scales knockback for testing", Category.COMBAT);
        setDisplayColor(0xFFAAAA);
    }

    @Override
    public void onEnable() {
        System.out.println("[Velocity] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Velocity] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        if (client.player.hurtTime > 0 && (!onlyWhenSprinting || client.player.isSprinting())) {
            client.player.setVelocity(
                client.player.getVelocity().x * horizontal,
                client.player.getVelocity().y * vertical,
                client.player.getVelocity().z * horizontal
            );
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Velocity: H " + horizontal + " V " + vertical,
            5,
            29,
            0xFFAAAA,
            true
        );
    }

    public void setHorizontal(float horizontal) {
        this.horizontal = Math.max(0.0f, Math.min(1.0f, horizontal));
    }

    public void setVertical(float vertical) {
        this.vertical = Math.max(0.0f, Math.min(1.0f, vertical));
    }

    public void setOnlyWhenSprinting(boolean onlyWhenSprinting) {
        this.onlyWhenSprinting = onlyWhenSprinting;
    }
}
