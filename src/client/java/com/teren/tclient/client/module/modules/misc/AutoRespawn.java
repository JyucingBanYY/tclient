package com.teren.tclient.client.module.modules.misc;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;

// 自动重生：死亡后立刻重生。
public class AutoRespawn extends Module {

    public AutoRespawn() {
        super("AutoRespawn", "死亡后自动重生", Category.MISC);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.isDeadOrDying()) {
            mc.player.respawn();
        }
    }
}