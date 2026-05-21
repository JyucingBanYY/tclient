package com.teren.tclient.client.module.modules.combat;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;

// 自动图腾：自动把不死图腾放到副手。
public class AutoTotem extends Module {

    public AutoTotem() {
        super("AutoTotem", "自动把不死图腾放到副手", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;
        if (mc.screen != null) return;
        // 副手已经有图腾就不用管
        if (mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) return;

        int slot = findTotem();
        if (slot == -1) return; // 背包里没有图腾

        // SWAP + 按钮 40 = 把这个槽位和副手互换
        mc.gameMode.handleInventoryMouseClick(0, slot, 40, ClickType.SWAP, mc.player);
    }

    // 找背包里图腾的"菜单槽位"，没有返回 -1
    private int findTotem() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getItem(i).is(Items.TOTEM_OF_UNDYING)) {
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }
}