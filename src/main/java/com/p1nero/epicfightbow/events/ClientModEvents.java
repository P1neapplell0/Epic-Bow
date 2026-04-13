package com.p1nero.epicfightbow.events;

import com.p1nero.epicfightbow.EpicFightBowMod;
import com.p1nero.epicfightbow.item.EFBowItemState;
import com.p1nero.epicfightbow.item.EFBowItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@EventBusSubscriber(modid = EpicFightBowMod.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemPropertyFunction pull = (itemStack, clientLevel, livingEntity, p_174638_) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getEntityPatch(livingEntity, LivingEntityPatch.class);
                if (livingEntityPatch != null && EFBowItemState.isFull(itemStack)) {
                    return 0.9F;
                }
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks()) / 20.0F;
            }
        };
        ItemPropertyFunction pulling = (itemStack, clientLevel, livingEntity, p_174633_) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getEntityPatch(livingEntity, LivingEntityPatch.class);
            if (livingEntityPatch != null && EFBowItemState.isFull(itemStack)) {
                return 1.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        };
        ItemProperties.register(EFBowItems.MORTIS.get(), ResourceLocation.fromNamespaceAndPath(EpicFightBowMod.MOD_ID, "pull"), pull);
        ItemProperties.register(EFBowItems.MORTIS.get(), ResourceLocation.fromNamespaceAndPath(EpicFightBowMod.MOD_ID, "pulling"), pulling);
        ItemProperties.register(Items.BOW, ResourceLocation.fromNamespaceAndPath(EpicFightBowMod.MOD_ID, "pull"), pull);
        ItemProperties.register(Items.BOW, ResourceLocation.fromNamespaceAndPath(EpicFightBowMod.MOD_ID, "pulling"), pulling);

    }
}
