package com.iterator.beubassclientmod.gui.screen;

import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.iterator.beubassclientmod.gui.widget.BigButton;
import com.iterator.beubassclientmod.sound.ModSounds;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class WelcomeScreen extends GuiScreen {
    Animation anim = new Animation(0, 255);
    Animation alphaAnim = new Animation(0, 255);

    private BigButton button;

    private boolean initedOnce;

    @Override
    public void initGui() {
        anim.startEndless(3, 5000);
        alphaAnim.lastValue = 255;

        int buttonWidth = 100;
        int buttonHeight = 33;

        int bX = this.width / 2 - (buttonWidth / 2);
        int bY = this.height / 2 - (buttonHeight / 2);

        button = new BigButton(bX, bY, buttonWidth, buttonHeight, I18n.format("menu.begin"), (sex) -> {
            alphaAnim.start(!alphaAnim.decrease, 2, 700);

            sex.enabled = false;
        });
        button.playPressSound = false;

        this.addButton(button);

        if (!initedOnce) {
            initedOnce = true;

            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(ModSounds.APEX_BUTT_DOWN, 1.0F));
        }
    }

    @Override
    public void updateScreen() {
        if (alphaAnim.lastValue == 0) {
            this.mc.displayGuiScreen(new PhonkMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0xFF000000);

        int topColor = Beu.colorMix(0x001c92d2, 0x00f2fcfe, 1 - ((float) anim.getValue() / 255));
        int botColor = Beu.getColorWithAlpha(Beu.colorMix(0xFF283c86, 0xFF45a247, (float) anim.getValue() / 255), alphaAnim.getValue());

        Beu.drawGradient4c(0, 0, width, height, botColor, botColor, topColor, topColor);

        button.setAlpha((float)alphaAnim.getValue() / 255);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /*
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

     */
}
