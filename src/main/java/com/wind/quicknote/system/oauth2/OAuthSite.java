package com.wind.quicknote.system.oauth2;

public enum OAuthSite {
	
	FACEBOOK("facebook"), GOOGLE("google");

	private String siteName;
	
	private OAuthSite(String name) {
		siteName = name;
	}
	
	public String getSiteName() {
		return siteName;
	}
}
