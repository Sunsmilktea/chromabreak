package com.github.chromabreak.system;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * 模组兼容性管理器
 * 管理哪些伤害类型和模组可以绕过韧性系统
 * <p>
 * Mod Compatibility Manager
 * Manages which damage types and mods can bypass the toughness system
 */
public class ModCompatibilityManager {

    // 存储可以绕过韧性的伤害类型
    // Set of damage types that can bypass toughness
    private static final Set<String> BYPASS_DAMAGE_TYPES = new HashSet<>();

    // 存储可以绕过韧性的模组ID
    // Set of mod IDs that can bypass toughness
    private static final Set<String> BYPASS_MOD_IDS = new HashSet<>();

    // 已知的直接造成血量伤害的模组ID（从配置文件加载）
    // Known mod IDs that deal direct health damage (loaded from config file)
    private static final Set<String> DIRECT_DAMAGE_MOD_IDS = new HashSet<>();

    // 已知的直接造成血量伤害的物品ID模式（从配置文件加载）
    // Known item ID patterns that deal direct health damage (loaded from config file)
    private static final Set<String> DIRECT_DAMAGE_ITEM_PATTERNS = new HashSet<>();

    // 私有构造器防止实例化
    // Private constructor to prevent instantiation
    private ModCompatibilityManager() {
    }

