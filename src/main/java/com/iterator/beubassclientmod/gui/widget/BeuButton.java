package com.iterator.beubassclientmod.gui.widget;

import com.iterator.beubassclientmod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;

public class BeuButton extends GuiButton {
	Random random = new Random();
	public boolean wasHovered;
	public float alpha;

	public IPressable onClick;
	public boolean playPressSound = true;

	public BeuButton(int x, int y, int widthIn, int heightIn, String buttonText, IPressable onClick) {
		super(0, x, y, widthIn, heightIn, buttonText);

		this.onClick = onClick;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void onClick() {

	}

	@Override
	public void playPressSound(SoundHandler soundHandler) {
		this.onClick.onPress(this);
		this.onClick();

		if (playPressSound) {
			soundHandler.playSound(PositionedSoundRecord.getMasterRecord(ModSounds.APEX_BUTT_DOWN, this.random.nextFloat()));
		}
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) && this.enabled;
		}
	}
}
