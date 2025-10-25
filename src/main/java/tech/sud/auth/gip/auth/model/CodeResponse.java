package tech.sud.auth.gip.auth.model;

import java.util.Date;

/**
 * Authentication code response class
 * Used to return generated authentication code and related information
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class CodeResponse extends BaseResponse {
    
    /**
     * Generated authentication code
     */
    private String code;
    
    /**
     * Authentication code expiration time
     */
    private Date expireDate;
    
    /**
     * Default constructor
     */
    public CodeResponse() {
        super();
    }
    
    /**
     * Success response constructor
     * 
     * @param code Authentication code
     * @param expireDate Expiration time
     */
    public CodeResponse(String code, Date expireDate) {
        super();
        this.code = code;
        this.expireDate = expireDate;
    }
    
    /**
     * Error response constructor
     * 
     * @param errorCode Error code
     */
    public CodeResponse(int errorCode) {
        super(errorCode);
    }
    
    /**
     * Complete constructor
     * 
     * @param isSuccess Whether successful
     * @param errorCode Error code
     * @param errorMessage Error message
     * @param code Authentication code
     * @param expireDate Expiration time
     */
    public CodeResponse(boolean isSuccess, int errorCode, String errorMessage, String code, Date expireDate) {
        super(isSuccess, errorCode, errorMessage);
        this.code = code;
        this.expireDate = expireDate;
    }
    
    /**
     * Get authentication code
     * 
     * @return Authentication code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Set authentication code
     * 
     * @param code Authentication code
     */
    public void setCode(String code) {
        this.code = code;
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
     * @param code Authentication code
     * @param expireDate Expiration time
     * @return CodeResponse instance
     */
    public static CodeResponse success(String code, Date expireDate) {
        return new CodeResponse(code, expireDate);
    }
    
    /**
     * Create error response
     * 
     * @param errorCode Error code
     * @return CodeResponse instance
     */
    public static CodeResponse error(int errorCode) {
        return new CodeResponse(errorCode);
    }
    
    /**
     * Create error response
     * 
     * @param errorCode Error code
     * @param errorMessage Error message
     * @return CodeResponse instance
     */
    public static CodeResponse error(int errorCode, String errorMessage) {
        CodeResponse response = new CodeResponse(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
    
    @Override
    public String toString() {
        return "CodeResponse{" +
                "code='" + code + '\'' +
                ", expireDate=" + expireDate +
                ", isSuccess=" + isSuccess() +
                ", errorCode=" + getErrorCode() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}