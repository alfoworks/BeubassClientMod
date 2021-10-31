package com.iterator.beubassclientmod.mixin;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {
	@ModifyVariable(at = @At("HEAD"), ordinal = 0, method = "renderText(Ljava/lang/String;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZII)F")
	private String injected(String p_228081_1_) {
		return "Cum";
	}

	@ModifyVariable(at = @At("HEAD"), ordinal = 0, method = "renderText(Lnet/minecraft/util/IReorderingProcessor;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZII)F")
	private IReorderingProcessor injected2(IReorderingProcessor p_238426_1_) {
		return new StringTextComponent("Cum cum cum").getVisualOrderText();
	}
}
