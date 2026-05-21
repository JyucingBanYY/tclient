package com.teren.tclient.client.mixin;

import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// @Mixin(LivingEntity.class) = 我要往 LivingEntity 这个类里注入代码
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    // 在 knockback（击退）方法的开头（HEAD）插入下面这个方法
    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void onKnockback(double strength, double x, double z, CallbackInfo ci) {
        // 在 Mixin 里，this 代表被击退的那个实体。
        // 只处理本地玩家自己，别的生物被击退不管。
        if ((Object) this != Minecraft.getInstance().player) return;

        Module velocity = ModuleManager.getInstance().getModule("Velocity");
        if (velocity != null && velocity.isEnabled()) {
            ci.cancel(); // 取消原本的 knockback 方法 -> 玩家不被击飞
        }
    }
}
