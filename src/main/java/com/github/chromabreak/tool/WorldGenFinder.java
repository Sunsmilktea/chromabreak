package com.github.chromabreak.tool;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WorldGenFinder - 世界生成查找工具类
 * World Generation Finder Tool Class
 * <p>
 * 负责查找、分析和统计Minecraft世界中的自然生成结构和特征
 * Responsible for finding, analyzing, and counting naturally generated structures and features in Minecraft world
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 获取所有注册的结构和特征信息
 * Get all registered structure and feature information
 * - 按模组ID过滤和统计结构/特征
 * Filter and count structures/features by mod ID
 * - 搜索特定名称的结构/特征
 * Search for structures/features by specific name
 * - 输出结构/特征信息到日志
 * Output structure/feature information to logs
 * - 统计各模组的结构/特征数量
 * Count structures/features by mod
 * <p>
 * 使用Minecraft的注册表系统来获取结构和特征信息
 * Uses Minecraft's registry system to get structure and feature information
 * <p>
 * 支持原版和模组添加的结构和特征
 * Supports vanilla and mod-added structures and features
 */
public enum WorldGenFinder {
    ;

    /**
     * 日志记录器
     * Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * 获取所有注册的结构
     * Get all registered structures
     *
     * @param registry 结构注册表
     * @return 结构信息列表
     */
    public static List<StructureInfo> getAllStructures(final Registry<Structure> registry) {
        final List<StructureInfo> structures = new ArrayList<>();

        if (null == registry) {
            WorldGenFinder.LOGGER.warn("结构注册表为空");
            return structures;
        }

        for (final Structure structure : registry) {
            final ResourceLocation id = registry.getKey(structure);
            if (null != id) {
                structures.add(new StructureInfo(id, structure.type()));
            }
        }

        // 按模组ID和结构名称排序
        structures.sort(Comparator
                .comparing(StructureInfo::getModId)
                .thenComparing(StructureInfo::getStructureName));

        return structures;
    }

    /**
     * 获取所有注册的特征
     * Get all registered features
     *
     * @param registry 特征注册表
     * @return 特征信息列表
     */
    public static List<FeatureInfo> getAllFeatures(final Registry<ConfiguredFeature<?, ?>> registry) {
        final List<FeatureInfo> features = new ArrayList<>();

        if (null == registry) {
            WorldGenFinder.LOGGER.warn("特征注册表为空");
            return features;
        }

        for (final ConfiguredFeature<?, ?> feature : registry) {
            final ResourceLocation id = registry.getKey(feature);
            if (null != id) {
                features.add(new FeatureInfo(id));
            }
        }

        // 按模组ID和特征名称排序
        features.sort(Comparator
                .comparing(FeatureInfo::getModId)
                .thenComparing(FeatureInfo::getFeatureName));

        return features;
    }

    /**
     * 按模组ID过滤结构
     * Filter structures by mod ID
     *
     * @param structures 结构列表
     * @param modId      模组ID
     * @return 过滤后的结构列表
     */
    public static List<StructureInfo> filterByModId(final List<StructureInfo> structures, final String modId) {
        return structures.stream()
                .filter(info -> info.getModId().equals(modId))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有模组ID
     * Get all mod IDs from structures
     *
     * @param structures 结构列表
     * @return 模组ID集合
     */
    public static Set<String> getAllModIds(final List<StructureInfo> structures) {
        return structures.stream()
                .map(StructureInfo::getModId)
                .collect(Collectors.toSet());
    }

    /**
     * 打印所有结构信息
     * Print all structure information
     *
     * @param structures 结构列表
     */
    public static void printAllStructures(final List<StructureInfo> structures) {
        if (structures.isEmpty()) {
            WorldGenFinder.LOGGER.info("未找到任何结构");
            return;
        }

        WorldGenFinder.LOGGER.info("=== 所有注册的自然生成结构 ===");
        WorldGenFinder.LOGGER.info("=== All Registered Natural Generation Structures ===");

        String currentMod = "";
        for (final StructureInfo info : structures) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                final String modName = WorldGenFinder.getModName(currentMod);
                WorldGenFinder.LOGGER.info("--- {} ({}) ---", modName, currentMod);
            }
            WorldGenFinder.LOGGER.info("  - {} (类型: {})", info.getStructureName(), info.getStructureType());
        }

        WorldGenFinder.LOGGER.info("=== 总计: {} 个结构 ===", structures.size());
        WorldGenFinder.LOGGER.info("=== Total: {} structures ===", structures.size());
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

