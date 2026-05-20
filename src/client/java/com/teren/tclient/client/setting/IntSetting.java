package com.teren.tclient.client.setting;

// 整数型设置：带最小/最大值，设置时自动夹在范围内
public class IntSetting extends Setting {

    private int value;
    private final int min;
    private final int max;

    public IntSetting(String name, String description, int defaultValue, int min, int max) {
        super(name, description);
        this.min = min;
        this.max = max;
        this.value = clamp(defaultValue);
    }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = clamp(value); }
    public int getMin() { return min; }
    public int getMax() { return max; }

    private int clamp(int v) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}