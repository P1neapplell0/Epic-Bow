package com.p1nero.epicfightbow.item;

import com.p1nero.epicfightbow.EpicFightBowMod;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EFBowItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EpicFightBowMod.MOD_ID);
    public static final DeferredItem<MortisBowItem> MORTIS = ITEMS.register("mortis", () -> new MortisBowItem(new Item.Properties().rarity(Rarity.EPIC).durability(2025).fireResistant()));
    public static final DeferredItem<BowItem> OBLIVIONIS = ITEMS.register("oblivionis", () -> new BowItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
}
