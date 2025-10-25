package tech.sud.auth.gip.auth.exception;

import tech.sud.auth.gip.auth.constant.ErrorCode;

/**
 * Token generation exception class
 * Thrown when token generation fails
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class TokenGenerationException extends SudGIPAuthException {
    
    /**
     * Constructor
     * 
     * @param message Error message
     */
    public TokenGenerationException(String message) {
        super(ErrorCode.TOKEN_CREATION_FAILED, message);
    }
    
    /**
     * Constructor
     * 
     * @param message Error message
     * @param cause Cause exception
     */
    public TokenGenerationException(String message, Throwable cause) {
        super(ErrorCode.TOKEN_CREATION_FAILED, message, cause);
    }
    
    /**
     * Constructor (using default error message)
     */
    public TokenGenerationException() {
        super(ErrorCode.TOKEN_CREATION_FAILED);
    }
    
    /**
     * Constructor (using default error message)
     * 
     * @param cause Cause exception
     */
    public TokenGenerationException(Throwable cause) {
        super(ErrorCode.TOKEN_CREATION_FAILED, cause);
    }
}