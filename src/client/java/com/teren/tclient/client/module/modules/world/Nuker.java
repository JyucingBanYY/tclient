package com.teren.tclient.client.module.modules.world;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.IntSetting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;

// Nuker：自动破坏你周围范围内的方块，由近到远一个个挖。
public class Nuker extends Module {

    private final IntSetting range = new IntSetting("范围", "破坏半径（格）", 4, 1, 6);

    private BlockPos current = null; // 当前正在挖的方块

    public Nuker() {
        super("Nuker", "自动破坏周围的方块", Category.WORLD);
        addSetting(range);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;

        int r = range.getValue();
        BlockPos center = mc.player.blockPosition();
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;

        // 扫描周围，挑最近的一个能挖的方块
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = mc.level.getBlockState(pos);
                    if (state.isAir()) continue;
                    if (!state.getFluidState().isEmpty()) continue;        // 跳过水/岩浆
                    if (state.getDestroySpeed(mc.level, pos) < 0) continue; // 跳过基岩等挖不动的

                    double dist = mc.player.distanceToSqr(
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    if (dist < bestDist) {
                        bestDist = dist;
                        best = pos;
                    }
                }
            }
        }

        if (best == null) return;

        // 换了目标就重新“开始挖”，同一个目标就“继续挖”
        if (!best.equals(current)) {
            mc.gameMode.startDestroyBlock(best, Direction.UP);
            current = best;
        } else {
            mc.gameMode.continueDestroyBlock(best, Direction.UP);
        }
        mc.player.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void onDisable() {
        if (mc.gameMode != null) {
            mc.gameMode.stopDestroyBlock();
        }
        current = null;
    }
}
