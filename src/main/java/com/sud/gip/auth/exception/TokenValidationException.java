package com.sud.gip.auth.exception;

import com.sud.gip.auth.constant.ErrorCode;

/**
 * 令牌验证异常类
 * 当令牌验证失败时抛出此异常
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class TokenValidationException extends SudGIPAuthException {
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public TokenValidationException(String message) {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED, message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public TokenValidationException(String message, Throwable cause) {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED, message, cause);
    }
    
    /**
     * 构造函数（使用默认错误信息）
     */
    public TokenValidationException() {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED);
    }
    
    /**
     * 构造函数（使用默认错误信息）
     * 
     * @param cause 原因异常
     */
    public TokenValidationException(Throwable cause) {
        super(ErrorCode.TOKEN_VERIFICATION_FAILED, cause);
    }
}