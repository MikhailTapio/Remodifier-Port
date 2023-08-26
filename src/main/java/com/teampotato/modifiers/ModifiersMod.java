package com.teampotato.modifiers;

import com.teampotato.modifiers.common.config.*;
import com.teampotato.modifiers.common.curios.ICurioProxy;
import com.teampotato.modifiers.common.events.Handler;
import com.teampotato.modifiers.common.item.ItemModifierBook;
import com.teampotato.modifiers.common.modifier.Modifiers;
import com.teampotato.modifiers.common.network.NetworkHandler;
import com.teampotato.modifiers.common.network.NetworkHandlerForge;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModifiersMod.MOD_ID)
public class ModifiersMod {
    public static final String MOD_ID = "modifiers";
    public static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> MODIFIER_BOOK;
    public static final Logger LOGGER = LogManager.getLogger();
    public static ICurioProxy CURIO_PROXY;
    private static final DeferredRegister<ItemGroup> GROUPS = DeferredRegister.create(Registries.ITEM_GROUP.getKey(), MOD_ID);
    public static RegistryObject<ItemGroup> GROUP_BOOKS;

    public ModifiersMod() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final ModLoadingContext ctx = ModLoadingContext.get();
        final ModConfig.Type COMMON = ModConfig.Type.COMMON;
        NetworkHandler.register();
        ITEM_DEFERRED_REGISTER.register(eventBus);
        GROUPS.register(eventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(Handler.class);
        ctx.registerConfig(COMMON, ReforgeConfig.CONFIG, "remodifier/reforge.toml");
        ctx.registerConfig(COMMON, CurioNArmorConfig.CONFIG, "remodifier/armor-n-curio-modifiers.toml");
        ctx.registerConfig(COMMON, ToolConfig.CONFIG, "remodifier/tool-modifiers.toml");
        ctx.registerConfig(COMMON, BowConfig.CONFIG, "remodifier/bow-modifiers.toml");
        ctx.registerConfig(COMMON, ShieldConfig.CONFIG, "remodifier/shield-modifiers.toml");
    }

    static {
        NetworkHandler.setProxy(new NetworkHandlerForge());
        MODIFIER_BOOK = ITEM_DEFERRED_REGISTER.register("modifier_book", ItemModifierBook::new);
        GROUP_BOOKS = GROUPS.register(MOD_ID + "_books", () -> ItemGroup.builder()
                .displayName(Text.translatable("itemGroup.modifiers_books"))
                .icon(() -> MODIFIER_BOOK.get().getDefaultStack())
                .entries((ctx, entries) -> ((ItemModifierBook) MODIFIER_BOOK.get()).getStacks().forEach(entries::add))
                .build());
    }

    private void setup(final FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("curios")) {
            try {
                CURIO_PROXY = (ICurioProxy) Class.forName("com.teampotato.modifiers.common.curios.CurioCompat").getDeclaredConstructor().newInstance();
                MinecraftForge.EVENT_BUS.register(CURIO_PROXY);
            } catch (Exception e) {
                LOGGER.error("Remodified failed to load Curios integration.");
                LOGGER.error(e.getMessage());
            }
        }
        if (CURIO_PROXY == null) {
            CURIO_PROXY = new ICurioProxy() {
            };
        }
        Modifiers.init();
    }
}
