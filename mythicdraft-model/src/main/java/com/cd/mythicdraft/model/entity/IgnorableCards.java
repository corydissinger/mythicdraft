package com.cd.mythicdraft.model.entity;

import org.apache.commons.lang3.ArrayUtils;


public enum IgnorableCards {
	PLAINS("Plains", new String [0]),
	ISLAND("Island", new String [0]),
	SWAMP("Swamp", new String [0]),
	MOUNTAIN("Mountain", new String [0]),
	FOREST("Forest", new String [0]);
	
	private IgnorableCards(final String name, final String [] setCodes) {
		this.name = name;
		this.setCodes = setCodes;
	}
	
	private final String name;
	private final String [] setCodes; 
	
	public static boolean isIgnorable(final String cardName, final String setCode) {
		for(IgnorableCards ignorable : values()) {
			if(ignorable.getName().equalsIgnoreCase(cardName.trim())) {
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
