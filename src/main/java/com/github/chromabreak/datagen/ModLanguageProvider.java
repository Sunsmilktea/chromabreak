package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModBlocks;
import com.github.chromabreak.util.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;

/**
 * ModLanguageProvider - 语言文件数据生成器
 * ModLanguageProvider - Language File Data Generator
 * <p>
 * 这个类负责为ChromaBreak模组生成多语言文件
 * This class is responsible for generating multilingual files for the ChromaBreak mod
 * <p>
 * 继承自NeoForge的LanguageProvider，用于在数据生成阶段自动创建语言文件
 * Extends NeoForge's LanguageProvider to automatically create language files during data generation phase
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 为物品和方块添加英文和中文翻译
 * Add English and Chinese translations for items and blocks
 * - 支持多种语言环境（en_us, zh_cn等）
 * Support multiple locales (en_us, zh_cn, etc.)
 * - 提供便捷方法添加物品和方块的翻译
 * Provide convenient methods to add translations for items and blocks
 * <p>
 * 生成的数据类型：
 * Generated data types:
 * - 语言JSON文件 (assets/chromabreak/lang/)
 * Language JSON files (assets/chromabreak/lang/)
 * <p>
 * 使用方法：
 * Usage:
 * 1. 创建实例时指定语言环境（en_us或zh_cn）
 * Specify locale (en_us or zh_cn) when creating instance
 * 2. 在addTranslations方法中添加翻译
 * Add translations in the addTranslations method
 * 3. 使用addItem和addBlock方法添加物品和方块的翻译
 * Use addItem and addBlock methods to add translations for items and blocks
 */
public class ModLanguageProvider extends LanguageProvider {
    private final String locale;  // 新增：保存传入的 locale
    // New: Store the passed locale

    /**
     * 构造函数 - 初始化语言提供器
     * Constructor - Initializes the language provider
     *
     * @param output PackOutput实例，用于输出生成的语言文件
     *               PackOutput instance for outputting generated language files
     * @param locale 语言环境代码（如"en_us"或"zh_cn"）
     *               Locale code (e.g., "en_us" or "zh_cn")
     */
    public ModLanguageProvider(final PackOutput output, final String locale) {
        super(output, ChromaBreak.MODID, locale);  // 正确调用父类构造器
        // Correctly call parent constructor
        this.locale = locale;  // 保存起来供后面使用
        // Store for later use
    }

    /**
     * 添加翻译 - 注册所有物品和方块的翻译
     * Add translations - register translations for all items and blocks
     * <p>
     * 这个方法在数据生成时被调用，用于添加模组中物品和方块的翻译
     * This method is called during data generation to add translations for items and blocks in the mod
     * <p>
     * 当前实现：
     * Current implementation:
     * - 添加橙色水晶碎片及其相关方块的翻译
     * Add translations for orange crystal shard and related blocks
     */
    @Override
    protected void addTranslations() {
        // 所有物品/方块都走这个方法
        // All items/blocks go through this method
        this.addItem("Orange Crystal Shard", "橙水晶碎片");

        this.addBlock(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD, "Small Orange Crystal Bud", "小型橙晶芽");
        this.addBlock(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD, "Medium Orange Crystal Bud", "中型橙晶芽");
        this.addBlock(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD, "Large Orange Crystal Bud", "大型橙晶芽");
        this.addBlock(ModBlocks.CRYSTALS_ORANGE_CLUSTER, "Orange Crystal Cluster", "橙水晶簇");

        this.addBlock(ModBlocks.BUDDING_ORANGE_CRYSTALS, "Budding Orange Crystals", "橙水晶母岩");
        this.addBlock(ModBlocks.CRYSTALS_ORANGE_BLOCK, "Block of Orange Crystals", "橙水晶块");

        //add("itemGroup.chromabreak.main", "ChromaBreak", "彩晶破晓");
    }

    /**
     * 添加物品翻译 - 为指定物品添加翻译
     * Add item translation - add translation for specified item
     *
     * @param en 英文翻译
     *           English translation
     * @param zh 中文翻译
     *           Chinese translation
     */
    private void addItem(final String en, final String zh) {
        this.add(((Supplier<? extends Item>) ModItems.ORANGE_CRYSTAL_SHARD).get().getDescriptionId(), en, zh);
    }

    /**
     * 添加方块翻译 - 为指定方块添加翻译
     * Add block translation - add translation for specified block
     *
     * @param block 方块提供器
     *              Block supplier
     * @param en    英文翻译
     *              English translation
     * @param zh    中文翻译
     *              Chinese translation
     */
    private void addBlock(final Supplier<? extends Block> block, final String en, final String zh) {
        this.add(block.get().getDescriptionId(), en, zh);
    }

    /**
     * 添加翻译 - 根据语言环境选择正确的翻译
     * Add translation - select correct translation based on locale
     *
     * @param key 翻译键
     *            Translation key
     * @param en  英文翻译
     *            English translation
     * @param zh  中文翻译
     *            Chinese translation
     */
    private void add(final String key, final String en, final String zh) {
        this.add(key, this.getText(en, zh));
    }

    /**
     * 获取文本 - 根据语言环境返回英文或中文文本
     * Get text - return English or Chinese text based on locale
     *
     * @param en 英文文本
     *           English text
     * @param zh 中文文本
     *           Chinese text
     * @return 根据语言环境选择的文本
     * Text selected based on locale
     */
    private String getText(final String en, final String zh) {
        return "en_us".equals(this.locale) ? en : zh;  // 这里用保存的 locale
        // Use stored locale here
    }
}
