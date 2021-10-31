package com.iterator.beubassclientmod.gui.screen;

import com.iterator.beubassclientmod.BeubassClientMod;
import com.iterator.beubassclientmod.gui.Animation;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.iterator.beubassclientmod.gui.widget.RegularButt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.login.ClientLoginNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ProtocolType;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.network.login.client.CLoginStartPacket;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class CCS extends Screen {
    Animation anim = new Animation(0, 255);

    private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();
    private NetworkManager connection;
    private boolean aborted;
    private final Screen parent;
    private ITextComponent status = new TranslationTextComponent("connect.connecting");
    private long lastNarration = -1L;

    ServerAddress addr;
    ServerData data;

    public CCS(Screen p_i1181_1_, Minecraft p_i1181_2_, ServerData p_i1181_3_) {
        super(NarratorChatListener.NO_TITLE);
        this.minecraft = p_i1181_2_;
        this.parent = p_i1181_1_;
        this.addr = ServerAddress.parseString(p_i1181_3_.ip);
        this.data = p_i1181_3_;

        startConnection();
    }

    private void startConnection() {
        Minecraft p_i1181_2_ = Minecraft.getInstance();

        p_i1181_2_.clearLevel();
        p_i1181_2_.setCurrentServer(data);
        this.connect(addr.getHost(), addr.getPort());
    }

    private void connect(final String p_146367_1_, final int p_146367_2_) {
        LOGGER.info("Connecting to {}, {}", p_146367_1_, p_146367_2_);
        Thread lvt_3_1_ = new Thread("Server Connector #" + UNIQUE_THREAD_ID.incrementAndGet()) {
            public void run() {
                InetAddress lvt_1_1_ = null;

                try {
                    if (CCS.this.aborted) {
                        return;
                    }

                    lvt_1_1_ = InetAddress.getByName(p_146367_1_);
                    CCS.this.connection = NetworkManager.connectToServer(lvt_1_1_, p_146367_2_, CCS.this.minecraft.options.useNativeTransport());
                    CCS.this.connection.setListener(new ClientLoginNetHandler(CCS.this.connection, CCS.this.minecraft, CCS.this.parent, (p_209549_1_) -> {
                        CCS.this.updateStatus(p_209549_1_);
                    }));
                    CCS.this.connection.send(new CHandshakePacket(p_146367_1_, p_146367_2_, ProtocolType.LOGIN));
                    CCS.this.connection.send(new CLoginStartPacket(CCS.this.minecraft.getUser().getGameProfile()));
                } catch (UnknownHostException var4) {
                    if (CCS.this.aborted) {
                        return;
                    }

                    BeubassClientMod.LOGGER.error("Couldn't connect to server", var4);
                    CCS.this.minecraft.execute(() -> {
                        CCS.this.minecraft.setScreen(new CDS(CCS.this.parent, DialogTexts.CONNECT_FAILED, new TranslationTextComponent("disconnect.genericReason", new Object[]{"Unknown host"})));
                    });
                } catch (Exception var5) {
                    if (CCS.this.aborted) {
                        return;
                    }

                    BeubassClientMod.LOGGER.error("Couldn't connect to server", var5);
                    String lvt_3_1_ = lvt_1_1_ == null ? var5.toString() : var5.toString().replaceAll(lvt_1_1_ + ":" + p_146367_2_, "");
                    CCS.this.minecraft.execute(() -> {
                        CCS.this.minecraft.setScreen(new CDS(CCS.this.parent, DialogTexts.CONNECT_FAILED, new TranslationTextComponent("disconnect.genericReason", new Object[]{lvt_3_1_})));
                    });
                }

            }
        };
        lvt_3_1_.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        lvt_3_1_.start();
    }

    private void updateStatus(ITextComponent p_209514_1_) {
        this.status = p_209514_1_;
    }

    public void tick() {
        if (this.connection != null) {
            if (this.connection.isConnected()) {
                this.connection.tick();
            } else {
                this.connection.handleDisconnection();
            }
        }

    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {
        anim.startEndless(3, 5000);

        this.addButton(new RegularButt(this.width / 2 - 50, this.height / 4 + 120 + 12, 100, DialogTexts.GUI_CANCEL, (p_212999_1_) -> {
            this.aborted = true;
            if (this.connection != null) {
                this.connection.disconnect(new TranslationTextComponent("connect.aborted"));
            }

            this.minecraft.setScreen(this.parent);
        }));
    }

    public void render(MatrixStack stack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        fill(stack, 0, 0, this.width, this.height, 0xFF000000);

        int topColor = Beu.colorMix(0x001c92d2, 0x00f2fcfe, 1 - ((float) anim.getValue() / 255));
        int botColor = Beu.colorMix(0xFF283c86, 0xFF45a247, (float) anim.getValue() / 255);

        Beu.drawGradient4c(stack, 0, 0, width, height, botColor, botColor, topColor, topColor);

        long lvt_5_1_ = Util.getMillis();
        if (lvt_5_1_ - this.lastNarration > 2000L) {
            this.lastNarration = lvt_5_1_;
            NarratorChatListener.INSTANCE.sayNow((new TranslationTextComponent("narrator.joining")).getString());
        }

        drawCenteredString(stack, this.font, this.status, this.width / 2, this.height / 2 - 50, 16777215);
        super.render(stack, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