    /**
     * 检查伤害源是否可以绕过韧性
     * Check if damage source can bypass toughness
     *
     * @param source 伤害源
     * @return 是否可以绕过韧性
     */
    public static boolean canBypassToughness(final DamageSource source) {
        if (null == source) {
            return false;
        }

        // 检查是否为虚空伤害（虚空伤害始终可以绕过韧性）
        // Check if it's void damage (void damage always bypasses toughness)
        // 在 Minecraft 1.21.1 中，通过检查伤害类型来判断虚空伤害
        // In Minecraft 1.21.1, check void damage by checking damage type
        try {
            if (null != source.typeHolder().unwrapKey().orElse(null)) {
                final String damageTypeId = source.typeHolder().unwrapKey().get().location().toString().toLowerCase();
                if (damageTypeId.contains("out_of_world") || damageTypeId.contains("void")) {
                    return true;
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
        }

        // 检查伤害源消息ID
        // Check damage source message ID
        final String msgId = source.getMsgId().toLowerCase();
        if (msgId.contains("outofworld") || msgId.contains("void")) {
            return true;
        }

        // 检查伤害类型
        // Check damage type
        final String damageType = source.getMsgId();
        if (ModCompatibilityManager.BYPASS_DAMAGE_TYPES.contains(damageType)) {
            return true;
        }

        // 检查伤害类型ID（1.21.1新系统）
        // Check damage type ID (1.21.1 new system)
        try {
            if (null != source.typeHolder().unwrapKey().orElse(null)) {
                final String damageTypeId = source.typeHolder().unwrapKey().get().location().toString();
                if (ModCompatibilityManager.BYPASS_DAMAGE_TYPES.contains(damageTypeId)) {
                    return true;
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
        }

        // 检查直接来源实体（攻击者）
        // Check direct source entity (attacker)
        final Entity directEntity = source.getDirectEntity();
        if (null != directEntity) {
            if (ModCompatibilityManager.isDirectDamageEntity(directEntity, source)) {
                return true;
            }
        }

        // 检查间接来源实体（如子弹的发射者）
        // Check indirect source entity (e.g., bullet shooter)
        final Entity entity = source.getEntity();
        if (null != entity && entity != directEntity) {
            if (ModCompatibilityManager.isDirectDamageEntity(entity, source)) {
                return true;
            }
        }

        // 检查模组ID（通过伤害源的来源实体或间接来源实体）
        // Check mod ID (through source entity or indirect source entity)
        if (null != directEntity) {
            final String modId = ModCompatibilityManager.getModIdFromEntity(directEntity);
            if (null != modId && ModCompatibilityManager.BYPASS_MOD_IDS.contains(modId)) {
                return true;
            }
            // 检查是否为已知的直接伤害模组
            // Check if it's a known direct damage mod
            if (null != modId && ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS.contains(modId)) {
                return true;
            }
        }

        if (null != entity && entity != directEntity) {
            final String modId = ModCompatibilityManager.getModIdFromEntity(entity);
            if (null != modId && ModCompatibilityManager.BYPASS_MOD_IDS.contains(modId)) {
                return true;
            }
            // 检查是否为已知的直接伤害模组
            // Check if it's a known direct damage mod
            if (null != modId && ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS.contains(modId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查实体是否为直接伤害实体（如子弹、投射物等）
     * Check if entity is a direct damage entity (e.g., bullet, projectile, etc.)
     *
     * @param entity 实体
     * @param source 伤害源
     * @return 是否为直接伤害实体
     */
    private static boolean isDirectDamageEntity(final Entity entity, final DamageSource source) {
        if (null == entity) {
            return false;
        }

        // 检查实体类型名称（子弹、投射物等）
        // Check entity type name (bullet, projectile, etc.)
        final String entityTypeName = entity.getType().toString().toLowerCase();
        if (entityTypeName.contains("bullet") ||
                entityTypeName.contains("projectile") ||
                entityTypeName.contains("ammo") ||
                entityTypeName.contains("shot")) {
            return true;
        }

        // 检查实体类名（通过反射）
        // Check entity class name (via reflection)
        final String className = entity.getClass().getName().toLowerCase();
        if (className.contains("bullet") ||
                className.contains("projectile") ||
                className.contains("ammo") ||
                className.contains("shot") ||
                className.contains("cgm") ||
                className.contains("combatguns") ||
                className.contains("techguns")) {
            return true;
        }

        // 检查伤害源消息ID（可能包含子弹、投射物等信息）
        // Check damage source message ID (may contain bullet, projectile, etc. information)
        final String msgId = source.getMsgId().toLowerCase();
        if (msgId.contains("bullet") ||
                msgId.contains("projectile") ||
                msgId.contains("ammo") ||
                msgId.contains("shot")) {
            return true;
        }

        return false;
    }

    /**
     * 从实体获取其所属模组的ID
     * Get the mod ID from an entity
     *
     * @param entity 实体
     * @return 模组ID，如果无法确定则为null
     */
    private static String getModIdFromEntity(final Entity entity) {
        if (null == entity) {
            return null;
        }

        // 方法1：从实体类型获取模组ID
        // Method 1: Get mod ID from entity type
        try {
            final net.minecraft.resources.ResourceLocation entityId = net.minecraft.world.entity.EntityType.getKey(entity.getType());
            if (null != entityId) {
                final String namespace = entityId.getNamespace();
                if (null != namespace && !"minecraft".equals(namespace)) {
                    return namespace;
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
        }

        // 方法2：从实体类名推断模组ID
        // Method 2: Infer mod ID from entity class name
        final String className = entity.getClass().getName().toLowerCase();
        for (final String modId : ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS) {
            if (className.contains(modId)) {
                return modId;
            }
        }

        // 方法3：如果是生物实体，检查其主手物品
        // Method 3: If it's a living entity, check its main hand item
        if (entity instanceof final LivingEntity livingEntity) {
            final ItemStack mainHandItem = livingEntity.getMainHandItem();
            if (!mainHandItem.isEmpty()) {
                final String modId = ModCompatibilityManager.getModIdFromItem(mainHandItem);
                if (null != modId) {
                    return modId;
                }
            }
        }

        return null;
    }

    /**
     * 从物品获取其所属模组的ID
     * Get the mod ID from an item
     *
     * @param itemStack 物品堆栈
     * @return 模组ID，如果无法确定则为null
     */
    private static String getModIdFromItem(final ItemStack itemStack) {
        if (null == itemStack || itemStack.isEmpty()) {
            return null;
        }

        try {
            final net.minecraft.resources.ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(itemStack.getItem());
            if (null != itemId) {
                final String namespace = itemId.getNamespace();
                if (null != namespace && !"minecraft".equals(namespace)) {
                    // 检查是否为已知的直接伤害物品
                    // Check if it's a known direct damage item
                    final String itemIdString = itemId.toString().toLowerCase();
                    for (final String pattern : ModCompatibilityManager.DIRECT_DAMAGE_ITEM_PATTERNS) {
                        if (itemIdString.startsWith(pattern)) {
                            return namespace;
                        }
                    }
                    return namespace;
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
        }

        return null;
    }

    /**
     * 添加可以绕过韧性的伤害类型
     * Add damage type that can bypass toughness
     *
     * @param damageType 伤害类型（如"magic", "fire", "drown"等）
     */
    public static void addBypassDamageType(final String damageType) {
        if (null != damageType && !damageType.isEmpty()) {
            ModCompatibilityManager.BYPASS_DAMAGE_TYPES.add(damageType);
        }
    }

    /**
     * 移除可以绕过韧性的伤害类型
     * Remove damage type that can bypass toughness
     *
     * @param damageType 伤害类型
     */
    public static void removeBypassDamageType(final String damageType) {
        if (null != damageType) {
            ModCompatibilityManager.BYPASS_DAMAGE_TYPES.remove(damageType);
        }
    }

    /**
     * 获取所有可以绕过韧性的伤害类型
     * Get all damage types that can bypass toughness
     *
     * @return 伤害类型集合
     */
    public static Set<String> getBypassDamageTypes() {
        return new HashSet<>(ModCompatibilityManager.BYPASS_DAMAGE_TYPES);
    }

    /**
     * 添加可以绕过韧性的模组ID
     * Add mod ID that can bypass toughness
     *
     * @param modId 模组ID
     */
    public static void addBypassModId(final String modId) {
        if (null != modId && !modId.isEmpty()) {
            ModCompatibilityManager.BYPASS_MOD_IDS.add(modId);
            // 同时添加到直接伤害模组ID列表（如果还没有）
            // Also add to direct damage mod IDs list (if not already present)
            ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS.add(modId);
        }
    }

    /**
     * 添加可以绕过韧性的物品ID模式
     * Add item ID pattern that can bypass toughness
     *
     * @param pattern 物品ID模式（使用 startsWith 匹配）
     */
    public static void addBypassItemPattern(final String pattern) {
        if (null != pattern && !pattern.isEmpty()) {
            ModCompatibilityManager.DIRECT_DAMAGE_ITEM_PATTERNS.add(pattern);
        }
    }

    /**
     * 移除可以绕过韧性的模组ID
     * Remove mod ID that can bypass toughness
     *
     * @param modId 模组ID
     */
    public static void removeBypassModId(final String modId) {
        if (null != modId) {
            ModCompatibilityManager.BYPASS_MOD_IDS.remove(modId);
        }
    }

    /**
     * 获取所有可以绕过韧性的模组ID
     * Get all mod IDs that can bypass toughness
     *
     * @return 模组ID集合
     */
    public static Set<String> getBypassModIds() {
        return new HashSet<>(ModCompatibilityManager.BYPASS_MOD_IDS);
    }

    /**
     * 检查伤害源是否为直接血量伤害（枪械、无尽贪婪等）
     * Check if damage source is direct health damage (guns, avaritia, etc.)
     *
     * @param source 伤害源
     * @return 是否为直接血量伤害
     */
    public static boolean isDirectHealthDamage(final DamageSource source) {
        if (null == source) {
            return false;
        }

        // 检查直接来源实体
        // Check direct source entity
        final Entity directEntity = source.getDirectEntity();
        if (null != directEntity) {
            // 检查是否为子弹、投射物等
            // Check if it's a bullet, projectile, etc.
            if (ModCompatibilityManager.isDirectDamageEntity(directEntity, source)) {
                return true;
            }

            // 检查实体所属模组
            // Check entity's mod
            final String modId = ModCompatibilityManager.getModIdFromEntity(directEntity);
            if (null != modId && ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS.contains(modId)) {
                return true;
            }
        }

        // 检查间接来源实体
        // Check indirect source entity
        final Entity entity = source.getEntity();
        if (null != entity && entity != directEntity) {
            final String modId = ModCompatibilityManager.getModIdFromEntity(entity);
            if (null != modId && ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS.contains(modId)) {
                return true;
            }
        }

        // 检查攻击者使用的武器
        // Check attacker's weapon
        if (entity instanceof final LivingEntity attacker) {
            final ItemStack mainHandItem = attacker.getMainHandItem();
            if (!mainHandItem.isEmpty()) {
                final String modId = ModCompatibilityManager.getModIdFromItem(mainHandItem);
                if (null != modId && ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS.contains(modId)) {
                    return true;
                }

                // 检查物品ID模式
                // Check item ID patterns
                try {
                    final net.minecraft.resources.ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(mainHandItem.getItem());
                    if (null != itemId) {
                        final String itemIdString = itemId.toString().toLowerCase();
                        for (final String pattern : ModCompatibilityManager.DIRECT_DAMAGE_ITEM_PATTERNS) {
                            if (itemIdString.startsWith(pattern)) {
                                return true;
                            }
                        }
                    }
                } catch (final Exception e) {
                    // Ignore exceptions
                }
            }
        }

        return false;
    }

    /**
     * 清除所有绕过配置
     * Clear all bypass configurations
     */
    public static void clearBypassConfigurations() {
        ModCompatibilityManager.BYPASS_DAMAGE_TYPES.clear();
        ModCompatibilityManager.BYPASS_MOD_IDS.clear();
        ModCompatibilityManager.DIRECT_DAMAGE_MOD_IDS.clear();
        ModCompatibilityManager.DIRECT_DAMAGE_ITEM_PATTERNS.clear();
    }
}

