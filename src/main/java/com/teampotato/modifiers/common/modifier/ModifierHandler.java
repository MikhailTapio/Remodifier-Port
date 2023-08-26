package com.teampotato.modifiers.common.modifier;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class ModifierHandler {
    public static final long COMMON_SEGMENT_CURIO = (0x7a6ca76cL) << 32;
    public static final long COMMON_SEGMENT_EQUIPMENT = 0x9225d5c4fd8d434bL;
    public static final String tagName = "itemModifier";
    public static final String bookTagName = "bookModifier";

    public static boolean canHaveModifiers(ItemStack stack) {
        return !stack.isEmpty() && stack.getMaxCount() <= 1 /* && stack.getItem() != ModItems.modifier_book*/;
    }

    @Nullable
    public static Modifier rollModifier(ItemStack stack, Random random) {
        if (!canHaveModifiers(stack)) return null;
        // TODO if multiple pools applicable, roll between them
        if (Modifiers.curio_pool.isApplicable.test(stack)) return Modifiers.curio_pool.roll(random);
        if (Modifiers.tool_pool.isApplicable.test(stack)) return Modifiers.tool_pool.roll(random);
        if (Modifiers.bow_pool.isApplicable.test(stack)) return Modifiers.bow_pool.roll(random);
        if (Modifiers.shield_pool.isApplicable.test(stack)) return Modifiers.shield_pool.roll(random);
        return null;
    }

    public static void setModifier(ItemStack stack, Modifier modifier) {
        NbtCompound tag = stack.getOrCreateNbt();
        tag.remove(tagName);
        tag.putString(tagName, modifier.name.toString());
    }

    public static boolean hasModifier(ItemStack stack) {
        NbtCompound tag = stack.getNbt();
        return tag != null && tag.contains(tagName);
    }

    @Nullable
    public static Modifier getModifier(ItemStack stack) {
        NbtCompound tag = stack.getNbt();
        if (tag == null) return null;
        if (!tag.contains(tagName)) return null;
        return Modifiers.MODIFIERS.get(new Identifier(tag.getString(tagName)));
    }

    public static UUID getCurioUuid(String identifier, int slot, int attributeIndex) {
        // There's probably a smarter way to do deterministic UUIDs but whatever :shrug:
        long second = (((long) attributeIndex) << 32) | ((long) identifier.hashCode());
        long first = COMMON_SEGMENT_CURIO | (((long) slot) << 32);
        return new UUID(first, second);
    }

    public static void applyCurioModifier(LivingEntity entity, Modifier modifier, String slotIdentifier, int index) {
        if (modifier.type == Modifier.ModifierType.HELD) {
            return;
        }
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<EntityAttribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getCurioUuid(slotIdentifier, index, i);
            EntityAttributeInstance instance = entity.getAttributeInstance(entry.getKey());
            if (instance != null && instance.getModifier(id) == null) {
                instance.addTemporaryModifier(entry.getValue().getAttributeModifier(id, "curio_modifier_" + modifier.debugName));
            }
        }
    }

    public static void removeCurioModifier(LivingEntity entity, Modifier modifier, String slotIdentifier, int index) {
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<EntityAttribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getCurioUuid(slotIdentifier, index, i);
            EntityAttributeInstance instance = entity.getAttributeInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(id);
            }
        }
    }

    public static UUID getEquipmentUuid(EquipmentSlot slot, int attributeIndex) {
        long second = (((long) attributeIndex) << 32) | ((long) slot.hashCode());
        return new UUID(COMMON_SEGMENT_EQUIPMENT, second);
    }

    public static void applyEquipmentModifier(LivingEntity entity, Modifier modifier, EquipmentSlot type) {
        if (modifier.type == Modifier.ModifierType.HELD && type.getType() == EquipmentSlot.Type.ARMOR
                || modifier.type == Modifier.ModifierType.EQUIPPED && type.getType() == EquipmentSlot.Type.HAND) {
            return;
        }
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<EntityAttribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getEquipmentUuid(type, i);
            EntityAttributeInstance instance = entity.getAttributeInstance(entry.getKey());
            if (instance != null && instance.getModifier(id) == null) {
                instance.addTemporaryModifier(entry.getValue().getAttributeModifier(id, "equipment_modifier_" + modifier.debugName));
            }
        }
    }

    public static void removeEquipmentModifier(LivingEntity entity, Modifier modifier, EquipmentSlot type) {
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<EntityAttribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getEquipmentUuid(type, i);
            EntityAttributeInstance instance = entity.getAttributeInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(id);
            }
        }
    }
}

