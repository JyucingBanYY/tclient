package com.teren.tclient.client.module.modules.misc;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.world.InteractionHand;

// 防挂机：定时摆手 + 转一点视角，避免被服务器当作挂机踢出。
public class AntiAFK extends Module {

    private int ticks = 0;

    public AntiAFK() {
        super("AntiAFK", "定时小动作，防止挂机被踢", Category.MISC);
    }

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        ticks++;
        // 每 100 刻（约 5 秒）动一次
        if (ticks >= 100) {
            ticks = 0;
            mc.player.swing(InteractionHand.MAIN_HAND);
            mc.player.setYRot(mc.player.getYRot() + 10.0f);
        }
    }
}
