package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;

// 自动上台阶：把"能直接走上去的高度"调大，走路自动上方块。
public class Step extends Module {

    private final DoubleSetting height =
            new DoubleSetting("台阶高度", "能直接走上的高度（格）", 1.0, 0.6, 2.0);

    public Step() {
        super("Step", "走路自动上台阶", Category.MOVEMENT);
        addSetting(height);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.setMaxUpStep((float) height.getValue());
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        mc.player.setMaxUpStep(0.6f); // 恢复成默认值
    }
}