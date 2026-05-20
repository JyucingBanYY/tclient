package com.teren.tclient.client.screen;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.config.ConfigManager;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.module.ModuleManager;
import com.teren.tclient.client.setting.BooleanSetting;
import com.teren.tclient.client.setting.DoubleSetting;
import com.teren.tclient.client.setting.IntSetting;
import com.teren.tclient.client.setting.Setting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// ClickGUI 菜单：按分类列出模块，每个模块下面显示它的设置。
public class ClickGuiScreen extends Screen {

    private static final int PANEL_WIDTH = 110;
    private static final int ROW_HEIGHT = 14;
    private static final int START_X = 10;
    private static final int START_Y = 10;
    private static final int PANEL_GAP = 6;

    // 正在被拖动的滑块设置（没有就是 null）
    private Setting draggingSetting = null;
    private int draggingPanelX = 0;

    public ClickGuiScreen() {
        super(Component.literal("TClient ClickGUI"));
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        renderBackground(gui);

        int x = START_X;
        for (Category category : Category.values()) {
            if (!categoryHasModules(category)) continue;
            renderPanel(gui, category, x, mouseX, mouseY);
            x += PANEL_WIDTH + PANEL_GAP;
        }

        super.render(gui, mouseX, mouseY, delta);
    }

    private void renderPanel(GuiGraphics gui, Category category, int x, int mouseX, int mouseY) {
        int y = START_Y;

        // 标题行
        gui.fill(x, y, x + PANEL_WIDTH, y + ROW_HEIGHT, 0xFF1A1A1A);
        gui.drawString(this.font, category.displayName, x + 4, y + 3, 0xFFFFFFFF);
        y += ROW_HEIGHT;

        for (Module module : ModuleManager.getInstance().getModules()) {
            if (module.getCategory() != category) continue;

            // 模块行
            boolean hovered = isInside(mouseX, mouseY, x, y);
            int bg;
            if (module.isEnabled()) {
                bg = hovered ? 0xFF3E8E41 : 0xFF327334;
            } else {
                bg = hovered ? 0xFF3C3C3C : 0xFF2A2A2A;
            }
            gui.fill(x, y, x + PANEL_WIDTH, y + ROW_HEIGHT, bg);
            gui.drawString(this.font, module.getName(), x + 4, y + 3, 0xFFFFFFFF);
            y += ROW_HEIGHT;

            // 该模块的每个设置行
            for (Setting setting : module.getSettings()) {
                renderSetting(gui, setting, x, y);
                y += ROW_HEIGHT;
            }
        }
    }

    private void renderSetting(GuiGraphics gui, Setting setting, int x, int y) {
        // 设置行底色（比模块行更暗）
        gui.fill(x, y, x + PANEL_WIDTH, y + ROW_HEIGHT, 0xFF202020);

        if (setting instanceof BooleanSetting boolSetting) {
            String text = setting.getName() + ": " + (boolSetting.getValue() ? "开" : "关");
            int color = boolSetting.getValue() ? 0xFF55FF55 : 0xFFAAAAAA;
            gui.drawString(this.font, text, x + 8, y + 3, color);

        } else if (setting instanceof DoubleSetting dblSetting) {
            double percent = (dblSetting.getValue() - dblSetting.getMin())
                    / (dblSetting.getMax() - dblSetting.getMin());
            int barWidth = (int) (PANEL_WIDTH * percent);
            gui.fill(x, y, x + barWidth, y + ROW_HEIGHT, 0xFF3E5E8E); // 蓝色进度条
            String text = setting.getName() + ": " + String.format("%.2f", dblSetting.getValue());
            gui.drawString(this.font, text, x + 8, y + 3, 0xFFFFFFFF);

        } else if (setting instanceof IntSetting intSetting) {
            double percent = (double) (intSetting.getValue() - intSetting.getMin())
                    / (intSetting.getMax() - intSetting.getMin());
            int barWidth = (int) (PANEL_WIDTH * percent);
            gui.fill(x, y, x + barWidth, y + ROW_HEIGHT, 0xFF3E5E8E);
            String text = setting.getName() + ": " + intSetting.getValue();
            gui.drawString(this.font, text, x + 8, y + 3, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // 鼠标左键
            int x = START_X;
            for (Category category : Category.values()) {
                if (!categoryHasModules(category)) continue;

                int y = START_Y + ROW_HEIGHT; // 跳过标题行
                for (Module module : ModuleManager.getInstance().getModules()) {
                    if (module.getCategory() != category) continue;

                    // 点到模块行 -> 开关模块
                    if (isInside((int) mouseX, (int) mouseY, x, y)) {
                        module.toggle();
                        return true;
                    }
                    y += ROW_HEIGHT;

                    // 点到设置行
                    for (Setting setting : module.getSettings()) {
                        if (isInside((int) mouseX, (int) mouseY, x, y)) {
                            handleSettingClick(setting, x, mouseX);
                            return true;
                        }
                        y += ROW_HEIGHT;
                    }
                }
                x += PANEL_WIDTH + PANEL_GAP;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleSettingClick(Setting setting, int panelX, double mouseX) {
        if (setting instanceof BooleanSetting boolSetting) {
            boolSetting.toggle();
        } else if (setting instanceof DoubleSetting || setting instanceof IntSetting) {
            // 数值设置：开始拖动滑块，并立即设一次值
            draggingSetting = setting;
            draggingPanelX = panelX;
            updateSlider(mouseX);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingSetting != null) {
            updateSlider(mouseX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingSetting = null; // 松开鼠标，结束拖动
        return super.mouseReleased(mouseX, mouseY, button);
    }

    // 根据鼠标 X 把正在拖的滑块设成对应的值
    private void updateSlider(double mouseX) {
        double percent = (mouseX - draggingPanelX) / PANEL_WIDTH;
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        if (draggingSetting instanceof DoubleSetting dblSetting) {
            dblSetting.setValue(dblSetting.getMin()
                    + percent * (dblSetting.getMax() - dblSetting.getMin()));
        } else if (draggingSetting instanceof IntSetting intSetting) {
            intSetting.setValue((int) Math.round(intSetting.getMin()
                    + percent * (intSetting.getMax() - intSetting.getMin())));
        }
    }

    private boolean isInside(int mx, int my, int x, int y) {
        return mx >= x && mx <= x + PANEL_WIDTH && my >= y && my <= y + ROW_HEIGHT;
    }

    private boolean categoryHasModules(Category category) {
        for (Module module : ModuleManager.getInstance().getModules()) {
            if (module.getCategory() == category) return true;
        }
        return false;
    }

    @Override
    public void removed() {
        ConfigManager.save(); // 关闭菜单时保存配置
    }

    @Override
    public boolean isPauseScreen() {
        return false; // 打开菜单时不暂停游戏
    }
}