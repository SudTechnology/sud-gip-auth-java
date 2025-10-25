package tech.sud.auth.gip.auth.exception;

import tech.sud.auth.gip.auth.constant.ErrorCode;

/**
 * Token validation exception class
 * Thrown when token validation fails
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class TokenValidationException extends SudGIPAuthException {
    
    /**
     * Constructor
     * 
     * @param message Error message
     */
    public TokenValidationException(String message) {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED, message);
    }
    
    /**
     * Constructor
     * 
     * @param message Error message
     * @param cause Cause exception
     */
    public TokenValidationException(String message, Throwable cause) {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED, message, cause);
    }
    
    /**
     * Constructor (using default error message)
     */
    public TokenValidationException() {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED);
    }
    
    /**
     * Constructor (using default error message)
     * 
     * @param cause Cause exception
     */
    public TokenValidationException(Throwable cause) {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED, cause);
    }
}