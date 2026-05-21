package com.teren.tclient.client.module.modules.render;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.util.RenderUtil;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

// ESP：给所有生物画白色线框，透墙也能看到。
public class Esp extends Module {

    public Esp() {
        super("ESP", "给生物画线框（透视）", Category.RENDER);
    }

    @Override
    public void onWorldRender(WorldRenderContext context) {
        if (mc.level == null || mc.player == null) return;
        float delta = context.tickDelta();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity)) continue;
            if (entity == mc.player) continue;

            // 把碰撞盒移到"插值后"的位置，盒子才能跟上平滑移动
            double dx = Mth.lerp(delta, entity.xOld, entity.getX()) - entity.getX();
            double dy = Mth.lerp(delta, entity.yOld, entity.getY()) - entity.getY();
            double dz = Mth.lerp(delta, entity.zOld, entity.getZ()) - entity.getZ();
            AABB box = entity.getBoundingBox().move(dx, dy, dz);

            RenderUtil.drawBox(context, box, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
