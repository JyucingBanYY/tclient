package com.teren.tclient.client.module.modules.render;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.IntSetting;

// 缩放：开启时拉近视野（降低 FOV），类似望远镜。
public class Zoom extends Module {

    private final IntSetting fov = new IntSetting("视野", "放大后的视野（越小越近）", 30, 30, 70);

    private int savedFov = 70;

    public Zoom() {
        super("Zoom", "拉近视野（望远镜）", Category.RENDER);
        addSetting(fov);
    }

    @Override
    public void onEnable() {
        savedFov = mc.options.fov().get(); // 记住原来的视野
    }

    @Override
    public void onTick() {
        mc.options.fov().set(fov.getValue());
    }

    @Override
    public void onDisable() {
        mc.options.fov().set(savedFov); // 恢复原来的视野
    }
}