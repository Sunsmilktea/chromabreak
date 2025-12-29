package com.github.chromabreak;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.chromabreak.tool.JsonTool.isValidJson;

/**
 * Config - 模组配置管理器
 * Mod Configuration Manager
 * <p>
 * 负责管理ChromaBreak模组的所有配置选项，使用NeoForge的配置系统
 * Responsible for managing all configuration options for ChromaBreak mod, using NeoForge's configuration system
 * <p>
 * 主要配置类别包括：
 * Main configuration categories include:
 * - 血条显示控制配置（是否显示、尺寸、位置等）
 * Health bar display control (show/hide, size, position, etc.)
 * - 血条外观配置（颜色、透明度、背景等）
 * Health bar appearance (colors, transparency, background, etc.)
 * - 生物生成配置（帽子生成、实体配置等）
 * Mob spawn configuration (hat spawning, entity configuration, etc.)
 * - 模组兼容性配置（绕过韧性系统的模组和物品）
 * Mod compatibility configuration (mods and items that bypass toughness system)
 * - 韧性削减配置（基于防御值的韧性削减幅度）
 * Toughness reduction configuration (reduction percentage based on defense)
 * - 世界生成配置（橙色水晶晶洞生成概率等）
 * World generation configuration (orange crystal geode probability, etc.)
 * <p>
 * 使用枚举模式确保单例，所有配置项都是静态常量
 * Uses enum pattern to ensure singleton, all configuration items are static constants
 * <p>
 * 配置项使用NeoForge的ModConfigSpec系统，支持热重载和配置文件管理
 * Configuration items use NeoForge's ModConfigSpec system, supporting hot reloading and config file management
 */
public enum Config {
    ;

    /**
     * 配置构建器，用于构建配置规范
     * <p>
     * Configuration builder for building config specification
     */
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    /**
     * 是否显示生物血条HUD
     * 布尔值配置选项，默认值为true（启用血条显示）
     * <p>
     * Whether to show health bar HUD for entities
     * Boolean configuration option with default value true (enable health bar display)
     */
    public static final ModConfigSpec.BooleanValue SHOW_ENTITY_HEALTH_BAR = Config.BUILDER
            .comment("Whether to show health bar HUD for entities\n是否显示生物血条HUD")
            .define("showEntityHealthBar", true);

    // ===== 血条显示控制配置 =====
    // ===== Health Bar Display Control Configuration =====
    /**
     * 生物血条HUD长度配置
     * 双精度浮点数配置选项，默认值为50.0，范围从10.0到200.0
     * 最小值：10.0，最大值：200.0
     * <p>
     * Health bar HUD width configuration for entities
     * Double configuration option with default value 50.0, range from 10.0 to 200.0
     * Minimum value: 10.0, Maximum value: 200.0
     */
    public static final ModConfigSpec.DoubleValue ENTITY_HEALTH_BAR_WIDTH = Config.BUILDER
            .comment("Health bar HUD width for entities. Minimum: 10.0, Maximum: 200.0\n生物血条HUD长度配置。最小值：10.0，最大值：200.0")
            .defineInRange("entityHealthBarWidth", 150.0, 10.0, 200.0);

    // ===== 血条尺寸配置 =====
    // ===== Health Bar Size Configuration =====
    /**
     * 生物血条HUD高度配置
     * 双精度浮点数配置选项，默认值为8.0，范围从4.0到20.0
     * 最小值：4.0，最大值：20.0
     * <p>
     * Health bar HUD height configuration for entities
     * Double configuration option with default value 8.0, range from 4.0 to 20.0
     * Minimum value: 4.0, Maximum value: 20.0
     */
    public static final ModConfigSpec.DoubleValue ENTITY_HEALTH_BAR_HEIGHT = Config.BUILDER
            .comment("Health bar HUD height for entities. Minimum: 4.0, Maximum: 20.0\n生物血条HUD高度配置。最小值：4.0，最大值：20.0")
            .defineInRange("entityHealthBarHeight", 4.0, 4.0, 20.0);
    /**
     * 血条背景颜色配置
     * 字符串配置选项，默认值为"#80808080"（半透明灰色）
     * <p>
     * Health bar background color configuration
     * String configuration option with default value "#80808080" (semi-transparent gray)
     */
    public static final ModConfigSpec.ConfigValue<String> HEALTH_BAR_BACKGROUND_COLOR = Config.BUILDER
            .comment("Health bar background color in hex format (e.g., #80808080 for semi-transparent gray)\n血条背景颜色配置（十六进制格式，例如：#80808080表示半透明灰色）")
            .define("healthBarBackgroundColor", "#80808080");

