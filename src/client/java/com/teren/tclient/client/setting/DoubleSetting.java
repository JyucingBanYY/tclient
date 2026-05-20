package com.teren.tclient.client.setting;

// 小数型设置：带最小/最大值，设置时自动夹在范围内
public class DoubleSetting extends Setting {

    private double value;
    private final double min;
    private final double max;

    public DoubleSetting(String name, String description, double defaultValue, double min, double max) {
        super(name, description);
        this.min = min;
        this.max = max;
        this.value = clamp(defaultValue);
    }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = clamp(value); }
    public double getMin() { return min; }
    public double getMax() { return max; }

    private double clamp(double v) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}