package org.denevell.droidnatch;

public class Urls {
	
	private static String sUsername = "";
	private static String sBasePath = "";
    private static String sAuthKey = "";

	public static void setBasePath(String base) {
		sBasePath = base;
	}
	
	public static String getBasePath() {
		return sBasePath;
	}

    public static String getAuthKey() {
        return sAuthKey;
    }

    public static void setAuthKey(String sAuthKey) {
        Urls.sAuthKey = sAuthKey;
    }

	public static String getUsername() {
		return sUsername;
	}

	public static void setUsername(String sUsername) {
		Urls.sUsername = sUsername;
	}
}
