package net.acts;

import net.acts.hud.HudRenderer;
import net.acts.modules.BaseModule;
import net.acts.modules.ModuleManager;
import net.acts.modules.combat.AimAssistModule;
import net.acts.modules.combat.AutoClickerModule;
import net.acts.modules.combat.KillAuraModule;
import net.acts.modules.combat.ReachDisplayModule;
import net.acts.modules.combat.VelocityModule;
import net.acts.modules.exploit.CrasherModule;
import net.acts.modules.exploit.DisablerModule;
import net.acts.modules.inventory.AutoArmorModule;
import net.acts.modules.inventory.AutoPotModule;
import net.acts.modules.inventory.AutoSoupModule;
import net.acts.modules.inventory.AutoTotemModule;
import net.acts.modules.inventory.ChestStealerModule;
import net.acts.modules.inventory.InventoryManagerModule;
import net.acts.modules.movement.FlyModule;
import net.acts.modules.movement.JesusModule;
import net.acts.modules.movement.NoFallModule;
import net.acts.modules.movement.NoSlowModule;
import net.acts.modules.movement.ScaffoldModule;
import net.acts.modules.movement.SpeedModule;
import net.acts.modules.movement.SpiderModule;
import net.acts.modules.movement.StepModule;
import net.acts.modules.network.PacketLoggerModule;
import net.acts.modules.network.PacketRateMeterModule;
import net.acts.modules.visual.ChestESPModule;
import net.acts.modules.visual.ESPModule;
import net.acts.modules.visual.FullbrightModule;
import net.acts.modules.visual.NameTagsModule;
import net.acts.modules.visual.TracersModule;
import net.acts.modules.visual.XRayModule;
import net.acts.modules.world.FastBreakModule;
import net.acts.modules.world.TimerModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ACTestMod implements ClientModInitializer {

    public static final String MOD_ID = "anticheattestsuite";

    private final ModuleManager moduleManager = new ModuleManager();
    private final HudRenderer hudRenderer = new HudRenderer(moduleManager);
    private final List<BaseModule> moduleList = new ArrayList<>();

    private KeyBinding nextModuleKey;
    private KeyBinding prevModuleKey;
    private KeyBinding toggleSelectedKey;
    private KeyBinding enableAllKey;
    private KeyBinding disableAllKey;
    private KeyBinding toggleHudKey;

    private int selectedIndex = 0;
    private boolean hudEnabled = true;

    @Override
    public void onInitializeClient() {
        registerModules();
        setupKeybinds();
        refreshModuleList();

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (hudEnabled) {
                hudRenderer.render(context, tickDelta);
            }
        });
    }

    private void registerModules() {
        moduleManager.registerModule(new AutoClickerModule());
        moduleManager.registerModule(new AimAssistModule());
        moduleManager.registerModule(new ReachDisplayModule());
        moduleManager.registerModule(new VelocityModule());
        moduleManager.registerModule(new KillAuraModule());

        moduleManager.registerModule(new SpeedModule());
        moduleManager.registerModule(new FlyModule());
        moduleManager.registerModule(new NoFallModule());
        moduleManager.registerModule(new NoSlowModule());
        moduleManager.registerModule(new JesusModule());
        moduleManager.registerModule(new SpiderModule());
        moduleManager.registerModule(new StepModule());
        moduleManager.registerModule(new ScaffoldModule());

        moduleManager.registerModule(new FastBreakModule());
        moduleManager.registerModule(new TimerModule());

        moduleManager.registerModule(new XRayModule());
        moduleManager.registerModule(new ESPModule());
        moduleManager.registerModule(new FullbrightModule());
        moduleManager.registerModule(new NameTagsModule());
        moduleManager.registerModule(new ChestESPModule());
        moduleManager.registerModule(new TracersModule());

        moduleManager.registerModule(new AutoTotemModule());
        moduleManager.registerModule(new AutoArmorModule());
        moduleManager.registerModule(new AutoPotModule());
        moduleManager.registerModule(new AutoSoupModule());
        moduleManager.registerModule(new ChestStealerModule());
        moduleManager.registerModule(new InventoryManagerModule());

        moduleManager.registerModule(new DisablerModule());
        moduleManager.registerModule(new CrasherModule());

        moduleManager.registerModule(new PacketLoggerModule());
        moduleManager.registerModule(new PacketRateMeterModule());
    }

    private void setupKeybinds() {
        nextModuleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.actests.next_module",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_PAGE_DOWN,
            "category.actests"
        ));
        prevModuleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.actests.prev_module",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_PAGE_UP,
            "category.actests"
        ));
        toggleSelectedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.actests.toggle_selected",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_INSERT,
            "category.actests"
        ));
        enableAllKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.actests.enable_all",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F6,
            "category.actests"
        ));
        disableAllKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.actests.disable_all",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            "category.actests"
        ));
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.actests.toggle_hud",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.actests"
        ));
    }

    private void refreshModuleList() {
        moduleList.clear();
        moduleList.addAll(moduleManager.getAllModules());
        moduleList.sort(Comparator.comparing(BaseModule::getName));
        selectedIndex = Math.min(selectedIndex, Math.max(0, moduleList.size() - 1));
        updateSelectedModuleHud();
    }

    private void onClientTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        while (nextModuleKey.wasPressed()) {
            cycleModule(1, client);
        }
        while (prevModuleKey.wasPressed()) {
            cycleModule(-1, client);
        }
        while (toggleSelectedKey.wasPressed()) {
            toggleSelected(client);
        }
        while (enableAllKey.wasPressed()) {
            moduleManager.enableAll();
            client.player.sendMessage(Text.of("[ACTests] Enabled all modules"), false);
        }
        while (disableAllKey.wasPressed()) {
            moduleManager.disableAll();
            client.player.sendMessage(Text.of("[ACTests] Disabled all modules"), false);
        }
        while (toggleHudKey.wasPressed()) {
            hudEnabled = !hudEnabled;
            client.player.sendMessage(Text.of("[ACTests] HUD " + (hudEnabled ? "enabled" : "disabled")), false);
        }

        moduleManager.onTick(client);
    }

    private void cycleModule(int delta, MinecraftClient client) {
        if (moduleList.isEmpty()) {
            return;
        }
        selectedIndex = (selectedIndex + delta + moduleList.size()) % moduleList.size();
        BaseModule module = moduleList.get(selectedIndex);
        updateSelectedModuleHud();
        client.player.sendMessage(Text.of("[ACTests] Selected " + module.getName()), false);
    }

    private void toggleSelected(MinecraftClient client) {
        if (moduleList.isEmpty()) {
            return;
        }
        BaseModule module = moduleList.get(selectedIndex);
        module.toggle();
        client.player.sendMessage(Text.of("[ACTests] " + module.getName() + " "
            + (module.isEnabled() ? "enabled" : "disabled")), false);
    }

    private void updateSelectedModuleHud() {
        if (moduleList.isEmpty()) {
            moduleManager.setSelectedModuleName(null);
            return;
        }
        moduleManager.setSelectedModuleName(moduleList.get(selectedIndex).getName());
    }
}
