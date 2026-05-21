package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;

// 抗击退：被攻击时不被击飞。
// 注意：这个模块没有 onTick —— 它的实际效果由 LivingEntityMixin 实现，
// Mixin 会检查这个模块开没开。
public class Velocity extends Module {

    public Velocity() {
        super("Velocity", "抗击退，被攻击时不被击飞", Category.MOVEMENT);
    }
}