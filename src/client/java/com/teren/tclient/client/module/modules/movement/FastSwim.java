package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.phys.Vec3;

// 快速游泳：在水里移动时加快水平速度。
public class FastSwim extends Module {

    private final DoubleSetting multiplier =
            new DoubleSetting("速度倍率", "水中速度乘以多少", 1.5, 1.0, 3.0);

    public FastSwim() {
        super("FastSwim", "在水里游得更快", Category.MOVEMENT);
        addSetting(multiplier);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isInWater()) return;

        Vec3 v = mc.player.getDeltaMovement();
        if (v.x * v.x + v.z * v.z < 0.001) return; // 没有水平移动就不处理

        double m = multiplier.getValue();
        mc.player.setDeltaMovement(v.x * m, v.y, v.z * m);
    }
}