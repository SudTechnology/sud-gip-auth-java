package tech.sud.auth.gip.auth.model;

/**
 * User ID response class
 * Used to return user ID obtained through token
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class UidResponse extends BaseResponse {
    
    /**
     * User ID
     */
    private String uid;
    
    /**
     * Default constructor
     */
    public UidResponse() {
        super();
    }
    
    /**
     * Success response constructor
     * 
     * @param uid User ID
     */
    public UidResponse(String uid) {
        super();
        this.uid = uid;
    }
    
    /**
     * Error response constructor
     * 
     * @param errorCode Error code
     */
    public UidResponse(int errorCode) {
        super(errorCode);
    }
    
    /**
     * Complete constructor
     * 
     * @param isSuccess Whether successful
     * @param errorCode Error code
     * @param errorMessage Error message
     * @param uid User ID
     */
    public UidResponse(boolean isSuccess, int errorCode, String errorMessage, String uid) {
        super(isSuccess, errorCode, errorMessage);
        this.uid = uid;
    }
    
    /**
     * Get user ID
     * 
     * @return User ID
     */
    public String getUid() {
        return uid;
    }
    
    /**
     * Set user ID
     * 
     * @param uid User ID
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    /**
     * Create success response
     * 
     * @param uid User ID
     * @return UidResponse instance
     */
    public static UidResponse success(String uid) {
        return new UidResponse(uid);
    }
    
    /**
     * Create error response
     * 
     * @param errorCode Error code
     * @return UidResponse instance
     */
    public static UidResponse error(int errorCode) {
        return new UidResponse(errorCode);
    }
    
    /**
     * Create error response
     * 
     * @param errorCode Error code
     * @param errorMessage Error message
     * @return UidResponse instance
     */
    public static UidResponse error(int errorCode, String errorMessage) {
        UidResponse response = new UidResponse(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
    
    @Override
    public String toString() {
        return "UidResponse{" +
                "uid='" + uid + '\'' +
                ", isSuccess=" + isSuccess() +
                ", errorCode=" + getErrorCode() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}