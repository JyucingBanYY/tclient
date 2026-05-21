package com.teren.tclient.client.module.modules.player;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.IntSetting;
import net.minecraft.world.item.ItemStack;

// AutoEat（自动进食）：饥饿值掉到阈值以下时，自动切到食物吃，吃饱再切回原来的物品。
public class AutoEat extends Module {

    private final IntSetting threshold = new IntSetting("饥饿阈值", "饥饿值低于这个数就开始吃", 16, 1, 19);

    private boolean eating = false;
    private int prevSlot = -1;

    public AutoEat() {
        super("AutoEat", "饥饿时自动进食", Category.PLAYER);
        addSetting(threshold);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        int food = mc.player.getFoodData().getFoodLevel();

        if (eating) {
            ItemStack held = mc.player.getInventory().getItem(mc.player.getInventory().selected);
            // 吃饱了，或者手里已经不是食物 -> 停下
            if (food >= 20 || !held.getItem().isEdible()) {
                stopEating();
            } else {
                mc.options.keyUse.setDown(true); // 持续按住右键 = 一直吃
            }
            return;
        }

        // 还没在吃，且饿了 -> 找食物开吃
        if (food <= threshold.getValue()) {
            int slot = findFoodSlot();
            if (slot != -1) {
                prevSlot = mc.player.getInventory().selected;
                mc.player.getInventory().selected = slot;
                eating = true;
                mc.options.keyUse.setDown(true);
            }
        }
    }

    private void stopEating() {
        mc.options.keyUse.setDown(false);
        if (prevSlot != -1) {
            mc.player.getInventory().selected = prevSlot;
            prevSlot = -1;
        }
        eating = false;
    }

    // 在快捷栏（0~8）里找第一格食物
    private int findFoodSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem().isEdible()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDisable() {
        if (eating) {
            stopEating();
        }
    }
}
