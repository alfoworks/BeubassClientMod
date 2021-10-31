package com.iterator.beubassclientmod.gui.widget;

import com.iterator.beubassclientmod.gui.util.Beu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.ITextComponent;

public class BigButton extends BeuButton {
    FontRenderer font = Minecraft.getMinecraft().fontRenderer;

    public BigButton(int x, int y, int widthIn, int heightIn, String buttonText, IPressable onClick) {
        super(x, y, widthIn, heightIn, buttonText, onClick);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        int a = (int) (this.alpha * 255);

        int whiteColor = Beu.getColorWithAlpha(0xFFFFFFFF, a);
        int panelColor = Beu.getColorWithAlpha(0xFF000000, Math.min(70, a));

        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, panelColor);

        drawRect(this.x, this.y, this.x + this.width, this.y + 1, whiteColor);
        drawRect(this.x, this.y, this.x + 1, this.y + this.height, whiteColor);
        drawRect(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, whiteColor);
        drawRect(this.x, this.y + this.height, this.x + this.width, this.y + this.height - 1, whiteColor);

        drawCenteredString(font, displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, Beu.getColorWithAlpha(0xFFFFFFFF, Math.max(5, a)));
    }
}
