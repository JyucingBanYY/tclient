package com.teren.tclient.client.module.modules.combat;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.EntityHitResult;

// 自动武器：准星对准生物时，自动切到快捷栏里的剑（没有剑就用斧）。
public class AutoWeapon extends Module {

    public AutoWeapon() {
        super("AutoWeapon", "对准生物时自动切武器", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!(mc.hitResult instanceof EntityHitResult entityHit)) return;
        if (!(entityHit.getEntity() instanceof LivingEntity)) return;

        int swordSlot = -1;
        int axeSlot = -1;
        for (int i = 0; i < 9; i++) {
            Item item = mc.player.getInventory().getItem(i).getItem();
            if (item instanceof SwordItem && swordSlot == -1) {
                swordSlot = i;
            } else if (item instanceof AxeItem && axeSlot == -1) {
                axeSlot = i;
            }
        }
        int best = swordSlot != -1 ? swordSlot : axeSlot;
        if (best != -1) {
            mc.player.getInventory().selected = best;
        }
    }
}