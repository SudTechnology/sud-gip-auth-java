package tech.sud.gip.auth.model;

import tech.sud.gip.auth.constant.ErrorCode;

/**
 * Sud GIP Auth base response class
 * Base class for all response classes, containing common status information
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public abstract class BaseResponse {
    
    /**
     * Whether the operation is successful
     */
    private boolean isSuccess;
    
    /**
     * Error code
     */
    private int errorCode;
    
    /**
     * Error message
     */
    private String errorMessage;
    
    /**
     * Default constructor
     */
    public BaseResponse() {
        this.isSuccess = true;
        this.errorCode = ErrorCode.SUCCESS;
        this.errorMessage = null;
    }
    
    /**
     * Success response constructor
     * 
     * @param isSuccess Whether successful
     */
    public BaseResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
        if (isSuccess) {
            this.errorCode = ErrorCode.SUCCESS;
            this.errorMessage = null;
        } else {
            this.errorCode = ErrorCode.UNKNOWN_ERROR;
            this.errorMessage = ErrorCode.getErrorMessage(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    /**
     * Error response constructor
     * 
     * @param errorCode Error code
     */
    public BaseResponse(int errorCode) {
        this.isSuccess = ErrorCode.isSuccess(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = ErrorCode.getErrorMessage(errorCode);
    }
    
    /**
     * Complete constructor
     * 
     * @param isSuccess Whether successful
     * @param errorCode Error code
     * @param errorMessage Error message
     */
    public BaseResponse(boolean isSuccess, int errorCode, String errorMessage) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * Get whether the operation is successful
     * 
     * @return Whether successful
     */
    public boolean isSuccess() {
        return isSuccess;
    }
    
    /**
     * Set whether the operation is successful
     * 
     * @param success Whether successful
     */
    public void setSuccess(boolean success) {
        this.isSuccess = success;
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
     * Set error code
     * 
     * @param errorCode Error code
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        this.isSuccess = ErrorCode.isSuccess(errorCode);
        if (this.errorMessage == null) {
            this.errorMessage = ErrorCode.getErrorMessage(errorCode);
        }
    }
    
    /**
     * Get error message
     * 
     * @return Error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Set error message
     * 
     * @param errorMessage Error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Set error status
     * 
     * @param errorCode Error code
     * @param errorMessage Error message
     */
    public void setError(int errorCode, String errorMessage) {
        this.isSuccess = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * Set success status
     */
    public void setSuccess() {
        this.isSuccess = true;
        this.errorCode = ErrorCode.SUCCESS;
        this.errorMessage = null;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "isSuccess=" + isSuccess +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}