package com.iterator.beubassclientmod.gui.widget;

import com.iterator.beubassclientmod.gui.util.Beu;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class BigButton extends Button {
    FontRenderer font = Minecraft.getInstance().font;

    public BigButton(int x, int y, int width, int height, ITextComponent message, IPressable onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void playDownSound(SoundHandler p_230988_1_) {

    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int a = (int) (this.alpha * 255);

        int whiteColor = Beu.getColorWithAlpha(0xFFFFFFFF, a);
        int panelColor = Beu.getColorWithAlpha(0xFF000000, Math.min(70, a));

        fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, panelColor);

        fill(stack, this.x, this.y, this.x + this.width, this.y + 1, whiteColor);
        fill(stack, this.x, this.y, this.x + 1, this.y + this.height, whiteColor);
        fill(stack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, whiteColor);
        fill(stack, this.x, this.y + this.height, this.x + this.width, this.y + this.height - 1, whiteColor);

        drawCenteredString(stack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, Beu.getColorWithAlpha(0xFFFFFFFF, Math.max(5, a)));
    }
}
