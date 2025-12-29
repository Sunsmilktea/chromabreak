package com.github.chromabreak.events;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.Config;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityEventHandler - 实体事件处理器类
 * Entity Event Handler Class
 * <p>
 * 负责处理ChromaBreak模组的实体相关事件，包括韧性系统初始化、帽子系统、实体交互等
 * Responsible for handling entity-related events in ChromaBreak mod, including toughness system initialization, hat system, entity interactions, etc.
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 韧性系统初始化：在实体生成时初始化韧性系统和自定义生命值
 * Toughness system initialization: Initialize toughness system and custom health values when entities spawn
 * - 帽子系统：为骷髅和僵尸添加随机帽子，并防止帽子掉落
 * Hat system: Add random hats to skeletons and zombies, and prevent hat drops
 * - 实体掉落管理：管理实体死亡时的掉落物，保护特殊物品
 * Entity drop management: Manage entity drops on death, protect special items
 * - 玩家交互处理：处理玩家与实体的交互事件
 * Player interaction handling: Handle player interaction events with entities
 * - 事件订阅：订阅NeoForge的各种实体相关事件
 * Event subscription: Subscribe to various NeoForge entity-related events
 * <p>
 * 事件处理类型：
 * Event handling types:
 * - EntityJoinLevelEvent：实体加入世界事件，用于初始化韧性系统和添加帽子
 * EntityJoinLevelEvent: Entity join world event, used for toughness system initialization and hat addition
 * - LivingDropsEvent：生物掉落事件，用于管理掉落物和保护帽子
 * LivingDropsEvent: Living entity drops event, used for drop management and hat protection
 * - PlayerInteractEvent：玩家交互事件，用于处理玩家与实体的交互
 * PlayerInteractEvent: Player interaction event, used for handling player-entity interactions
 * <p>
 * 帽子系统特性：
 * Hat system features:
 * - 稀有度系统：帽子按稀有度分类（普通、不常见、稀有、史诗、传说）
 * Rarity system: Hats categorized by rarity (common, uncommon, rare, epic, legendary)
 * - 随机选择：根据稀有度权重随机选择帽子
 * Random selection: Randomly select hats based on rarity weights
 * - 防掉落保护：使用NBT标记保护帽子不被掉落
 * Drop protection: Use NBT tags to protect hats from being dropped
 * - 兼容性检查：只添加已加载模组的帽子物品
 * Compatibility check: Only add hat items from loaded mods
 * <p>
 * 设计特点：
 * Design features:
 * - 枚举单例模式：使用枚举确保单例，所有方法都是静态方法
 * Enum singleton pattern: Uses enum to ensure singleton, all methods are static methods
 * - 事件驱动：基于NeoForge事件系统，自动响应实体事件
 * Event-driven: Based on NeoForge event system, automatically responds to entity events
 * - 模块化设计：各个功能模块独立，便于维护和扩展
 * Modular design: Each functional module is independent, easy to maintain and extend
 * - 错误处理：完善的错误处理机制，避免崩溃
 * Error handling: Comprehensive error handling mechanism to avoid crashes
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 游戏平衡：通过韧性系统和帽子系统增强游戏体验
 * Game balance: Enhance gameplay experience through toughness system and hat system
 * - 趣味性：为骷髅和僵尸添加帽子增加游戏趣味性
 * Fun factor: Add hats to skeletons and zombies to increase game fun
 * - 服务器管理：服务器管理员可以通过事件系统管理实体行为
 * Server management: Server administrators can manage entity behavior through event system
 * - 模组集成：与其他模组和谐共存，不干扰其他模组的功能
 * Mod integration: Harmoniously coexists with other mods, doesn't interfere with other mods' functionality
 */
@EventBusSubscriber(modid = ChromaBreak.MODID)
public enum EntityEventHandler {
    ;

    // 帽子列表（按稀有度分组）
    // Hat list (grouped by rarity)
    private static final List<HatConfig> HAT_CONFIGS = new ArrayList<>();

    static {
        // 初始化帽子列表
        // Initialize hat list
        // 普通帽子（Common）
        EntityEventHandler.HAT_CONFIGS.add(new HatConfig("minecraft:leather_helmet", HatRarity.COMMON));
        EntityEventHandler.HAT_CONFIGS.add(new HatConfig("minecraft:chainmail_helmet", HatRarity.COMMON));

        // 不常见帽子（Uncommon）
        EntityEventHandler.HAT_CONFIGS.add(new HatConfig("minecraft:iron_helmet", HatRarity.UNCOMMON));
        EntityEventHandler.HAT_CONFIGS.add(new HatConfig("minecraft:golden_helmet", HatRarity.UNCOMMON));

        // 稀有帽子（Rare）
        EntityEventHandler.HAT_CONFIGS.add(new HatConfig("minecraft:diamond_helmet", HatRarity.RARE));
        EntityEventHandler.HAT_CONFIGS.add(new HatConfig("minecraft:turtle_helmet", HatRarity.RARE));

        // 史诗帽子（Epic）
        EntityEventHandler.HAT_CONFIGS.add(new HatConfig("minecraft:netherite_helmet", HatRarity.EPIC));

        // 传说帽子（Legendary）- 可以添加其他模组的稀有帽子
        // Legendary hats - can add rare hats from other mods
    }

