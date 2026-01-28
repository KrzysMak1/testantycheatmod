package net.acts.modules.movement;

import net.acts.modules.BaseModule;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

public class JesusModule extends BaseModule {

    private boolean lavaWalk = false;
    private boolean bobbing = false;

    public JesusModule() {
        super("Jesus", "Keeps player afloat on liquids", Category.MOVEMENT);
        setDisplayColor(0x55FFFF);
    }

    @Override
    public void onEnable() {
        System.out.println("[Jesus] Enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("[Jesus] Disabled");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }
        BlockPos below = client.player.getBlockPos().down();
        boolean water = client.world.getBlockState(below).isOf(Blocks.WATER);
        boolean lava = client.world.getBlockState(below).isOf(Blocks.LAVA);
        if (water || (lavaWalk && lava)) {
            double offset = bobbing ? (Math.sin(System.currentTimeMillis() / 150.0) * 0.02) : 0.0;
            client.player.setVelocity(client.player.getVelocity().x, 0.1 + offset, client.player.getVelocity().z);
            client.player.setOnGround(true);
        }
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        if (!isEnabled()) {
            return;
        }
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            "Jesus: " + (lavaWalk ? "Water+Lava" : "Water"),
            5,
            53,
            0x55FFFF,
            true
        );
    }

    public void setLavaWalk(boolean lavaWalk) {
        this.lavaWalk = lavaWalk;
    }

    public void setBobbing(boolean bobbing) {
        this.bobbing = bobbing;
    }
}
