package com.cd.mythicdraft.model.entity;

import java.util.Collection;

public enum PairedSets {

	BFZ(new String [] { "EXP" }),
	OGW(new String [] { "EXP" });;
	
	private final String [] setsPaired;
	
	private PairedSets(final String [] setsPaired) {
		this.setsPaired = setsPaired;
	}

	public static Collection<String> getAllPairedSetCodes(Collection<String> setCodesToConsider) {
		for(PairedSets paired : values()) {
			if(setCodesToConsider.contains(paired.name())) {
				for(String pairedSet : paired.setsPaired) {
					setCodesToConsider.add(pairedSet);
				}
			}
		}
		
		return setCodesToConsider;
	}
	
	public String [] getSetsPaired() {
		return setsPaired;
	}
}
