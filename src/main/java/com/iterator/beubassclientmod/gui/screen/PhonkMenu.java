package com.iterator.beubassclientmod.gui.screen;

import com.iterator.beubassclientmod.BeubassClientMod;
import com.iterator.beubassclientmod.Constants;
import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.particles.ParticleConfigBuilder;
import com.iterator.beubassclientmod.gui.particles.ParticleSystem;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.iterator.beubassclientmod.gui.widget.RegularButt;
import com.iterator.beubassclientmod.gui.widget.ServerButton;
import com.iterator.beubassclientmod.sound.ModSounds;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.screen.ModListScreen;
import org.lwjgl.opengl.GL11;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal") // Нет блядь, не может, я не хочу превращать этот класс в парад говнокода
public class PhonkMenu extends Screen {
	private final ResourceLocation LOGO = new ResourceLocation("beubassclientmod", "textures/gui/logo.png");

	private final int PADDING_BOTTOM = 10;
	private final int PADDING_TOP = 10;

	private final int RG_WIDTH = 160;
	private final int SECONDARY_PANEL_WIDTH = 180;

	private final int TIME_PANEL_HEIGHT = 20;

	private final int SPONSORS_PANEL_HEIGHT = 60;

	private final int MAIN_PANEL_WIDTH = 170;
	private final int MAIN_PANEL_PADDING = 20;

	private final int BEUBASS_LOGO_WIDTH = 256;
	private final int PSERVER_WIDTH = 128;
	private final int BEUBASS_WAVES_Y_OFFSET = 17;

	private boolean initedOnce;
	private static boolean soundPlayed;
	private boolean beenRemoved;
	private String expirationDays;

	private final Animation logoAnim = new Animation(-400, 400);
	private final Animation blackAnim = new Animation(0, 255);

	private ParticleSystem sponsorsParticles;

	private ServerButton serverButton;
	private Thread buttonUpdateThread;

	private List<String> sponsors;

	public PhonkMenu() {
		super(new StringTextComponent("PhonkMenu"));
	}

	@Override
	protected void init() {
		// misc ========================= //

		expirationDays = String.valueOf(ChronoUnit.DAYS.between(new Date().toInstant(), Constants.SERVER_EXPIRE_DATE.toInstant()));
		sponsorsParticles = new ParticleSystem(60, Icons.HEART, new ParticleConfigBuilder().withRandomXVel(-0.1F, -0.2F).withLifetime(100, 7000).withRandomPos(0, 50).build());

		// sponsors prep =============== //

		String[][] sponsorsSplit = splitArray(BeubassClientMod.sponsors, 2);
		sponsors = new ArrayList<>();

		for (String[] s : sponsorsSplit) {
			sponsors.add(String.join(", ", s).replaceAll("(\\r|\\n)", ""));
		}

		// animations ================== //

		logoAnim.startEndless(3, 2000);
		blackAnim.start(true, 1, 1000);

		// button init ================= //

		int serverButtonX = MAIN_PANEL_PADDING + ((MAIN_PANEL_WIDTH / 2) - (100 / 2));
		int serverButtonY = (this.height / 2) - 20;

		if (!initedOnce) {
			this.serverButton = new ServerButton(0, 0, this);
		}

		this.serverButton.x = serverButtonX;
		this.serverButton.y = serverButtonY;
		this.addButton(serverButton);

		int panelFarX = MAIN_PANEL_PADDING + MAIN_PANEL_WIDTH;

		this.addButton(new RegularButt(this.MAIN_PANEL_PADDING + 10, this.height - 25, 30, new TranslationTextComponent("fml.menu.mods"), (sex) -> {
			this.minecraft.setScreen(new ModListScreen(this));
		}));

		this.addButton(new RegularButt(panelFarX - 30, this.height - 25, Icons.TURN_OFF, (sex) -> {
			this.minecraft.stop();
		}));

		this.addButton(new RegularButt(panelFarX - 55, this.height - 25, Icons.SETTINGS, (sex) -> {
			this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
		}));

		this.addButton(new RegularButt(panelFarX - 80, this.height - 25, Icons.SINGLEPLAYER, (sex) -> {
			this.minecraft.setScreen(new WorldSelectionScreen(this));
		}));

		this.addButton(new RegularButt(panelFarX - 105, this.height - 25, Icons.BEU, (sex) -> {
			this.minecraft.setScreen(new VanyaScreen());
		}));

		// ============================== //

		if (!initedOnce || beenRemoved) {
			beenRemoved = false;

			buttonUpdateThread = new Thread(() -> {
				while (true) {
					serverButton.ping();
					try {
						Thread.sleep(7000);
					} catch (InterruptedException e) {
						return;
					}
				}
			});
			buttonUpdateThread.start();
		}

		if (!initedOnce) {
			initedOnce = true;
		}
	}

	@Override
	public void tick() {
		if (!soundPlayed && blackAnim.startTime == 0) {
			soundPlayed = true;

			this.minecraft.getSoundManager().play(SimpleSound.forUI(ModSounds.MENU, 1.0F));
		}
	}

