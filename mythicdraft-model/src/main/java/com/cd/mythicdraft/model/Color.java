package com.cd.mythicdraft.model;

public enum Color {
	WHITE,
	BLUE,
	BLACK,
	RED,
	GREEN,
	GOLD,
	COLORLESS;
	
	public static Color getColorFromString(final String aColor) {
		for(Color color : values()) {
			if(color.name().equalsIgnoreCase(aColor)) {
				return color;
			}
		}
		
		return COLORLESS;
	}
}
