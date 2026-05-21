package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.phys.Vec3;

// 快速爬梯：在梯子/藤蔓上按前进键时，加快向上爬的速度。
public class FastClimb extends Module {

    private final DoubleSetting speed =
            new DoubleSetting("爬升速度", "爬梯子多快", 0.25, 0.1, 0.6);

    public FastClimb() {
        super("FastClimb", "快速爬梯子/藤蔓", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.onClimbable()) return;

        if (mc.options.keyUp.isDown()) {
            Vec3 v = mc.player.getDeltaMovement();
            mc.player.setDeltaMovement(v.x, speed.getValue(), v.z);
        }
    }
}
