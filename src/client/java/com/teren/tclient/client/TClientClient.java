package com.teren.tclient.client;

import com.teren.tclient.client.config.ConfigManager;
import com.teren.tclient.client.hud.HudRenderer;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.lwjgl.glfw.GLFW;

public class TClientClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModuleManager manager = ModuleManager.getInstance();
		manager.init();

		// 给模块绑定按键
		Module fullBright = manager.getModule("FullBright");
		if (fullBright != null) fullBright.setKey(GLFW.GLFW_KEY_B);

		Module autoSprint = manager.getModule("AutoSprint");
		if (autoSprint != null) autoSprint.setKey(GLFW.GLFW_KEY_V);

		// 读取上次保存的配置
		ConfigManager.load();

		// 每个游戏刻：检测按键 + 运行已开启的模块
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			manager.onKey();
			manager.onTick();
		});

		// 每帧：画 HUD
		HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> HudRenderer.render(guiGraphics));

		// 每帧：在 3D 世界里渲染（ESP、Tracers 等）
		WorldRenderEvents.AFTER_ENTITIES.register(context -> manager.onWorldRender(context));

		// 游戏关闭时：保存配置
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ConfigManager.save());
	}
}
