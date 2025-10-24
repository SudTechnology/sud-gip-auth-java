package tech.sud.gip.auth.exception;

import tech.sud.gip.auth.constant.ErrorCode;

/**
 * Sud GIP Auth base exception class
 * Base class for all Sud GIP Auth related exceptions
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SudGIPAuthException extends Exception {
    
    /**
     * Error code
     */
    private final int errorCode;
    
    /**
     * Constructor
     * 
     * @param errorCode Error code
     * @param message Error message
     */
    public SudGIPAuthException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructor
     * 
     * @param errorCode Error code
     * @param message Error message
     * @param cause Cause exception
     */
    public SudGIPAuthException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructor (using default error message)
     * 
     * @param errorCode Error code
     */
    public SudGIPAuthException(int errorCode) {
        super(ErrorCode.getErrorMessage(errorCode));
        this.errorCode = errorCode;
    }
    
    /**
     * Constructor (using default error message)
     * 
     * @param errorCode Error code
     * @param cause Cause exception
     */
    public SudGIPAuthException(int errorCode, Throwable cause) {
        super(ErrorCode.getErrorMessage(errorCode), cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Get error code
     * 
     * @return Error code
     */
    public int getErrorCode() {
        return errorCode;
    }
    
    /**
     * Check if it matches the specified error code
     * 
     * @param errorCode Error code to check
     * @return Whether it matches
     */
    public boolean isErrorCode(int errorCode) {
        return this.errorCode == errorCode;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "errorCode=" + errorCode +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}