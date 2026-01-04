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

    public ModLanguageProvider(final PackOutput output, final String locale) {
        super(output, ChromaBreak.MODID, locale);  // 正确调用父类构造器
        this.locale = locale;  // 保存起来供后面使用
    }

    @Override
    protected void addTranslations() {
        // 所有物品/方块都走这个方法
        this.addItem("Orange Crystal Shard", "橙水晶碎片");

        this.addBlock(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD, "Small Orange Crystal Bud", "小型橙晶芽");
        this.addBlock(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD, "Medium Orange Crystal Bud", "中型橙晶芽");
        this.addBlock(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD, "Large Orange Crystal Bud", "大型橙晶芽");
        this.addBlock(ModBlocks.CRYSTALS_ORANGE_CLUSTER, "Orange Crystal Cluster", "橙水晶簇");

        this.addBlock(ModBlocks.BUDDING_ORANGE_CRYSTALS, "Budding Orange Crystals", "橙水晶母岩");
        this.addBlock(ModBlocks.CRYSTALS_ORANGE_BLOCK, "Block of Orange Crystals", "橙水晶块");

        //add("itemGroup.chromabreak.main", "ChromaBreak", "彩晶破晓");
    }

    private void addItem(final String en, final String zh) {
        this.add(((Supplier<? extends Item>) ModItems.ORANGE_CRYSTAL_SHARD).get().getDescriptionId(), en, zh);
    }

    private void addBlock(final Supplier<? extends Block> block, final String en, final String zh) {
        this.add(block.get().getDescriptionId(), en, zh);
    }

    private void add(final String key, final String en, final String zh) {
        this.add(key, this.getText(en, zh));
    }

    private String getText(final String en, final String zh) {
        return "en_us".equals(this.locale) ? en : zh;  // 这里用保存的 locale
    }
}
