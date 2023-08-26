package com.teampotato.modifiers.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ReforgeConfig {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.ConfigValue<? extends String> UNIVERSAL_REFORGE_ITEM;
    public static ForgeConfigSpec.BooleanValue DISABLE_REPAIR_REFORGED;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Remodifier");
        UNIVERSAL_REFORGE_ITEM = builder.define("UniversalReforgeItem", "minecraft:diamond", o -> o instanceof String);
        DISABLE_REPAIR_REFORGED = builder.define("DisableRepairingReforging", false);
        builder.pop();
        CONFIG = builder.build();
    }
}