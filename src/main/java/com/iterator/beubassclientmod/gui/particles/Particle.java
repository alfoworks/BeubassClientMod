package com.iterator.beubassclientmod.gui.particles;

public class Particle {
	float x;
	float y;
	float velX;
	float velY;

	long startTime;
	int lifetime;

	float alpha;
	int rotation;
	float scale;

	int oldCurrentLife;

	public Particle(float x, float y, float velX, float velY, int lifetime) {
		this.startTime = System.currentTimeMillis();

		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;

		this.lifetime = lifetime;
	}

	public void update() {
		int currentLife = (int) (System.currentTimeMillis() - startTime);

		int test = currentLife - oldCurrentLife;

		this.x += (float) (test / 4) * velX;
		this.y += (float) (test / 4) * velY;

		float percent = (float) (lifetime - currentLife) / lifetime;
		this.alpha = percent;
		this.scale = percent;

		this.rotation = 0;

		oldCurrentLife = currentLife;
	}
}
