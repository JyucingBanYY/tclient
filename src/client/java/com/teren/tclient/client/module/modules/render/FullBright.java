package com.teren.tclient.client.module.modules.render;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

// 永久夜视：每个游戏刻给自己续一个夜视效果，效果就不会断。
public class FullBright extends Module {

    public FullBright() {
        super("FullBright", "永久夜视，黑暗里也能看清", Category.RENDER);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.addEffect(new MobEffectInstance(
                MobEffects.NIGHT_VISION, 400, 0, false, false, false));
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}