        return ModList.get().getModContainerById(modId)
                .map(container -> container.getModInfo().getDisplayName())
                .orElse(modId);
    }

    /**
     * 查找特定结构
     * Find specific structure
     *
     * @param structures    结构列表
     * @param structureName 结构名称（支持部分匹配）
     * @return 匹配的结构列表
     */
    public static List<StructureInfo> findStructure(final List<StructureInfo> structures, final String structureName) {
        return structures.stream()
                .filter(info -> info.getStructureName().toLowerCase().contains(structureName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 统计结构数量
     * Count structures by mod
     *
     * @param structures 结构列表
     */
    public static void countStructuresByMod(final List<StructureInfo> structures) {
        WorldGenFinder.LOGGER.info("=== 各模组结构数量统计 ===");
        WorldGenFinder.LOGGER.info("=== Structure Count by Mod ===");

        structures.stream()
                .collect(Collectors.groupingBy(StructureInfo::getModId, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // 按数量降序
                .forEach(entry -> {
                    final String modName = WorldGenFinder.getModName(entry.getKey());
                    WorldGenFinder.LOGGER.info("{} ({}): {} 个结构", modName, entry.getKey(), entry.getValue());
                });
    }

    /**
     * 按模组ID过滤特征
     * Filter features by mod ID
     *
     * @param features 特征列表
     * @param modId    模组ID
     * @return 过滤后的特征列表
     */
    public static List<FeatureInfo> filterFeaturesByModId(final List<FeatureInfo> features, final String modId) {
        return features.stream()
                .filter(info -> info.getModId().equals(modId))
                .collect(Collectors.toList());
    }

    /**
     * 查找特定特征
     * Find specific feature
     *
     * @param features    特征列表
     * @param featureName 特征名称（支持部分匹配）
     * @return 匹配的特征列表
     */
    public static List<FeatureInfo> findFeature(final List<FeatureInfo> features, final String featureName) {
        return features.stream()
                .filter(info -> info.getFeatureName().toLowerCase().contains(featureName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 打印所有特征信息
     * Print all feature information
     *
     * @param features 特征列表
     */
    public static void printAllFeatures(final List<FeatureInfo> features) {
        if (features.isEmpty()) {
            WorldGenFinder.LOGGER.info("未找到任何特征");
            return;
        }

        WorldGenFinder.LOGGER.info("=== 所有注册的自然生成特征 ===");
        WorldGenFinder.LOGGER.info("=== All Registered Natural Generation Features ===");

        String currentMod = "";
        for (final FeatureInfo info : features) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                final String modName = WorldGenFinder.getModName(currentMod);
                WorldGenFinder.LOGGER.info("--- {} ({}) ---", modName, currentMod);
            }
            WorldGenFinder.LOGGER.info("  - {}", info.getFeatureName());
        }

        WorldGenFinder.LOGGER.info("=== 总计: {} 个特征 ===", features.size());
        WorldGenFinder.LOGGER.info("=== Total: {} features ===", features.size());
    }

    /**
     * 统计特征数量
     * Count features by mod
     *
     * @param features 特征列表
     */
    public static void countFeaturesByMod(final List<FeatureInfo> features) {
        WorldGenFinder.LOGGER.info("=== 各模组特征数量统计 ===");
        WorldGenFinder.LOGGER.info("=== Feature Count by Mod ===");

        features.stream()
                .collect(Collectors.groupingBy(FeatureInfo::getModId, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // 按数量降序
                .forEach(entry -> {
                    final String modName = WorldGenFinder.getModName(entry.getKey());
                    WorldGenFinder.LOGGER.info("{} ({}): {} 个特征", modName, entry.getKey(), entry.getValue());
                });
    }

    /**
     * 结构信息类
     * Structure Information Class
     */
    public static class StructureInfo {
        private final ResourceLocation id;
        private final String modId;
        private final String structureName;
        private final StructureType<?> structureType;

        public StructureInfo(final ResourceLocation id, final StructureType<?> structureType) {
            this.id = id;
            this.modId = id.getNamespace();
            this.structureName = id.getPath();
            this.structureType = structureType;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public String getModId() {
            return this.modId;
        }

        public String getStructureName() {
            return this.structureName;
        }

        public StructureType<?> getStructureType() {
            return this.structureType;
        }

        @Override
        public String toString() {
            return String.format("%s (%s) - Type: %s", this.id, this.modId, this.structureType.toString());
        }
    }

    /**
     * 特征信息类
     * Feature Information Class
     */
    public static class FeatureInfo {
        private final ResourceLocation id;
        private final String modId;
        private final String featureName;

        public FeatureInfo(final ResourceLocation id) {
            this.id = id;
            this.modId = id.getNamespace();
            this.featureName = id.getPath();
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public String getModId() {
            return this.modId;
        }

        public String getFeatureName() {
            return this.featureName;
        }

        @Override
        public String toString() {
            return String.format("%s (%s)", this.id, this.modId);
        }
    }
}
