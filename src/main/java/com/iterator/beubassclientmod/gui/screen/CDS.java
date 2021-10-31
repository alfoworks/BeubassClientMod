package com.iterator.beubassclientmod.gui.screen;

import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.iterator.beubassclientmod.gui.widget.RegularButt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IBidiRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CDS extends Screen {
    Animation anim = new Animation(0, 255);

    private final ITextComponent reason;
    private IBidiRenderer message;
    private final Screen parent;
    private int textHeight;

    public CDS(Screen p_i242056_1_, ITextComponent p_i242056_2_, ITextComponent p_i242056_3_) {
        super(p_i242056_2_);
        this.message = IBidiRenderer.EMPTY;
        this.parent = p_i242056_1_;
        this.reason = p_i242056_3_;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {
        anim.startEndless(3, 5000);

        this.message = IBidiRenderer.create(this.font, this.reason, this.width - 50);
        int var10001 = this.message.getLineCount();
        this.font.getClass();
        this.textHeight = var10001 * 9;
        int var10003 = this.width / 2 - 50;
        int var10004 = this.height / 2 + this.textHeight / 2;
        this.font.getClass();
        this.addButton(new RegularButt(var10003, Math.min(var10004 + 9, this.height - 30), 100, new TranslationTextComponent("gui.toMenu"), (p_213033_1_) -> {
            this.minecraft.setScreen(this.parent);
        }));
    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        fill(p_230430_1_, 0, 0, this.width, this.height, 0xFF000000);

        int topColor = Beu.colorMix(0x001c92d2, 0x00f2fcfe, 1 - ((float) anim.getValue() / 255));
        int botColor = Beu.colorMix(0xFF283c86, 0xFF45a247, (float) anim.getValue() / 255);

        Beu.drawGradient4c(p_230430_1_, 0, 0, width, height, botColor, botColor, topColor, topColor);

        this.renderBackground(p_230430_1_);
        FontRenderer var10001 = this.font;
        ITextComponent var10002 = this.title;
        int var10003 = this.width / 2;
        int var10004 = this.height / 2 - this.textHeight / 2;
        this.font.getClass();
        drawCenteredString(p_230430_1_, var10001, var10002, var10003, var10004 - 9 * 2, 11184810);
        this.message.renderCentered(p_230430_1_, this.width / 2, this.height / 2 - this.textHeight / 2);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
