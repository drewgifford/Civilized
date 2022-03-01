package com.drewgifford.civilized.permissions;

public enum PermissionLevel {
	
	OUTSIDER("Outsider", "outsider"),
	ALLY("Ally", "ally"),
	NATION_MEMBER("Nation Member", "nationmember"),
	NATION_OFFICER("Nation Officer", "nationofficer"),
	NATION_OWNER("Nation Owner", "nationowner"),
	MEMBER("Member", "member"),
	OFFICER("Officer", "officer"),
	OWNER("Owner", "owner");
	
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

}
