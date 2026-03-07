package com.p1nero.epicfightbow.gameassets;


import com.p1nero.epicfightbow.EpicFightBowMod;
import com.p1nero.epicfightbow.capabilities.item.BowCapability;
import com.p1nero.epicfightbow.capabilities.item.MortisBowCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = EpicFightBowMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EFBowWeaponPresets {
    public static final Function<Item, CapabilityItem.Builder> BOW =
            (item) -> RangedWeaponCapability.builder()
                    .zoomInType(CapabilityItem.ZoomInType.USE_TICK)
                    .addAnimationsModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
                    .addAnimationsModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
                    .constructor(BowCapability::new);

    public static final Function<Item, CapabilityItem.Builder> MORTIS =
            (item) -> RangedWeaponCapability.builder()
                    .zoomInType(CapabilityItem.ZoomInType.USE_TICK)
                    .addAnimationsModifier(LivingMotions.RUN, EFBowAnimations.BOW_RUN)
                    .addAnimationsModifier(LivingMotions.CHASE, EFBowAnimations.BOW_RUN)
                    .addAnimationsModifier(LivingMotions.AIM, Animations.BIPED_BOW_AIM)
                    .addAnimationsModifier(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT)
                    .constructor(MortisBowCapability::new);
    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(EpicFightBowMod.MOD_ID, "bow"), BOW);
        event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(EpicFightBowMod.MOD_ID, "mortis"), MORTIS);
    }

}
