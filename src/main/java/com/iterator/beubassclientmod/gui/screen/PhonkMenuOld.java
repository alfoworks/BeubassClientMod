package com.iterator.beubassclientmod.gui.screen;

import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.particles.ParticleConfigBuilder;
import com.iterator.beubassclientmod.gui.particles.ParticleSystem;
import com.iterator.beubassclientmod.gui.widget.ApexButt;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class PhonkMenuOld extends Screen {
	private static final ResourceLocation LOGO = new ResourceLocation("beubassclientmod", "textures/gui/logo.png");

	int PANEL_WIDTH = 160;
	int panelCenter;

	int SPONSORS_WIDTH = 90;
	int SPONSORS_HEIGHT = 120;
	int SPONSORS_PADDING = 10;

	int sponsorsX;
	int sponsorsY;
	int heightCenter;

	boolean playStartSound = true;
	boolean isWindowFocused;

	Animation sponsorsAnim = new Animation(0, 190);
	Animation logoAnim = new Animation(-400, 400);

	ParticleSystem sponsorsSys;

	ApexButt apexButt;

	ServerData beuData = new ServerData("BEUBASS Zone", "46.174.13.150:27041", false);
	ServerPinger pinger = new ServerPinger();
	Thread pingThread;

	public PhonkMenuOld() {
		super(new StringTextComponent("Beubass"));
	}

	@Override
	protected void init() {
		this.panelCenter = (this.width / 2) - this.PANEL_WIDTH / 2;
		heightCenter = this.height / 2;
		sponsorsX = this.width - SPONSORS_WIDTH - SPONSORS_PADDING;
		sponsorsY = heightCenter - SPONSORS_HEIGHT / 2;

		apexButt = new ApexButt((this.width / 2) - 50, (this.height / 2) - 20, 100, 30, 0xFF2372fa, new TranslationTextComponent("menu.join"), (sex) -> {

		});

		this.addButton(apexButt);
		/*
		this.addButton(new RegularButt(this.panelCenter + 10, this.height - 25, 30, new TranslationTextComponent("fml.menu.mods"), (sex) -> {
			this.minecraft.setScreen(new ModListScreen(this));
		}));

		this.addButton(new RegularButt(this.panelCenter + this.PANEL_WIDTH - 30, this.height - 25, Icons.QUIT, (sex) -> {
			this.minecraft.stop();
		}));

		this.addButton(new RegularButt(this.panelCenter + this.PANEL_WIDTH - 55, this.height - 25, Icons.SETTINGS, (sex) -> {
			this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
		}));

		this.addButton(new RegularButt(this.panelCenter + this.PANEL_WIDTH - 80, this.height - 25, Icons.SINGLEPLAYER, (sex) -> {
			this.minecraft.setScreen(new WorldSelectionScreen(this));
		}));

		if (this.playStartSound) {
			this.minecraft.getSoundManager().play(SimpleSound.forUI(ModSounds.MENU, 1.0F));

			this.playStartSound = false;
		}

		 */

		this.sponsorsAnim.startEndless(3, 2000);
		this.logoAnim.startEndless(3, 2000);

		this.sponsorsSys = new ParticleSystem(50, Icons.HEART, new ParticleConfigBuilder().withRandomPos(SPONSORS_WIDTH, 0).withConstYVel(-0.2F).withLifetime(100, 5000).build());
		/*
		sponsorsSys = new ParticleSystem(sponsorsX - 7, sponsorsY, 50, ParticleMapping.HEART, new AbstractParticleData() {
			@Override
			public int getX() {
				return randomNumber(0, SPONSORS_WIDTH);
			}

			@Override
			public int getY() {
				return 0;
			}

			@Override
			public float getMaxVel() {
				return 2;
			}

			@Override
			public float getVelX() {
				return 0;
			}

			@Override
			public float getVelY() {
				return -0.2F;
			}

			@Override
			public int getLifetime() {
				return randomNumber(100, 5000);
			}
		});

		 */

		//sponsorsSys = new ParticleSystem(sponsorsX - 7, sponsorsY, 50, Icons.HEART, new ParticleDataExact())

		pingThread = new Thread(() -> {
			while (true) {
				try {
					if (!apexButt.hasBeenUpdated) {
						apexButt.setStatus("Loading", 0xFFFFFFFF, false);
					}

					try {
						pinger.pingServer(beuData, () -> {});
					} catch (Exception e) {
						apexButt.setStatus("Error (CFE 0x0)", 0xFFFF0000, false);
					} finally {
						apexButt.setStatus(beuData.status.getString(), 0xFF2372fa, true);
					}

					Thread.sleep(5000);
				} catch (InterruptedException e) {
					break;
				}
			}
		});
		pingThread.start();
	}

	@Override
	public void tick() {
		for (Widget widget : this.buttons) {
			if (widget instanceof ApexButt) {
				((ApexButt) widget).tick();
			}
		}

		isWindowFocused = GLFW.glfwGetWindowAttrib(this.minecraft.getWindow().getWindow(), GLFW.GLFW_FOCUSED) == 1;
	}

	@Override
	public void render(MatrixStack stack, int hoverX, int hoverY, float p_230430_4_) {
		fill(stack, 0, 0, this.width, this.height, 0xFF121212);

		this.minecraft.getTextureManager().bind(LOGO);

		int logoX = (this.width / 2) - 256 / 2;
		int logoY = 35;
		int waveOffset = 17;
		float coOffset = (float) logoAnim.getValue() / 400;
		float angle = coOffset;

		fill(stack, this.panelCenter, 0, this.panelCenter + this.PANEL_WIDTH, this.height, 0x20FFFFFF);

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glPushMatrix();

		float magicX = logoX + 128;
		float magicY = logoY + 25.55F;

		GL11.glTranslatef(magicX, magicY, 0);
		GL11.glRotatef(angle, 0, 0, 1F);
		GL11.glTranslatef(-magicX, -magicY, 0);

		this.renderBeubassLogo(stack, logoX, logoY, 1.0F, 0.0F, 0.0F, coOffset);
		this.renderBeubassLogo(stack, logoX, logoY, 0.0F, 0.0F, 1.0F, -coOffset);
		this.renderBeubassLogo(stack, logoX, logoY, 1.0F, 1.0F, 1.0F, 0);

		GL11.glPopMatrix();

		this.renderBeubassWaves(stack, logoX, logoY - waveOffset);

		GL11.glPopMatrix();

		sponsorsSys.render(stack, 0, 0);
		renderSponsors(stack);

		super.render(stack, hoverX, hoverY, p_230430_4_);

		int pigX = hoverX - 4;
		int pigY = hoverY - 4;
	}

	private void renderSponsors(MatrixStack stack) {
		fillGradient(stack, sponsorsX, sponsorsY, this.width - SPONSORS_PADDING, heightCenter + SPONSORS_HEIGHT / 2, 0xFF00e5ff, 0xFFff00e1);
	}

	private void renderBeubassLogo(MatrixStack stack, int x, int y, float r, float g, float b, float offset) {
		GL11.glPushMatrix();
		RenderSystem.color3f(r, g, b);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0);
		GL11.glTranslatef(offset, offset, offset);
		blit(stack, x, y, this.getBlitOffset(), 0, 56.1F, 256, 128, 256, 256);
		GL11.glPopMatrix();
	}

	private void renderBeubassWaves(MatrixStack stack, int x, int y) {
		blit(stack, x, y, this.getBlitOffset(), 0, 0, 256, 56, 256, 256);
	}
}
