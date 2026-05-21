package com.teren.tclient.client.module.modules.combat;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

// 自动装甲：把背包里的盔甲自动穿到空的盔甲位上。
public class AutoArmor extends Module {

    public AutoArmor() {
        super("AutoArmor", "自动穿上背包里的盔甲", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;
        if (mc.screen != null) return; // 打开界面时不操作背包

        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!(stack.getItem() instanceof ArmorItem armorItem)) continue;

            EquipmentSlot slot = armorItem.getEquipmentSlot();
            // 对应的盔甲位必须是空的才穿
            if (!mc.player.getItemBySlot(slot).isEmpty()) continue;

            int menuSlot = i < 9 ? i + 36 : i; // 背包下标 -> 菜单槽位
            // QUICK_MOVE = 相当于 Shift+点击，会自动把盔甲塞到盔甲位
            mc.gameMode.handleInventoryMouseClick(0, menuSlot, 0, ClickType.QUICK_MOVE, mc.player);
            break; // 一刻只穿一件
        }
    }
}