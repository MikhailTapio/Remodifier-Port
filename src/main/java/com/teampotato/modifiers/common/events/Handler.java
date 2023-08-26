package com.teampotato.modifiers.common.events;

import com.teampotato.modifiers.common.item.ItemModifierBook;
import com.teampotato.modifiers.common.modifier.Modifier;
import com.teampotato.modifiers.common.modifier.ModifierHandler;
import com.teampotato.modifiers.common.modifier.Modifiers;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Handler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack right = event.getRight();
        if (right.getItem() instanceof ItemModifierBook && ModifierHandler.canHaveModifiers(event.getLeft()) && right.getNbt() != null) {
            Modifier modifier = Modifiers.MODIFIERS.get(new Identifier(right.getNbt().getString(ModifierHandler.bookTagName)));
            if (modifier != null) {
                ItemStack output = event.getLeft().copy();
                ModifierHandler.setModifier(output, modifier);
                event.setMaterialCost(1);
                event.setCost(1);
                event.setOutput(output);
                event.setCanceled(false);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEquipChange(LivingEquipmentChangeEvent event) {
        EquipmentSlot slotType = event.getSlot();
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        Modifier fromMod = ModifierHandler.getModifier(from);
        if (fromMod != null) ModifierHandler.removeEquipmentModifier(event.getEntity(), fromMod, slotType);
        Modifier toMod = ModifierHandler.getModifier(to);
        if (toMod == null) {
            toMod = ModifierHandler.rollModifier(to, ThreadLocalRandom.current());
            if (toMod == null) return;
            ModifierHandler.setModifier(to, toMod);
        }
        ModifierHandler.applyEquipmentModifier(event.getEntity(), toMod, slotType);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGetTooltip(ItemTooltipEvent event) {
        Modifier modifier = ModifierHandler.getModifier(event.getItemStack());
        if (modifier != null) {
            event.getToolTip().addAll(modifier.getInfoLines());
        }
    }
}
