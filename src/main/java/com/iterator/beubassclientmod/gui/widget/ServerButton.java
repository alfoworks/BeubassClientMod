package com.iterator.beubassclientmod.gui.widget;

import br.com.azalim.mcserverping.MCPing;
import br.com.azalim.mcserverping.MCPingOptions;
import br.com.azalim.mcserverping.MCPingResponse;
import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.util.Beu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

public class ServerButton extends BeuButton {
	Minecraft minecraft = Minecraft.getMinecraft();

	ButtonState prevState;
	public ButtonState currentState;

	Animation stateAnim = new Animation(0, 255);
	Animation mainAnim = new Animation(127, 250);

	String onlineString = "none";
	GuiScreen prevScreen;

	private final MCPingOptions options = new MCPingOptions("eu.mineplex.com", 25565);

	private long clickTime;

	public ServerButton(int x, int y, GuiScreen prevScreen) {
		super(x, y, 100, 30, "", (sex) -> {});

		currentState = ButtonState.LOADING;
		this.enabled = false;

		this.prevScreen = prevScreen;
	}

	@Override
	public void onClick() {
		clickTime = System.currentTimeMillis();
		this.enabled = false;
	}

	private void realClick() {
		ServerData data = new ServerData("BEUBASS Zone", String.format("%s:%s", options.hostname, options.port), false);
		this.minecraft.displayGuiScreen(new GuiConnecting(prevScreen, this.minecraft, data));
	}

	public void ping() {
		if (this.currentState == ButtonState.ERROR) {
			setState(ButtonState.LOADING);
		}

		try {
			MCPingResponse response = MCPing.getPing(options, 5000);

			this.onlineString = String.format("%s/%s", response.players.online, response.players.max);
			setState(ButtonState.AVAILABLE);
		} catch (Exception ignored) {
			setState(ButtonState.ERROR);
		}
	}

	public void setState(ButtonState state) {
		prevState = currentState;
		currentState = state;

		this.enabled = state == ButtonState.AVAILABLE;

		stateAnim.start(false, 3, 1000);
	}

	private void renderState(ButtonState state, int alpha) {
		if (clickTime != 0 && System.currentTimeMillis() - clickTime >= 1500) {
			clickTime = 0;

			realClick();
			this.enabled = true;
		}

		this.drawGradientRect(this.x, this.y + 10, this.x + this.width, this.y + this.height, Beu.getColorWithAlpha(state.color, 0), Beu.getColorWithAlpha(state.color, Math.min(mainAnim.getValue(), alpha)));

		int stateColor = Beu.getColorWithAlpha(state.color, alpha);
		int whiteColor = Beu.getColorWithAlpha(0xFFFFFFFF, Math.max(alpha, 5));

		drawRect(this.x, this.y, this.x + this.width, this.y + 1, 0xFFFFFFFF);
		drawRect(this.x + (this.width / 2) - 25, this.y + 1, this.x + (this.width / 2) + 25, this.y + 3, 0xFFFFFFFF);
		drawRect(this.x, this.y, this.x + 1, this.y + this.height, 0xFFFFFFFF);
		drawRect(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0xFFFFFFFF);

		drawRect(this.x, this.y + this.height - 4, this.x + this.width, this.y + this.height, stateColor);

		this.minecraft.getTextureManager().bindTexture(Beu.ICONS);
		GL11.glColor4f(1F, 1F, 1F, (float) alpha / 255);
		Beu.renderIcon(state.icon, this.x + 3, this.y + 5, 16, 16);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		String text1 = I18n.format("menu.join");
		String text2 = state == ButtonState.AVAILABLE ? onlineString : I18n.format(currentState.text);

		int textAreaWidth = 100 - 16 - 3 - 2 - 3;
		int textAreaStart = this.x + 16 + 3 + 2;

		drawString(minecraft.fontRenderer, text1, textAreaStart, this.y + 4, whiteColor);

		drawRect(textAreaStart, this.y + 13, textAreaStart + textAreaWidth, this.y + 15, 0xFFFFFFFF);

		int text2X = textAreaStart;
		int text2Y = this.y + 17;

		GL11.glPushMatrix();
		GL11.glTranslatef(text2X, text2Y, 0F);
		GL11.glScalef(0.8F, 0.8F, 0F);
		drawString(minecraft.fontRenderer, text2, 0, 0, whiteColor);
		GL11.glPopMatrix();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.hovered && !this.wasHovered || !this.hovered && this.wasHovered) {
			this.mainAnim.start(!this.hovered, 0, 200);
		}

		int currentStateAlpha = 255;

		if (prevState != null) {
			renderState(prevState, 255 - stateAnim.getValue());

			if (stateAnim.startTime == 0) {
				prevState = null;
			}

			currentStateAlpha = stateAnim.getValue();
		}

		renderState(currentState, currentStateAlpha);
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
