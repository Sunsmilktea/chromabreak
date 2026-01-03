package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChromaBreak.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CRYSTALS_ORANGE_BLOCK.get())
                .add(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.CRYSTALS_ORANGE_BLOCK.get())
                .add(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get());
    }
}

