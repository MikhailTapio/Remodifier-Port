package com.teampotato.modifiers.mixin;

import com.teampotato.modifiers.common.config.ReforgeConfig;
import com.teampotato.modifiers.common.modifier.Modifier;
import com.teampotato.modifiers.common.modifier.ModifierHandler;
import com.teampotato.modifiers.common.reforge.SmithingScreenHandlerReforge;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(SmithingScreenHandler.class)
public abstract class MixinSmithingTableContainer extends ForgingScreenHandler implements SmithingScreenHandlerReforge {
    public MixinSmithingTableContainer(ScreenHandlerType<?> a, int b, PlayerInventory c, ScreenHandlerContext d) {
        super(a, b, c, d);
    }

    @Override
    public void modifiers$tryReforge() {
        if (ReforgeConfig.DISABLE_REPAIR_REFORGED.get()) return;
        ItemStack stack = input.getStack(0);
        ItemStack material = input.getStack(1);

        if (ModifierHandler.canHaveModifiers(stack)) {
            if (stack.getItem().canRepair(stack, material) || ReforgeConfig.UNIVERSAL_REFORGE_ITEM.get().equals(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(material.getItem())).toString())) {

                boolean hadModifier = ModifierHandler.hasModifier(stack);
                Modifier modifier = ModifierHandler.rollModifier(stack, ThreadLocalRandom.current());
                if (modifier != null) {
                    ModifierHandler.setModifier(stack, modifier);
                    if (hadModifier) {
                        material.decrement(1);
                        // We do this for markDirty() mostly, I think
                        input.setStack(1, material);
                    }
                }
            }
        }
    }
}