	@Override
	public void removed() {
		buttonUpdateThread.stop();
		this.beenRemoved = true;
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		// bg
		fill(stack, 0, 0, this.width, this.height, 0xFF121212);

		// main panel
		fill(stack, MAIN_PANEL_PADDING, 0, MAIN_PANEL_PADDING + MAIN_PANEL_WIDTH, this.height, 0x20FFFFFF);

		int sPanelX = width - SECONDARY_PANEL_WIDTH;
		int gX = width - RG_WIDTH;
		int tPanelY = PADDING_TOP;

		// particles
		sponsorsParticles.render(stack, width, height - PADDING_BOTTOM - SPONSORS_PANEL_HEIGHT);

		// time & sponsor gradients
		Beu.drawGradient4c(stack, sPanelX, tPanelY, SECONDARY_PANEL_WIDTH, TIME_PANEL_HEIGHT, 0x00ffff00, 0xFF15c1ff, 0xFF15c1ff, 0x00ffff00);
		Beu.drawGradient4c(stack, sPanelX, height - PADDING_BOTTOM - SPONSORS_PANEL_HEIGHT, SECONDARY_PANEL_WIDTH, SPONSORS_PANEL_HEIGHT, 0x00feb47b, 0xFFff7e5f, 0xFFff7e5f, 0x00feb47b);

		// second panel
		Beu.drawGradient4c(stack, sPanelX, 0, SECONDARY_PANEL_WIDTH, height, 0x00614385, 0xFF516395, 0xff516395, 0x00614385);

		// time & sponsor texts
		ITextComponent expirationComponent = new TranslationTextComponent("menu.expiration", expirationDays);

		drawString(stack, this.minecraft.font, expirationComponent, width - 10 - this.minecraft.font.width(expirationComponent), (tPanelY + TIME_PANEL_HEIGHT / 2) - font.lineHeight / 2, 0xFFFFFFFF);

		ITextComponent sponsorsTitle = new TranslationTextComponent("menu.sponsors");

		drawString(stack, this.minecraft.font, sponsorsTitle, width - 10 - this.minecraft.font.width(sponsorsTitle), height - PADDING_BOTTOM - SPONSORS_PANEL_HEIGHT + 4, 0xFFFFFFFF);

		for (int i = 0; i < sponsors.size(); i++) {
			String s = sponsors.get(i);
			drawString(stack, this.font, s, width - 10 - font.width(s), height - PADDING_BOTTOM - SPONSORS_PANEL_HEIGHT + (25 + i * 9), 0xFFFFFFFF);
		}

		// logo
		int logoX = 27;
		int logoY = PADDING_TOP + BEUBASS_WAVES_Y_OFFSET;

		this.minecraft.getTextureManager().bind(LOGO);

		float magicX = logoX + 128;
		float magicY = logoY + 25.55F;

		float coOffset = (float) logoAnim.getValue() / 400;

		GL11.glPushMatrix();

		GL11.glTranslatef(magicX, magicY, 0);
		GL11.glRotatef(coOffset, 0, 0, 1F);
		GL11.glTranslatef(-magicX, -magicY, 0);

		this.renderBeubassLogo(stack, logoX, logoY, 1.0F, 0.0F, 0.0F, coOffset);
		this.renderBeubassLogo(stack, logoX, logoY, 0.0F, 0.0F, 1.0F, -coOffset);
		this.renderBeubassLogo(stack, logoX, logoY, 1.0F, 1.0F, 1.0F, 0);

		GL11.glPopMatrix();

		renderBeubassWaves(stack, logoX, logoY - BEUBASS_WAVES_Y_OFFSET);

		this.renderPrivateServer(stack, MAIN_PANEL_PADDING + (MAIN_PANEL_WIDTH / 2 - PSERVER_WIDTH / 2), 75);

		super.render(stack, mouseX, mouseY, partialTicks);

		if (blackAnim.startTime != 0) {
			fill(stack, 0, 0, width, height, Beu.getColorWithAlpha(0xFF000000, blackAnim.getValue()));
		}
	}

	// ================================================== //

	private void renderBeubassLogo(MatrixStack stack, int x, int y, float r, float g, float b, float offset) {
		GL11.glPushMatrix();
		RenderSystem.color3f(r, g, b);
		GL11.glTranslatef(offset, offset, offset);
		Beu.renderTexture(stack, x, y, 0, 56.1F, 256, 21, 256, 257);
		GL11.glPopMatrix();
	}

	private void renderPrivateServer(MatrixStack stack, int x, int y) {
		Beu.renderTexture(stack, x, y, 0, 77F, 256, 21, 256, 256);
	}

	private void renderBeubassWaves(MatrixStack stack, int x, int y) {
		Beu.renderTexture(stack, x, y, 0, 0, 256, 56, 256, 256);
	}

	private static String[][] splitArray(String[] arrayToSplit, int chunkSize){
		int rest = arrayToSplit.length % chunkSize;
		int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);
		String[][] arrays = new String[chunks][];

		for(int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++){
			arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
		}

		if(rest > 0){
			arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
		}

		return arrays;
	}
}
