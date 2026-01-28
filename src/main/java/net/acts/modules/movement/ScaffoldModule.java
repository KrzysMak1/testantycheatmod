package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class ScaffoldModule extends BaseModule {

    private boolean towerMode = false;
    private int placeDelayTicks = 2;
    private long lastPlaceTick = 0;

    public ScaffoldModule() {
        super("Scaffold", "Assists with block placement under player", Category.MOVEMENT);
        setDisplayColor(0xFFFFFF);
    }

    @Override
    public void onEnable() {
        System.out.println("[Scaffold] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Scaffold] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }
        BlockPos below = client.player.getBlockPos().down();
        long tick = client.world.getTime();
        if (tick - lastPlaceTick < placeDelayTicks) {
            return;
        }
        if (client.world.getBlockState(below).isAir() && client.player.getMainHandStack().getItem() instanceof BlockItem) {
            if (towerMode && client.options.jumpKey.isPressed()) {
                client.player.addVelocity(0.0, 0.2, 0.0);
            }
            System.out.println("[Scaffold] Block placement hint at " + below);
            lastPlaceTick = tick;
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Scaffold: " + (towerMode ? "Tower" : "Normal"),
            5,
            89,
            0xFFFFFF,
            true
        );
    }

    public void setTowerMode(boolean towerMode) {
        this.towerMode = towerMode;
    }

    public void setPlaceDelayTicks(int placeDelayTicks) {
        this.placeDelayTicks = Math.max(0, Math.min(10, placeDelayTicks));
    }
}
