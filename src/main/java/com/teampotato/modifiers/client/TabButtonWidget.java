package com.teampotato.modifiers.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class TabButtonWidget extends ButtonWidget {
    public boolean toggled;

    protected Identifier texture;
    protected int u;
    protected int v;
    protected int pressedUOffset;
    protected int hoverVOffset;

    public TabButtonWidget(int i, int j, int k, int l, Text text, PressAction pressAction) {
        super(new Builder(text, pressAction).dimensions(i, j, k, l));
    }

    public TabButtonWidget(int i, int j, int k, int l, Text text, PressAction pressAction, Tooltip tooltipSupplier) {
        super(new Builder(text, pressAction).dimensions(i, j, k, l).tooltip(tooltipSupplier));
    }

    public void setTextureUV(int i, int j, int k, int l, Identifier identifier) {
        this.u = i;
        this.v = j;
        this.pressedUOffset = k;
        this.hoverVOffset = l;
        this.texture = identifier;
    }

    @Override
    public void renderButton(DrawContext ctx, int i, int j, float f) {

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        minecraftClient.getTextureManager().bindTexture(this.texture);
        RenderSystem.disableDepthTest();
        int u = this.u;
        int v = this.v;
        if (this.toggled) {
            u += this.pressedUOffset;
        }

        if (this.isHovered()) {
            v += this.hoverVOffset;
        }

        this.drawTexture(ctx, this.texture, this.getX(), this.getY(), u, v, hoverVOffset, this.width, this.height, 256, 256);
        int color = this.active ? 16777215 : 10526880;
        ctx.drawCenteredTextWithShadow(textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color | MathHelper.ceil(this.alpha * 255.0F) << 24);
        if (this.isHovered()) {
            final Tooltip tooltip = getTooltip();
            if (tooltip != null)
                ctx.drawTooltip(textRenderer, tooltip.getLines(minecraftClient), getTooltipPositioner(), i, j);
        }
        RenderSystem.enableDepthTest();
        super.renderButton(ctx, i, j, f);
    }
}