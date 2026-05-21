package com.teren.tclient.client.module.modules.movement;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

// 免摔伤：下落一定距离时，给服务器发"我在地面上"的包，让它不计算摔落伤害。
public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "免疫摔落伤害", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.getConnection() == null) return;

        if (mc.player.fallDistance > 2.0f) {
            mc.getConnection().send(new ServerboundMovePlayerPacket.StatusOnly(true));
        }
    }
}
