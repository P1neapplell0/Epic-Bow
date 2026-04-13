package com.p1nero.epicfightbow.item;

import com.p1nero.epicfightbow.mob_effect.EFBowEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MortisBowItem extends BowItem {
    public MortisBowItem(Properties properties) {
        super(properties);
    }

    public void releaseUsingSingleShot(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int remainTime) {
        super.releaseUsing(itemStack, level, livingEntity, remainTime);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int remainTime) {
        if (livingEntity.hasEffect(EFBowEffects.DOUBLE_ARROW)) {
            super.releaseUsing(itemStack, level, livingEntity, remainTime);
        }
        super.releaseUsing(itemStack, level, livingEntity, remainTime);
    }

}