    // ===== 血条外观配置 =====
    // ===== Health Bar Appearance Configuration =====
    /**
     * 血条前景颜色配置
     * 字符串配置选项，默认值为"#FFFF6EC7"（粉色）
     * <p>
     * Health bar foreground (filled) color configuration
     * String configuration option with default value "#FFFF6EC7" (pink)
     */
    public static final ModConfigSpec.ConfigValue<String> HEALTH_BAR_FOREGROUND_COLOR = Config.BUILDER
            .comment("Health bar foreground (filled) color in hex format (e.g., #FFC0CB for pink)\n血条前景（填充）颜色配置（十六进制格式，例如：#FFC0CB表示粉色）")
            .define("healthBarForegroundColor", "#FFC0CB");
    /**
     * 血条红色填充透明度配置
     * 双精度浮点数配置选项，默认值为0.9，范围从0.0到1.0
     * 最小值：0.0（完全透明），最大值：1.0（完全不透明）
     * <p>
     * Health bar red fill opacity configuration
     * Double configuration option with default value 0.9, range from 0.0 to 1.0
     * Minimum value: 0.0 (fully transparent), Maximum value: 1.0 (fully opaque)
     */
    public static final ModConfigSpec.DoubleValue HEALTH_BAR_RED_OPACITY = Config.BUILDER
            .comment("Health bar red fill opacity. 0.0 for fully transparent, 1.0 for fully opaque. Minimum: 0.0, Maximum: 1.0\n血条红色填充透明度配置。0.0表示完全透明，1.0表示完全不透明。最小值：0.0，最大值：1.0")
            .defineInRange("healthBarRedOpacity", 0.9, 0.0, 1.0);

    // ===== 生物生成配置 =====
    // ===== Mob Spawn Configuration =====
    /**
     * 是否启用骷髅和僵尸生成时必定有帽子
     * 布尔值配置选项，默认值为true（启用）
     * <p>
     * Whether skeletons and zombies always spawn with hats
     * Boolean configuration option with default value true (enabled)
     */
    public static final ModConfigSpec.BooleanValue ENABLE_MOB_HAT_SPAWN = Config.BUILDER
            .comment("Whether skeletons and zombies always spawn with hats\n是否启用骷髅和僵尸生成时必定有帽子")
            .define("enableMobHatSpawn", true);

    // ===== 实体配置 =====
    // ===== Entity Configuration =====
    /**
     * 实体配置列表（JSON格式字符串列表）
     * 每个字符串是一个实体配置，格式：
     * {"entityType":"minecraft:zombie","maxHealth":30.0,"maxToughness":100.0,"toughnessColor":"red"}
     * 或
     * {"entityType":"minecraft:spider","maxHealth":20.0,"maxToughness":80.0,"toughnessColors":{"orange":0.4,"yellow":0.3,"green":0.2,"white":0.1}}
     * <p>
     * Entity configuration list (JSON format string list)
     * Each string is an entity configuration, format:
     * {"entityType":"minecraft:zombie","maxHealth":30.0,"maxToughness":100.0,"toughnessColor":"red"}
     * or
     * {"entityType":"minecraft:spider","maxHealth":20.0,"maxToughness":80.0,"toughnessColors":{"orange":0.4,"yellow":0.3,"green":0.2,"white":0.1}}
     */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITY_CONFIGS = Config.BUILDER
            .comment(
                    "Entity configuration list (JSON format).\n",
                    "实体配置列表（JSON格式）\n",
                    "For detailed instructions and examples, please refer to chromabreak-common.toml.example file.\n",
                    "详细说明和示例请参考 chromabreak-common.toml.example 文件。\n",
                    "Available colors (7 types, one-to-one with Minecraft dyes): red, blue, green, yellow, white, black, orange\n",
                    "可用颜色（7种，与Minecraft染料一一对应）：red, blue, green, yellow, white, black, orange"
            )
            .defineList(
                    "entityConfigs",
                    Collections::emptyList,  // 默认空列表（supplier）
                    () -> "{ \"example\": \"replace with real JSON\" }",  // 在配置 GUI 中点击“+”添加新条目时的默认值（一个占位 JSON 字符串）
                    obj -> obj instanceof String && !((String) obj).isBlank() && isValidJson((String) obj)  // 验证：必须是非空字符串且是合法 JSON
            );

