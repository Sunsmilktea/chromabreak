package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {


    public ModItemTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final CompletableFuture<TagLookup<Block>> blockTags, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ChromaBreak.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {

    }
}
