package com.github.chromabreak.tool;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * WorldGenCommands - 世界生成命令类
 * World Generation Commands Class
 * <p>
 * 提供Minecraft结构查找和管理的聊天命令，帮助玩家和开发者查找、定位和管理游戏中的自然生成结构和特征
 * Provides Minecraft structure finding and management chat commands to help players and developers find, locate, and manage natural generation structures and features in the game
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 结构列表：列出所有注册的自然生成结构，按模组分类显示
 * Structure listing: Lists all registered natural generation structures, categorized by mod
 * - 特征列表：列出所有注册的自然生成特征，按模组分类显示
 * Feature listing: Lists all registered natural generation features, categorized by mod
 * - 结构定位：定位特定结构或特征，提供详细的定位信息和错误提示
 * Structure locating: Locates specific structures or features, provides detailed location information and error messages
 * - 模组筛选：按模组ID筛选和显示结构/特征
 * Mod filtering: Filters and displays structures/features by mod ID
 * - 结构/特征搜索：按名称搜索结构/特征，支持模糊匹配
 * Structure/feature search: Searches structures/features by name, supports fuzzy matching
 * - 统计功能：统计各模组的结构/特征数量
 * Statistics: Counts structure/feature numbers by mod
 * <p>
 * 命令系统：
 * Command system:
 * - /findstructures - 主命令，显示所有结构
 * /findstructures - Main command, shows all structures
 * - /findstructures all - 列出所有结构
 * /findstructures all - Lists all structures
 * - /findstructures mod <modid> - 按模组列出结构
 * /findstructures mod <modid> - Lists structures by mod
 * - /findstructures search <name> - 搜索结构
 * /findstructures search <name> - Searches structures
 * - /findstructures count - 统计结构数量
 * /findstructures count - Counts structures
 * - /findstructures locate <structure> - 定位结构/特征
 * /findstructures locate <structure> - Locates structure/feature
 * - /findstructures features - 显示所有特征
 * /findstructures features - Shows all features
 * - /findstructures features all - 列出所有特征
 * /findstructures features all - Lists all features
 * - /findstructures features mod <modid> - 按模组列出特征
 * /findstructures features mod <modid> - Lists features by mod
 * - /findstructures features search <name> - 搜索特征
 * /findstructures features search <name> - Searches features
 * - /findstructures features count - 统计特征数量
 * /findstructures features count - Counts features
 * <p>
 * 技术实现：
 * Technical implementation:
 * - Brigadier框架：使用Minecraft的Brigadier命令框架
 * Brigadier framework: Uses Minecraft's Brigadier command framework
 * - 注册表访问：通过Registry API访问结构和特征注册表
 * Registry access: Accesses structure and feature registries through Registry API
 * - 反射兼容：使用反射处理不同版本的API差异
 * Reflection compatibility: Uses reflection to handle API differences across versions
 * - 错误处理：完善的错误处理和用户友好的错误信息
 * Error handling: Comprehensive error handling with user-friendly error messages
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 玩家探索：帮助玩家查找和定位感兴趣的结构和特征
 * Player exploration: Helps players find and locate structures and features of interest
 * - 模组开发：开发者可以验证模组结构和特征的正确注册和生成
 * Mod development: Developers can verify correct registration and generation of mod structures and features
 * - 服务器管理：服务器管理员可以管理结构/特征生成和调试问题
 * Server management: Server administrators can manage structure/feature generation and debug issues
 * - 测试验证：测试人员可以验证结构/特征生成功能
 * Testing verification: Testers can verify structure/feature generation functionality
 * <p>
 * 设计特点：
 * Design features:
 * - 用户友好：提供详细的中英文提示和错误信息
 * User-friendly: Provides detailed Chinese and English prompts and error messages
 * - 权限控制：需要操作员权限（权限等级2）
 * Permission control: Requires operator permission (permission level 2)
 * - 模块化设计：各个命令功能独立，便于维护和扩展
 * Modular design: Each command function is independent, easy to maintain and extend
 * - 性能优化：避免不必要的计算和内存分配
 * Performance optimization: Avoids unnecessary calculations and memory allocations
 * <p>
 * 集成功能：
 * Integration features:
 * - WorldGenFinder集成：与WorldGenFinder工具类紧密集成
 * WorldGenFinder integration: Tightly integrated with WorldGenFinder utility class
 * - 日志记录：使用Log4j记录命令执行日志
 * Logging: Uses Log4j to record command execution logs
 * - 多语言支持：支持中英文双语输出
 * Multi-language support: Supports bilingual output in Chinese and English
 */
