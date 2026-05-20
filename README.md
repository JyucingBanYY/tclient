# TClient

基于Fabric的Minecraft客户端,版本 1.20.1。

> ⚠️ 仅供在**服务器管理员允许**的服务器、或单机中使用。请勿用于禁止外置客户端的服务器。

## 功能

**战斗**
- `KillAura` — 自动攻击附近的实体

**移动**
- `AutoSprint` — 自动疾跑
- `Speed` — 地面加速
- `Flight` — 创造模式式飞行
- `Step` — 自动上台阶
- `AutoWalk` — 自动前进

**渲染**
- `FullBright` — 永久夜视

**杂项**
- `AutoRespawn` — 死亡后自动重生

另外还有:ClickGUI 菜单(`右Shift` 打开)、屏幕 HUD、配置自动保存。

## 安装

1. 安装 [Fabric Loader](https://fabricmc.net/)(Minecraft 1.20.1)
2. 下载 [Fabric API](https://modrinth.com/mod/fabric-api) 的 1.20.1 版本
3. 把 Fabric API 和 TClient 的 jar 一起放进 `.minecraft/mods` 文件夹

## 从源码构建

```
./gradlew build
```

构建产物在 `build/libs/` 下。

---

个人学习项目。