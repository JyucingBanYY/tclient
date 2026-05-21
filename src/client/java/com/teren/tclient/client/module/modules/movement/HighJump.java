package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.phys.Vec3;

// 高跳：起跳的瞬间给一个更大的向上速度。
public class HighJump extends Module {

    private final DoubleSetting power =
            new DoubleSetting("跳跃力度", "跳多高（普通跳约 0.42）", 0.6, 0.42, 1.5);

    // 记录上一刻是不是站在地面上
    private boolean wasOnGround = false;

    public HighJump() {
        super("HighJump", "跳得更高", Category.MOVEMENT);
        addSetting(power);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        boolean onGround = mc.player.onGround();
        Vec3 v = mc.player.getDeltaMovement();

        // 上一刻在地面、这一刻离地且在上升 -> 刚起跳
        if (wasOnGround && !onGround && v.y > 0) {
            mc.player.setDeltaMovement(v.x, power.getValue(), v.z);
        }
        wasOnGround = onGround;
    }
}
