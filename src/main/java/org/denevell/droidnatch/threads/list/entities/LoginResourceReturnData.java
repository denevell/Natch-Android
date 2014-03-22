package org.denevell.droidnatch.threads.list.entities;

public class LoginResourceReturnData extends SuccessOrError {
	private String authKey = "";
	private boolean admin;

	public String getAuthKey() {
		return this.authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
