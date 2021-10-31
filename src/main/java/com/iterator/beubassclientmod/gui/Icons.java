package com.iterator.beubassclientmod.gui;

public enum Icons {
	HEART(0, 0),
	CIRCLE(1, 0),
	BEU(2, 0),
	PIG(3,0),
	TURN_OFF(0, 1),
	SETTINGS(1, 1),
	SINGLEPLAYER(2, 1),
	PLAY(3, 1),
	OFFLINE(0, 2),
	LOADING(1, 2),
	UNKNOWN(2, 2);

	public int x;
	public int y;

	Icons(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
