package com.teren.tclient.client.module.modules.combat;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

// 瞄准即攻击：准星对准生物时自动攻击它。
public class TriggerBot extends Module {

    public TriggerBot() {
        super("TriggerBot", "准星对准生物时自动攻击", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;
        // 等攻击冷却好，保证满伤害
        if (mc.player.getAttackStrengthScale(0.0f) < 1.0f) return;
        // 准星必须对着一个生物
        if (!(mc.hitResult instanceof EntityHitResult entityHit)) return;
        if (!(entityHit.getEntity() instanceof LivingEntity target)) return;
        if (target == mc.player) return;

        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND);
    }
}