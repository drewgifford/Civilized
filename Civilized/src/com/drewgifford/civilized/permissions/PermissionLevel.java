package com.drewgifford.civilized.permissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PermissionLevel {
	
	OUTSIDER("Outsider", "outsider"),
	ALLY("Ally", "ally"),
	NATION_MEMBER("Nation_Member", "nationmember"),
	NATION_OFFICER("Nation_Officer", "nationofficer"),
	NATION_OWNER("Nation_Owner", "nationowner"),
	MEMBER("Member", "member"),
	OFFICER("Officer", "officer"),
	OWNER("Owner", "owner"),
	TRUSTED("Trusted", "trusted");
	
	private String id;
	private String label;

	PermissionLevel(String label, String id) {
		this.label = label;
		this.id = id;
	}
	
	public String getLabel() {
		return this.label;
	}
	public String getId() {
		return this.id;
	}
	
	public static String getOptionsString() {
		
		List<String> strings = new ArrayList<String>();
		
		for (PermissionLevel level : PermissionLevel.values()) {
			strings.add(level.label.toUpperCase());
		}
		
		Collections.sort(strings);
		
		return String.join(", ", strings);
		
		
		
	}
	public static PermissionLevel fromString(String string) {
		
		for (PermissionLevel level : PermissionLevel.values()) {
			if (level.getLabel().equalsIgnoreCase(string)) return level;
		}
		return null;
	}

}
