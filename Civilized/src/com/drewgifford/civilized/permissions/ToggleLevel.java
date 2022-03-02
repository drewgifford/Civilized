package com.drewgifford.civilized.permissions;

public enum ToggleLevel {
	
	ENABLED(true),
	DISABLED(false),
	DEFAULT(0);
	
	private Object value;
	
	ToggleLevel(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return this.value;
	}

	public static ToggleLevel fromBoolean(boolean b) {
		if(b) {
			return ToggleLevel.ENABLED;
		} else return ToggleLevel.DISABLED;
	}
	
	public static ToggleLevel opposite(ToggleLevel level) {
		switch(level) {
			case ENABLED:
				return ToggleLevel.DISABLED;
			case DISABLED:
				return ToggleLevel.ENABLED;
			default:
				return ToggleLevel.ENABLED;
		}
	}
	
	

}
