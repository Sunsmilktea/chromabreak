package com.github.chromabreak.system;

/**
 * ToughnessColor - 韧性颜色枚举类
 * Toughness Color Enum Class
 * <p>
 * 定义ChromaBreak模组中韧性条可以使用的7种颜色，与Minecraft的7种染料颜色一一对应
 * Defines 7 colors that can be used for toughness bars in ChromaBreak mod,
 * one-to-one correspondence with Minecraft's 7 dye colors
 * <p>
 * 颜色系统设计：
 * Color system design:
 * - 每种颜色对应一种Minecraft染料
 * Each color corresponds to one Minecraft dye
 * - 颜色值基于Minecraft原版染料的实际颜色
 * Color values are based on actual colors of vanilla Minecraft dyes
 * - 支持RGB和ARGB颜色格式
 * Supports both RGB and ARGB color formats
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 颜色枚举定义：7种标准颜色
 * Color enum definition: 7 standard colors
 * - 颜色名称查找：通过字符串名称获取颜色
 * Color name lookup: Get color by string name
 * - 颜色值转换：RGB和ARGB格式转换
 * Color value conversion: RGB and ARGB format conversion
 * - 颜色名称获取：获取小写颜色名称
 * Color name retrieval: Get lowercase color name
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 韧性条颜色渲染
 * Toughness bar color rendering
 * - 染料到韧性颜色的映射
 * Dye to toughness color mapping
 * - 配置系统中的颜色选择
 * Color selection in configuration system
 */
public enum ToughnessColor {
    /**
     * 红色韧性条颜色
     * Red toughness bar color
     * <p>
     * 对应Minecraft红色染料 (Red Dye)
     * Corresponds to Minecraft Red Dye
     * RGB: #B02E26 (176, 46, 38)
     * 十六进制: 0xB02E26
     * Hexadecimal: 0xB02E26
     */
    RED(0xB02E26),

    /**
     * 蓝色韧性条颜色
     * Blue toughness bar color
     * <p>
     * 对应Minecraft蓝色染料 (Blue Dye)
     * Corresponds to Minecraft Blue Dye
     * RGB: #3C44AA (60, 68, 170)
     * 十六进制: 0x3C44AA
     * Hexadecimal: 0x3C44AA
     */
    BLUE(0x3C44AA),

    /**
     * 绿色韧性条颜色
     * Green toughness bar color
     * <p>
     * 对应Minecraft绿色染料 (Green Dye)
     * Corresponds to Minecraft Green Dye
     * RGB: #5E7C16 (94, 124, 22)
     * 十六进制: 0x5E7C16
     * Hexadecimal: 0x5E7C16
     */
    GREEN(0x5E7C16),

    /**
     * 黄色韧性条颜色
     * Yellow toughness bar color
     * <p>
     * 对应Minecraft黄色染料 (Yellow Dye)
     * Corresponds to Minecraft Yellow Dye
     * RGB: #FED83D (254, 216, 61)
     * 十六进制: 0xFED83D
     * Hexadecimal: 0xFED83D
     */
    YELLOW(0xFED83D),

    /**
     * 白色韧性条颜色（默认颜色）
     * White toughness bar color (default color)
     * <p>
     * 对应Minecraft白色染料 (White Dye)
     * Corresponds to Minecraft White Dye
     * RGB: #F9FFFE (249, 255, 254)
     * 十六进制: 0xF9FFFE
     * Hexadecimal: 0xF9FFFE
     * 当颜色名称无效或未指定时使用此颜色
     * Used when color name is invalid or not specified
     */
    WHITE(0xF9FFFE),

    /**
     * 黑色韧性条颜色
     * Black toughness bar color
     * <p>
     * 对应Minecraft黑色染料 (Black Dye)
     * Corresponds to Minecraft Black Dye
     * RGB: #1D1D21 (29, 29, 33)
     * 十六进制: 0x1D1D21
     * Hexadecimal: 0x1D1D21
     */
    BLACK(0x1D1D21),

