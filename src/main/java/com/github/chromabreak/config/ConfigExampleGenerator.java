package com.github.chromabreak.config;

import com.github.chromabreak.ChromaBreak;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * ConfigExampleGenerator - 配置文件示例生成器
 * Configuration Example File Generator
 * <p>
 * 负责自动生成ChromaBreak模组的配置文件示例文件 (chromabreak-common.toml.example)
 * Responsible for automatically generating configuration example file for ChromaBreak mod (chromabreak-common.toml.example)
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 配置文件生成：在模组启动时自动生成配置文件示例
 * Configuration file generation: Automatically generate configuration example file when mod starts
 * - 示例内容生成：生成包含详细说明和示例的配置文件内容
 * Example content generation: Generate configuration file content with detailed instructions and examples
 * - 文件管理：检查文件存在性、创建目录、写入文件
 * File management: Check file existence, create directories, write files
 * - 错误处理：处理文件操作过程中的异常
 * Error handling: Handle exceptions during file operations
 * <p>
 * 生成的配置文件内容：
 * Generated configuration file content:
 * - 实体配置示例：包含多种实体的韧性条配置示例
 * Entity configuration examples: Contains toughness bar configuration examples for various entities
 * - 模组兼容性配置：绕过韧性系统的模组ID和物品ID模式
 * Mod compatibility configuration: Mod IDs and item ID patterns that bypass the toughness system
 * - 详细说明文档：包含配置格式说明和使用示例
 * Detailed documentation: Contains configuration format instructions and usage examples
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 模组首次启动时自动生成示例配置文件
 * Automatically generate example configuration file when mod first starts
 * - 为开发者提供配置模板和参考
 * Provide configuration templates and references for developers
 * - 帮助用户理解配置格式和选项
 * Help users understand configuration formats and options
 * <p>
 * 设计特点：
 * Design features:
 * - 非覆盖性生成：如果示例文件已存在，不会覆盖现有文件
 * Non-overwriting generation: If example file already exists, won't overwrite existing file
 * - 详细文档：生成的配置文件包含详细的中英双语说明
 * Detailed documentation: Generated configuration file contains detailed bilingual instructions
 * - 实用示例：提供多种实体的配置示例，覆盖常见使用场景
 * Practical examples: Provide configuration examples for various entities, covering common usage scenarios
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 */
public enum ConfigExampleGenerator {
    ;

    /**
     * 示例文件名
     * Example file name
     * <p>
     * 生成的配置文件示例的文件名
     * File name of the generated configuration example file
     */
    private static final String EXAMPLE_FILE_NAME = "chromabreak-common.toml.example";

    /**
     * 生成配置文件示例文件
     * Generate configuration example file
     * <p>
     * 主生成方法，负责创建配置文件示例文件
     * Main generation method, responsible for creating configuration example file
     * <p>
     * 处理逻辑：
     * Processing logic:
     * 1. 获取配置目录路径
     * Get configuration directory path
     * 2. 检查示例文件是否已存在（避免覆盖）
     * Check if example file already exists (avoid overwriting)
     * 3. 确保配置目录存在
     * Ensure configuration directory exists
     * 4. 生成示例文件内容
     * Generate example file content
     * 5. 将内容写入文件
     * Write content to file
     * 6. 记录生成结果
     * Log generation result
     * <p>
     * 异常处理：如果发生IO异常，记录警告日志但不中断程序
     * Exception handling: If IO exception occurs, log warning but don't interrupt program
     */
    public static void generateExampleFile() {
        try {
            final Path configDir = FMLPaths.CONFIGDIR.get();
            final Path exampleFile = configDir.resolve(ConfigExampleGenerator.EXAMPLE_FILE_NAME);

            // 如果文件已存在，不覆盖
            // If file already exists, don't overwrite
            if (Files.exists(exampleFile)) {
                ChromaBreak.LOGGER.debug("Configuration example file already exists: {}", exampleFile);
                return;
            }

            // 确保配置目录存在
            // Ensure config directory exists
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }

            // 生成示例文件内容
            // Generate example file content
            final String content = ConfigExampleGenerator.generateExampleContent();

            // 写入文件
            // Write to file
            Files.writeString(exampleFile, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            ChromaBreak.LOGGER.info("Generated configuration example file: {}", exampleFile);
            ChromaBreak.LOGGER.info("已生成配置文件示例：{}", exampleFile);
        } catch (final IOException e) {
            ChromaBreak.LOGGER.warn("Failed to generate configuration example file", e);
            ChromaBreak.LOGGER.warn("生成配置文件示例失败", e);
        }
    }

