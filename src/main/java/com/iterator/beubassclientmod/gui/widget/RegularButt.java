package com.iterator.beubassclientmod.gui.widget;

import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.util.Beu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;


public class RegularButt extends BeuButton {
	private static final ResourceLocation ICONS = new ResourceLocation("beubassclientmod", "textures/gui/icons.png");

	Icons icon;
	FontRenderer font = Minecraft.getMinecraft().fontRenderer;
	TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

	boolean wasHovered = false;

	Animation anim = new Animation(127, 250);

	public RegularButt(int x, int y, int width, String text, IPressable onClick) {
		super(x, y, width, 20, text, onClick);
	}

	public RegularButt(int x, int y, Icons icon, IPressable onClick) {
		this(x, y, 20, "", onClick);

		this.icon = icon;
	}

	@Override
	public void drawButton(Minecraft mc, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
		if (this.hovered && !this.wasHovered || !this.hovered && this.wasHovered) {
			this.anim.start(!this.hovered, 0, 200);

			this.wasHovered = this.hovered;
		}

		this.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height, 0xff4e1d & 0xFFFFFF | this.anim.getValue() << 24, 0x00ff4e1d);

		drawRect(this.x, this.y, this.x + this.width, this.y + 2, 0xFFFFFFFF);
		drawRect(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0xFFFFFFFF);

		drawRect(this.x, this.y, this.x + 1, this.y + this.height, 0xFFFFFFFF);
		drawRect(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0xFFFFFFFF);

		if (this.icon == null) {
			int textWidth = this.font.getStringWidth(displayString);
			int textPos = (this.width / 2) - textWidth / 2;

			drawString(this.font, displayString, this.x + textPos, this.y + 10 - this.font.FONT_HEIGHT / 2, 0xFFFFFFFF);
		} else {
			this.textureManager.bindTexture(ICONS);

			Beu.renderIcon(this.icon, this.x + 3, this.y + 3, 14, 14);
		}
	}
}
