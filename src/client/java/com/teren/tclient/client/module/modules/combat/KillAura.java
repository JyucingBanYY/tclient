package com.teren.tclient.client.module.modules.combat;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.BooleanSetting;
import com.teren.tclient.client.setting.DoubleSetting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

// 自动攻击：自动攻击范围内的实体。
public class KillAura extends Module {

    private final DoubleSetting range =
            new DoubleSetting("攻击范围", "多远以内会被攻击", 4.0, 3.0, 6.0);
    private final BooleanSetting playersOnly =
            new BooleanSetting("仅攻击玩家", "只打玩家，不打动物/怪物", false);

    public KillAura() {
        super("KillAura", "自动攻击附近的实体", Category.COMBAT);
        addSetting(range);
        addSetting(playersOnly);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        // 等攻击冷却转好，保证满伤害
        if (mc.player.getAttackStrengthScale(0.0f) < 1.0f) return;

        LivingEntity target = findTarget();
        if (target != null) {
            mc.gameMode.attack(mc.player, target);
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

    // 找范围内最近的目标
    private LivingEntity findTarget() {
        LivingEntity closest = null;
        double closestDistance = range.getValue();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (entity == mc.player) continue;
            if (!entity.isAlive()) continue;
            if (playersOnly.getValue() && !(entity instanceof Player)) continue;

            double distance = mc.player.distanceTo(entity);
            if (distance <= closestDistance) {
                closestDistance = distance;
                closest = living;
            }
        }
        return closest;
    }
}