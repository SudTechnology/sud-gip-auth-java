package com.sud.gip.auth.exception;

import com.sud.gip.auth.constant.ErrorCode;

/**
 * Sud GIP Auth 基础异常类
 * 所有Sud GIP Auth相关异常的基类
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SudGIPAuthException extends Exception {
    
    /**
     * 错误码
     */
    private final int errorCode;
    
    /**
     * 构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public SudGIPAuthException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误信息
     * @param cause 原因异常
     */
    public SudGIPAuthException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数（使用默认错误信息）
     * 
     * @param errorCode 错误码
     */
    public SudGIPAuthException(int errorCode) {
        super(ErrorCode.getErrorMessage(errorCode));
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数（使用默认错误信息）
     * 
     * @param errorCode 错误码
     * @param cause 原因异常
     */
    public SudGIPAuthException(int errorCode, Throwable cause) {
        super(ErrorCode.getErrorMessage(errorCode), cause);
        this.errorCode = errorCode;
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
     * 检查是否为指定错误码
     * 
     * @param errorCode 要检查的错误码
     * @return 是否匹配
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