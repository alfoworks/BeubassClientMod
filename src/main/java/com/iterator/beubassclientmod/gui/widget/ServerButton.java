package com.iterator.beubassclientmod.gui.widget;

import br.com.azalim.mcserverping.MCPing;
import br.com.azalim.mcserverping.MCPingOptions;
import br.com.azalim.mcserverping.MCPingResponse;
import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.screen.CCS;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

public class ServerButton extends PhonkSoundButt {
	Minecraft minecraft = Minecraft.getInstance();

	ButtonState prevState;
	public ButtonState currentState;

	Animation stateAnim = new Animation(0, 255);
	Animation mainAnim = new Animation(127, 250);

	ITextComponent onlineString = new StringTextComponent("none");
	Screen prevScreen;

	private final MCPingOptions options = new MCPingOptions("46.174.53.150", 27041);

	private long clickTime;

	public ServerButton(int x, int y, Screen prevScreen) {
		super(x, y, 100, 30, new StringTextComponent(""), (sex) -> {});

		currentState = ButtonState.LOADING;
		this.active = false;

		this.prevScreen = prevScreen;
	}

	@Override
	public void onClick(double p_230982_1_, double p_230982_3_) {
		super.onClick(p_230982_1_, p_230982_3_);

		clickTime = System.currentTimeMillis();
		this.active = false;
	}

	private void realClick() {
		ServerData data = new ServerData("BEUBASS Zone", String.format("%s:%s", options.hostname, options.port), false);
		this.minecraft.setScreen(new CCS(prevScreen, this.minecraft, data));
	}

	public void ping() {
		if (this.currentState == ButtonState.ERROR) {
			setState(ButtonState.LOADING);
		}

		try {
			MCPingResponse response = MCPing.getPing(options, 5000);

			this.onlineString = new StringTextComponent(String.format("%s/%s", response.players.online, response.players.max));
			setState(ButtonState.AVAILABLE);
		} catch (Exception ignored) {
			setState(ButtonState.ERROR);
		}
	}

	public void setState(ButtonState state) {
		prevState = currentState;
		currentState = state;

		this.active = state == ButtonState.AVAILABLE;

		stateAnim.start(false, 3, 1000);
	}

	private void renderState(MatrixStack stack, ButtonState state, int alpha) {
		if (clickTime != 0 && System.currentTimeMillis() - clickTime >= 1500) {
			clickTime = 0;

			realClick();
			this.active = true;
		}

		this.fillGradient(stack, this.x, this.y + 10, this.x + this.width, this.y + this.height, Beu.getColorWithAlpha(state.color, 0), Beu.getColorWithAlpha(state.color, Math.min(mainAnim.getValue(), alpha)));

		int stateColor = Beu.getColorWithAlpha(state.color, alpha);
		int whiteColor = Beu.getColorWithAlpha(0xFFFFFFFF, Math.max(alpha, 5));

		fill(stack, this.x, this.y, this.x + this.width, this.y + 1, 0xFFFFFFFF);
		fill(stack, this.x + (this.width / 2) - 25, this.y + 1, this.x + (this.width / 2) + 25, this.y + 3, 0xFFFFFFFF);
		fill(stack, this.x, this.y, this.x + 1, this.y + this.height, 0xFFFFFFFF);
		fill(stack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0xFFFFFFFF);

		fill(stack, this.x, this.y + this.height - 4, this.x + this.width, this.y + this.height, stateColor);

		this.minecraft.getTextureManager().bind(Beu.ICONS);
		GL11.glColor4f(1F, 1F, 1F, (float) alpha / 255);
		Beu.renderIcon(stack, state.icon, this.x + 3, this.y + 5, 16, 16);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		ITextComponent text1 = new TranslationTextComponent("menu.join");
		ITextComponent text2 = state == ButtonState.AVAILABLE ? onlineString : new TranslationTextComponent(state.text);

		int textAreaWidth = 100 - 16 - 3 - 2 - 3;
		int textAreaStart = this.x + 16 + 3 + 2;

		drawString(stack, minecraft.font, text1, textAreaStart, this.y + 4, whiteColor);

		fill(stack, textAreaStart, this.y + 13, textAreaStart + textAreaWidth, this.y + 15, 0xFFFFFFFF);

		int text2X = textAreaStart;
		int text2Y = this.y + 17;

		GL11.glPushMatrix();
		GL11.glTranslatef(text2X, text2Y, 0F);
		GL11.glScalef(0.8F, 0.8F, 0F);
		drawString(stack, minecraft.font, text2, 0, 0, whiteColor);
		GL11.glPopMatrix();
	}

	@Override
	public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		if (this.isHovered && !this.wasHovered || !this.isHovered && this.wasHovered) {
			this.mainAnim.start(!this.isHovered, 0, 200);
		}

		int currentStateAlpha = 255;

		if (prevState != null) {
			renderState(stack, prevState, 255 - stateAnim.getValue());

			if (stateAnim.startTime == 0) {
				prevState = null;
			}

			currentStateAlpha = stateAnim.getValue();
		}

		renderState(stack, currentState, currentStateAlpha);
	}

	public enum ButtonState {
		AVAILABLE(0xFF2372fa, "%s/%s", Icons.PLAY),
		LOADING(0xbdb155, "menu.connecting", Icons.LOADING),
		ERROR(0xFFFF3131, "menu.offline", Icons.OFFLINE);

		public int color;
		public String text;
		public Icons icon;

		ButtonState(int color, String text, Icons icon) {
			this.color = color;
			this.text = text;
			this.icon = icon;
		}
	}
}
