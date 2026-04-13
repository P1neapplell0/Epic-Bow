package com.p1nero.epicfightbow.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class EFBowItemState {
    private static final String FULL_DRAW_KEY = "is_full";

    private EFBowItemState() {
    }

    public static boolean isFull(ItemStack itemStack) {
        CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.contains(FULL_DRAW_KEY) && customData.copyTag().getBoolean(FULL_DRAW_KEY);
    }

    public static void setFull(ItemStack itemStack, boolean isFull) {
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> {
            if (isFull) {
                tag.putBoolean(FULL_DRAW_KEY, true);
            } else {
                tag.remove(FULL_DRAW_KEY);
            }
        });
    }
}
