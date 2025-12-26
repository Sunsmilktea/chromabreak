package com.github.chromabreak.tool;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 结构查找命令
 * Structure Finder Command
 * <p>
 * 提供聊天命令来查找游戏中的自然生成结构
 * Provides chat commands to find natural generation structures in the game
 */
public class StructureFinderCommand {
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * 注册命令
     * Register commands
     * 
     * @param dispatcher 命令分发器
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("findstructures")
            .requires(source -> source.hasPermission(2)) // 需要操作员权限
            .executes(StructureFinderCommand::listAllStructures)
            .then(Commands.literal("all")
                .executes(StructureFinderCommand::listAllStructures))
            .then(Commands.literal("mod")
                .then(Commands.argument("modid", StringArgumentType.string())
                    .executes(StructureFinderCommand::listStructuresByMod)))
            .then(Commands.literal("search")
                .then(Commands.argument("name", StringArgumentType.string())
                    .executes(StructureFinderCommand::searchStructures)))
            .then(Commands.literal("count")
                .executes(StructureFinderCommand::countStructures))
            .then(Commands.literal("locate")
                .then(Commands.argument("structure", StringArgumentType.string())
                    .executes(StructureFinderCommand::locateStructure)))
        );
    }
    
    /**
     * 列出所有结构
     * List all structures
     * 
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int listAllStructures(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);
        
        if (structureRegistry == null) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }
        
        List<StructureFinder.StructureInfo> structures = StructureFinder.getAllStructures(structureRegistry);
        
        if (structures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("未找到任何结构"), false);
            return 1;
        }
        
        source.sendSuccess(() -> Component.literal("=== 所有注册的自然生成结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== All Registered Natural Generation Structures ==="), false);
        
        String currentMod = "";
        for (StructureFinder.StructureInfo info : structures) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                String modName = getModName(currentMod);
                String finalCurrentMod = currentMod;
                source.sendSuccess(() -> Component.literal("--- " + modName + " (" + finalCurrentMod + ") ---"), false);
            }
            source.sendSuccess(() -> Component.literal("  - " + info.getStructureName() + " (类型: " + info.getStructureType() + ")"), false);
        }
        
        source.sendSuccess(() -> Component.literal("=== 总计: " + structures.size() + " 个结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + structures.size() + " structures ==="), false);
        
        // 同时在控制台输出
        StructureFinder.printAllStructures(structures);
        
        return structures.size();
    }
    
    /**
     * 定位并验证结构或特征生成
     * Locate and verify structure or feature generation
     * 
     * @param context 命令上下文
     * @return 命令结果
     */
    private static int locateStructure(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String targetName = StringArgumentType.getString(context, "structure");
        
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("只有玩家可以使用此命令"));
            return 0;
        }
        
        try {
            // 构建完整的ID
            String fullId;
            if (targetName.contains(":")) {
                fullId = targetName; // 已经是完整ID
            } else {
                fullId = "chromabreak:" + targetName; // 添加默认命名空间
            }
            
            // 首先尝试作为结构定位
            String structureLocateCommand = "locate structure " + fullId;
            var structureResult = source.getServer().getCommands().getDispatcher().execute(
                structureLocateCommand, 
                source.withSuppressedOutput().withMaximumPermission(2)
            );
            
            if (structureResult > 0) {
                source.sendSuccess(() -> Component.literal("✅ 已找到结构: " + fullId), false);
                source.sendSuccess(() -> Component.literal("💡 可以使用 /locate structure " + fullId + " 查看具体位置"), false);
                source.sendSuccess(() -> Component.literal("🚀 然后使用 /tp 命令传送到该位置"), false);
                return structureResult;
            }
            
            // 如果结构未找到，尝试作为特征定位
            String featureLocateCommand = "locate feature " + fullId;
            var featureResult = source.getServer().getCommands().getDispatcher().execute(
                featureLocateCommand, 
                source.withSuppressedOutput().withMaximumPermission(2)
            );
            
            if (featureResult > 0) {
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
            
            return 0;
        } catch (Exception e) {
            source.sendFailure(Component.literal("❌ 定位时出错: " + e.getMessage()));
            LOGGER.error("定位时出错:", e);
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
    private static int listStructuresByMod(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String modId = StringArgumentType.getString(context, "modid");
        Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);
        
        if (structureRegistry == null) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }
        
        List<StructureFinder.StructureInfo> allStructures = StructureFinder.getAllStructures(structureRegistry);
        List<StructureFinder.StructureInfo> filteredStructures = StructureFinder.filterByModId(allStructures, modId);
        
        if (filteredStructures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("模组 '" + modId + "' 未注册任何结构"), false);
            return 0;
        }
        
        String modName = getModName(modId);
        source.sendSuccess(() -> Component.literal("=== " + modName + " (" + modId + ") 的结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Structures for " + modName + " (" + modId + ") ==="), false);
        
        for (StructureFinder.StructureInfo info : filteredStructures) {
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
    private static int searchStructures(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String searchTerm = StringArgumentType.getString(context, "name");
        Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);
        
        if (structureRegistry == null) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }
        
        List<StructureFinder.StructureInfo> allStructures = StructureFinder.getAllStructures(structureRegistry);
        List<StructureFinder.StructureInfo> foundStructures = StructureFinder.findStructure(allStructures, searchTerm);
        
        if (foundStructures.isEmpty()) {
            source.sendSuccess(() -> Component.literal("未找到包含 '" + searchTerm + "' 的结构"), false);
            return 0;
        }
        
        source.sendSuccess(() -> Component.literal("=== 包含 '" + searchTerm + "' 的结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Structures containing '" + searchTerm + "' ==="), false);
        
        String currentMod = "";
        for (StructureFinder.StructureInfo info : foundStructures) {
            if (!currentMod.equals(info.getModId())) {
                currentMod = info.getModId();
                String modName = getModName(currentMod);
                String finalCurrentMod = currentMod;
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
    private static int countStructures(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Registry<Structure> structureRegistry = source.registryAccess().registry(Registries.STRUCTURE).orElse(null);
        
        if (structureRegistry == null) {
            source.sendFailure(Component.literal("无法访问结构注册表"));
            return 0;
        }
        
        List<StructureFinder.StructureInfo> structures = StructureFinder.getAllStructures(structureRegistry);
        
        source.sendSuccess(() -> Component.literal("=== 各模组结构数量统计 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Structure Count by Mod ==="), false);
        
        structures.stream()
            .collect(java.util.stream.Collectors.groupingBy(StructureFinder.StructureInfo::getModId, java.util.stream.Collectors.counting()))
            .entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // 按数量降序
            .forEach(entry -> {
                String modName = getModName(entry.getKey());
                source.sendSuccess(() -> Component.literal(modName + " (" + entry.getKey() + "): " + entry.getValue() + " 个结构"), false);
            });
        
        source.sendSuccess(() -> Component.literal("=== 总计: " + structures.size() + " 个结构 ==="), false);
        source.sendSuccess(() -> Component.literal("=== Total: " + structures.size() + " structures ==="), false);
        
        // 同时在控制台输出统计
        StructureFinder.countStructuresByMod(structures);
        
        return structures.size();
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
        
        return net.neoforged.fml.ModList.get().getModContainerById(modId)
            .map(container -> container.getModInfo().getDisplayName())
            .orElse(modId);
    }
}