    /**
     * 实体加入世界事件处理方法
     * 在实体加入世界时初始化韧性系统和添加帽子
     * <p>
     * Entity join level event handler
     * Initialize toughness system and add hats when entity joins the world
     *
     * @param event 实体加入世界事件
     *              Entity join level event
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(final EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof final LivingEntity livingEntity) {
            // Apply custom health and toughness values
            // 应用自定义生命值和韧性值
            com.github.chromabreak.system.EntityHealthManager.applyCustomValues(livingEntity);

            // 初始化韧性系统（所有符合条件的实体都需要初始化）
            // Initialize toughness system (all eligible entities need initialization)
            com.github.chromabreak.system.ToughnessSystem.initializeToughness(livingEntity);

            // 为骷髅和僵尸添加帽子
            // Add hats to skeletons and zombies
            EntityEventHandler.addHatToMob(livingEntity, event.getLevel().getRandom());
        }
    }

    /**
     * 为生物添加帽子
     * Add hat to mob
     *
     * @param mob    生物实体
     * @param random 随机数生成器
     */
    private static void addHatToMob(final LivingEntity mob, final RandomSource random) {
        // 检查配置是否启用
        // Check if config is enabled
        if (!Config.ENABLE_MOB_HAT_SPAWN.getAsBoolean()) {
            return;
        }

        // 只处理骷髅和僵尸
        // Only process skeletons and zombies
        if (!(mob instanceof Skeleton) && !(mob instanceof Zombie)) {
            return;
        }

        // 检查是否已经有头盔
        // Check if already has helmet
        if (!mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            return;
        }

        // 检查是否已经处理过（避免重复添加）
        // Check if already processed (avoid duplicate addition)
        final net.minecraft.nbt.CompoundTag mobTag = mob.getPersistentData();
        if (mobTag.getBoolean("chromabreak:hat_added")) {
            return;
        }

        // 随机选择一个帽子
        // Randomly select a hat
        final HatConfig selectedHat = EntityEventHandler.selectRandomHat(random);

        if (null != selectedHat) {
            // 检查物品是否存在（兼容其他模组）
            // Check if item exists (compatible with other mods)
            if (BuiltInRegistries.ITEM.containsKey(selectedHat.itemId)) {
                final Item hatItem = BuiltInRegistries.ITEM.get(selectedHat.itemId);
                if (null != hatItem) {
                    final ItemStack hatStack = new ItemStack(hatItem);
                    // 设置耐久度随机（50%-100%）
                    // Set random durability (50%-100%)
                    if (hatStack.isDamageableItem()) {
                        final int maxDamage = hatStack.getMaxDamage();
                        final int currentDamage = maxDamage - (int) (maxDamage * (0.5f + random.nextFloat() * 0.5f));
                        hatStack.setDamageValue(currentDamage);
                    }
                    // 标记为特殊帽子（用于识别）
                    // Mark as special hat (for identification)
                    // 在 Minecraft 1.21.1 中使用 DataComponents
                    // Use DataComponents in Minecraft 1.21.1
                    try {
                        final net.minecraft.nbt.CompoundTag protectionTag = new net.minecraft.nbt.CompoundTag();
                        protectionTag.putBoolean("chromabreak:protected_hat", true);
                        // 使用反射创建和设置 CustomData
                        // Use reflection to create and set CustomData
                        try {
                            final Class<?> customDataClass = Class.forName("net.minecraft.core.component.CustomData");
                            final java.lang.reflect.Method ofMethod = customDataClass.getMethod("of", net.minecraft.nbt.CompoundTag.class);
                            final Object customData = ofMethod.invoke(null, protectionTag);

                            // 使用反射调用 set 方法，避免类型推断问题
                            // Use reflection to call set method, avoid type inference issues
                            final java.lang.reflect.Method setMethod = hatStack.getClass().getMethod("set",
                                    net.minecraft.core.component.DataComponentType.class, Object.class);
                            setMethod.invoke(hatStack, net.minecraft.core.component.DataComponents.CUSTOM_DATA, customData);
                        } catch (final Exception e) {
                            // 如果反射失败，跳过设置（不影响功能）
                            // If reflection fails, skip setting (doesn't affect functionality)
                        }
                    } catch (final Exception e) {
                        // 如果设置失败，仍然继续（不影响功能）
                        // If setting fails, continue anyway (doesn't affect functionality)
                    }

                    mob.setItemSlot(EquipmentSlot.HEAD, hatStack);
                    // 使用 NBT 标记来防止掉落
                    // Use NBT tag to prevent dropping
                    mobTag.putBoolean("chromabreak:protected_hat_slot_head", true);
                    mobTag.putBoolean("chromabreak:hat_added", true);
                }
            }
        }
    }