    // ===== 模组兼容性配置 =====
    // ===== Mod Compatibility Configuration =====
    /**
     * 可以绕过韧性系统的模组ID列表
     * 这些模组的伤害会直接造成血量伤害，不经过韧性系统
     * <p>
     * List of mod IDs that can bypass the toughness system
     * Damage from these mods will deal direct health damage, bypassing the toughness system
     */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BYPASS_MOD_IDS = Config.BUILDER
            .comment(
                    "List of mod IDs that can bypass the toughness system.\n",
                    "Damage from these mods will deal direct health damage, bypassing the toughness system.\n",
                    "Examples: cgm, avaritia, techguns, mekanism\n",
                    "可以绕过韧性系统的模组ID列表。\n",
                    "这些模组的伤害会直接造成血量伤害，不经过韧性系统。\n",
                    "示例：cgm, avaritia, techguns, mekanism"
            )
            .defineList(
                    "bypassModIds",
                    () -> Arrays.asList("cgm", "combatguns", "guns", "avaritia", "mekanism", "techguns", "immersiveengineering"),  // 默认值
                    () -> "",  // 在配置界面点击“+”添加新元素时的默认值（空字符串，玩家可以直接输入）
                    obj -> obj instanceof String && !((String) obj).isBlank()  // 验证：必须是字符串且不为空白
            );

    /**
     * 可以绕过韧性系统的物品ID模式列表
     * 匹配这些模式的物品造成的伤害会直接造成血量伤害，不经过韧性系统
     * <p>
     * List of item ID patterns that can bypass the toughness system
     * Damage from item matching these patterns will deal direct health damage, bypassing the toughness system
     */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BYPASS_ITEM_PATTERNS = Config.BUILDER
            .comment(
                    "List of item ID patterns that can bypass the toughness system.\n",
                    "Damage from item matching these patterns will deal direct health damage, bypassing the toughness system.\n",
                    "Patterns are matched using startsWith, so 'avaritia:infinity' matches all item starting with 'avaritia:infinity'.\n",
                    "Examples: avaritia:infinity, cgm:, techguns:\n",
                    "可以绕过韧性系统的物品ID模式列表。\n",
                    "匹配这些模式的物品造成的伤害会直接造成血量伤害，不经过韧性系统。\n",
                    "模式使用 startsWith 匹配，所以 'avaritia:infinity' 会匹配所有以 'avaritia:infinity' 开头的物品。\n",
                    "示例：avaritia:infinity, cgm:, combatguns:, guns:, techguns:"
            )
            .defineList(
                    "bypassItemPatterns",
                    () -> Arrays.asList("avaritia:infinity", "cgm:", "combatguns:", "guns:", "techguns:"),  // 默认值供应商
                    () -> "",  // 当在配置 GUI 中点击“添加”时，新元素的默认值（这里是空字符串）
                    obj -> obj instanceof String  // 验证器
            );
    // ===== 韧性削减配置 =====
    // ===== Toughness Reduction Configuration =====
    /**
     * 韧性削减幅度最小值（基于防御值计算）
     * 范围：0.0 - 1.0（0% - 100%）
     * <p>
     * Minimum toughness reduction percentage (based on defense value)
     * Range: 0.0 - 1.0 (0% - 100%)
     */
    public static final ModConfigSpec.DoubleValue TOUGHNESS_REDUCTION_MIN = Config.BUILDER
            .comment(
                    "Minimum toughness reduction percentage (based on entity defense).\n",
                    "Range: 0.0 - 1.0 (0% - 100%).\n",
                    "Default: 0.1 (10%).\n",
                    "韧性削减幅度最小值（基于生物防御值计算）。\n",
                    "范围：0.0 - 1.0（0% - 100%）。\n",
                    "默认值：0.1（10%）。"
            )
            .defineInRange("toughnessReductionMin", 0.1, 0.0, 1.0);

