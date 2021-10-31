package com.iterator.beubassclientmod;

import com.iterator.beubassclientmod.gui.screen.PhonkMenu;
import com.iterator.beubassclientmod.gui.PigScreenOverride;
import com.iterator.beubassclientmod.gui.screen.WelcomeScreen;
import com.iterator.beubassclientmod.sound.ModSounds;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Mod("beubassclientmod")
public class BeubassClientMod
{
    public static final Logger LOGGER = LogManager.getLogger();
    private boolean welcomeScreenOpened;

    public static String[] sponsors = new String[]{};

    public BeubassClientMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new PigScreenOverride());
    }

    @SubscribeEvent
    public void onGuiScreenEvent(GuiOpenEvent event) {
        if (event.getGui() != null && (event.getGui() instanceof MainMenuScreen || event.getGui().getClass().getName().equals("dev.latvian.kubejs.ui.ScreenKubeJSUI"))) {
            Screen screen;

            if (welcomeScreenOpened) {
                screen = new PhonkMenu();
            } else {
                screen = new WelcomeScreen();

                welcomeScreenOpened = true;
            }

            event.setGui(screen);
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Reading sponsors");

        String sponsorsFile = Minecraft.getInstance().gameDirectory.getPath() + "/sponsors.txt";

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(sponsorsFile));

            sponsors = new String(encoded, StandardCharsets.UTF_8).split("\n");
        } catch (IOException e) {
            LOGGER.error("Can't read sponsors");
            e.printStackTrace();
        }
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onSoundsRegistry(final RegistryEvent.Register<SoundEvent> event) {
            ModSounds.registerSounds(event.getRegistry());

            LOGGER.info("Hello from register anal");
        }
    }
}
