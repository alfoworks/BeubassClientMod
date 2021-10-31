package com.iterator.beubassclientmod.gui;

public class Animation {
	int minValue;
	int maxValue;
	int duration;

	public int lastValue;
	public long startTime;

	private int currentEasing;

	public boolean decrease;

	boolean fadeInOut;

	boolean isEndless;

	public Animation(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue - minValue;

		this.lastValue = minValue;
	}

	public void startEndless(int easing, int duration) {
		this.isEndless = true;
		this.start(false, easing, duration);
	}

	public void start(boolean decrease, int easing, int duration) {
		this.duration = duration;

		this.startTime = System.currentTimeMillis();

		this.currentEasing = easing;

		this.decrease = decrease;

		this.fadeInOut = false;
	}

	public void startFadeInOut(int easing, int duration) {
		this.fadeInOut = true;
		this.start(false, easing, duration / 2);
	}

	public int getValue() {
		if (this.startTime == 0) {
			return this.lastValue;
		}

		int elapsed = Math.min(this.duration, (int) (System.currentTimeMillis() - this.startTime));

		if (this.decrease) {
			elapsed = this.duration - elapsed;
		}

		float percent = (float) elapsed / this.duration;

		this.lastValue = this.ease(this.currentEasing, percent, elapsed, this.minValue, this.maxValue, this.duration);

		if ((this.decrease && percent == 0) || (!this.decrease && percent == 1)) {
			if (this.fadeInOut) {
				this.start(true, this.currentEasing, this.duration);
			} else if (this.isEndless) {
				this.start(!decrease, this.currentEasing, this.duration);
			} else {
				this.startTime = 0;
			}
		}

		return this.lastValue;
	}

	public int ease(int type, float x, float t, float b, float c, float d) {
		float val;

		switch (type) {
			default:
			case 0: // linear
				val = b + c * x;
				break;
			case 1: // easeInQuad
				val = c * (t /= d) * t + b;
				break;
			case 2: // easeOutQuad
				val = -c * (t /= d) * (t - 2) + b;
				break;
			case 3: // easeInOutQuad
				if ((t /= d / 2) < 1) {
					val = c / 2 * t * t + b;
				} else {
					val = -c / 2 * ((--t) * (t - 2) - 1) + b;
				}
				break;
		}

		return (int) val;
	}
}
