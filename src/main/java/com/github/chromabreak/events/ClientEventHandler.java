package com.github.chromabreak.events;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.render.HealthBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

/**
 * 客户端事件处理器类
 * 负责处理客户端渲染事件，显示血条HUD
 * <p>
 * Client Event Handler Class
 * Responsible for handling client rendering events, displaying health bar HUD
 */
@EventBusSubscriber(modid = ChromaBreak.MODID, value = Dist.CLIENT)
public enum ClientEventHandler {
    ;

    /**
     * 渲染GUI覆盖层事件处理方法
     * 在游戏窗口顶部中间显示光标指向生物的血条
     * <p>
     * Render GUI overlay event handler
     * Display health bar of entity under cursor at top center of game window
     */
    @SubscribeEvent
    public static void onRenderGuiOverlay(final RenderGuiEvent.Post event) {
        // Update cached configuration values
        // 更新缓存的配置值
        PerformanceCache.updateCachedConfig();

        // Don't render if disabled in config
        // 如果配置禁用则不渲染
        if (!PerformanceCache.getShowHealthBar()) {
            return;
        }

        final Minecraft minecraft = Minecraft.getInstance();

        // Check if player exists and is in game
        // 检查玩家是否存在且在游戏中
        if (null == minecraft.player || null == minecraft.level) {
            return;
        }

        // Get the entity the player is looking at
        // 获取玩家正在查看的实体
        final net.minecraft.world.phys.EntityHitResult hitResult = minecraft.hitResult instanceof net.minecraft.world.phys.EntityHitResult
                ? (net.minecraft.world.phys.EntityHitResult) minecraft.hitResult
                : null;

        if (null == hitResult || !(hitResult.getEntity() instanceof final LivingEntity targetEntity)) {
            return;
        }

        // Don't render health bar for players or dead entities
        // 不为玩家或死亡的实体渲染血条
        if (targetEntity instanceof Player || !targetEntity.isAlive()) {
            return;
        }

        // Performance optimization: check distance limit
        // 性能优化：检查距离限制
        if (!PerformanceCache.shouldRender(targetEntity, minecraft.player)) {
            return;
        }

        // Additional compatibility checks for modded entities
        // 为模组生物添加额外的兼容性检查
        try {
            // Check if entity has valid health values
            // 检查实体是否有有效的生命值
            final float health = targetEntity.getHealth();
            final float maxHealth = targetEntity.getMaxHealth();

            // Validate health values to prevent issues with modded entities
            // 验证生命值以防止模组生物出现问题
            if (0 > health || 0 >= maxHealth || Float.isNaN(health) || Float.isNaN(maxHealth) || Float.isInfinite(health) || Float.isInfinite(maxHealth)) {
                return;
            }

            // Calculate health percentage with safety checks
            // 计算生命值百分比并添加安全检查
            final float healthPercentage = Math.min(Math.max(health / maxHealth, 0.0f), 1.0f);

            // Only render if health percentage is meaningful
            // 只有当生命值百分比有意义时才渲染
            if (0.0f >= healthPercentage) {
                return;
            }

            // Render health bar at top center of screen
            // 在屏幕顶部中间渲染血条
            final GuiGraphics guiGraphics = event.getGuiGraphics();
            final int screenWidth = minecraft.getWindow().getGuiScaledWidth();

            // Position at top center, with some offset from top
            // 定位在顶部中间，距离顶部有一些偏移
            final int barY = 20; // Offset from top
            // 距离顶部的偏移量

            // Get toughness value for the entity using the toughness system
            // 使用韧性系统获取实体的韧性值
            final float toughnessPercentage = com.github.chromabreak.system.ToughnessSystem.getToughnessPercentage(targetEntity);

            // Get toughness color distribution for the entity
            // 获取实体的韧性颜色分布
            final com.github.chromabreak.system.ToughnessColorDistribution colorDistribution =
                    com.github.chromabreak.system.ToughnessSystem.getColorDistribution(targetEntity);

            // Use HealthBarRenderer to render the health bar and toughness bar with cached config values
            // 使用HealthBarRenderer渲染血条和韧性条，使用缓存的配置值
            HealthBarRenderer.renderHealthBarHUD(
                    guiGraphics,
                    screenWidth,
                    barY,
                    healthPercentage,
                    toughnessPercentage,
                    PerformanceCache.getWidth(),
                    PerformanceCache.getHeight(),
                    PerformanceCache.getForegroundColor(),
                    PerformanceCache.getRedOpacity(),
                    colorDistribution
            );
        } catch (final Exception e) {
            // Silently ignore exceptions from modded entities to prevent crashes
            // 静默忽略模组生物的异常以防止崩溃
            return;
        }
    }


}

/**
 * 性能优化相关变量
 * Performance optimization related variables
 */
enum PerformanceCache {
    ;
    // 最大渲染距离（方块）
    // Maximum render distance (blocks)
    private static final double MAX_RENDER_DISTANCE = 20.0;
    private static final long CONFIG_CHECK_INTERVAL = 1000; // 1 second
    // 缓存的配置值
    // Cached configuration values
    private static boolean showHealthBarCached = true;
    private static float widthCached = 150.0f;
    private static float heightCached = 4.0f;
    private static String foregroundColorCached = "#FFC0CB";
    private static float redOpacityCached = 0.9f;
    private static long lastConfigCheckTime;

    /**
     * 检查是否应该渲染（基于距离）
     * Check if should render (based on distance)
     */
    public static boolean shouldRender(final LivingEntity targetEntity, final Player player) {
        // 检查距离限制
        // Check distance limit
        return PerformanceCache.MAX_RENDER_DISTANCE * PerformanceCache.MAX_RENDER_DISTANCE >= player.distanceToSqr(targetEntity);
    }

    /**
     * 获取缓存的配置值，避免频繁读取配置
     * Get cached configuration values to avoid frequent config reads
     */
    public static void updateCachedConfig() {
        final long currentTime = System.currentTimeMillis();
        if (PerformanceCache.CONFIG_CHECK_INTERVAL < currentTime - PerformanceCache.lastConfigCheckTime) {
            PerformanceCache.showHealthBarCached = com.github.chromabreak.Config.SHOW_ENTITY_HEALTH_BAR.getAsBoolean();
            PerformanceCache.widthCached = (float) com.github.chromabreak.Config.ENTITY_HEALTH_BAR_WIDTH.getAsDouble();
            PerformanceCache.heightCached = (float) com.github.chromabreak.Config.ENTITY_HEALTH_BAR_HEIGHT.getAsDouble();
            PerformanceCache.foregroundColorCached = com.github.chromabreak.Config.HEALTH_BAR_FOREGROUND_COLOR.get();
            PerformanceCache.redOpacityCached = (float) com.github.chromabreak.Config.HEALTH_BAR_RED_OPACITY.getAsDouble();
            PerformanceCache.lastConfigCheckTime = currentTime;
        }
    }

    public static boolean getShowHealthBar() {
        return PerformanceCache.showHealthBarCached;
    }

    public static float getWidth() {
        return PerformanceCache.widthCached;
    }

    public static float getHeight() {
        return PerformanceCache.heightCached;
    }

    public static String getForegroundColor() {
        return PerformanceCache.foregroundColorCached;
    }

    public static float getRedOpacity() {
        return PerformanceCache.redOpacityCached;
    }
}
