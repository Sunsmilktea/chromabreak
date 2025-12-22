package com.github.chromabreak;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ChromaBreak模组配置类
 * <p>
 * ChromaBreak Mod Configuration Class
 * <p>
 * 血条HUD功能配置类
 * <p>
 * Health Bar HUD Configuration Class
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
     * 血条圆角半径配置
     * 双精度浮点数配置选项，默认值为2.0，范围从0.0到10.0
     * 最小值：0.0（直角），最大值：10.0
     * <p>
     * Health bar corner radius configuration
     * Double configuration option with default value 2.0, range from 0.0 to 10.0
     * Minimum value: 0.0 (square corners), Maximum value: 10.0
     */
    public static final ModConfigSpec.DoubleValue HEALTH_BAR_CORNER_RADIUS = Config.BUILDER
            .comment("Health bar corner radius. 0.0 for square corners, higher values for more rounded corners. Minimum: 0.0, Maximum: 10.0\n血条圆角半径配置。0.0表示直角，值越大圆角越明显。最小值：0.0，最大值：10.0")
            .defineInRange("healthBarCornerRadius", 2.0, 0.0, 10.0);
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
    @SuppressWarnings("unchecked")
    public static final ModConfigSpec.ConfigValue<List<?>> ENTITY_CONFIGS = Config.BUILDER
            .comment(
                    "Entity configuration list (JSON format).\n",
                    "实体配置列表（JSON格式）\n",
                    "For detailed instructions and examples, please refer to chromabreak-common.toml.example file.\n",
                    "详细说明和示例请参考 chromabreak-common.toml.example 文件。\n",
                    "Available colors (7 types, one-to-one with Minecraft dyes): red, blue, green, yellow, white, black, orange\n",
                    "可用颜色（7种，与Minecraft染料一一对应）：red, blue, green, yellow, white, black, orange"
            )
            .defineList("entityConfigs", Collections.emptyList(), obj -> obj instanceof String);

    // ===== 模组兼容性配置 =====
    // ===== Mod Compatibility Configuration =====
    /**
     * 可以绕过韧性系统的模组ID列表
     * 这些模组的伤害会直接造成血量伤害，不经过韧性系统
     * <p>
     * List of mod IDs that can bypass the toughness system
     * Damage from these mods will deal direct health damage, bypassing the toughness system
     */
    @SuppressWarnings("unchecked")
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BYPASS_MOD_IDS = Config.BUILDER
            .comment(
                    "List of mod IDs that can bypass the toughness system.\n",
                    "Damage from these mods will deal direct health damage, bypassing the toughness system.\n",
                    "Examples: cgm, avaritia, techguns, mekanism\n",
                    "可以绕过韧性系统的模组ID列表。\n",
                    "这些模组的伤害会直接造成血量伤害，不经过韧性系统。\n",
                    "示例：cgm, avaritia, techguns, mekanism"
            )
            .defineList("bypassModIds", Arrays.asList("cgm", "combatguns", "guns", "avaritia", "mekanism", "techguns", "immersiveengineering"), obj -> obj instanceof String);

    /**
     * 可以绕过韧性系统的物品ID模式列表
     * 匹配这些模式的物品造成的伤害会直接造成血量伤害，不经过韧性系统
     * <p>
     * List of item ID patterns that can bypass the toughness system
     * Damage from item matching these patterns will deal direct health damage, bypassing the toughness system
     */
    @SuppressWarnings("unchecked")
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BYPASS_ITEM_PATTERNS = Config.BUILDER
            .comment(
                    "List of item ID patterns that can bypass the toughness system.\n",
                    "Damage from item matching these patterns will deal direct health damage, bypassing the toughness system.\n",
                    "Patterns are matched using startsWith, so 'avaritia:infinity' matches all item starting with 'avaritia:infinity'.\n",
                    "Examples: avaritia:infinity, cgm:, techguns:\n",
                    "可以绕过韧性系统的物品ID模式列表。\n",
                    "匹配这些模式的物品造成的伤害会直接造成血量伤害，不经过韧性系统。\n",
                    "模式使用 startsWith 匹配，所以 'avaritia:infinity' 会匹配所有以 'avaritia:infinity' 开头的物品。\n",
                    "示例：avaritia:infinity, cgm:, techguns:"
            )
            .defineList("bypassItemPatterns", Arrays.asList("avaritia:infinity", "cgm:", "combatguns:", "guns:", "techguns:"), obj -> obj instanceof String);

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

    /**
     * 配置规范实例
     * 通过构建器构建的最终配置规范
     * <p>
     * Configuration specification instance
     * Final configuration specification built by the builder
     */
    static final ModConfigSpec SPEC = Config.BUILDER.build();

}