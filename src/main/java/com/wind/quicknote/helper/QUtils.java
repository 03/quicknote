package com.wind.quicknote.helper;

import java.util.Random;

public class QUtils {

	public static final String DIALOG_WARNING = "Warning";
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	public static String getRandomIconURL() {
		Random randomGenerator = new Random();
		int num = 1 + randomGenerator.nextInt(12);
		return "/assets/images/filetypes/ft" + num + ".gif";
	}
	
	public static String getRandomString(int length) {
		
		if(length <= 0)
			throw new IllegalArgumentException("Length cannot be less than or equal to 0");
		
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		
		for(int i=0; i<length; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		
		return sb.toString();
		
	}
}
