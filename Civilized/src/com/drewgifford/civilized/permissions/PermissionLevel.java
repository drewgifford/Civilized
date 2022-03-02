package com.drewgifford.civilized.permissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PermissionLevel {
	
	OUTSIDER("Outsider", "outsider", 0),
	ALLY("Ally", "ally", 1),
	NATION_MEMBER("Nation_Member", "nationmember", 2),
	MEMBER("Member", "member", 3),
	TRUSTED("Trusted", "trusted", 4),
	NATION_OFFICER("Nation_Officer", "nationofficer", 5),
	NATION_OWNER("Nation_Owner", "nationowner", 6),
	OFFICER("Officer", "officer", 7),
	OWNER("Owner", "owner", 8),
	DEFAULT("Default", "default", 0);
	
	private String id;
	private String label;
	private int priority;

	PermissionLevel(String label, String id, int priority) {
		this.label = label;
		this.id = id;
		this.priority = priority;
	}
	
	public String getLabel() {
		return this.label;
	}
	public String getId() {
		return this.id;
	}
	public int getPriority() {
		return this.priority;
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
