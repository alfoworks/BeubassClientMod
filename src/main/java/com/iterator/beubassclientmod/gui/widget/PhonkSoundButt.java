package com.iterator.beubassclientmod.gui.widget;

import com.iterator.beubassclientmod.sound.ModSounds;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;

public class PhonkSoundButt extends Button {
	Random random = new Random();
	public boolean wasHovered;

	public PhonkSoundButt(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, IPressable p_i232255_6_) {
		super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
	}

	@Override
	public void playDownSound(SoundHandler soundHandler) {
		soundHandler.play(SimpleSound.forUI(ModSounds.APEX_BUTT_DOWN, this.random.nextFloat() * 1.5F));
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (this.visible) {
			this.isHovered = (p_230430_2_ >= this.x && p_230430_3_ >= this.y && p_230430_2_ < this.x + this.width && p_230430_3_ < this.y + this.height) && this.active;
			if (this.wasHovered != this.isHovered()) {
				if (this.isHovered()) {
					this.queueNarration(750);
				} else {
					this.nextNarration = Long.MAX_VALUE;
				}
			}

			if (this.visible) {
				this.renderButton(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
			}

			this.narrate();
			this.wasHovered = this.isHovered();
		}
	}
}
