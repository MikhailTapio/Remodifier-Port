package com.teampotato.modifiers.common.curios;

import net.minecraft.item.ItemStack;

public interface ICurioProxy {
    default boolean isModifiableCurio(ItemStack stack) {
        return false;
    }
}

