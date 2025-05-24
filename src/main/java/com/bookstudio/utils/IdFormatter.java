package com.bookstudio.utils;

public class IdFormatter {
	public static String formatId(String rawId, String prefix) {
		try {
			int id = Integer.parseInt(rawId);
			return String.format("%s%04d", prefix, id);
		} catch (NumberFormatException e) {
			return rawId;
		}
	}
}
