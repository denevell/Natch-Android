package org.denevell.droidnatch.baseclasses;

public class FailureResult {
    
    public String errorCode;
    public String errorMessage;
    public int statusCode;

    public FailureResult() {
    }
    public FailureResult(String errorCode, String errorMessage, int statusCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }
}