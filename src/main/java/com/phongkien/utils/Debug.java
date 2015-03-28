package com.phongkien.utils;

public class Debug {
	public static final boolean ON = true;
	
	public static final void log(String message) {
		if (ON) {
			System.out.println(message);
		}
	}
}
