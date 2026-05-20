package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.BooleanSetting;

// 自动疾跑：开启后把玩家设为疾跑状态。
public class AutoSprint extends Module {

    // 设置项：开启后只在玩家实际移动时才疾跑
    private final BooleanSetting onlyWhenMoving =
            new BooleanSetting("仅移动时", "只在实际移动时才疾跑", true);

    public AutoSprint() {
        super("AutoSprint", "自动疾跑，不用一直按疾跑键", Category.MOVEMENT);
        addSetting(onlyWhenMoving);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        // 如果"仅移动时"开着，而玩家没在动，就不疾跑
        if (onlyWhenMoving.getValue() && !isMoving()) {
            return;
        }
        mc.player.setSprinting(true);
    }

    // 用水平速度判断玩家有没有在移动
    private boolean isMoving() {
        double dx = mc.player.getDeltaMovement().x;
        double dz = mc.player.getDeltaMovement().z;
        return (dx * dx + dz * dz) > 0.001;
    }
}