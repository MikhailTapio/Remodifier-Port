package com.teampotato.modifiers.mixin.client;

import com.teampotato.modifiers.client.SmithingScreenReforge;
import com.teampotato.modifiers.client.TabButtonWidget;
import com.teampotato.modifiers.common.config.ReforgeConfig;
import com.teampotato.modifiers.common.modifier.Modifier;
import com.teampotato.modifiers.common.modifier.ModifierHandler;
import com.teampotato.modifiers.common.network.NetworkHandler;
import com.teampotato.modifiers.common.network.PacketC2SReforge;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(SmithingScreen.class)
public abstract class MixinSmithingScreen extends ForgingScreen<SmithingScreenHandler> implements SmithingScreenReforge {
    @Unique
    private static final Identifier modifiers$reforger = new Identifier("modifiers", "textures/gui/reforger.png");
    @Unique
    private TabButtonWidget modifiers_reforgeButton;
    @Unique
    private TabButtonWidget modifiers_tabButton1;
    @Unique
    private TabButtonWidget modifiers_tabButton2;
    @Unique
    private boolean modifiers_onTab2 = false;
    @Unique
    private boolean modifiers_canReforge = false;

    @Unique
    private Text modifiers_tab1Title;
    @Unique
    private Text modifiers_tab2Title;

    @Unique
    private int modifiers_outputSlotX;
    @Unique
    private int modifiers_outputSlotY;

    public MixinSmithingScreen(SmithingScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Unique
    private void modifiers_toTab1() {
        modifiers_onTab2 = false;
        modifiers_reforgeButton.visible = false;
        this.title = modifiers_tab1Title;
        Slot outputSlot = this.getScreenHandler().slots.get(3);
        outputSlot.x = modifiers_outputSlotX;
        outputSlot.y = modifiers_outputSlotY;
        this.modifiers_tabButton1.toggled = true;
        this.modifiers_tabButton2.toggled = false;
    }

    @Unique
    private void modifiers_toTab2() {
        modifiers_onTab2 = true;
        modifiers_reforgeButton.visible = true;
        this.title = modifiers_tab2Title;
        Slot outputSlot = this.getScreenHandler().slots.get(3);
        outputSlot.x = 152;
        outputSlot.y = 8;
        this.modifiers_tabButton1.toggled = false;
        this.modifiers_tabButton2.toggled = true;
    }

    @Override
    public void modifiers_init() {
        int k = (this.width - this.backgroundWidth) / 2;
        int l = (this.height - this.backgroundHeight) / 2;
        Slot outputSlot = this.getScreenHandler().slots.get(3);
        modifiers_outputSlotX = outputSlot.x;
        modifiers_outputSlotY = outputSlot.y;
        this.modifiers_tabButton1 = new TabButtonWidget(k - 70, l + 2, 70, 18, Text.translatable("container.modifiers.reforge.tab1"), (button) -> modifiers_toTab1());
        this.modifiers_tabButton2 = new TabButtonWidget(k - 70, l + 22, 70, 18, Text.translatable("container.modifiers.reforge.tab2"), (button) -> modifiers_toTab2());
        this.modifiers_tabButton1.setTextureUV(0, 166, 70, 18, new Identifier("modifiers", "textures/gui/reforger.png"));
        this.modifiers_tabButton2.setTextureUV(0, 166, 70, 18, new Identifier("modifiers", "textures/gui/reforger.png"));
        this.modifiers_reforgeButton = new TabButtonWidget(k + 132, l + 45, 20, 20, Text.of(""),
                (button) -> NetworkHandler.sendToServer(new PacketC2SReforge()),
                Tooltip.of(Text.translatable("container.modifiers.reforge.reforge")));
        this.modifiers_reforgeButton.setTextureUV(0, 202, 20, 20, new Identifier("modifiers", "textures/gui/reforger.png"));

        this.addDrawableChild(this.modifiers_tabButton1);
        this.addDrawableChild(this.modifiers_tabButton2);
        this.addDrawableChild(this.modifiers_reforgeButton);

        modifiers_tab1Title = this.title;
        modifiers_tab2Title = Text.translatable("container.modifiers.reforge");
        this.modifiers_toTab1();
    }

    @Override
    public boolean modifiers_isOnTab2() {
        return modifiers_onTab2;
    }

    @Override
    public void modifiers_setCanReforge(boolean canReforge) {
        this.modifiers_canReforge = canReforge;
        this.modifiers_reforgeButton.toggled = canReforge;
        this.modifiers_reforgeButton.active = canReforge;
    }

    @Override
    protected void renderForeground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderForeground(context, mouseX, mouseY, delta);
        if (this.modifiers_onTab2) {
            ItemStack stack = this.handler.getSlot(0).getStack();
            Modifier modifier = ModifierHandler.getModifier(stack);
            if (modifier != null) {
                context.drawText(textRenderer, Text.translatable("misc.modifiers.modifier_prefix").append(Text.translatable(modifier.getTranslationKey())), this.titleX - 15, this.titleY + 15, 4210752, false);
            }
        }
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void inject$drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (((SmithingScreenReforge) this).modifiers_isOnTab2()) {
            ci.cancel();
            ctx.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int k = (this.width - this.backgroundWidth) / 2;
            int l = (this.height - this.backgroundHeight) / 2;
            ctx.drawTexture(modifiers$reforger, k, l, 0, 0, this.backgroundWidth, this.backgroundHeight);
            ItemStack stack1 = this.handler.getSlot(0).getStack();
            ItemStack stack2 = this.handler.getSlot(1).getStack();
            boolean isUniversal = ReforgeConfig.UNIVERSAL_REFORGE_ITEM.get().equals(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack2.getItem())).toString());

            // TODO add a util function somewhere for `canReforge(stack1, stack2)`
            boolean cantReforge = !stack1.isEmpty() && !stack1.getItem().canRepair(stack1, stack2);
            if (isUniversal && cantReforge) cantReforge = false;
            // canReforge is also true for empty slot 1. Probably how it should behave.
            ((SmithingScreenReforge) this).modifiers_setCanReforge(!cantReforge);
            if (!stack1.isEmpty() && !(stack1.getItem().canRepair(stack1, stack2) || isUniversal)) {
                ctx.drawTexture(modifiers$reforger, k + 99 - 53, l + 45, this.backgroundWidth, 0, 28, 21);
            }
        }
    }

    @Inject(method = "renderSlotTooltip", at = @At("HEAD"), cancellable = true)
    private void inject$renderSlotTooltip(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        if (modifiers_onTab2) ci.cancel();
    }
}