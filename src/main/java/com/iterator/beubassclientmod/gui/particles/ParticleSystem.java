package com.iterator.beubassclientmod.gui.particles;

import com.iterator.beubassclientmod.gui.Icons;
import com.iterator.beubassclientmod.gui.util.Beu;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ParticleSystem {
	Particle[] particles;

	boolean createNewParticles = true;

	Icons icon;
	Minecraft minecraft = Minecraft.getInstance();

	ParticleConfig config;

	public ParticleSystem(int maxParticles, Icons icon, ParticleConfig config) {
		this.particles = new Particle[maxParticles];

		this.icon = icon;

		this.config = config;
	}

	public void setCreateNewParticles(boolean flag) {
		createNewParticles = flag;
	}

	public void render(MatrixStack stack, int x, int y) {
		for (int i = 0; i < this.particles.length; i++) {
			if (particles[i] == null && createNewParticles) {
				particles[i] = new Particle(x + config.getX(), y + config.getY(), config.getVelX(), config.getVelY(), config.getLifetime());
			}

			if (particles[i] == null) continue;

			Particle particle = particles[i];

			GL11.glPushMatrix();

			GL11.glTranslatef(particle.x, particle.y, 0);
			GL11.glRotatef(particle.rotation, 0F, 0F, 1F);
			minecraft.getTextureManager().bind(Beu.ICONS);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, particle.alpha);
			GL11.glScalef(particle.scale, particle.scale, particle.scale);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0);
			//blit(stack, 0, 0, getBlitOffset(), icon.x * 16, icon.y * 16, 16, 16, 64, 64);
			Beu.renderIcon(stack, icon, 0, 0, 16, 16);
			GL11.glDisable(GL11.GL_BLEND);

			GL11.glPopMatrix();

			particle.update();

			if (System.currentTimeMillis() - particle.startTime >= particle.lifetime) {
				particles[i] = null;
			}
		}
	}
}
