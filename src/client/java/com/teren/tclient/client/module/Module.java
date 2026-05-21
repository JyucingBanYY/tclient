package com.teren.tclient.client.module;

import com.teren.tclient.client.setting.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

// 所有功能模块的父类。每写一个新功能，就新建一个类继承 Module。
public abstract class Module {

    // mc 是游戏本体，所有模块都能用它访问玩家、世界等。
    protected static final Minecraft mc = Minecraft.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting> settings = new ArrayList<>();
    private boolean enabled = false;
    private int key = -1; // 绑定的按键（GLFW 键码），-1 表示没绑定

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // 切换开关：开 -> 调 onEnable()，关 -> 调 onDisable()
    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    // 子类按需重写下面这些方法：
    public void onEnable() {}    // 模块被打开的瞬间
    public void onDisable() {}   // 模块被关闭的瞬间
    public void onTick() {}      // 开启时，每个游戏刻执行一次
    public void onWorldRender(WorldRenderContext context) {} // 开启时，每帧在 3D 世界里渲染

    // 子类在构造方法里调用，登记自己的设置项
    protected void addSetting(Setting setting) {
        settings.add(setting);
    }

    public List<Setting> getSettings() { return settings; }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
}
