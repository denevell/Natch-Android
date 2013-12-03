package org.denevell.droidnatch.baseclasses;

public class FailureResult {
    
    private String errorCode;
    private String errorMessage;
    private int statusCode;

    public FailureResult() {
    }
    public FailureResult(String errorCode, String errorMessage, int statusCode) {
        this.setErrorCode(errorCode);
        this.setErrorMessage(errorMessage);
        this.setStatusCode(statusCode);
    }
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}