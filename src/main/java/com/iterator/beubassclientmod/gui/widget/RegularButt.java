package com.iterator.beubassclientmod.gui.widget;

import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

public class RegularButt extends PhonkSoundButt {
	private static final ResourceLocation ICONS = new ResourceLocation("beubassclientmod", "textures/gui/icons.png");

	Icons icon;
	FontRenderer font;
	TextureManager textureManager;

	boolean wasHovered = false;

	Animation anim = new Animation(127, 250);

	public RegularButt(int x, int y, int width, ITextComponent text, IPressable onPress) {
		super(x, y, width, 20, text, onPress);

		this.font = Minecraft.getInstance().font;
		this.textureManager = Minecraft.getInstance().textureManager;
	}

	public RegularButt(int x, int y, Icons icon, IPressable onPress) {
		this(x, y, 20, new StringTextComponent(""), onPress);

		this.icon = icon;
	}

	@Override
	public void renderButton(MatrixStack stack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
		if (this.isHovered && !this.wasHovered || !this.isHovered && this.wasHovered) {
			this.anim.start(!this.isHovered, 0, 200);

			this.wasHovered = this.isHovered;
		}

		this.fillGradient(stack, this.x, this.y, this.x + this.width, this.y + this.height, 0xff4e1d & 0xFFFFFF | this.anim.getValue() << 24, 0x00ff4e1d);

		fill(stack, this.x, this.y, this.x + this.width, this.y + 2, 0xFFFFFFFF);
		fill(stack, this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0xFFFFFFFF);

		fill(stack, this.x, this.y, this.x + 1, this.y + this.height, 0xFFFFFFFF);
		fill(stack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0xFFFFFFFF);

		if (this.icon == null) {
			int textWidth = this.font.width(this.getMessage());
			int textPos = (this.width / 2) - textWidth / 2;

			drawString(stack, this.font, this.getMessage(), this.x + textPos, this.y + 10 - this.font.lineHeight / 2, 0xFFFFFFFF);
		} else {
			this.textureManager.bind(ICONS);

			Beu.renderIcon(stack, this.icon, this.x + 3, this.y + 3, 14, 14);
		}
	}
}
