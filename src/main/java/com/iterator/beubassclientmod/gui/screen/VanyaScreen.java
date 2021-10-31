package com.iterator.beubassclientmod.gui.screen;

import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.particles.ParticleConfigBuilder;
import com.iterator.beubassclientmod.gui.particles.ParticleSystem;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

public class VanyaScreen extends Screen {
    Animation anim = new Animation(0, 255);
    ParticleSystem particles;

    private int textWidth;

    public VanyaScreen() {
        super(new StringTextComponent("VANYA"));
    }

    @Override
    protected void init() {
        textWidth = font.width("86_65_78_89_65") * 3;

        particles = new ParticleSystem(50, Icons.BEU, new ParticleConfigBuilder().withRandomPos(textWidth, 0).withRandomYVel(0.1F, 0.3F).withRandomXVel(-0.1F, 0.1F).withLifetime(100, 10000).build());

        anim.startEndless(3, 5000);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, 0, 0, this.width, this.height, 0xFF000000);

        int topColor = Beu.colorMix(0x0036d1dc, 0x00c33764, 1 - ((float) anim.getValue() / 255));
        int botColor = Beu.colorMix(0xFF5b86e5, 0xFF1d2671, (float) anim.getValue() / 255);

        float scale = 3;

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        drawCenteredString(stack, this.minecraft.font, "86_65_78_89_65", (int) (this.width / 2 / scale), 30, 0xFEFFFFFF);
        GL11.glPopMatrix();

        particles.render(stack, (this.width / 2) - (textWidth / 2), 115);

        Beu.drawGradient4c(stack, 0, 0, width, height, botColor, botColor, topColor, topColor);

        String exitMessage = "Press ESC to exit...";
        drawString(stack, font, exitMessage, width - font.width(exitMessage), height - font.lineHeight, 0x20FFFFFF);
    }
}
