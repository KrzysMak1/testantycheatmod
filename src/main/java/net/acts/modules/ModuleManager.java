package net.acts.modules;

import net.acts.hud.ModuleStatusHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModuleManager {

    private final Map<String, BaseModule> modules = new LinkedHashMap<>();
    private final Map<BaseModule.Category, List<BaseModule>> byCategory =
        new EnumMap<>(BaseModule.Category.class);
    private final ModuleStatusHud statusHud = new ModuleStatusHud();

    public ModuleManager() {
        for (BaseModule.Category category : BaseModule.Category.values()) {
            byCategory.put(category, new ArrayList<>());
        }
    }

    public void registerModule(BaseModule module) {
        modules.put(module.getName().toLowerCase(), module);
        byCategory.get(module.getCategory()).add(module);
    }

    public Optional<BaseModule> getModule(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(modules.get(name.toLowerCase()));
    }

    public Collection<BaseModule> getAllModules() {
        return modules.values();
    }

    public List<BaseModule> getModulesByCategory(BaseModule.Category category) {
        return new ArrayList<>(byCategory.getOrDefault(category, List.of()));
    }

    public void enableAll() {
        modules.values().forEach(module -> {
            if (!module.isEnabled()) {
                module.toggle();
            }
        });
    }

    public void disableAll() {
        modules.values().forEach(module -> {
            if (module.isEnabled()) {
                module.toggle();
            }
        });
    }

    public void onTick(MinecraftClient client) {
        modules.values().forEach(module -> {
            if (module.isEnabled()) {
                module.onTick(client);
            }
        });
    }

    public void onRender(DrawContext context, float tickDelta) {
        modules.values().forEach(module -> {
            if (module.isEnabled()) {
                module.onRender(context, tickDelta);
            }
        });
        statusHud.render(context, tickDelta, getEnabledModules());
    }

    private List<BaseModule> getEnabledModules() {
        List<BaseModule> enabled = new ArrayList<>();
        for (BaseModule module : modules.values()) {
            if (module.isEnabled()) {
                enabled.add(module);
            }
        }
        return enabled;
    }
}
