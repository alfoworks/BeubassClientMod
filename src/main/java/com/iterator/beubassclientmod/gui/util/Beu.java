package com.iterator.beubassclientmod.gui.util;

import com.iterator.beubassclientmod.gui.Icons;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

public class Beu {
	public static final ResourceLocation ICONS = new ResourceLocation("beubassclientmod", "textures/gui/icons.png");

	public static void renderIcon(MatrixStack stack, Icons icon, int x, int y, int width, int height) {
		renderTexture(stack, x, y, icon.x * width, icon.y * height, width, height, width * 4, height * 4);
	}

	public static void renderTexture(MatrixStack stack, int x, int y, float u, float v, int u1, int v1, int width, int height) {
		GL11.glPushMatrix();
		RenderSystem.enableBlend();
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		AbstractGui.blit(stack, x, y, u, v, u1, v1, width, height);
		RenderSystem.disableBlend();
		GL11.glPopMatrix();
	}

	public static void drawGradient4c(MatrixStack stack, int x, int y, int width, int height, int cBL, int cBR, int cTR, int cTL) {
		if (((cBL >> 24) & 0xFF) < 2 && ((cBR >> 24) & 0xFF) < 2 && ((cTR >> 24) & 0xFF) < 2 && ((cTL >> 24) & 0xFF) < 2) {
			return;
		}

		float az = 1;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuilder();
		Matrix4f m = stack.last().pose();

		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(7425);
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0F);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		buffer.vertex(m, x, y + height, az).color((cBL >> 16) & 0xFF, (cBL >> 8) & 0xFF, cBL & 0xFF, (cBL >> 24) & 0xFF).endVertex();
		buffer.vertex(m, x + width, y + height, az).color((cBR >> 16) & 0xFF, (cBR >> 8) & 0xFF, cBR & 0xFF, (cBR >> 24) & 0xFF).endVertex();
		buffer.vertex(m, x + width, y, az).color((cTR >> 16) & 0xFF, (cTR >> 8) & 0xFF, cTR & 0xFF, (cTR >> 24) & 0xFF).endVertex();
		buffer.vertex(m, x, y, az).color((cTL >> 16) & 0xFF, (cTL >> 8) & 0xFF, cTL & 0xFF, (cTL >> 24) & 0xFF).endVertex();

		tessellator.end();

		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	public static int getColorWithAlpha(int originalColor, int alpha) {
		return originalColor & 0xFFFFFF | (clamp(alpha, 0, 255)) << 24;
	}

	public static int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}

	public static int colorMix(int startColor, int endColor, float mix) {
		float startAlpha = ((startColor >> 24) & 0xFF) / 255f;
		float startRed = ((startColor >> 16) & 0xFF) / 255f;
		float startGreen = ((startColor >> 8) & 0xFF) / 255f;
		float startBlue = (startColor & 0xFF) / 255f;

		float endAlpha = ((endColor >> 24) & 0xFF) / 255f;
		float endRed = ((endColor >> 16) & 0xFF) / 255f;
		float endGreen = ((endColor >> 8) & 0xFF) / 255f;
		float endBlue = (endColor & 0xFF) / 255f;

		int mixAlpha = (int) (((1 - mix) * startAlpha + mix * endAlpha) * 255);
		int mixRed = (int) (((1 - mix) * startRed + mix * endRed) * 255);
		int mixGreen = (int) (((1 - mix) * startGreen + mix * endGreen) * 255);
		int mixBlue = (int) (((1 - mix) * startBlue + mix * endBlue) * 255);

		return (mixAlpha << 24) | (mixRed << 16) | (mixGreen << 8) | mixBlue;
	}
}
