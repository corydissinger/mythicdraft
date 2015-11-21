package com.cd.mythicdraft.model;

import org.apache.commons.lang3.ArrayUtils;


public enum IgnorableCards {
	PLAINS("Plains", new String [] {""}),
	ISLAND("Island", new String [] {""}),
	SWAMP("Swamp", new String [] {""}),
	MOUNTAIN("Mountain", new String [] {""}),
	FOREST("Forest", new String [] {""});
	
	private IgnorableCards(final String name, final String [] setCodes) {
		this.name = name;
		this.setCodes = setCodes;
	}
	
	private final String name;
	private final String [] setCodes; 
	
	public static boolean isIgnorable(final String cardName, final String setCode) {
		for(IgnorableCards ignorable : values()) {
			if(ignorable.getName().equalsIgnoreCase(cardName)) {
				if(ignorable.getSetCodes().length == 0 || ArrayUtils.contains(ignorable.getSetCodes(), setCode)) {
					return true;					
				}
			}
		}
		
		return false;
	}

	public String getName() {
		return name;
	}

	public String [] getSetCodes() {
		return setCodes;
	}
}
