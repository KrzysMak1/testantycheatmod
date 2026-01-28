package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class StepModule extends BaseModule {

    private float stepHeight = 1.0f;

    public StepModule() {
        super("Step", "Increases step height for obstacle tests", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        System.out.println("[Step] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Step] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        client.player.stepHeight = stepHeight;
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Step: " + stepHeight,
            5,
            77,
            0xDDDD55,
            true
        );
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = Math.max(0.6f, Math.min(2.5f, stepHeight));
    }
}
