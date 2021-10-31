package com.iterator.beubassclientmod.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {
	public static SoundEvent APEX_BUTT_DOWN;
	public static SoundEvent MENU;

	public static void registerSounds(IForgeRegistry<SoundEvent> registry) {
		APEX_BUTT_DOWN = register("phonk", registry);
		MENU = register("menu", registry);
	}

	private static SoundEvent register(String name, IForgeRegistry<SoundEvent> registry) {
		ResourceLocation loc = new ResourceLocation("beubassclientmod", name);
		SoundEvent soundEvent = new SoundEvent(loc);
		soundEvent.setRegistryName(loc);

		registry.register(soundEvent);

		return soundEvent;
	}
}
