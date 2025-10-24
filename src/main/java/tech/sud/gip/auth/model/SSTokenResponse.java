package tech.sud.gip.auth.model;

import java.util.Date;

/**
 * Server-to-server token response class
 * Used to return generated SSToken and related information
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SSTokenResponse extends BaseResponse {
    
    /**
     * Generated SSToken
     */
    private String token;
    
    /**
     * SSToken expiration time
     */
    private Date expireDate;
    
    /**
     * Default constructor
     */
    public SSTokenResponse() {
        super();
    }
    
    /**
     * Success response constructor
     * 
     * @param token SSToken
     * @param expireDate Expiration time
     */
    public SSTokenResponse(String token, Date expireDate) {
        super();
        this.token = token;
        this.expireDate = expireDate;
    }
    
    /**
     * Error response constructor
     * 
     * @param errorCode Error code
     */
    public SSTokenResponse(int errorCode) {
        super(errorCode);
    }
    
    /**
     * Complete constructor
     * 
     * @param isSuccess Whether successful
     * @param errorCode Error code
     * @param errorMessage Error message
     * @param token SSToken
     * @param expireDate Expiration time
     */
    public SSTokenResponse(boolean isSuccess, int errorCode, String errorMessage, String token, Date expireDate) {
        super(isSuccess, errorCode, errorMessage);
        this.token = token;
        this.expireDate = expireDate;
    }
    
    /**
     * Get SSToken
     * 
     * @return SSToken
     */
    public String getToken() {
        return token;
    }
    
    /**
     * Set SSToken
     * 
     * @param token SSToken
     */
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * Get expiration time
     * 
     * @return Expiration time
     */
    public Date getExpireDate() {
        return expireDate;
    }
    
    /**
     * Set expiration time
     * 
     * @param expireDate Expiration time
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    
    /**
     * Create success response
     * 
     * @param token SSToken
     * @param expireDate Expiration time
     * @return SSTokenResponse instance
     */
    public static SSTokenResponse success(String token, Date expireDate) {
        return new SSTokenResponse(token, expireDate);
    }
    
    /**
     * Create error response
     * 
     * @param errorCode Error code
     * @return SSTokenResponse instance
     */
    public static SSTokenResponse error(int errorCode) {
        return new SSTokenResponse(errorCode);
    }
    
    /**
     * Create error response
     * 
     * @param errorCode Error code
     * @param errorMessage Error message
     * @return SSTokenResponse instance
     */
    public static SSTokenResponse error(int errorCode, String errorMessage) {
        SSTokenResponse response = new SSTokenResponse(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
    
    @Override
    public String toString() {
        return "SSTokenResponse{" +
                "token='" + token + '\'' +
                ", expireDate=" + expireDate +
                ", isSuccess=" + isSuccess() +
                ", errorCode=" + getErrorCode() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}