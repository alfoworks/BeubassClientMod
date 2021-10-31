package com.iterator.beubassclientmod;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {
	/**
	 * Connect to Mixin
	 */
	@Override
	public void connect() {
		Mixins.addConfiguration("beubassclientmod.mixins.json");
	}
}