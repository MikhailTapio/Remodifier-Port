package com.teampotato.modifiers.common.network;

import com.teampotato.modifiers.ModifiersMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.thread.ThreadExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class NetworkHandler {
    private static NetworkHandlerProxy proxy;

    public static void setProxy(NetworkHandlerProxy proxy) {
        NetworkHandler.proxy = proxy;
    }

    public static void register() {
        // Commented out example until I actually add any packets
        registerMessage(new Identifier(ModifiersMod.MOD_ID, "reforge"), 0, Side.ClientToServer,
                PacketC2SReforge.class, PacketC2SReforge::encode,
                PacketC2SReforge::new, mainThreadHandler(PacketC2SReforge.Handler::handle));
    }

    private static <T> BiConsumer<T, PacketContext> mainThreadHandler(Consumer<? super T> handler) {
        return (packet, ctx) -> ctx.threadExecutor.submit(() -> handler.accept(packet));
    }

    private static <T> BiConsumer<T, PacketContext> mainThreadHandler(BiConsumer<? super T, PacketContext> handler) {
        return (packet, ctx) -> ctx.threadExecutor.submit(() -> handler.accept(packet, ctx));
    }


    /**
     * id only used on fabric, discrim only used on forge
     */
    public static <MSG> void registerMessage(
            Identifier id, int discrim, Side side,
            Class<MSG> clazz,
            BiConsumer<MSG, PacketByteBuf> encode,
            Function<PacketByteBuf, MSG> decode,
            BiConsumer<MSG, NetworkHandler.PacketContext> handler) {
        proxy.registerMessage(id, discrim, side, clazz, encode, decode, handler);
    }

    public static <MSG> void sendToServer(MSG packet) {
        proxy.sendToServer(packet);
    }

    public static <MSG> void sendTo(MSG packet, ServerPlayerEntity player) {
        proxy.sendTo(packet, player);
    }

    public static <MSG> void sendToAllPlayers(MSG packet) {
        proxy.sendToAllPlayers(packet);
    }

    // Based on Fabric's PacketContext
    @SuppressWarnings("ClassCanBeRecord")
    public static class PacketContext {
        @Nullable
        public final PlayerEntity player;
        public final ThreadExecutor<?> threadExecutor;

        public PacketContext(@Nullable PlayerEntity player, ThreadExecutor<?> threadExecutor) {
            this.player = player;
            this.threadExecutor = threadExecutor;
        }
    }

    public enum Side {
        ServerToClient, ClientToServer
    }
}

