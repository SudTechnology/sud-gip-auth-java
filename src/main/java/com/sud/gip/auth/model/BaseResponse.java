package com.sud.gip.auth.model;

import com.sud.gip.auth.constant.ErrorCode;

/**
 * Sud GIP Auth 基础响应类
 * 所有响应类的基类，包含通用的状态信息
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public abstract class BaseResponse {
    
    /**
     * 操作是否成功
     */
    private boolean isSuccess;
    
    /**
     * 错误码
     */
    private int errorCode;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 默认构造函数
     */
    public BaseResponse() {
        this.isSuccess = true;
        this.errorCode = ErrorCode.SUCCESS;
        this.errorMessage = null;
    }
    
    /**
     * 成功响应构造函数
     * 
     * @param isSuccess 是否成功
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
     * 错误响应构造函数
     * 
     * @param errorCode 错误码
     */
    public BaseResponse(int errorCode) {
        this.isSuccess = ErrorCode.isSuccess(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = ErrorCode.getErrorMessage(errorCode);
    }
    
    /**
     * 完整构造函数
     * 
     * @param isSuccess 是否成功
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    public BaseResponse(boolean isSuccess, int errorCode, String errorMessage) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * 获取操作是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return isSuccess;
    }
    
    /**
     * 设置操作是否成功
     * 
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public int getErrorCode() {
        return errorCode;
    }
    
    /**
     * 设置错误码
     * 
     * @param errorCode 错误码
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        this.isSuccess = ErrorCode.isSuccess(errorCode);
        if (this.errorMessage == null) {
            this.errorMessage = ErrorCode.getErrorMessage(errorCode);
        }
    }
    
    /**
     * 获取错误信息
     * 
     * @return 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * 设置错误信息
     * 
     * @param errorMessage 错误信息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * 设置错误状态
     * 
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    public void setError(int errorCode, String errorMessage) {
        this.isSuccess = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * 设置成功状态
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