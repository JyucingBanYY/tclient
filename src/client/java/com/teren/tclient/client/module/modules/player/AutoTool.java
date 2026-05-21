package com.teren.tclient.client.module.modules.player;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

// 自动切工具：看着方块时，自动切换到挖得最快的工具。
public class AutoTool extends Module {

    public AutoTool() {
        super("AutoTool", "自动切换到最合适的工具", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult hit = (BlockHitResult) mc.hitResult;
        BlockState state = mc.level.getBlockState(hit.getBlockPos());

        int bestSlot = -1;
        float bestSpeed = 1.0f;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            float speed = stack.getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        if (bestSlot != -1) {
            mc.player.getInventory().selected = bestSlot;
        }
    }
}
