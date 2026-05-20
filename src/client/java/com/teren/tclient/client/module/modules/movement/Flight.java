package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;

// 飞行：开启后获得创造模式式的飞行能力（双击空格起飞）。
public class Flight extends Module {

    private final DoubleSetting speed =
            new DoubleSetting("飞行速度", "飞得多快", 0.1, 0.05, 0.5);

    public Flight() {
        super("Flight", "创造模式式飞行", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.getAbilities().mayfly = true;
        mc.player.getAbilities().setFlyingSpeed((float) speed.getValue());
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        mc.player.getAbilities().mayfly = false;
        mc.player.getAbilities().flying = false;
        mc.player.getAbilities().setFlyingSpeed(0.05f);
    }
}