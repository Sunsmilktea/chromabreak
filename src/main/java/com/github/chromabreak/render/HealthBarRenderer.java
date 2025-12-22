package com.github.chromabreak.render;

import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

/**
 * 血条渲染器类
 * 负责在HUD上渲染生物血条
 * <p>
 * Health Bar Renderer Class
 * Responsible for rendering health bars on HUD for entities
 */
public class HealthBarRenderer {

    // 私有构造器防止实例化
    // Private constructor to prevent instantiation
    private HealthBarRenderer() {
    }

    /**
     * 在HUD上渲染血条和韧性条（屏幕坐标）
     * Render health bar and toughness bar on HUD (screen coordinates)
     *
     * @param guiGraphics         GUI图形上下文
     * @param screenWidth         屏幕宽度
     * @param y                   血条Y坐标
     * @param healthPercentage    生命值百分比 (0.0 - 1.0)
     * @param toughnessPercentage 韧性值百分比 (0.0 - 1.0)
     * @param width               血条宽度
     * @param height              血条高度
     * @param foregroundColor     前景颜色（十六进制格式）
     * @param redOpacity          红色填充透明度
     * @param colorDistribution   韧性颜色分布（可选，如果为null则使用默认白色）
     */
    public static void renderHealthBarHUD(final GuiGraphics guiGraphics,
                                          final int screenWidth, final int y,
                                          final float healthPercentage,
                                          final float toughnessPercentage,
                                          final float width, final float height,
                                          final String foregroundColor,
                                          final float redOpacity,
                                          final com.github.chromabreak.system.ToughnessColorDistribution colorDistribution) {
        final float filledWidth = Math.max(0.0f, width * healthPercentage);
        final float toughnessWidth = Math.max(0.0f, width * toughnessPercentage);
        final float shadowWidth = 1.0f; // Black shadow border width
        // 黑色阴影边框宽度

        // Calculate base position (centered horizontally)
        // 计算基础位置（水平居中）
        final float baseX = (screenWidth - width) / 2.0f;
        final float baseY = (float) y;

        // Calculate toughness bar position (0.2 above health bar)
        // 计算韧性条位置（血条上方0.2位置）
        final float toughnessY = baseY - height - 0.2f * height;

        // Parse foreground color from config
        // 从配置解析前景颜色
        final float[] fgColor = HealthBarRenderer.parseHexColor(foregroundColor);

        // Reduce color saturation for a softer look
        // 降低颜色饱和度以获得更柔和的外观
        final float saturationFactor = 0.6f;
        final float grayValue = 0.5f;
        final float desaturatedRed = fgColor[0] * saturationFactor + grayValue * (1.0f - saturationFactor);
        final float desaturatedGreen = fgColor[1] * saturationFactor + grayValue * (1.0f - saturationFactor);
        final float desaturatedBlue = fgColor[2] * saturationFactor + grayValue * (1.0f - saturationFactor);

        // Use GuiGraphics to render rectangles (simpler than vertex rendering for HUD)
        // 使用GuiGraphics渲染矩形（对于HUD来说比顶点渲染更简单）

        // ===== 渲染韧性条 =====
        // ===== Render Toughness Bar =====
        if (0.0f < toughnessPercentage) {
            // Render toughness bar shadow border
            // 渲染韧性条阴影边框
            guiGraphics.fill(
                    (int) (baseX - shadowWidth), (int) (toughnessY - shadowWidth),
                    (int) (baseX + width + shadowWidth), (int) (toughnessY + height + shadowWidth),
                    0xCC000000 // Black with some transparency
            );

            // Render toughness bar background (white/light gray)
            // 渲染韧性条背景（白色/浅灰色）
            guiGraphics.fill(
                    (int) baseX, (int) toughnessY,
                    (int) (baseX + width), (int) (toughnessY + height),
                    0xE6FFFFFF // White with some transparency
            );

            // Render filled toughness bar with colors
            // 使用颜色渲染填充韧性条
            if (0.01f < toughnessWidth) {
                // 直接调用renderColoredToughnessBar，它内部会处理null和空的情况
                // Directly call renderColoredToughnessBar, it will handle null and empty cases internally
                HealthBarRenderer.renderColoredToughnessBar(guiGraphics, baseX, toughnessY, toughnessWidth, height, colorDistribution);
            }
        }

        // ===== 渲染血条 =====
        // ===== Render Health Bar =====
        // Render black shadow border (slightly larger than the bar)
        // 渲染黑色阴影边框（比血条稍大）
        guiGraphics.fill(
                (int) (baseX - shadowWidth), (int) (baseY - shadowWidth),
                (int) (baseX + width + shadowWidth), (int) (baseY + height + shadowWidth),
                0xCC000000 // Black with some transparency
        );

        // Render white/light gray background
        // 渲染白色/浅灰色背景
        guiGraphics.fill(
                (int) baseX, (int) baseY,
                (int) (baseX + width), (int) (baseY + height),
                0xE6FFFFFF // White with some transparency
        );

        // Render filled health bar with desaturated red color
        // 使用降低饱和度的红色渲染填充血条
        if (0.01f < filledWidth) {
            final int red = (int) (desaturatedRed * 255);
            final int green = (int) (desaturatedGreen * 255);
            final int blue = (int) (desaturatedBlue * 255);
            final int alpha = (int) (redOpacity * 255);
            final int color = (alpha << 24) | (red << 16) | (green << 8) | blue;

            guiGraphics.fill(
                    (int) baseX, (int) baseY,
                    (int) (baseX + filledWidth), (int) (baseY + height),
                    color
            );
        }
    }

