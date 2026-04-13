package com.p1nero.epicfightbow;

import com.p1nero.epicfightbow.gameassets.EFBowSkills;
import com.p1nero.epicfightbow.gameassets.EFBowWeaponPresets;
import com.p1nero.epicfightbow.item.EFBowItems;
import com.p1nero.epicfightbow.mob_effect.EFBowEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.registry.entries.EpicFightCreativeTabs;

@Mod(EpicFightBowMod.MOD_ID)
public class EpicFightBowMod {

    public static final String MOD_ID = "p1nero_bow";

    public EpicFightBowMod(IEventBus modEventBus) {
        EFBowItems.ITEMS.register(modEventBus);
        EFBowEffects.MOB_EFFECTS.register(modEventBus);
        EFBowSkills.SKILLS.register(modEventBus);
        modEventBus.addListener(this::addCreativeTab);

        EpicFightEventHooks.Registry.WEAPON_CAPABILITY_PRESET.registerEvent(EFBowWeaponPresets::register, MOD_ID);
    }

    private void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == EpicFightCreativeTabs.ITEMS.get()) {
            event.accept(EFBowItems.MORTIS);
        }
    }
}
