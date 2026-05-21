package com.teren.tclient.client.module.modules.combat;

import com.teren.tclient.client.module.Category;
import com.teren.tclient.client.module.Module;

// 攻击距离：把攻击/交互距离从原版的 3 格延长到约 6 格。
public class Reach extends Module {

    public Reach() {
        super("Reach", "延长攻击距离到约 6 格", Category.COMBAT);
    }
}