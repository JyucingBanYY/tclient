package com.teren.tclient.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.teren.tclient.client.module.Module;
import com.teren.tclient.client.module.ModuleManager;
import com.teren.tclient.client.setting.BooleanSetting;
import com.teren.tclient.client.setting.DoubleSetting;
import com.teren.tclient.client.setting.IntSetting;
import com.teren.tclient.client.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

// 配置管理器：把模块开关和设置保存到文件，下次启动时读回来。
public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    // 配置文件位置：游戏 config 文件夹里的 tclient.json
    private static final Path FILE =
            FabricLoader.getInstance().getConfigDir().resolve("tclient.json");

    // 保存：把所有模块的当前状态写进 tclient.json
    public static void save() {
        JsonObject root = new JsonObject();

        for (Module module : ModuleManager.getInstance().getModules()) {
            JsonObject moduleJson = new JsonObject();
            moduleJson.addProperty("enabled", module.isEnabled());

            JsonObject settingsJson = new JsonObject();
            for (Setting setting : module.getSettings()) {
                if (setting instanceof BooleanSetting boolSetting) {
                    settingsJson.addProperty(setting.getName(), boolSetting.getValue());
                } else if (setting instanceof DoubleSetting dblSetting) {
                    settingsJson.addProperty(setting.getName(), dblSetting.getValue());
                } else if (setting instanceof IntSetting intSetting) {
                    settingsJson.addProperty(setting.getName(), intSetting.getValue());
                }
            }
            moduleJson.add("settings", settingsJson);
            root.add(module.getName(), moduleJson);
        }

        try {
            Files.writeString(FILE, GSON.toJson(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取：从 tclient.json 把状态恢复到各模块
    public static void load() {
        if (!Files.exists(FILE)) return; // 第一次运行，还没有配置文件

        try {
            String json = Files.readString(FILE);
            JsonObject root = GSON.fromJson(json, JsonObject.class);
            if (root == null) return;

            for (Module module : ModuleManager.getInstance().getModules()) {
                if (!root.has(module.getName())) continue;
                JsonObject moduleJson = root.getAsJsonObject(module.getName());

                // 先恢复设置
                if (moduleJson.has("settings")) {
                    JsonObject settingsJson = moduleJson.getAsJsonObject("settings");
                    for (Setting setting : module.getSettings()) {
                        if (!settingsJson.has(setting.getName())) continue;
                        if (setting instanceof BooleanSetting boolSetting) {
                            boolSetting.setValue(settingsJson.get(setting.getName()).getAsBoolean());
                        } else if (setting instanceof DoubleSetting dblSetting) {
                            dblSetting.setValue(settingsJson.get(setting.getName()).getAsDouble());
                        } else if (setting instanceof IntSetting intSetting) {
                            intSetting.setValue(settingsJson.get(setting.getName()).getAsInt());
                        }
                    }
                }

                // 再恢复开关状态
                if (moduleJson.has("enabled")) {
                    boolean shouldEnable = moduleJson.get("enabled").getAsBoolean();
                    if (shouldEnable != module.isEnabled()) {
                        module.toggle();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}