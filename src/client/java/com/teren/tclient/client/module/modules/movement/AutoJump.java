package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;

// 自动跳跃：开启后一直按住跳跃键（一落地就自动跳）。
public class AutoJump extends Module {

    public AutoJump() {
        super("AutoJump", "自动跳跃", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        mc.options.keyJump.setDown(true);
    }

    @Override
    public void onDisable() {
        mc.options.keyJump.setDown(false);
    }
}