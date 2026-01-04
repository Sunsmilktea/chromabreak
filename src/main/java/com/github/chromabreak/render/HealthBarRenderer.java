package com.github.chromabreak.render;

import com.github.chromabreak.system.ToughnessColor;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;

/**
 * HealthBarRenderer - 血条渲染器类
 * Health Bar Renderer Class
 * <p>
 * 负责在HUD上渲染生物的血条和韧性条，提供丰富的视觉效果和自定义选项
 * Responsible for rendering health bars and toughness bars on HUD for entities, providing rich visual effects and customization options
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 血条和韧性条的HUD渲染（屏幕坐标）
 * HUD rendering of health bars and toughness bars (screen coordinates)
 * - 颜色解析和饱和度调整，支持十六进制颜色格式
 * Color parsing and saturation adjustment, supporting hexadecimal color format
 * - 渐变过渡渲染，实现平滑的颜色过渡效果
 * Gradient transition rendering, achieving smooth color transition effects
 * - 多色韧性条渲染，支持按百分比分布的颜色渲染
 * Multi-color toughness bar rendering, supporting percentage-based color distribution
 * - 阴影边框和背景渲染，增强视觉效果
 * Shadow border and background rendering, enhancing visual effects
 * - 性能优化，避免频繁的颜色解析和计算
 * Performance optimization, avoiding frequent color parsing and calculations
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 * <p>
 * 支持通过配置文件自定义血条外观和颜色
 * Supports customizing health bar appearance and colors through configuration files
 * <p>
 * 注意：这是一个客户端专用类，不会在服务器端加载
 * Note: This is a client-only class and will not load on server side
 */
@OnlyIn(Dist.CLIENT)
public enum HealthBarRenderer {
    ;

