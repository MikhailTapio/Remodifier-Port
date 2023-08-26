package com.teampotato.modifiers.mixin.client;

import com.teampotato.modifiers.client.SmithingScreenReforge;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen extends Screen {

    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (modifiers$getThis() instanceof SmithingScreen) {
            ((SmithingScreenReforge) this).modifiers_init();
        }
    }

    @Unique
    @SuppressWarnings("rawtypes")
    private HandledScreen modifiers$getThis() {
        return (HandledScreen) (Object) this;
    }
}
