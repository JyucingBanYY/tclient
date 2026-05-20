package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;

// 自动前进：开启后角色一直往前走，不用按 W。
public class AutoWalk extends Module {

    public AutoWalk() {
        super("AutoWalk", "自动一直往前走", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        mc.options.keyUp.setDown(true);
    }

    @Override
    public void onDisable() {
        mc.options.keyUp.setDown(false);
    }
}