package com.teren.tclient.client.module;

import com.mojang.blaze3d.platform.InputConstants;
import com.teren.tclient.client.module.modules.combat.KillAura;
import com.teren.tclient.client.module.modules.misc.AutoRespawn;
import com.teren.tclient.client.module.modules.movement.AutoSprint;
import com.teren.tclient.client.module.modules.movement.AutoWalk;
import com.teren.tclient.client.module.modules.movement.Flight;
import com.teren.tclient.client.module.modules.movement.Speed;
import com.teren.tclient.client.module.modules.movement.Step;
import com.teren.tclient.client.module.modules.render.FullBright;
import com.teren.tclient.client.screen.ClickGuiScreen;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 模块管理器：注册、存储、运行所有模块。全局只有一个（单例）。
public class ModuleManager {

    private static final ModuleManager INSTANCE = new ModuleManager();
    public static ModuleManager getInstance() { return INSTANCE; }

    private final List<Module> modules = new ArrayList<>();
    private final Set<Integer> heldKeys = new HashSet<>(); // 当前正按住的键

    // 注册所有模块。以后加新模块，就在这里加一行。
    public void init() {
        modules.add(new FullBright());
        modules.add(new AutoSprint());
        modules.add(new Speed());
        modules.add(new Flight());
        modules.add(new Step());
        modules.add(new AutoWalk());
        modules.add(new KillAura());
        modules.add(new AutoRespawn());
    }

    public List<Module> getModules() {
        return modules;
    }

    // 按名字找模块（不区分大小写）
    public Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    // 每个游戏刻调用：让所有已开启的模块执行一次。
    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }

    // 每个游戏刻调用：检测按键。
    public void onKey() {
        Minecraft mc = Minecraft.getInstance();
        // 打开了界面（聊天框、菜单等）时不响应按键
        if (mc.screen != null) return;

        long window = mc.getWindow().getWindow();

        // 右 Shift：打开 ClickGUI 菜单
        int guiKey = GLFW.GLFW_KEY_RIGHT_SHIFT;
        boolean guiDown = InputConstants.isKeyDown(window, guiKey);
        if (guiDown && !heldKeys.contains(guiKey)) {
            heldKeys.add(guiKey);
            mc.setScreen(new ClickGuiScreen());
            return;
        } else if (!guiDown) {
            heldKeys.remove(guiKey);
        }

        // 各模块的绑定键
        for (Module module : modules) {
            int key = module.getKey();
            if (key == -1) continue;

            boolean down = InputConstants.isKeyDown(window, key);
            if (down && !heldKeys.contains(key)) {
                module.toggle();
                heldKeys.add(key);
            } else if (!down) {
                heldKeys.remove(key);
            }
        }
    }
}