package com.teren.tclient.client.module.modules.render;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.util.RenderUtil;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

// Tracers：从你的视角画线连到每个生物。
public class Tracers extends Module {

    public Tracers() {
        super("Tracers", "画线连向附近的生物", Category.RENDER);
    }

    @Override
    public void onWorldRender(WorldRenderContext context) {
        if (mc.level == null || mc.player == null) return;

        // 线的起点：相机所在位置
        Vec3 start = context.camera().getPosition();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity)) continue;
            if (entity == mc.player) continue;

            Vec3 end = entity.getBoundingBox().getCenter();
            RenderUtil.drawLine(context, start, end, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
