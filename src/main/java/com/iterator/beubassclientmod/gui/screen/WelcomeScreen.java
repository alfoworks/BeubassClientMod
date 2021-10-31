package com.iterator.beubassclientmod.gui.screen;

import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.iterator.beubassclientmod.gui.widget.BigButton;
import com.iterator.beubassclientmod.sound.ModSounds;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

public class WelcomeScreen extends Screen {
    Animation anim = new Animation(0, 255);
    Animation alphaAnim = new Animation(0, 255);

    private Button button;

    private boolean initedOnce;

    public WelcomeScreen() {
        super(new StringTextComponent("Welcome Screen"));
    }

    @Override
    protected void init() {
        anim.startEndless(3, 5000);
        alphaAnim.lastValue = 255;

        int buttonWidth = 100;
        int buttonHeight = 33;

        int bX = this.width / 2 - (buttonWidth / 2);
        int bY = this.height / 2 - (buttonHeight / 2);

        button = new BigButton(bX, bY, buttonWidth, buttonHeight, new TranslationTextComponent("menu.begin"), (sex) -> {
            alphaAnim.start(!alphaAnim.decrease, 2, 700);

            sex.active = false;
        });

        this.addButton(button);

        if (!initedOnce) {
            initedOnce = true;

            this.minecraft.getSoundManager().play(SimpleSound.forUI(ModSounds.APEX_BUTT_DOWN, 1.0F));
        }
    }

    @Override
    public void tick() {
        if (alphaAnim.lastValue == 0) {
            this.minecraft.setScreen(new PhonkMenu());
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, 0, 0, this.width, this.height, 0xFF000000);

        int topColor = Beu.colorMix(0x001c92d2, 0x00f2fcfe, 1 - ((float) anim.getValue() / 255));
        int botColor = Beu.getColorWithAlpha(Beu.colorMix(0xFF283c86, 0xFF45a247, (float) anim.getValue() / 255), alphaAnim.getValue());

        Beu.drawGradient4c(stack, 0, 0, width, height, botColor, botColor, topColor, topColor);

        button.setAlpha((float)alphaAnim.getValue() / 255);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
