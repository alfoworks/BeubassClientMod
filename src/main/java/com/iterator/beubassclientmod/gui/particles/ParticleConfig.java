package com.iterator.beubassclientmod.gui.particles;

import java.util.Random;

public class ParticleConfig {
	public float maxVelX;
	public float maxVelY;
	public float minVelX;
	public float minVelY;
	public float constVelX;
	public float constVelY;
	public boolean randomVelX;
	public boolean randomVelY;

	public int maxX;
	public int maxY;
	public int constX;
	public int constY;
	public boolean randomX;
	public boolean randomY;

	public int minLifetime;
	public int maxLifetime;

	private final Random random = new Random();

	public ParticleConfig(float maxVelX, float maxVelY, float minVelX, float minVelY, float constVelX, float constVelY, boolean randomVelX, boolean randomVelY, int maxX, int maxY, int constX, int constY, boolean randomX, boolean randomY, int minLifetime, int maxLifetime) {
		this.maxVelX = maxVelX;
		this.maxVelY = maxVelY;
		this.minVelX = minVelX;
		this.minVelY = minVelY;
		this.constVelX = constVelX;
		this.constVelY = constVelY;
		this.randomVelX = randomVelX;
		this.randomVelY = randomVelY;

		this.maxX = maxX;
		this.maxY = maxY;
		this.constX = constX;
		this.constY = constY;
		this.randomX = randomX;
		this.randomY = randomY;

		this.minLifetime = minLifetime;
		this.maxLifetime = maxLifetime;
	}

	public int getX() {
		return randomX ? randomInt(0, maxX) : constX;
	}

	public int getY() {
		return randomY ? randomInt(0, maxY) : constY;
	}

	public float getVelX() {
		return randomVelX ? randomFloat(minVelX, maxVelX) : constVelX;
	}

	public float getVelY() {
		return randomVelY ? randomFloat(minVelY, maxVelY) : constVelY;
	}

	public int getLifetime() {
		return randomInt(minLifetime, maxLifetime);
	}

	// ======================== //

	private float randomFloat(float min, float max) {
		return min + random.nextFloat() * (max - min);
	}

	private int randomInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}
}
