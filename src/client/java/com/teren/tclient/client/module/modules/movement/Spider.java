package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.phys.Vec3;

// Spider（蜘蛛）：贴着墙、并朝墙的方向走，就能像蜘蛛一样往上爬。
public class Spider extends Module {

    private final DoubleSetting climbSpeed = new DoubleSetting("爬墙速度", "贴墙时向上爬的速度", 0.2, 0.05, 0.5);

    public Spider() {
        super("Spider", "贴墙就能往上爬", Category.MOVEMENT);
        addSetting(climbSpeed);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // horizontalCollision = 水平方向撞到了东西（贴着墙）
        if (mc.player.horizontalCollision && hasMovementInput()) {
            Vec3 v = mc.player.getDeltaMovement();
            mc.player.setDeltaMovement(v.x, climbSpeed.getValue(), v.z);
            mc.player.fallDistance = 0; // 别累积摔落伤害
        }
    }

    private boolean hasMovementInput() {
        return mc.options.keyUp.isDown() || mc.options.keyDown.isDown()
                || mc.options.keyLeft.isDown() || mc.options.keyRight.isDown();
    }
}