    /**
     * 橙色韧性条颜色
     * Orange toughness bar color
     * <p>
     * 对应Minecraft橙色染料 (Orange Dye)
     * Corresponds to Minecraft Orange Dye
     * RGB: #F9801D (249, 128, 29)
     * 十六进制: 0xF9801D
     * Hexadecimal: 0xF9801D
     */
    ORANGE(0xF9801D);

    /**
     * 颜色的RGB值（不包含alpha通道）
     * Color RGB value (without alpha channel)
     * <p>
     * 存储为24位RGB整数格式 (0xRRGGBB)
     * Stored as 24-bit RGB integer format (0xRRGGBB)
     * 高位字节为红色，中间字节为绿色，低位字节为蓝色
     * High byte is red, middle byte is green, low byte is blue
     */
    private final int rgb;

    /**
     * 韧性颜色构造函数
     * Toughness Color Constructor
     *
     * @param rgb RGB颜色值，格式为0xRRGGBB
     *            RGB color value in 0xRRGGBB format
     *            <p>
     *            示例：0xB02E26 表示红色 (176, 46, 38)
     *            Example: 0xB02E26 represents red (176, 46, 38)
     */
    ToughnessColor(final int rgb) {
        this.rgb = rgb;
    }

    /**
     * 根据名称获取颜色枚举
     * Get color enum by name
     *
     * @param name 颜色名称（不区分大小写，支持：red, blue, green, yellow, white, black, orange）
     *             Color name (case-insensitive, supports: red, blue, green, yellow, white, black, orange)
     * @return 对应的颜色枚举，如果名称无效或未找到则返回默认颜色WHITE
     * Corresponding color enum, returns default color WHITE if name is invalid or not found
     * <p>
     * 示例：byName("red") 返回 ToughnessColor.RED
     * Example: byName("red") returns ToughnessColor.RED
     * 示例：byName("invalid") 返回 ToughnessColor.WHITE
     * Example: byName("invalid") returns ToughnessColor.WHITE
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
     * 获取颜色的RGB值
     * Get color RGB value
     *
     * @return RGB颜色值，格式为0xRRGGBB（不包含alpha通道）
     * RGB color value in 0xRRGGBB format (without alpha channel)
     * <p>
     * 示例：ToughnessColor.RED.getRgb() 返回 0xB02E26
     * Example: ToughnessColor.RED.getRgb() returns 0xB02E26
     */
    public int getRgb() {
        return this.rgb;
    }

    /**
     * 获取颜色的ARGB值（带alpha通道）
     * Get color ARGB value (with alpha channel)
     *
     * @param alpha Alpha通道值，范围0.0-1.0（0.0完全透明，1.0完全不透明）
     *              Alpha channel value, range 0.0-1.0 (0.0 fully transparent, 1.0 fully opaque)
     * @return ARGB颜色值，格式为0xAARRGGBB
     * ARGB color value in 0xAARRGGBB format
     * <p>
     * 示例：ToughnessColor.RED.getArgb(1.0f) 返回 0xFFB02E26
     * Example: ToughnessColor.RED.getArgb(1.0f) returns 0xFFB02E26
     * 示例：ToughnessColor.RED.getArgb(0.5f) 返回 0x80B02E26
     * Example: ToughnessColor.RED.getArgb(0.5f) returns 0x80B02E26
     */
    public int getArgb(final float alpha) {
        final int a = (int) (alpha * 255.0f) & 0xFF;
        return (a << 24) | (this.rgb & 0x00FFFFFF);
    }

    /**
     * 获取颜色的名称（小写）
     * Get color name (lowercase)
     *
     * @return 颜色的小写名称，如"red", "blue", "green"等
     * Lowercase color name, e.g., "red", "blue", "green", etc.
     * <p>
     * 示例：ToughnessColor.RED.getName() 返回 "red"
     * Example: ToughnessColor.RED.getName() returns "red"
     */
    public String getName() {
        return this.name().toLowerCase();
    }
}

