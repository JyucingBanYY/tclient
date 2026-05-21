package com.teren.tclient.client.module.modules.combat;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.setting.IntSetting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.EntityHitResult;

// 自动连点（连点器）：按设定的频率自动左键点击，攻击准星对准的实体。
public class AutoClicker extends Module {

    private final IntSetting cps = new IntSetting("每秒点击", "每秒点几下", 10, 1, 20);

    private int ticks = 0;

    public AutoClicker() {
        super("AutoClicker", "自动连点（连点器）", Category.COMBAT);
        addSetting(cps);
    }

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameMode == null) return;

        ticks++;
        int delay = Math.max(1, 20 / cps.getValue()); // 20 刻/秒 换算成间隔
        if (ticks >= delay) {
            ticks = 0;
            click();
        }
    }

    private void click() {
        // 准星对着实体 -> 攻击它
        if (mc.hitResult instanceof EntityHitResult entityHit) {
            mc.gameMode.attack(mc.player, entityHit.getEntity());
        }
        // 挥手，让动作可见
        mc.player.swing(InteractionHand.MAIN_HAND);
    }
}