package com.drewgifford.civilized.permissions;

public class CityPermissions {
	
	public PermissionLevel block_break;
	public PermissionLevel block_place;
	public PermissionLevel interact;
	
	public CityPermissions() {
		block_break = PermissionLevel.MEMBER;
		block_place = PermissionLevel.MEMBER;
		interact = PermissionLevel.MEMBER;
	}

}
