package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;

// 自动潜行：开启后一直保持潜行状态。
public class Sneak extends Module {

    public Sneak() {
        super("Sneak", "自动潜行", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        mc.options.keyShift.setDown(true);
    }

    @Override
    public void onDisable() {
        mc.options.keyShift.setDown(false);
    }
}
