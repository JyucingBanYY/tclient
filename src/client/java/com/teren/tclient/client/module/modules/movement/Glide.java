package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.phys.Vec3;

// 缓降：下落时把下落速度限制住，缓慢飘下去。
public class Glide extends Module {

    private final DoubleSetting fallSpeed =
            new DoubleSetting("下落速度", "下落多快（越小越慢）", 0.1, 0.02, 0.3);

    public Glide() {
        super("Glide", "缓慢下落，不会快速坠落", Category.MOVEMENT);
        addSetting(fallSpeed);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.onGround()) return;

        Vec3 v = mc.player.getDeltaMovement();
        if (v.y < 0) {
            mc.player.setDeltaMovement(v.x, -fallSpeed.getValue(), v.z);
        }
    }
}
