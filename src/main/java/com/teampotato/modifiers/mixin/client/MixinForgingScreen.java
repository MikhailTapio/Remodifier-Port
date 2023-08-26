package com.teampotato.modifiers.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgingScreen.class)
public abstract class MixinForgingScreen<T extends ForgingScreenHandler> extends HandledScreen<T> {


    @Unique
    private static final Identifier modifiers$reforger = new Identifier("modifiers", "textures/gui/reforger.png");

    public MixinForgingScreen(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void onDrawBackground(DrawContext ctx, float f, int i, int j, CallbackInfo ci) {
        //if (((Object) this) instanceof SmithingScreen) {
        //    if (((SmithingScreenReforge) this).modifiers_isOnTab2()) {
        //        ci.cancel();
        //        ctx.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //        int k = (this.width - this.backgroundWidth) / 2;
        //        int l = (this.height - this.backgroundHeight) / 2;
        //        ctx.drawTexture(modifiers$reforger, k, l, 0, 0, this.backgroundWidth, this.backgroundHeight);
        //        ItemStack stack1 = this.handler.getSlot(0).getStack();
        //        ItemStack stack2 = this.handler.getSlot(1).getStack();
        //        boolean isUniversal = ReforgeConfig.UNIVERSAL_REFORGE_ITEM.get().equals(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack2.getItem())).toString());
//
        //        // TODO add a util function somewhere for `canReforge(stack1, stack2)`
        //        boolean cantReforge = !stack1.isEmpty() && !stack1.getItem().canRepair(stack1, stack2);
        //        if (isUniversal && cantReforge) cantReforge = false;
        //        // canReforge is also true for empty slot 1. Probably how it should behave.
        //        ((SmithingScreenReforge) this).modifiers_setCanReforge(!cantReforge);
        //        if (!stack1.isEmpty() && !(stack1.getItem().canRepair(stack1, stack2) || isUniversal)) {
        //            ctx.drawTexture(modifiers$reforger, k + 99 - 53, l + 45, this.backgroundWidth, 0, 28, 21);
        //        }
        //    }
        //}
    }
}