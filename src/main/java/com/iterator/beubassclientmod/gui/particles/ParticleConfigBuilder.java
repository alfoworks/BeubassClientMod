package com.iterator.beubassclientmod.gui.particles;

public class ParticleConfigBuilder {
	private float maxVelX;
	private float maxVelY;
	private float minVelX;
	private float minVelY;
	private float constVelX;
	private float constVelY;
	private boolean randomVelX;
	private boolean randomVelY;

	private int maxX;
	private int maxY;
	private int constX;
	private int constY;
	private boolean randomX;
	private boolean randomY;

	private int minLifetime;
	private int maxLifetime;

	public ParticleConfigBuilder withLifetime(int min, int max) {
		this.minLifetime = min;
		this.maxLifetime = max;

		return this;
	}

	public ParticleConfigBuilder withRandomPos(int x, int y) {
		this.maxX = x;
		this.maxY = y;
		this.randomX = true;
		this.randomY = true;

		return this;
	}

	public ParticleConfigBuilder withRandomGlobalVel(float maxVel) {
		return this.withRandomXVel(-maxVel, maxVel).withRandomYVel(-maxVel, maxVel);
	}

	public ParticleConfigBuilder withRandomXVel(float min, float max) {
		this.randomVelX = true;
		this.minVelX = min;
		this.maxVelX = max;

		return this;
	}

	public ParticleConfigBuilder withRandomYVel(float min, float max) {
		this.randomVelY = true;
		this.minVelY = min;
		this.maxVelY = max;

		return this;
	}

	public ParticleConfigBuilder withConstXVel(float velX) {
		this.constVelX = velX;

		return this;
	}

	public ParticleConfigBuilder withConstYVel(float velY) {
		this.constVelY = velY;

		return this;
	}

	public ParticleConfig build() {
		return new ParticleConfig(maxVelX, maxVelY, minVelX, minVelY, constVelX, constVelY, randomVelX, randomVelY, maxX, maxY, constX, constY, randomX, randomY, minLifetime, maxLifetime);
	}
}
