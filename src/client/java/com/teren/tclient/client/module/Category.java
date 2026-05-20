package com.teren.tclient.client.module;

// 模块分类。每个模块属于其中一类，以后做菜单时按这个分组。
public enum Category {
    COMBAT("战斗"),
    MOVEMENT("移动"),
    RENDER("渲染"),
    PLAYER("玩家"),
    WORLD("世界"),
    MISC("杂项");

    public final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
