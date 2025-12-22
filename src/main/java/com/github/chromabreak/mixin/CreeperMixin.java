package com.github.chromabreak.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 爬行者Mixin
 * 处理爬行者爆炸时的韧性和血量清空逻辑
 * <p>
 * Creeper Mixin
 * Handles toughness and health clearing logic when creeper explodes
 */
@Mixin(Creeper.class)
public class CreeperMixin {

    /**
     * 在爬行者爆炸时注入，清空韧性和血量
     * Inject when creeper explodes, clear toughness and health
     * 使用多种注入点以确保兼容性
     * Use multiple injection points for compatibility
     *
     * @param ci 回调信息
     */
    @Inject(method = "explodeCreeper", at = @At("HEAD"), remap = true)
    private void onExplodeCreeper(final CallbackInfo ci) {
        this.clearToughnessAndHealth();
    }

    // 注意：hurt 方法在 LivingEntity 中，不在 Creeper 中
    // 爬行者爆炸时的韧性和血量清空已通过 explodeCreeper 方法处理
    // Note: hurt method is in LivingEntity, not in Creeper
    // Creeper explosion toughness and health clearing is handled via explodeCreeper method

    /**
     * 清空韧性和血量
     * Clear toughness and health
     */
    private void clearToughnessAndHealth() {
        final Creeper creeper = (Creeper) (Object) this;

        // 检查是否有韧性值
        // Check if has toughness value
        final net.minecraft.nbt.CompoundTag tag = creeper.getPersistentData();
        if (tag.contains("chromabreak_toughness")) {
            // 清空韧性值
            // Clear toughness value
            tag.putFloat("chromabreak_toughness", 0.0f);
            tag.putBoolean("chromabreak_toughness_broken", true);
        }

        // 清空血量（设置为0）
        // Clear health (set to 0)
        creeper.setHealth(0.0f);
    }
}
