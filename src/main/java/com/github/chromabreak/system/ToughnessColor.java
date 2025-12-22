package com.github.chromabreak.system;

/**
 * 韧性颜色枚举
 * 定义韧性条可以使用的颜色
 * <p>
 * Toughness Color Enum
 * Defines colors that can be used for toughness bars
 */
public enum ToughnessColor {
    /**
     * 红色（对应红色染料 Red Dye）
     * Red (corresponds to Red Dye)
     * RGB: #B02E26 (176, 46, 38)
     */
    RED(0xB02E26),

    /**
     * 蓝色（对应蓝色染料 Blue Dye）
     * Blue (corresponds to Blue Dye)
     * RGB: #3C44AA (60, 68, 170)
     */
    BLUE(0x3C44AA),

    /**
     * 绿色（对应绿色染料 Green Dye）
     * Green (corresponds to Green Dye)
     * RGB: #5E7C16 (94, 124, 22)
     */
    GREEN(0x5E7C16),

    /**
     * 黄色（对应黄色染料 Yellow Dye）
     * Yellow (corresponds to Yellow Dye)
     * RGB: #FED83D (254, 216, 61)
     */
    YELLOW(0xFED83D),

    /**
     * 白色（对应白色染料 White Dye，默认）
     * White (corresponds to White Dye, default)
     * RGB: #F9FFFE (249, 255, 254)
     */
    WHITE(0xF9FFFE),

    /**
     * 黑色（对应黑色染料 Black Dye）
     * Black (corresponds to Black Dye)
     * RGB: #1D1D21 (29, 29, 33)
     */
    BLACK(0x1D1D21),

    /**
     * 橙色（对应橙色染料 Orange Dye）
     * Orange (corresponds to Orange Dye)
     * RGB: #F9801D (249, 128, 29)
     */
    ORANGE(0xF9801D);

    /**
     * 颜色的RGB值（不包含alpha通道）
     * Color RGB value (without alpha channel)
     */
    private final int rgb;

    /**
     * 构造函数
     * Constructor
     *
     * @param rgb RGB颜色值
     */
    ToughnessColor(final int rgb) {
        this.rgb = rgb;
    }

    /**
     * 获取颜色的RGB值
     * Get color RGB value
     *
     * @return RGB颜色值
     */
    public int getRgb() {
        return this.rgb;
    }

    /**
     * 获取颜色的ARGB值（带alpha通道，alpha=1.0）
     * Get color ARGB value (with alpha channel, alpha=1.0)
     *
     * @param alpha Alpha通道值（0.0-1.0）
     * @return ARGB颜色值
     */
    public int getArgb(final float alpha) {
        final int a = (int) (alpha * 255.0f) & 0xFF;
        return (a << 24) | (this.rgb & 0x00FFFFFF);
    }

    /**
     * 根据名称获取颜色
     * Get color by name
     *
     * @param name 颜色名称（不区分大小写）
     * @return 颜色枚举，如果未找到则返回WHITE
     */
    public static ToughnessColor byName(final String name) {
        if (null == name || name.isEmpty()) {
            return ToughnessColor.WHITE;
        }

        final String upperName = name.toUpperCase();
        try {
            return ToughnessColor.valueOf(upperName);
        } catch (final IllegalArgumentException e) {
            return ToughnessColor.WHITE;
        }
    }

    /**
     * 获取颜色的名称（小写）
     * Get color name (lowercase)
     *
     * @return 颜色名称
     */
    public String getName() {
        return this.name().toLowerCase();
    }
}