public enum WorldGenCommands {
    ;

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * 注册命令
     * Register commands
     *
     * @param dispatcher 命令分发器
     */
    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("findstructures")
                .requires(source -> source.hasPermission(2)) // 需要操作员权限
                .executes(WorldGenCommands::listAllStructures)
                .then(Commands.literal("all")
                        .executes(WorldGenCommands::listAllStructures))
                .then(Commands.literal("mod")
                        .then(Commands.argument("modid", StringArgumentType.string())
                                .executes(WorldGenCommands::listStructuresByMod)))
                .then(Commands.literal("search")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(WorldGenCommands::searchStructures)))
                .then(Commands.literal("count")
                        .executes(WorldGenCommands::countStructures))
                .then(Commands.literal("locate")
                        .then(Commands.argument("structure", StringArgumentType.string())
                                .executes(WorldGenCommands::locateStructure)))
                .then(Commands.literal("features")
                        .executes(WorldGenCommands::listAllFeatures)
                        .then(Commands.literal("all")
                                .executes(WorldGenCommands::listAllFeatures))
                        .then(Commands.literal("mod")
                                .then(Commands.argument("modid", StringArgumentType.string())
                                        .executes(WorldGenCommands::listFeaturesByMod)))
                        .then(Commands.literal("search")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .executes(WorldGenCommands::searchFeatures)))
                        .then(Commands.literal("count")
                                .executes(WorldGenCommands::countFeatures)))
        );
    }

    /**
     * 列出所有结构
     * List all structures
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int listAllStructures(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);

        if (null == structureRegistry) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }

        final List<WorldGenFinder.StructureInfo> structures = WorldGenFinder.getAllStructures(structureRegistry);

        if (structures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("未找到任何结构"), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("=== 所有注册的自然生成结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== All Registered Natural Generation Structures ==="), false);

        String currentMod = "";
        for (final WorldGenFinder.StructureInfo info : structures) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                final String modName = WorldGenCommands.getModName(currentMod);
                final String finalCurrentMod = currentMod;
                source.sendSuccess(() -> Component.literal("--- " + modName + " (" + finalCurrentMod + ") ---"), false);
            }
            source.sendSuccess(() -> Component.literal("  - " + info.getStructureName() + " (类型: " + info.getStructureType() + ")"), false);
        }

        source.sendSuccess(() -> Component.literal("=== 总计: " + structures.size() + " 个结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + structures.size() + " structures ==="), false);

        // 同时在控制台输出
        WorldGenFinder.printAllStructures(structures);

        return structures.size();
    }

    /**
     * 定位并验证结构或特征生成
     * Locate and verify structure or feature generation
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int locateStructure(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final String targetName = StringArgumentType.getString(context, "structure");

        if (!(source.getEntity() instanceof final ServerPlayer player)) {
            source.sendFailure(Component.literal("只有玩家可以使用此命令"));
            return 0;
        }

        try {
            // 构建完整的ID
            final String fullId;
            if (targetName.contains(":")) {
                fullId = targetName; // 已经是完整ID
            } else {
                fullId = "chromabreak:" + targetName; // 添加默认命名空间
            }

            // 首先尝试作为结构定位
            final String structureLocateCommand = "locate structure " + fullId;
            final var structureResult = source.getServer().getCommands().getDispatcher().execute(
                    structureLocateCommand,
                    source.withSuppressedOutput().withMaximumPermission(2)
            );

            if (0 < structureResult) {
                source.sendSuccess(() -> Component.literal("✅ 已找到结构: " + fullId), false);
                source.sendSuccess(() -> Component.literal("💡 可以使用 /locate structure " + fullId + " 查看具体位置"), false);
                source.sendSuccess(() -> Component.literal("🚀 然后使用 /tp 命令传送到该位置"), false);
                return structureResult;
            }

            // 如果结构未找到，尝试作为特征定位
            final String featureLocateCommand = "locate feature " + fullId;
            final var featureResult = source.getServer().getCommands().getDispatcher().execute(
                    featureLocateCommand,
                    source.withSuppressedOutput().withMaximumPermission(2)
            );

            if (0 < featureResult) {
                source.sendSuccess(() -> Component.literal("✅ 已找到特征: " + fullId), false);
                source.sendSuccess(() -> Component.literal("💡 可以使用 /locate feature " + fullId + " 查看具体位置"), false);
                source.sendSuccess(() -> Component.literal("🚀 然后使用 /tp 命令传送到该位置"), false);
                return featureResult;
            }

            // 如果都未找到，提供详细错误信息
            source.sendFailure(Component.literal("❌ 未找到结构或特征: " + fullId));
            source.sendFailure(Component.literal("🔍 请检查以下可能的原因："));
            source.sendFailure(Component.literal("1. 名称是否正确（尝试使用完整ID如 chromabreak:orange_crystal_geode）"));
            source.sendFailure(Component.literal("2. 世界生成范围是否包含该结构/特征"));
            source.sendFailure(Component.literal("3. 生成概率配置是否正确"));
            source.sendFailure(Component.literal("4. 是否在当前维度生成（橙色水晶晶洞在主世界生成）"));
            source.sendFailure(Component.literal("5. 世界是否已经生成了该区域"));

            // 提供帮助命令
            source.sendSuccess(() -> Component.literal("💡 可以使用以下命令查看所有可用结构/特征："), false);
            source.sendSuccess(() -> Component.literal("/findstructures all - 查看所有结构"), false);
            source.sendSuccess(() -> Component.literal("/findstructures features all - 查看所有特征"), false);

            return 0;
        } catch (final Exception e) {
            source.sendFailure(Component.literal("❌ 定位时出错: " + e.getMessage()));
            WorldGenCommands.LOGGER.error("定位时出错:", e);
            return 0;
        }
    }

    /**
     * 按模组列出结构
     * List structures by mod
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int listStructuresByMod(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final String modId = StringArgumentType.getString(context, "modid");
        final Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);

        if (null == structureRegistry) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }

        final List<WorldGenFinder.StructureInfo> allStructures = WorldGenFinder.getAllStructures(structureRegistry);
        final List<WorldGenFinder.StructureInfo> filteredStructures = WorldGenFinder.filterByModId(allStructures, modId);

        if (filteredStructures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("模组 '" + modId + "' 未注册任何结构"), false);
            return 0;
        }

        final String modName = WorldGenCommands.getModName(modId);
        source.sendSuccess(() -> Component.literal("=== " + modName + " (" + modId + ") 的结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Structures for " + modName + " (" + modId + ") ==="), false);

        for (final WorldGenFinder.StructureInfo info : filteredStructures) {
            source.sendSuccess(() -> Component.literal("  - " + info.getStructureName() + " (类型: " + info.getStructureType() + ")"), false);
        }

        source.sendSuccess(() -> Component.literal("=== 总计: " + filteredStructures.size() + " 个结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + filteredStructures.size() + " structures ==="), false);

        return filteredStructures.size();
    }

    /**
     * 搜索结构
     * Search structures
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int searchStructures(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final String searchTerm = StringArgumentType.getString(context, "name");
        final Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);

        if (null == structureRegistry) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }

        final List<WorldGenFinder.StructureInfo> allStructures = WorldGenFinder.getAllStructures(structureRegistry);
        final List<WorldGenFinder.StructureInfo> foundStructures = WorldGenFinder.findStructure(allStructures, searchTerm);

        if (foundStructures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("未找到包含 '" + searchTerm + "' 的结构"), false);
            return 0;
        }

        source.sendSuccess(() -> Component.literal("=== 包含 '" + searchTerm + "' 的结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Structures containing '" + searchTerm + "' ==="), false);

        String currentMod = "";
        for (final WorldGenFinder.StructureInfo info : foundStructures) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                final String modName = WorldGenCommands.getModName(currentMod);
                final String finalCurrentMod = currentMod;
                source.sendSuccess(() -> Component.literal("--- " + modName + " (" + finalCurrentMod + ") ---"), false);
            }
            source.sendSuccess(() -> Component.literal("  - " + info.getStructureName() + " (类型: " + info.getStructureType() + ")"), false);
        }

        source.sendSuccess(() -> Component.literal("=== 总计: " + foundStructures.size() + " 个匹配结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + foundStructures.size() + " matching structures ==="), false);

        return foundStructures.size();
    }

    /**
     * 统计结构数量
     * Count structures
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int countStructures(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);

        if (null == structureRegistry) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }

        final List<WorldGenFinder.StructureInfo> structures = WorldGenFinder.getAllStructures(structureRegistry);

        source.sendSuccess(() -> Component.literal("=== 各模组结构数量统计 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Structure Count by Mod ==="), false);

        structures.stream()
                .collect(java.util.stream.Collectors.groupingBy(WorldGenFinder.StructureInfo::getModId, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // 按数量降序
                .forEach(entry -> {
                    final String modName = WorldGenCommands.getModName(entry.getKey());
                    source.sendSuccess(() -> Component.literal(modName + " (" + entry.getKey() + "): " + entry.getValue() + " 个结构"), false);
                });

        source.sendSuccess(() -> Component.literal("=== 总计: " + structures.size() + " 个结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + structures.size() + " structures ==="), false);

        // 同时在控制台输出统计
        WorldGenFinder.countStructuresByMod(structures);

        return structures.size();
    }

    /**
     * 获取模组名称
     * Get mod name
     *
     * @param modId 模组ID
     * @return 模组名称
     */
    private static String getModName(final String modId) {
        if ("minecraft".equals(modId)) {
            return "Minecraft (原版)";
        }

        return net.neoforged.fml.ModList.get().getModContainerById(modId)
                .map(container -> container.getModInfo().getDisplayName())
                .orElse(modId);
    }

    /**
     * 列出所有特征
     * List all features
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int listAllFeatures(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final Registry<ConfiguredFeature<?, ?>> featureRegistry = source.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElse(null);

        if (null == featureRegistry) {
            source.sendFailure(Component.literal("无法访问特征注册表"));
            return 0;
        }

        final List<WorldGenFinder.FeatureInfo> features = WorldGenFinder.getAllFeatures(featureRegistry);

        if (features.isEmpty()) {
            source.sendSuccess(() -> Component.literal("未找到任何特征"), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("=== 所有注册的自然生成特征 ==="), false);
        source.sendSuccess(() -> Component.literal("=== All Registered Natural Generation Features ==="), false);

        String currentMod = "";
        for (final WorldGenFinder.FeatureInfo info : features) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                final String modName = WorldGenCommands.getModName(currentMod);
                final String finalCurrentMod = currentMod;
                source.sendSuccess(() -> Component.literal("--- " + modName + " (" + finalCurrentMod + ") ---"), false);
            }
            source.sendSuccess(() -> Component.literal("  - " + info.getFeatureName()), false);
        }

        source.sendSuccess(() -> Component.literal("=== 总计: " + features.size() + " 个特征 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + features.size() + " features ==="), false);

        // 同时在控制台输出
        WorldGenFinder.printAllFeatures(features);

        return features.size();
    }

    /**
     * 按模组列出特征
     * List features by mod
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int listFeaturesByMod(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final String modId = StringArgumentType.getString(context, "modid");
        final Registry<ConfiguredFeature<?, ?>> featureRegistry = source.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElse(null);

        if (null == featureRegistry) {
            source.sendFailure(Component.literal("无法访问特征注册表"));
            return 0;
        }

        final List<WorldGenFinder.FeatureInfo> allFeatures = WorldGenFinder.getAllFeatures(featureRegistry);
        final List<WorldGenFinder.FeatureInfo> filteredFeatures = WorldGenFinder.filterFeaturesByModId(allFeatures, modId);

        if (filteredFeatures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("模组 '" + modId + "' 未注册任何特征"), false);
            return 0;
        }

        final String modName = WorldGenCommands.getModName(modId);
        source.sendSuccess(() -> Component.literal("=== " + modName + " (" + modId + ") 的特征 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Features for " + modName + " (" + modId + ") ==="), false);

        for (final WorldGenFinder.FeatureInfo info : filteredFeatures) {
            source.sendSuccess(() -> Component.literal("  - " + info.getFeatureName()), false);
        }

        source.sendSuccess(() -> Component.literal("=== 总计: " + filteredFeatures.size() + " 个特征 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + filteredFeatures.size() + " features ==="), false);

        return filteredFeatures.size();
    }

    /**
     * 搜索特征
     * Search features
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int searchFeatures(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final String searchTerm = StringArgumentType.getString(context, "name");
        final Registry<ConfiguredFeature<?, ?>> featureRegistry = source.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElse(null);

        if (null == featureRegistry) {
            source.sendFailure(Component.literal("无法访问特征注册表"));
            return 0;
        }

        final List<WorldGenFinder.FeatureInfo> allFeatures = WorldGenFinder.getAllFeatures(featureRegistry);
        final List<WorldGenFinder.FeatureInfo> foundFeatures = WorldGenFinder.findFeature(allFeatures, searchTerm);

        if (foundFeatures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("未找到包含 '" + searchTerm + "' 的特征"), false);
            return 0;
        }

        source.sendSuccess(() -> Component.literal("=== 包含 '" + searchTerm + "' 的特征 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Features containing '" + searchTerm + "' ==="), false);

        String currentMod = "";
        for (final WorldGenFinder.FeatureInfo info : foundFeatures) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                final String modName = WorldGenCommands.getModName(currentMod);
                final String finalCurrentMod = currentMod;
                source.sendSuccess(() -> Component.literal("--- " + modName + " (" + finalCurrentMod + ") ---"), false);
            }
            source.sendSuccess(() -> Component.literal("  - " + info.getFeatureName()), false);
        }

        source.sendSuccess(() -> Component.literal("=== 总计: " + foundFeatures.size() + " 个匹配特征 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + foundFeatures.size() + " matching features ==="), false);

        return foundFeatures.size();
    }

    /**
     * 统计特征数量
     * Count features
     *
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int countFeatures(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final Registry<ConfiguredFeature<?, ?>> featureRegistry = source.registryAccess().registry(Registries.CONFIGURED_FEATURE).orElse(null);

        if (null == featureRegistry) {
            source.sendFailure(Component.literal("无法访问特征注册表"));
            return 0;
        }

        final List<WorldGenFinder.FeatureInfo> features = WorldGenFinder.getAllFeatures(featureRegistry);

        source.sendSuccess(() -> Component.literal("=== 各模组特征数量统计 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Feature Count by Mod ==="), false);

        features.stream()
                .collect(java.util.stream.Collectors.groupingBy(WorldGenFinder.FeatureInfo::getModId, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // 按数量降序
                .forEach(entry -> {
                    final String modName = WorldGenCommands.getModName(entry.getKey());
                    source.sendSuccess(() -> Component.literal(modName + " (" + entry.getKey() + "): " + entry.getValue() + " 个特征"), false);
                });

        source.sendSuccess(() -> Component.literal("=== 总计: " + features.size() + " 个特征 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + features.size() + " features ==="), false);

        // 同时在控制台输出统计
        WorldGenFinder.countFeaturesByMod(features);

        return features.size();
    }
}
