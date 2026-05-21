package com.teren.tclient.client.mixin;

import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.module.ModuleManager;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 注入 MultiPlayerGameMode，用来实现 Reach（攻击距离）。
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    // hasFarPickRange 决定能否远距离选中实体。
    // 原版只有创造模式返回 true。Reach 开启时也让它返回 true，
    // 这会绕过生存模式 3 格的实体瞄准上限，把攻击距离变成约 6 格。
    @Inject(method = "hasFarPickRange", at = @At("HEAD"), cancellable = true)
    private void onHasFarPickRange(CallbackInfoReturnable<Boolean> cir) {
        Module reach = ModuleManager.getInstance().getModule("Reach");
        if (reach != null && reach.isEnabled()) {
            cir.setReturnValue(true);
        }
    }
}