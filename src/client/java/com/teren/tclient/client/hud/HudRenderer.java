package com.teren.tclient.client.hud;

import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

// 屏幕 HUD：左上角水印 + 右上角已开启模块列表。
public class HudRenderer {

    public static void render(GuiGraphics gui) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.options.hideGui) return; // 玩家按 F1 隐藏界面时不画

        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();

        // 左上角水印
        gui.drawString(font, "TClient", 4, 4, 0xFF55FFFF);

        // 右上角：已开启的模块，一行一个
        int y = 4;
        for (Module module : ModuleManager.getInstance().getModules()) {
            if (!module.isEnabled()) continue;

            String name = module.getName();
            int textWidth = font.width(name);
            int x = screenWidth - textWidth - 4;
            gui.drawString(font, name, x, y, 0xFFFFFFFF);
            y += 10;
        }
    }
}