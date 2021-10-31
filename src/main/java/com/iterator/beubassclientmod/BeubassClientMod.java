package com.iterator.beubassclientmod;

import com.iterator.beubassclientmod.gui.screen.PhonkMenu;
import com.iterator.beubassclientmod.gui.PigScreenOverride;
import com.iterator.beubassclientmod.gui.screen.WelcomeScreen;
import com.iterator.beubassclientmod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Mod(modid = "beubassclientmod", name = "BeubassClientMod", version = "1.0")
public class BeubassClientMod
{
    public static final Logger LOGGER = LogManager.getLogger();
    private boolean welcomeScreenOpened;

    public static String[] sponsors = new String[]{};

    public BeubassClientMod() {
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new PigScreenOverride());
    }

    @SubscribeEvent
    public void onGuiScreenEvent(GuiOpenEvent event) {
        if (event.getGui() != null && (event.getGui() instanceof GuiMainMenu || event.getGui().getClass().getName().equals("dev.latvian.kubejs.ui.ScreenKubeJSUI"))) {
            GuiScreen screen;

            if (welcomeScreenOpened) {
                screen = new PhonkMenu();
            } else {
                screen = new WelcomeScreen();

                welcomeScreenOpened = true;
            }

            event.setGui(screen);
        }
    }

    @Mod.EventHandler
    private void setup(final FMLPreInitializationEvent event)
    {
        LOGGER.info("Reading sponsors");

        String sponsorsFile = Minecraft.getMinecraft().mcDataDir.getPath() + "/sponsors.txt";

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(sponsorsFile));

            sponsors = new String(encoded, StandardCharsets.UTF_8).split("\n");
        } catch (IOException e) {
            LOGGER.error("Can't read sponsors");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        ModSounds.registerSounds(event.getRegistry());
    }
}