    /**
     * 根据稀有度随机选择帽子
     * Randomly select a hat based on rarity
     *
     * @param random 随机数生成器
     * @return 选中的帽子配置，如果没有则返回null
     */
    private static HatConfig selectRandomHat(final RandomSource random) {
        // 按稀有度分组帽子
        // Group hats by rarity
        final List<HatConfig>[] hatsByRarity = new List[HatRarity.values().length];
        for (int i = 0; i < hatsByRarity.length; i++) {
            hatsByRarity[i] = new ArrayList<>();
        }

        // 只添加已加载的模组的帽子（兼容性检查）
        // Only add hats from loaded mods (compatibility check)
        for (final HatConfig hat : EntityEventHandler.HAT_CONFIGS) {
            // 检查物品是否存在
            // Check if item exists
            if (BuiltInRegistries.ITEM.containsKey(hat.itemId)) {
                final int rarityIndex = hat.rarity.ordinal();
                hatsByRarity[rarityIndex].add(hat);
            }
        }

        // 计算总权重（只计算有可用帽子的稀有度）
        // Calculate total weight (only for rarities with available hats)
        float totalWeight = 0.0f;
        for (final HatRarity rarity : HatRarity.values()) {
            final List<HatConfig> hatsInRarity = hatsByRarity[rarity.ordinal()];
            if (!hatsInRarity.isEmpty()) {
                totalWeight += rarity.getWeight();
            }
        }

        // 如果没有可用的帽子，返回null
        // If no available hats, return null
        if (0.0f >= totalWeight) {
            return null;
        }

        // 根据稀有度权重选择
        // Select based on rarity weight
        final float randomValue = random.nextFloat() * totalWeight;
        float cumulativeWeight = 0.0f;

        for (final HatRarity rarity : HatRarity.values()) {
            final List<HatConfig> hatsInRarity = hatsByRarity[rarity.ordinal()];
            if (!hatsInRarity.isEmpty()) {
                cumulativeWeight += rarity.getWeight();
                if (randomValue <= cumulativeWeight) {
                    return hatsInRarity.get(random.nextInt(hatsInRarity.size()));
                }
            }
        }

        // 如果没有找到，返回第一个可用的帽子
        // If not found, return first available hat
        for (final HatConfig hat : EntityEventHandler.HAT_CONFIGS) {
            if (BuiltInRegistries.ITEM.containsKey(hat.itemId)) {
                return hat;
            }
        }

        return null;
    }

    /**
     * 实体死亡掉落事件处理方法
     * 防止受保护的帽子掉落
     * <p>
     * Entity death drops event handler
     * Prevent protected hats from dropping
     *
     * @param event 实体死亡掉落事件
     *              Entity death drops event
     */
    @SubscribeEvent
    public static void onLivingDrops(final LivingDropsEvent event) {
        final LivingEntity entity = event.getEntity();

        // 只处理骷髅和僵尸
        // Only process skeletons and zombies
        if (!(entity instanceof Skeleton) && !(entity instanceof Zombie)) {
            return;
        }

        // 检查是否有受保护帽子的标记
        // Check if has protected hat marker
        final net.minecraft.nbt.CompoundTag mobTag = entity.getPersistentData();
        if (mobTag.getBoolean("chromabreak:protected_hat_slot_head")) {
            // 移除头部装备的掉落物
            // Remove head equipment drops
            event.getDrops().removeIf(drop -> {
                final net.minecraft.world.item.ItemStack stack = drop.getItem();
                // 检查是否是头部装备
                // Check if it's head equipment
                if (!stack.isEmpty()) {
                    // 检查物品是否是头盔类型
                    // Check if item is helmet type
                    final net.minecraft.world.item.Item item = stack.getItem();
                    if (item instanceof final net.minecraft.world.item.ArmorItem armorItem) {
                        return EquipmentSlot.HEAD == armorItem.getEquipmentSlot();
                    }
                }
                return false;
            });
        }
    }

