package tech.sud.auth.gip.auth.constant;

/**
 * Sud GIP Auth error code constant definitions
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public final class ErrorCode {
    
    /**
     * Success
     */
    public static final int SUCCESS = 0;
    
    /**
     * Token creation failed
     */
    public static final int TOKEN_CREATION_FAILED = 1001;
    
    /**
     * Token verification failed
     */
    public static final int TOKEN_VERIFICATION_FAILED = 1002;
    
    /**
     * Token decoding failed
     */
    public static final int TOKEN_DECODING_FAILED = 1003;
    
    /**
     * Token invalid
     */
    public static final int TOKEN_INVALID = 1004;
    
    /**
     * Token expired
     */
    public static final int TOKEN_EXPIRED = 1005;
    
    /**
     * App data invalid
     */
    public static final int APP_DATA_INVALID = 1101;
    
    /**
     * Unknown error
     */
    public static final int UNKNOWN_ERROR = 9999;
    
    /**
     * Private constructor to prevent instantiation
     */
    private ErrorCode() {
        throw new AssertionError("ErrorCode class should not be instantiated");
    }
    
    /**
     * Get description message for error code
     * 
     * @param errorCode Error code
     * @return Error description
     */
    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case SUCCESS:
                return "Success";
            case TOKEN_CREATION_FAILED:
                return "Token creation failed";
            case TOKEN_VERIFICATION_FAILED:
                return "Token verification failed";
            case TOKEN_DECODING_FAILED:
                return "Token decoding failed";
            case TOKEN_INVALID:
                return "Token is invalid";
            case TOKEN_EXPIRED:
                return "Token has expired";
            case APP_DATA_INVALID:
                return "App data is invalid";
            case UNKNOWN_ERROR:
                return "Unknown error";
            default:
                return "Unknown error code: " + errorCode;
        }
    }
    
    /**
     * Check if error code indicates success
     * 
     * @param errorCode Error code
     * @return Whether successful
     */
    public static boolean isSuccess(int errorCode) {
        return errorCode == SUCCESS;
    }
}