    /**
     * 在HUD上渲染血条和韧性条（屏幕坐标）
     * Render health bar and toughness bar on HUD (screen coordinates)
     * <p>
     * 这是主要的渲染方法，负责在屏幕顶部居中位置渲染血条和韧性条
     * This is the main rendering method, responsible for rendering health bar and toughness bar at top center of screen
     * <p>
     * 渲染流程包括：
     * Rendering process includes:
     * - 计算血条和韧性条的填充宽度
     * Calculate filled width for health bar and toughness bar
     * - 解析前景颜色并调整饱和度
     * Parse foreground color and adjust saturation
     * - 渲染韧性条（如果有韧性值）
     * Render toughness bar (if toughness value exists)
     * - 渲染血条
     * Render health bar
     * - 添加阴影边框和背景
     * Add shadow border and background
     *
     * @param guiGraphics         GUI图形上下文，用于绘制操作
     *                            GUI graphics context for drawing operations
     * @param screenWidth         屏幕宽度，用于水平居中计算
     *                            Screen width for horizontal centering calculation
     * @param y                   血条Y坐标，从屏幕顶部开始计算
     *                            Health bar Y coordinate, calculated from top of screen
     * @param healthPercentage    生命值百分比 (0.0 - 1.0)
     *                            Health percentage (0.0 - 1.0)
     * @param toughnessPercentage 韧性值百分比 (0.0 - 1.0)
     *                            Toughness percentage (0.0 - 1.0)
     * @param width               血条宽度（像素）
     *                            Health bar width (pixels)
     * @param height              血条高度（像素）
     *                            Health bar height (pixels)
     * @param foregroundColor     前景颜色（十六进制格式，如"#FFC0CB"）
     *                            Foreground color (hexadecimal format, e.g., "#FFC0CB")
     * @param redOpacity          红色填充透明度 (0.0 - 1.0)
     *                            Red fill opacity (0.0 - 1.0)
     * @param colorDistribution   韧性颜色分布（可选，如果为null则使用默认白色）
     *                            Toughness color distribution (optional, uses default white if null)
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
     * 渲染颜色渐变过渡
     * Render color gradient transition
     *
     * @param guiGraphics   GUI图形上下文
     * @param startColor    起始颜色
     * @param endColor      结束颜色
     * @param startX        起始X坐标
     * @param endX          结束X坐标
     * @param y             Y坐标
     * @param height        高度
     * @param gradientWidth 渐变宽度
     */
    private static void renderGradientTransition(final GuiGraphics guiGraphics,
                                                 final com.github.chromabreak.system.ToughnessColor startColor,
                                                 final com.github.chromabreak.system.ToughnessColor endColor,
                                                 final float startX, final float endX,
                                                 final float y, final float height, final float gradientWidth) {
        final int startArgb = startColor.getArgb(1.0f);
        final int endArgb = endColor.getArgb(1.0f);

        // 提取起始和结束颜色的RGBA分量
        // Extract RGBA components from start and end colors
        final int startAlpha = (startArgb >> 24) & 0xFF;
        final int startRed = (startArgb >> 16) & 0xFF;
        final int startGreen = (startArgb >> 8) & 0xFF;
        final int startBlue = startArgb & 0xFF;

        final int endAlpha = (endArgb >> 24) & 0xFF;
        final int endRed = (endArgb >> 16) & 0xFF;
        final int endGreen = (endArgb >> 8) & 0xFF;
        final int endBlue = endArgb & 0xFF;

        // 计算渐变步数（每像素一个步长）
        // Calculate gradient steps (one step per pixel)
        final int gradientSteps = Math.max(1, (int) (endX - startX));

        // 逐像素渲染渐变
        // Render gradient pixel by pixel
        for (int i = 0; i < gradientSteps; i++) {
            final float progress = (float) i / (float) gradientSteps;

            // 线性插值计算当前像素的颜色
            // Linear interpolation to calculate current pixel color
            final int currentAlpha = (int) (startAlpha + (endAlpha - startAlpha) * progress);
            final int currentRed = (int) (startRed + (endRed - startRed) * progress);
            final int currentGreen = (int) (startGreen + (endGreen - startGreen) * progress);
            final int currentBlue = (int) (startBlue + (endBlue - startBlue) * progress);

            final int currentColor = (currentAlpha << 24) | (currentRed << 16) | (currentGreen << 8) | currentBlue;

            // 渲染当前像素
            // Render current pixel
            final int pixelX = (int) (startX + i);
            guiGraphics.fill(
                    pixelX, (int) y,
                    pixelX + 1, (int) (y + height),
                    currentColor
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

        // 多色分布：按百分比分段渲染，使用渐变过渡
        // Multi-color distribution: render segments with gradient transitions
        // 对颜色进行排序以确保渲染顺序一致
        // Sort colors to ensure consistent rendering order
        final java.util.List<java.util.Map.Entry<com.github.chromabreak.system.ToughnessColor, Float>> sortedEntries =
                new java.util.ArrayList<>(colorMap.entrySet());
        sortedEntries.sort((a, b) -> a.getKey().name().compareTo(b.getKey().name()));

        float currentX = x;
        final float totalWidth = width; // 保存总宽度
        float totalPercentage = 0.0f; // 用于验证百分比总和

        // 计算每个颜色段的起始和结束位置
        // Calculate start and end positions for each color segment
        final java.util.List<ColorSegment> segments = new java.util.ArrayList<>();
        for (final java.util.Map.Entry<com.github.chromabreak.system.ToughnessColor, Float> entry : sortedEntries) {
            final com.github.chromabreak.system.ToughnessColor color = entry.getKey();
            final float percentage = entry.getValue();
            totalPercentage += percentage;
            final float segmentWidth = totalWidth * percentage;

            if (0.01f < segmentWidth) {
                final float segmentStart = currentX;
                final float segmentEnd = currentX + segmentWidth;
                segments.add(new ColorSegment(color, segmentStart, segmentEnd));
                currentX = segmentEnd;
            }
        }

        // 如果还有剩余空间（由于浮点数精度问题），添加最后一个颜色段
        // If there's remaining space (due to floating point precision), add last color segment
        if (currentX < x + totalWidth - 0.5f) {
            final float remainingWidth = (x + totalWidth) - currentX;
            if (0.5f < remainingWidth && !sortedEntries.isEmpty()) {
                final com.github.chromabreak.system.ToughnessColor lastColor = sortedEntries.get(sortedEntries.size() - 1).getKey();
                segments.add(new ColorSegment(lastColor, currentX, x + totalWidth));
            }
        }

        // 渲染渐变韧性条
        // Render gradient toughness bar
        if (1 == segments.size()) {
            // 单一颜色：直接渲染
            // Single color: render directly
            final ColorSegment segment = segments.get(0);
            final int argb = segment.color.getArgb(1.0f);
            guiGraphics.fill(
                    (int) segment.start, (int) y,
                    (int) segment.end, (int) (y + height),
                    argb
            );
        } else if (1 < segments.size()) {
            // 多色：使用渐变渲染
            // Multiple colors: use gradient rendering

            // 计算渐变过渡区域宽度（总宽度的5%或10像素，取较小值）
            // Calculate gradient transition width (5% of total width or 10 pixels, whichever is smaller)
            final float gradientWidth = Math.min(totalWidth * 0.05f, 10.0f);

            // 逐段渲染，在段之间添加渐变过渡
            // Render segment by segment, adding gradient transitions between segments
            for (int i = 0; i < segments.size(); i++) {
                final ColorSegment currentSegment = segments.get(i);

                // 渲染当前颜色段的主体部分（减去渐变区域）
                // Render main part of current color segment (minus gradient area)
                final float mainSegmentEnd = (i < segments.size() - 1) ?
                        currentSegment.end - gradientWidth / 2 : currentSegment.end;

                if (currentSegment.start < mainSegmentEnd) {
                    final int argb = currentSegment.color.getArgb(1.0f);
                    guiGraphics.fill(
                            (int) currentSegment.start, (int) y,
                            (int) mainSegmentEnd, (int) (y + height),
                            argb
                    );
                }

                // 渲染到下一个颜色段的渐变过渡
                // Render gradient transition to next color segment
                if (i < segments.size() - 1) {
                    final ColorSegment nextSegment = segments.get(i + 1);
                    HealthBarRenderer.renderGradientTransition(guiGraphics,
                            currentSegment.color, nextSegment.color,
                            currentSegment.end - gradientWidth / 2,
                            currentSegment.end + gradientWidth / 2,
                            y, height, gradientWidth);
                }
            }
        }
    }

    /**
     * 颜色段信息类
     * Color segment information class
     */
    private record ColorSegment(ToughnessColor color, float start, float end) {
    }
}