    /**
     * 生成示例文件内容
     * Generate example file content
     * <p>
     * 生成包含详细说明和示例的配置文件内容
     * Generate configuration file content with detailed instructions and examples
     *
     * @return 示例文件内容，包含实体配置示例和模组兼容性配置
     * Example file content, containing entity configuration examples and mod compatibility configuration
     * <p>
     * 内容结构：
     * Content structure:
     * - 文件头说明：配置文件的用途和基本说明
     * File header description: Purpose and basic instructions of configuration file
     * - 实体配置说明：配置格式和使用方法的详细说明
     * Entity configuration instructions: Detailed instructions on configuration format and usage
     * - 实体配置示例：多种实体的配置示例
     * Entity configuration examples: Configuration examples for various entities
     * - 模组兼容性配置：绕过韧性系统的模组和物品配置
     * Mod compatibility configuration: Mod and item configurations that bypass toughness system
     */
    private static String generateExampleContent() {
        return """
                # ChromaBreak 配置文件示例
                # ChromaBreak Configuration File Example
                #
                # 实体配置说明：
                # Entity Configuration Instructions:
                #
                # 1. 单一颜色配置（韧性条显示一种颜色）：
                #    Single color configuration (toughness bar shows one color):
                #    {"entityType":"minecraft:zombie","maxHealth":30.0,"maxToughness":100.0,"toughnessColor":"red"}
                #
                # 2. 多色分布配置（韧性条从左到右显示多种颜色，按百分比分段）：
                #    Multi-color distribution configuration (toughness bar shows multiple colors from left to right, segmented by percentage):
                #    {"entityType":"minecraft:spider","maxHealth":20.0,"maxToughness":80.0,"toughnessColors":{"orange":0.4,"yellow":0.3,"green":0.2,"white":0.1}}
                #    说明：这个配置表示韧性条从左到右，40%是橙色，30%是黄色，20%是绿色，10%是白色
                #    Explanation: This configuration means the toughness bar from left to right: 40% orange, 30% yellow, 20% green, 10% white
                #
                # 3. 多色分布百分比会自动归一化（总和不需要等于1.0）：
                #    Multi-color distribution percentages are automatically normalized (sum doesn't need to equal 1.0):
                #    {"entityType":"minecraft:witch","maxHealth":26.0,"maxToughness":90.0,"toughnessColors":{"blue":60,"green":40}}
                #    说明：这里写了60和40，会自动归一化为60%和40%
                #    Explanation: Here 60 and 40 are written, which will be automatically normalized to 60% and 40%
                #
                # 可用颜色（7种，与Minecraft染料一一对应）：
                # Available colors (7 types, one-to-one correspondence with Minecraft dyes):
                # red (红色染料), blue (蓝色染料), green (绿色染料), yellow (黄色染料), 
                # white (白色染料), black (黑色染料), orange (橙色染料)
                #
                # 配置示例：
                # Configuration Examples:
                
                entityConfigs = [
                    # 僵尸：红色韧性条，最大血量30，最大韧性100
                    # Zombie: Red toughness bar, max health 30, max toughness 100
                    "{\\"entityType\\":\\"minecraft:zombie\\",\\"maxHealth\\":30.0,\\"maxToughness\\":100.0,\\"toughnessColor\\":\\"red\\"}",
                
                    # 骷髅：蓝色韧性条，最大血量25，最大韧性120
                    # Skeleton: Blue toughness bar, max health 25, max toughness 120
                    "{\\"entityType\\":\\"minecraft:skeleton\\",\\"maxHealth\\":25.0,\\"maxToughness\\":120.0,\\"toughnessColor\\":\\"blue\\"}",
                
                    # 苦力怕：绿色韧性条，最大血量25，最大韧性150
                    # Creeper: Green toughness bar, max health 25, max toughness 150
                    "{\\"entityType\\":\\"minecraft:creeper\\",\\"maxHealth\\":25.0,\\"maxToughness\\":150.0,\\"toughnessColor\\":\\"green\\"}",
                
                    # 末影人：黑色韧性条，最大血量50，最大韧性200
                    # Enderman: Black toughness bar, max health 50, max toughness 200
                    "{\\"entityType\\":\\"minecraft:enderman\\",\\"maxHealth\\":50.0,\\"maxToughness\\":200.0,\\"toughnessColor\\":\\"black\\"}",
                
                    # 蜘蛛：多色韧性条（从左到右：40%橙色，30%黄色，20%绿色，10%白色），最大血量20，最大韧性80
                    # Spider: Multi-color toughness bar (from left to right: 40% orange, 30% yellow, 20% green, 10% white), max health 20, max toughness 80
                    "{\\"entityType\\":\\"minecraft:spider\\",\\"maxHealth\\":20.0,\\"maxToughness\\":80.0,\\"toughnessColors\\":{\\"orange\\":0.4,\\"yellow\\":0.3,\\"green\\":0.2,\\"white\\":0.1}}",
                
                    # 女巫：多色韧性条（从左到右：60%蓝色，40%绿色），最大血量26，最大韧性90
                    # Witch: Multi-color toughness bar (from left to right: 60% blue, 40% green), max health 26, max toughness 90
                    "{\\"entityType\\":\\"minecraft:witch\\",\\"maxHealth\\":26.0,\\"maxToughness\\":90.0,\\"toughnessColors\\":{\\"blue\\":0.6,\\"green\\":0.4}}",
                
                    # 烈焰人：多色韧性条（从左到右：50%红色，30%橙色，20%黄色），最大血量30，最大韧性120
                    # Blaze: Multi-color toughness bar (from left to right: 50% red, 30% orange, 20% yellow), max health 30, max toughness 120
                    "{\\"entityType\\":\\"minecraft:blaze\\",\\"maxHealth\\":30.0,\\"maxToughness\\":120.0,\\"toughnessColors\\":{\\"red\\":0.5,\\"orange\\":0.3,\\"yellow\\":0.2}}",
                
                    # 凋灵：多色韧性条（从左到右：40%黑色，30%红色，20%蓝色，10%绿色），最大血量600，最大韧性300
                    # Wither: Multi-color toughness bar (from left to right: 40% black, 30% red, 20% blue, 10% green), max health 600, max toughness 300
                    "{\\"entityType\\":\\"minecraft:wither\\",\\"maxHealth\\":600.0,\\"maxToughness\\":300.0,\\"toughnessColors\\":{\\"black\\":0.4,\\"red\\":0.3,\\"blue\\":0.2,\\"green\\":0.1}}"
                ]
                
                # ===== 模组兼容性配置 =====
                # ===== Mod Compatibility Configuration =====
                #
                # 可以绕过韧性系统的模组ID列表
                # 这些模组的伤害会直接造成血量伤害，不经过韧性系统
                # List of mod IDs that can bypass the toughness system
                # Damage from these mods will deal direct health damage, bypassing the toughness system
                # 示例：cgm, avaritia, techguns, mekanism
                # Examples: cgm, avaritia, techguns, mekanism
                bypassModIds = ["cgm", "combatguns", "guns", "avaritia", "mekanism", "techguns", "immersiveengineering"]
                
                # 可以绕过韧性系统的物品ID模式列表
                # 匹配这些模式的物品造成的伤害会直接造成血量伤害，不经过韧性系统
                # List of item ID patterns that can bypass the toughness system
                # Damage from items matching these patterns will deal direct health damage, bypassing the toughness system
                # 模式使用 startsWith 匹配，所以 'avaritia:infinity' 会匹配所有以 'avaritia:infinity' 开头的物品
                # Patterns are matched using startsWith, so 'avaritia:infinity' matches all items starting with 'avaritia:infinity'
                # 示例：avaritia:infinity, cgm:, techguns:
                # Examples: avaritia:infinity, cgm:, techguns:
                bypassItemPatterns = ["avaritia:infinity", "cgm:", "combatguns:", "guns:", "techguns:"]
                
                """;
    }
}

