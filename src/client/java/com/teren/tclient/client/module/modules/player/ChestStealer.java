package com.teren.tclient.client.module.modules.player;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.IntSetting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

// ChestStealer（自动偷箱子）：打开箱子等容器界面时，自动把里面的物品一格格搬进背包。
public class ChestStealer extends Module {

    private final IntSetting delay = new IntSetting("间隔", "每搬一格之间等几刻", 2, 0, 10);

    private int timer = 0;

    public ChestStealer() {
        super("ChestStealer", "打开容器时自动搬空里面的物品", Category.PLAYER);
        addSetting(delay);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;

        // 必须正开着一个容器界面
        if (!(mc.screen instanceof AbstractContainerScreen)) return;
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) mc.screen;
        AbstractContainerMenu menu = screen.getMenu();

        // 玩家自己的背包界面不处理
        if (menu instanceof InventoryMenu) return;

        if (timer > 0) {
            timer--;
            return;
        }

        // 找一个属于“容器”（不是玩家背包）的、有物品的格子，shift 搬进背包
        for (int i = 0; i < menu.slots.size(); i++) {
            Slot slot = menu.slots.get(i);
            if (slot.container instanceof Inventory) continue; // 玩家背包的格子，跳过
            if (!slot.hasItem()) continue;

            mc.gameMode.handleInventoryMouseClick(
                    menu.containerId, i, 0, ClickType.QUICK_MOVE, mc.player);
            timer = delay.getValue();
            return; // 一刻搬一格
        }
    }
}
