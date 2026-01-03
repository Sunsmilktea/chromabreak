package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModBlocks;
import com.github.chromabreak.util.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;


public class ModLanguageProvider extends LanguageProvider {
    private final String locale;  // 新增：保存传入的 locale

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, ChromaBreak.MODID, locale);  // 正确调用父类构造器
        this.locale = locale;  // 保存起来供后面使用
    }

    @Override
    protected void addTranslations() {
        // 所有物品/方块都走这个方法
        addItem(ModItems.ORANGE_CRYSTAL_SHARD, "Orange Crystal Shard", "橙水晶碎片");

        addBlock(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD, "Small Orange Crystal Bud", "小型橙晶芽");
        addBlock(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD, "Medium Orange Crystal Bud", "中型橙晶芽");
        addBlock(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD, "Large Orange Crystal Bud", "大型橙晶芽");
        addBlock(ModBlocks.CRYSTALS_ORANGE_CLUSTER, "Orange Crystal Cluster", "橙水晶簇");

        addBlock(ModBlocks.BUDDING_ORANGE_CRYSTALS, "Budding Orange Crystals", "橙水晶母岩");
        addBlock(ModBlocks.CRYSTALS_ORANGE_BLOCK, "Block of Orange Crystals", "橙水晶块");

        //add("itemGroup.chromabreak.main", "ChromaBreak", "彩晶破晓");
    }

    private void addItem(Supplier<? extends Item> item, String en, String zh) {
        add(item.get().getDescriptionId(), en, zh);
    }

    private void addBlock(Supplier<? extends Block> block, String en, String zh) {
        add(block.get().getDescriptionId(), en, zh);
    }

    private void add(String key, String en, String zh) {
        add(key, getText(en, zh));
    }

    private String getText(String en, String zh) {
        return "en_us".equals(locale) ? en : zh;  // 这里用保存的 locale
    }
}
