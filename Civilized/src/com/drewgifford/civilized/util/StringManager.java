package com.drewgifford.civilized.util;

public class StringManager {
	
	private static String ALPHANUMERIC = "^[a-zA-Z0-9_-]*$";
	
	private static int size = 20;
	private static int boardSize = 140;
	
	public static boolean isAlphanumeric(String s) {
		return s.matches(ALPHANUMERIC);
	}
	
	public static boolean isValidName(String s) {
		return isAlphanumeric(s) && (s.length() <= size);
	}
	
	public static boolean isValidBoard(String s) {
		return isAlphanumeric(s) && (s.length() <= boardSize);
	}

}
