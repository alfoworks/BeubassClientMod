package com.iterator.beubassclientmod.gui.widget;

import com.iterator.beubassclientmod.gui.Animation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ApexButt extends PhonkSoundButt {
	private static final ResourceLocation ICONS = new ResourceLocation("beubassclientmod", "textures/gui/icons.png");

	int color;
	boolean wasHovered = false;
	Minecraft minecraft;

	public String serverStr;
	public boolean hasBeenUpdated;
	private boolean enabled;

	Animation anim = new Animation(127, 250);

	public ApexButt(int x, int y, int width, int height, int color, ITextComponent text, IPressable action) {
		super(x, y, width, height, text, action);
		this.color = color;

		this.minecraft = Minecraft.getInstance();
	}

	public void tick() {

	}

	public void setStatus(String desc, int color, boolean enable) {
		this.serverStr = desc;
		this.color = color;

		hasBeenUpdated = true;

		this.enabled = enable;
	}

	@Override
	protected boolean clicked(double p_230992_1_, double p_230992_3_) {
		if (!enabled) {
			return false;
		}

		return super.clicked(p_230992_1_, p_230992_3_);
	}

	@Override
	public void renderButton(MatrixStack stack, int hoverX, int hoverY, float p_230431_4_) {
		if (this.isHovered && !this.wasHovered || !this.isHovered && this.wasHovered) {
			this.anim.start(!this.isHovered, 0, 200);

			this.wasHovered = this.isHovered;
		}

		this.fillGradient(stack, this.x, this.y + 10, this.x + this.width, this.y + this.height, this.color & 0xFFFFFF | (0) << 24, this.color & 0xFFFFFF | (this.anim.getValue()) << 24);

		fill(stack, this.x, this.y, this.x + this.width, this.y + 1, 0xFFFFFFFF);
		fill(stack, this.x + (this.width / 2) - 25, this.y + 1, this.x + (this.width / 2) + 25, this.y + 3, 0xFFFFFFFF);
		fill(stack, this.x, this.y, this.x + 1, this.y + this.height, 0xFFFFFFFF);
		fill(stack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0xFFFFFFFF);

		fill(stack, this.x, this.y + this.height - 4, this.x + this.width, this.y + this.height, this.color);

		this.minecraft.textureManager.bind(ICONS);

		blit(stack, this.x + 5, this.y + this.height / 2 - 9, this.getBlitOffset(), 16, 16, 16, 16,32, 32);

		drawCenteredString(stack, this.minecraft.font, this.getMessage(), this.x + this.width / 2 + 5, this.y + 5, 0xFFFFFFFF);
		fill(stack, this.x + 25, this.y + (this.height / 2) - 1, this.x + this.width - 5, this.y + (this.height / 2), 0xFFFFFFFF);

		drawCenteredString(stack, this.minecraft.font, serverStr, this.x + this.width / 2 + 5, this.y + 17, 0xFFFFFFFF);
	}
}
