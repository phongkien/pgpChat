package com.phongkien.utils;


public final class UtilsFunctions {
	private static final String keyString = "abcdefghijklmnopqrstuvwxzy0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Integer keyLength = keyString.length();
	public static final String SALT = "Pow! Straight to the moon!";
	
	public static boolean isNull(String value) {
		return value == null || value.trim().length() == 0;
	}
	
	public static String generateRandomKey(int length) {
		StringBuilder key = new StringBuilder();
		if (length > 0) {
			Double d = keyLength.doubleValue() - 1;
			for (int i = 0; i < length; i++) {
				int k = (int)(Math.random() * d);
				key.append(keyString.charAt(k));
			}
		}
		
		return key.toString();
	}
}
