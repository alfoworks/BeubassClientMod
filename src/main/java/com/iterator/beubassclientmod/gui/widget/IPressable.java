package com.iterator.beubassclientmod.gui.widget;

import net.minecraft.client.gui.GuiButton;

@FunctionalInterface
public interface IPressable {
    public void onPress(GuiButton button);
}
