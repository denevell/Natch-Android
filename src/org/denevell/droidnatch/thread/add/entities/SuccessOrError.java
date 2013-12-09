package org.denevell.droidnatch.thread.add.entities;


public class SuccessOrError {

	private boolean successful;
	private String error = "";

	public boolean isSuccessful() {
		return successful;
	}
	
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
}
