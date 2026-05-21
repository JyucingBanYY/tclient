package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.phys.Vec3;

// ElytraFly（鞘翅飞行）：正在用鞘翅滑翔时，用移动键自由控制方向，松开按键则悬停不掉高。
public class ElytraFly extends Module {

    private final DoubleSetting speed = new DoubleSetting("飞行速度", "鞘翅飞行的速度", 1.0, 0.3, 3.0);

    public ElytraFly() {
        super("ElytraFly", "鞘翅飞行增强（可控+不掉高）", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isFallFlying()) return; // 必须正在用鞘翅滑翔

        double s = speed.getValue();
        Vec3 look = mc.player.getLookAngle();
        double mx = 0, my = 0, mz = 0;

        if (mc.options.keyUp.isDown()) {   // 前进 -> 朝视线方向飞
            mx += look.x * s;
            mz += look.z * s;
        }
        if (mc.options.keyDown.isDown()) { // 后退
            mx -= look.x * s;
            mz -= look.z * s;
        }
        if (mc.options.keyJump.isDown())  my += s; // 跳跃键 -> 上升
        if (mc.options.keyShift.isDown()) my -= s; // 潜行键 -> 下降

        mc.player.setDeltaMovement(mx, my, mz);
        mc.player.fallDistance = 0;
    }
}
