package com.wind.quicknote.helper;

import java.util.Random;

public class QUtils {

	public static final String DIALOG_WARNING = "Warning";
	
	public static String getRandomIconURL() {
		Random randomGenerator = new Random();
		int num = 1 + randomGenerator.nextInt(12);
		return "/assets/images/filetypes/ft" + num + ".gif";
	}
	
}
