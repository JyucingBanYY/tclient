package com.teren.tclient.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

// 在 3D 世界里画线框/连线的工具。
public class RenderUtil {

    // 画一个线框盒子（box 用世界坐标，颜色 r/g/b/a 取值 0~1）
    public static void drawBox(WorldRenderContext context, AABB box,
                               float r, float g, float b, float a) {
        if (context.matrixStack() == null || context.consumers() == null) return;

        Vec3 cam = context.camera().getPosition();
        VertexConsumer buffer = context.consumers().getBuffer(EspRenderType.ESP_LINES);
        // 把盒子从世界坐标平移到"相对相机"的坐标
        AABB local = box.move(-cam.x, -cam.y, -cam.z);
        LevelRenderer.renderLineBox(context.matrixStack(), buffer, local, r, g, b, a);
    }

    // 画一条线（from / to 都是世界坐标）
    public static void drawLine(WorldRenderContext context, Vec3 from, Vec3 to,
                                float r, float g, float b, float a) {
        if (context.matrixStack() == null || context.consumers() == null) return;

        Vec3 cam = context.camera().getPosition();
        VertexConsumer buffer = context.consumers().getBuffer(EspRenderType.ESP_LINES);
        PoseStack.Pose pose = context.matrixStack().last();
        Matrix4f matrix = pose.pose();
        Matrix3f normalMat = pose.normal();

        float x1 = (float) (from.x - cam.x), y1 = (float) (from.y - cam.y), z1 = (float) (from.z - cam.z);
        float x2 = (float) (to.x - cam.x), y2 = (float) (to.y - cam.y), z2 = (float) (to.z - cam.z);

        // 线的方向当作法线
        float nx = x2 - x1, ny = y2 - y1, nz = z2 - z1;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len > 0) { nx /= len; ny /= len; nz /= len; }

        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(normalMat, nx, ny, nz).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(normalMat, nx, ny, nz).endVertex();
    }
}
