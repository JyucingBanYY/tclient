package com.teren.tclient.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

// 自定义渲染层：和原版的"线"一样，但关掉了深度测试 ——
// 这样线框/连线能透过墙壁看到（真正的"透视"）。
public class EspRenderType extends RenderType {

    // 这个构造方法永远不会被真正调用。
    // 继承 RenderType 只是为了能用到它里面 protected 的工具（create、各种 Shard）。
    private EspRenderType(String name, VertexFormat format, VertexFormat.Mode mode,
                          int bufferSize, boolean affectsCrumbling, boolean sortOnUpload,
                          Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    // 透墙的线。和原版 RenderType.lines() 一样，只多加了 NO_DEPTH_TEST。
    public static final RenderType ESP_LINES = create(
            "tclient_esp_lines",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LINES_SHADER)
                    .setLineState(new LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .createCompositeState(false));
}
