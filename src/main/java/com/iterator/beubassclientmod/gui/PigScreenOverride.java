package com.iterator.beubassclientmod.gui;

import com.iterator.beubassclientmod.gui.particles.ParticleConfigBuilder;
import com.iterator.beubassclientmod.gui.particles.ParticleSystem;
import com.iterator.beubassclientmod.gui.screen.PhonkMenu;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class PigScreenOverride extends AbstractGui {
	private boolean drawPigCursor;

	Minecraft minecraft = Minecraft.getInstance();
	Icons cursorImage = Icons.PIG;

	ParticleSystem pigSys = new ParticleSystem(50, cursorImage, new ParticleConfigBuilder().withRandomGlobalVel(0.5F).withLifetime(200, 1000).build());

	public PigScreenOverride() {
		pigSys.setCreateNewParticles(false);
	}

	public void setDrawPigCursor(boolean flag) {
		GLFW.glfwSetInputMode(this.minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, flag ? GLFW.GLFW_CURSOR_HIDDEN : GLFW.GLFW_CURSOR_NORMAL);

		drawPigCursor = flag;
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		setDrawPigCursor(event.getGui() instanceof PhonkMenu);
	}

	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent event) {
		if (drawPigCursor) {
			renderCursorPig(event.getMatrixStack(), event.getMouseX() - 4, event.getMouseY() - 4);
		}

		pigSys.render(event.getMatrixStack(), event.getMouseX(), event.getMouseY());
	}

	@SubscribeEvent
	public void onClick(GuiScreenEvent.MouseClickedEvent event) {
		if (drawPigCursor && event.getButton() == 0) {
			pigSys.setCreateNewParticles(true);
		}
	}

	@SubscribeEvent
	public void onRelease(GuiScreenEvent.MouseReleasedEvent event) {
		pigSys.setCreateNewParticles(false);
	}

	private void renderCursorPig(MatrixStack stack, int hoverX, int hoverY) {
		this.minecraft.textureManager.bind(Beu.ICONS);

		//blit(stack, hoverX, hoverY, getBlitOffset(), cursorImage.x * 16, cursorImage.y * 16, 16, 16, 64, 64);
		Beu.renderIcon(stack, cursorImage, hoverX, hoverY, 16, 16);
	}
}
