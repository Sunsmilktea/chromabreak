package com.github.chromabreak.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenerationProvider extends DatapackBuiltinEntriesProvider {
    public ModWorldGenerationProvider(final PackOutput output, final CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries, final Set<String> modIds) {
        super(output, registries, modIds);
    }
}
