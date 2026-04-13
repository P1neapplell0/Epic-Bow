package com.p1nero.epicfightbow.mob_effect;

import com.p1nero.epicfightbow.EpicFightBowMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EFBowEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, EpicFightBowMod.MOD_ID);
    public static final DeferredHolder<MobEffect, DoubleArrowEffect> DOUBLE_ARROW = MOB_EFFECTS.register("double_arrow", () -> new DoubleArrowEffect(0X6c6a5c));
}