    /**
     * 解析十六进制颜色字符串为RGBA浮点值
     * Parse hex color string to RGBA float values
     */
    private static float[] parseHexColor(final String hexColor) {
        try {
            // Remove # if present
            String colorString = hexColor;
            if (colorString.startsWith("#")) {
                colorString = colorString.substring(1);
            }

            // Parse the color
            final Color color;
            if (6 == colorString.length()) {
                // RGB format, add full alpha
                color = new Color(
                        Integer.parseInt(colorString.substring(0, 2), 16),
                        Integer.parseInt(colorString.substring(2, 4), 16),
                        Integer.parseInt(colorString.substring(4, 6), 16),
                        255
                );
            } else if (8 == colorString.length()) {
                // RGBA format
                color = new Color(
                        Integer.parseInt(colorString.substring(0, 2), 16),
                        Integer.parseInt(colorString.substring(2, 4), 16),
                        Integer.parseInt(colorString.substring(4, 6), 16),
                        Integer.parseInt(colorString.substring(6, 8), 16)
                );
            } else {
                // Default to red if invalid format
                color = new Color(255, 0, 0, 255);
            }

            return new float[]{
                    color.getRed() / 255.0f,
                    color.getGreen() / 255.0f,
                    color.getBlue() / 255.0f,
                    color.getAlpha() / 255.0f
            };
        } catch (final Exception e) {
            // Return default red color on error
            return new float[]{1.0f, 0.0f, 0.0f, 1.0f};
        }
    }


    /**
     * 渲染彩色韧性条（带颜色分布）
     * Render colored toughness bar (with color distribution)
     *
     * @param guiGraphics       GUI图形上下文
     * @param x                 起始X坐标
     * @param y                 起始Y坐标
     * @param width             宽度
     * @param height            高度
     * @param colorDistribution 颜色分布
     */
    public static void renderColoredToughnessBar(final GuiGraphics guiGraphics, final float x, final float y,
                                                 final float width, final float height,
                                                 final com.github.chromabreak.system.ToughnessColorDistribution colorDistribution) {
        if (null == colorDistribution) {
            // 默认白色
            // Default white
            final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("HealthBarRenderer");
            logger.debug("Color distribution is null, using default white");
            guiGraphics.fill(
                    (int) x, (int) y,
                    (int) (x + width), (int) (y + height),
                    0xFFFFFFFF
            );
            return;
        }

        final java.util.Map<com.github.chromabreak.system.ToughnessColor, Float> colorMap = colorDistribution.getColorMap();
        if (null == colorMap || colorMap.isEmpty()) {
            // 默认白色
            // Default white
            final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("HealthBarRenderer");
            logger.debug("Color map is null or empty, using default white");
            guiGraphics.fill(
                    (int) x, (int) y,
                    (int) (x + width), (int) (y + height),
                    0xFFFFFFFF
            );
            return;
        }

        // 调试日志：记录正在渲染的颜色
        // Debug log: record colors being rendered
        final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("HealthBarRenderer");
        logger.debug("Rendering toughness bar with colors: {}", colorMap.keySet());

        // 如果是单一颜色，直接渲染
        // If single color, render directly
        if (1 == colorMap.size()) {
            final com.github.chromabreak.system.ToughnessColor color = colorMap.keySet().iterator().next();
            // 使用不透明的ARGB颜色值
            // Use opaque ARGB color value
            final int argb = color.getArgb(1.0f);
            guiGraphics.fill(
                    (int) x, (int) y,
                    (int) (x + width), (int) (y + height),
                    argb
            );
            return;
        }

        // 多色分布：按百分比分段渲染
        // Multi-color distribution: render segments by percentage
        // 对颜色进行排序以确保渲染顺序一致
        // Sort colors to ensure consistent rendering order
        final java.util.List<java.util.Map.Entry<com.github.chromabreak.system.ToughnessColor, Float>> sortedEntries =
                new java.util.ArrayList<>(colorMap.entrySet());
        sortedEntries.sort((a, b) -> a.getKey().name().compareTo(b.getKey().name()));

        float currentX = x;
        final float totalWidth = width; // 保存总宽度
        float totalPercentage = 0.0f; // 用于验证百分比总和

        for (final java.util.Map.Entry<com.github.chromabreak.system.ToughnessColor, Float> entry : sortedEntries) {
            final com.github.chromabreak.system.ToughnessColor color = entry.getKey();
            final float percentage = entry.getValue();
            totalPercentage += percentage;
            final float segmentWidth = totalWidth * percentage;

            if (0.01f < segmentWidth) {
                // 使用不透明的ARGB颜色值
                // Use opaque ARGB color value
                final int argb = color.getArgb(1.0f);
                final int endX = (int) (currentX + segmentWidth);
                guiGraphics.fill(
                        (int) currentX, (int) y,
                        endX, (int) (y + height),
                        argb
                );
                currentX += segmentWidth;
            }
        }

        // 如果还有剩余空间（由于浮点数精度问题），用最后一个颜色填充
        // If there's remaining space (due to floating point precision), fill with last color
        if (currentX < x + totalWidth - 0.5f) {
            final float remainingWidth = (x + totalWidth) - currentX;
            if (0.5f < remainingWidth && !sortedEntries.isEmpty()) {
                final com.github.chromabreak.system.ToughnessColor lastColor = sortedEntries.get(sortedEntries.size() - 1).getKey();
                final int argb = lastColor.getArgb(1.0f);
                guiGraphics.fill(
                        (int) currentX, (int) y,
                        (int) (x + totalWidth), (int) (y + height),
                        argb
                );
            }
        }
    }
}