package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.phys.Vec3;

// 加速：在地面上、且正在按移动键时，把水平速度乘上一个倍率。
public class Speed extends Module {

    private final DoubleSetting multiplier =
            new DoubleSetting("速度倍率", "水平速度乘以多少", 1.5, 1.0, 3.0);

    public Speed() {
        super("Speed", "在地面上移动时加速", Category.MOVEMENT);
        addSetting(multiplier);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.onGround()) return;   // 只在地面上加速
        if (!hasMovementInput()) return;     // 没按移动键就不加速 -> 松手立刻停下

        Vec3 v = mc.player.getDeltaMovement();
        if (v.x * v.x + v.z * v.z < 0.001) return; // 没有水平移动就不处理

        double m = multiplier.getValue();
        mc.player.setDeltaMovement(v.x * m, v.y, v.z * m);
    }

    // 玩家有没有在按 前/后/左/右 任意一个移动键
    private boolean hasMovementInput() {
        return mc.options.keyUp.isDown()
                || mc.options.keyDown.isDown()
                || mc.options.keyLeft.isDown()
                || mc.options.keyRight.isDown();
    }
}