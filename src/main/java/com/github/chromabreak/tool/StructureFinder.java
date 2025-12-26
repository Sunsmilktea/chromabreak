package com.github.chromabreak.tool;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 结构和特征查找工具类
 * Structure and Feature Finder Tool Class
 * <p>
 * 用于查找游戏里注册的所有自然生成结构和特征，包括模组和原版
 * Used to find all natural generation structures and features registered in the game, including modded and vanilla
 */
public class StructureFinder {
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * 结构信息类
     * Structure Information Class
     */
    public static class StructureInfo {
        private final ResourceLocation id;
        private final String modId;
        private final String structureName;
        private final StructureType<?> structureType;
        
        public StructureInfo(ResourceLocation id, StructureType<?> structureType) {
            this.id = id;
            this.modId = id.getNamespace();
            this.structureName = id.getPath();
            this.structureType = structureType;
        }
        
        public ResourceLocation getId() {
            return id;
        }
        
        public String getModId() {
            return modId;
        }
        
        public String getStructureName() {
            return structureName;
        }
        
        public StructureType<?> getStructureType() {
            return structureType;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s) - Type: %s", id, modId, structureType.toString());
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
        
        public FeatureInfo(ResourceLocation id) {
            this.id = id;
            this.modId = id.getNamespace();
            this.featureName = id.getPath();
        }
        
        public ResourceLocation getId() {
            return id;
        }
        
        public String getModId() {
            return modId;
        }
        
        public String getFeatureName() {
            return featureName;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s)", id, modId);
        }
    }
    
    /**
     * 获取所有注册的结构
     * Get all registered structures
     * 
     * @param registry 结构注册表
     * @return 结构信息列表
     */
    public static List<StructureInfo> getAllStructures(Registry<Structure> registry) {
        List<StructureInfo> structures = new ArrayList<>();
        
        if (registry == null) {
            LOGGER.warn("结构注册表为空");
            return structures;
        }
        
        for (Structure structure : registry) {
            ResourceLocation id = registry.getKey(structure);
            if (id != null) {
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
    public static List<FeatureInfo> getAllFeatures(Registry<ConfiguredFeature<?, ?>> registry) {
        List<FeatureInfo> features = new ArrayList<>();
        
        if (registry == null) {
            LOGGER.warn("特征注册表为空");
            return features;
        }
        
        for (ConfiguredFeature<?, ?> feature : registry) {
            ResourceLocation id = registry.getKey(feature);
            if (id != null) {
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
     * @param modId 模组ID
     * @return 过滤后的结构列表
     */
    public static List<StructureInfo> filterByModId(List<StructureInfo> structures, String modId) {
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
    public static Set<String> getAllModIds(List<StructureInfo> structures) {
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
    public static void printAllStructures(List<StructureInfo> structures) {
        if (structures.isEmpty()) {
            LOGGER.info("未找到任何结构");
            return;
        }
        
        LOGGER.info("=== 所有注册的自然生成结构 ===");
        LOGGER.info("=== All Registered Natural Generation Structures ===");
        
        String currentMod = "";
        for (StructureInfo info : structures) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                String modName = getModName(currentMod);
                LOGGER.info("--- {} ({}) ---", modName, currentMod);
            }
            LOGGER.info("  - {} (类型: {})", info.getStructureName(), info.getStructureType());
        }
        
        LOGGER.info("=== 总计: {} 个结构 ===", structures.size());
        LOGGER.info("=== Total: {} structures ===", structures.size());
    }
    
    /**
     * 获取模组名称
     * Get mod name
     * 
     * @param modId 模组ID
     * @return 模组名称
     */
    private static String getModName(String modId) {
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
     * @param structures 结构列表
     * @param structureName 结构名称（支持部分匹配）
     * @return 匹配的结构列表
     */
    public static List<StructureInfo> findStructure(List<StructureInfo> structures, String structureName) {
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
    public static void countStructuresByMod(List<StructureInfo> structures) {
        LOGGER.info("=== 各模组结构数量统计 ===");
        LOGGER.info("=== Structure Count by Mod ===");
        
        structures.stream()
            .collect(Collectors.groupingBy(StructureInfo::getModId, Collectors.counting()))
            .entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // 按数量降序
            .forEach(entry -> {
                String modName = getModName(entry.getKey());
                LOGGER.info("{} ({}): {} 个结构", modName, entry.getKey(), entry.getValue());
            });
    }
}
