package com.teren.tclient.client.module.modules.world;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.BooleanSetting;
import com.teren.tclient.client.setting.IntSetting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

// Scaffold（脚手架/搭桥）：自动在脚下铺方块，让你能凭空往前走。
// 普通模式：不潜行，往前走就自动补脚下缺的方块。
// Telly 模式：自动潜行 + 自动小跳，一边弹跳一边搭桥，比较安全。
public class Scaffold extends Module {

    private final BooleanSetting telly = new BooleanSetting("Telly模式", "自动潜行+自动小跳的搭桥方式", false);
    private final IntSetting tellySpeed = new IntSetting("Telly跳跃力度", "Telly模式每次小跳的高度", 5, 1, 10);
    private final BooleanSetting swing = new BooleanSetting("摆手动画", "放方块时挥手", true);

    private boolean forcedSneak = false; // 记录潜行是不是我们按下的

    public Scaffold() {
        super("Scaffold", "自动在脚下铺方块（搭桥）", Category.WORLD);
        addSetting(telly);
        addSetting(tellySpeed);
        addSetting(swing);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;

        // —— Telly 模式：潜行 + 小跳 ——
        if (telly.getValue()) {
            mc.options.keyShift.setDown(true);
            forcedSneak = true;
            if (mc.player.onGround() && hasMovementInput()) {
                double hop = 0.20 + tellySpeed.getValue() * 0.04;
                Vec3 v = mc.player.getDeltaMovement();
                mc.player.setDeltaMovement(v.x, hop, v.z);
            }
        } else if (forcedSneak) {
            // 关掉 Telly 后，把潜行松开
            mc.options.keyShift.setDown(false);
            forcedSneak = false;
        }

        // —— 铺方块 ——
        int slot = findBlockSlot();
        if (slot == -1) return; // 背包里没有方块

        BlockPos below = mc.player.blockPosition().below();
        BlockState state = mc.level.getBlockState(below);
        if (!state.isAir() && !state.canBeReplaced()) return; // 脚下已经有方块了

        // 当前手里不是方块的话，切到方块那一格
        ItemStack held = mc.player.getInventory().getItem(mc.player.getInventory().selected);
        if (!(held.getItem() instanceof BlockItem)) {
            mc.player.getInventory().selected = slot;
        }

        placeBlock(below);
    }

    // 在 target 处放一个方块：找一个挨着它的实心方块当“支撑面”，对着那个面放。
    private void placeBlock(BlockPos target) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = target.relative(dir);
            BlockState neighborState = mc.level.getBlockState(neighbor);
            if (neighborState.isAir()) continue;
            if (neighborState.canBeReplaced()) continue;            // 草、雪层之类，靠不住
            if (!neighborState.getFluidState().isEmpty()) continue;  // 水、岩浆

            Direction face = dir.getOpposite();
            Vec3 hit = Vec3.atCenterOf(neighbor).add(
                    face.getStepX() * 0.5, face.getStepY() * 0.5, face.getStepZ() * 0.5);
            BlockHitResult result = new BlockHitResult(hit, face, neighbor, false);

            InteractionResult r = mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, result);
            if (r.consumesAction()) {
                if (swing.getValue()) mc.player.swing(InteractionHand.MAIN_HAND);
                return;
            }
        }
    }

    // 在快捷栏（0~8）里找第一格方块
    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasMovementInput() {
        return mc.options.keyUp.isDown() || mc.options.keyDown.isDown()
                || mc.options.keyLeft.isDown() || mc.options.keyRight.isDown();
    }

    @Override
    public void onDisable() {
        if (forcedSneak) {
            mc.options.keyShift.setDown(false);
            forcedSneak = false;
        }
    }
}