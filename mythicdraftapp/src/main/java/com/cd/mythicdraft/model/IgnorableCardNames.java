package com.cd.mythicdraft.model;

public enum IgnorableCardNames {
	PLAINS,
	ISLAND,
	SWAMP,
	MOUNTAIN,
	FOREST;
	
	public static boolean isIgnorable(final String cardName) {
		for(IgnorableCardNames ignorable : values()) {
			if(ignorable.name().equalsIgnoreCase(cardName)) {
				return true;
			}
		}
		
		return false;
	}
}
