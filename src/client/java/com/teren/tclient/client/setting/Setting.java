package com.teren.tclient.client.setting;

// 所有设置项的父类。每个设置都有名字和说明。
public abstract class Setting {

    private final String name;
    private final String description;

    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}