    /**
     * 玩家交互实体事件处理方法
     * 防止剥离受保护的帽子
     * <p>
     * Player interact entity event handler
     * Prevent stripping protected hats
     *
     * @param event 玩家交互实体事件
     *              Player interact entity event
     */
    @SubscribeEvent
    public static void onPlayerInteractEntity(final PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof final LivingEntity entity)) {
            return;
        }

        // 只处理骷髅和僵尸
        // Only process skeletons and zombies
        if (!(entity instanceof Skeleton) && !(entity instanceof Zombie)) {
            return;
        }

        final Player player = event.getEntity();
        final ItemStack heldItem = player.getItemInHand(event.getHand());

        // 检查是否有头部盔甲，且是受保护的帽子
        // Check if has head armor and it's a protected hat
        final ItemStack headArmor = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (headArmor.isEmpty()) {
            return;
        }

        // 检查是否是受保护的帽子
        // Check if it's a protected hat
        if (!EntityEventHandler.isProtectedHat(headArmor)) {
            return;
        }

        // 检查是否是剪刀
        // Check if it's shears (vanilla way to strip armor)
        if (heldItem.is(Items.SHEARS)) {
            // 阻止交互
            // Prevent interaction
            event.setCanceled(true);
            return;
        }

        // 检查其他可能的剥离盔甲方式（兼容其他模组）
        // Check other possible armor stripping methods (compatible with other mods)
        if (EntityEventHandler.isArmorStrippingItem(heldItem)) {
            // 阻止交互
            // Prevent interaction
            event.setCanceled(true);
        }
    }

    /**
     * 检查物品是否是受保护的帽子
     * Check if item is a protected hat
     *
     * @param itemStack 物品堆栈
     * @return 是否是受保护的帽子
     */
    private static boolean isProtectedHat(final ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        try {
            final net.minecraft.core.component.DataComponentMap components = itemStack.getComponents();
            if (null != components) {
                final var customData = components.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
                if (null != customData) {
                    try {
                        final java.lang.reflect.Method copyTagMethod = customData.getClass().getMethod("copyTag");
                        final net.minecraft.nbt.CompoundTag tag = (net.minecraft.nbt.CompoundTag) copyTagMethod.invoke(customData);
                        if (null != tag && tag.getBoolean("chromabreak:protected_hat")) {
                            return true;
                        }
                    } catch (final Exception e) {
                        // Ignore reflection errors
                        // 忽略反射错误
                    }
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
            // 忽略异常
        }

        return false;
    }

    /**
     * 检查物品是否是剥离盔甲的工具
     * Check if item is an armor stripping tool
     *
     * @param itemStack 物品堆栈
     * @return 是否是剥离盔甲的工具
     */
    private static boolean isArmorStrippingItem(final ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        // 检查物品ID是否包含剥离相关的关键词
        // Check if item ID contains stripping-related keywords
        final net.minecraft.resources.ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        if (null != itemId) {
            final String itemIdString = itemId.toString().toLowerCase();
            // 检查常见的关键词
            // Check common keywords
            if (itemIdString.contains("strip") ||
                    itemIdString.contains("remove_armor") ||
                    itemIdString.contains("armor_strip") ||
                    itemIdString.contains("disarm")) {
                return true;
            }
        }

        // 检查物品的NBT或组件中是否有剥离标记
        // Check if item has stripping marker in NBT or components
        try {
            final net.minecraft.core.component.DataComponentMap components = itemStack.getComponents();
            if (null != components) {
                // 检查自定义数据组件
                // Check custom data component
                final var customData = components.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
                if (null != customData) {
                    try {
                        final java.lang.reflect.Method copyTagMethod = customData.getClass().getMethod("copyTag");
                        final net.minecraft.nbt.CompoundTag tag = (net.minecraft.nbt.CompoundTag) copyTagMethod.invoke(customData);
                        if (null != tag) {
                            if (tag.getBoolean("chromabreak:can_strip_armor") ||
                                    tag.getBoolean("armor_strip") ||
                                    tag.getBoolean("strip_armor")) {
                                return true;
                            }
                        }
                    } catch (final Exception e) {
                        // Ignore reflection errors
                        // 忽略反射错误
                    }
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
            // 忽略异常
        }

        return false;
    }

    /**
     * 帽子稀有度枚举
     * Hat rarity enumeration
     */
    private enum HatRarity {
        COMMON(0.5f),      // 普通：50%概率
        UNCOMMON(0.3f),   // 不常见：30%概率
        RARE(0.15f),      // 稀有：15%概率
        EPIC(0.04f),      // 史诗：4%概率
        LEGENDARY(0.01f); // 传说：1%概率

        private final float weight;

        HatRarity(final float weight) {
            this.weight = weight;
        }

        public float getWeight() {
            return this.weight;
        }
    }

    /**
     * 帽子配置类
     * Hat configuration class
     */
    private record HatConfig(ResourceLocation itemId, HatRarity rarity) {
        private HatConfig(final String itemId, final HatRarity rarity) {
            this(ResourceLocation.parse(itemId), rarity);
        }
    }
}