package com.teampotato.modifiers.common.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ToolConfig {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> NAMES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> WEIGHTS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ATTRIBUTES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> AMOUNTS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> OPERATIONS_IDS;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        BUILDER.push("Modifiers for tools");
        BUILDER.comment("This configuration file is based on index. it means, 'legendary' -> '30' ->'generic.attack_damage;generic.attack_speed' -> '0.15;0.1' -> '2;2' is the first index, and in it, 'generic.attack_damage' -> '0.15' -> '2', 'generic.attack_speed' -> '0.1' -> '2'", "WARNING: You have to make a resource pack to save the customization-required translation key for the attributes if the mod author didn't do that, or your customization on other mods' attribute will crash the game!", "A hint on the translation key format: attribute.modxxx.attributexxx, e.g. attribute.minecraft.generic.attack_damage");
        NAMES = BUILDER.comment("The name of the modifier").defineList("NAMES", Lists.newArrayList("legendary", "deadly", "vicious", "sharp", "broken", "damaged", "agile", "swift", "sluggish", "slow", "light", "heavy"), o -> true);
        WEIGHTS = BUILDER.comment("The weight of the modifier in the modifiers pool").defineList("WEIGHTS", Lists.newArrayList("30", "100", "100", "100", "70", "100", "100", "100", "100", "100", "100", "100"), o -> true);
        ATTRIBUTES = BUILDER.comment("The attribute of the modifier has. One modifier can have multiple attributes. Use ';' to split different attributes").defineList("ATTRIBUTES", Lists.newArrayList("minecraft:generic.attack_damage;minecraft:generic.attack_speed", "minecraft:generic.attack_damage", "minecraft:generic.attack_damage", "minecraft:generic.attack_damage", "minecraft:generic.attack_damage", "minecraft:generic.attack_damage", "minecraft:generic.attack_speed;minecraft:generic.movement_speed", "minecraft:generic.attack_speed", "minecraft:generic.attack_speed;minecraft:generic.movement_speed", "minecraft:generic.attack_speed", "minecraft:generic.attack_damage;minecraft:generic.attack_speed", "minecraft:generic.attack_damage;minecraft:generic.attack_speed;minecraft:generic.movement_speed"), o -> true);
        AMOUNTS = BUILDER.comment("The amount used to calculate the attribute effect. Also can be multiple. Use ';' to split").defineList("AMOUNTS", Lists.newArrayList("0.15;0.1", "0.15", "0.1", "0.05", "-0.2", "-0.1", "0.05;0.1", "0.1", "-0.05;-0.1", "-0.15", "-0.1;0.15", "0.2;-0.15;-0.05"), o -> true);
        OPERATIONS_IDS = BUILDER.comment("The operation ID of the attribute calculation. Can be three values: 0,1,2. 0 is ADDITION. 1 is MULTIPLY_BASE. 2 is MULTIPLY_TOTAL. you can refer to the calculation of the attributes already in the game").defineList("OPERATIONS_IDS", Lists.newArrayList("2;2", "2", "2", "2", "2", "2", "2;2", "2", "2;2", "2", "2;2", "2;2;2"), o -> true);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}