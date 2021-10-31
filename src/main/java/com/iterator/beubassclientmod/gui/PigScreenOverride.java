package com.iterator.beubassclientmod.gui;

import com.iterator.beubassclientmod.gui.particles.ParticleConfigBuilder;
import com.iterator.beubassclientmod.gui.particles.ParticleSystem;
import com.iterator.beubassclientmod.gui.screen.PhonkMenu;
import com.iterator.beubassclientmod.gui.util.Beu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class PigScreenOverride extends GuiScreen {
	private boolean drawPigCursor;

	Minecraft minecraft = Minecraft.getMinecraft();
	Icons cursorImage = Icons.PIG;

	ParticleSystem pigSys = new ParticleSystem(50, cursorImage, new ParticleConfigBuilder().withRandomGlobalVel(0.5F).withLifetime(200, 1000).build());

	private boolean wasPressed;

	public PigScreenOverride() {
		pigSys.setCreateNewParticles(false);
	}

	public void setDrawPigCursor(boolean flag) {
		Mouse.setGrabbed(flag);

		drawPigCursor = flag;
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		setDrawPigCursor(event.getGui() instanceof PhonkMenu);
	}

	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent event) {
		if (drawPigCursor) {
			renderCursorPig(event.getMouseX() - 4, event.getMouseY() - 4);
		}

		pigSys.render(event.getMouseX(), event.getMouseY());
	}

	@SubscribeEvent
	public void onClick(GuiScreenEvent.MouseInputEvent event) {
		/*
		if (drawPigCursor && event.getButton() == 0) {
			pigSys.setCreateNewParticles(true);
		}

		 */

		boolean isDown = Mouse.isButtonDown(0);

		pigSys.setCreateNewParticles((!wasPressed && isDown) || (wasPressed && !isDown));
	}

	private void renderCursorPig(int hoverX, int hoverY) {
		this.minecraft.getTextureManager().bindTexture(Beu.ICONS);

		//blit(stack, hoverX, hoverY, getBlitOffset(), cursorImage.x * 16, cursorImage.y * 16, 16, 16, 64, 64);
		GlStateManager.disableBlend();
		Beu.renderIcon(cursorImage, hoverX, hoverY, 16, 16);
		GlStateManager.enableBlend();
	}
}