    /**
     * 韧性削减幅度最大值（基于防御值计算）
     * 范围：0.0 - 1.0（0% - 100%）
     * <p>
     * Maximum toughness reduction percentage (based on defense value)
     * Range: 0.0 - 1.0 (0% - 100%)
     */
    public static final ModConfigSpec.DoubleValue TOUGHNESS_REDUCTION_MAX = Config.BUILDER
            .comment(
                    "Maximum toughness reduction percentage (based on entity defense).\n",
                    "Range: 0.0 - 1.0 (0% - 100%).\n",
                    "Default: 0.35 (35%).\n",
                    "韧性削减幅度最大值（基于生物防御值计算）。\n",
                    "范围：0.0 - 1.0（0% - 100%）。\n",
                    "默认值：0.35（35%）。"
            )
            .defineInRange("toughnessReductionMax", 0.35, 0.0, 1.0);

    // ===== 世界生成配置 =====
    // ===== World Generation Configuration =====
    /**
     * 橙色水晶晶洞生成概率（相对于紫水晶晶洞）
     * 双精度浮点数配置选项，默认值为0.8（比紫水晶晶洞小20%）
     * 范围从0.0到1.0，0.0表示不生成，1.0表示与紫水晶晶洞相同概率
     * <p>
     * Orange crystal geode generation probability (relative to amethyst geodes)
     * Double configuration option with default value 0.8 (20% less than amethyst geodes)
     * Range from 0.0 to 1.0, 0.0 means no generation, 1.0 means same probability as amethyst geodes
     */
    public static final ModConfigSpec.DoubleValue ORANGE_CRYSTAL_GEODE_PROBABILITY = Config.BUILDER
            .comment(
                    "Orange crystal geode generation probability (relative to amethyst geodes).\n",
                    "Range: 0.0 - 1.0, 0.0 means no generation, 1.0 means same probability as amethyst geodes.\n",
                    "Default: 0.8 (20% less than amethyst geodes).\n",
                    "橙色水晶晶洞生成概率（相对于紫水晶晶洞）。\n",
                    "范围：0.0 - 1.0，0.0表示不生成，1.0表示与紫水晶晶洞相同概率。\n",
                    "默认值：0.8（比紫水晶晶洞小20%）。"
            )
            .defineInRange("orangeCrystalGeodeProbability", 0.8, 0.0, 1.0);

    /**
     * 世界生成调试模式
     * 布尔值配置选项，默认值为false（禁用）
     * 启用后会在控制台输出世界生成相关的调试信息
     * <p>
     * World generation debug mode
     * Boolean configuration option with default value false (disabled)
     * When enabled, outputs world generation debug information to console
     */
    public static final ModConfigSpec.BooleanValue WORLD_GEN_DEBUG = Config.BUILDER
            .comment(
                    "World generation debug mode.\n",
                    "When enabled, outputs world generation debug information to console.\n",
                    "Useful for verifying if orange crystal geodes are generating correctly.\n",
                    "世界生成调试模式。\n",
                    "启用后会在控制台输出世界生成相关的调试信息。\n",
                    "用于验证橙色水晶晶洞是否正确生成。"
            )
            .define("worldGenDebug", false);

    /**
     * 配置规范实例
     * 通过构建器构建的最终配置规范
     * <p>
     * Configuration specification instance
     * Final configuration specification built by the builder
     */
    static final ModConfigSpec SPEC = Config.BUILDER.build();

}