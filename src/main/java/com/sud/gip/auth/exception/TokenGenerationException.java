package com.sud.gip.auth.exception;

import com.sud.gip.auth.constant.ErrorCode;

/**
 * 令牌生成异常类
 * 当令牌生成失败时抛出此异常
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class TokenGenerationException extends SudGIPAuthException {
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public TokenGenerationException(String message) {
        super(ErrorCode.TOKEN_CREATION_FAILED, message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 原因异常
     */
    public TokenGenerationException(String message, Throwable cause) {
        super(ErrorCode.TOKEN_CREATION_FAILED, message, cause);
    }
    
    /**
     * 构造函数（使用默认错误信息）
     */
    public TokenGenerationException() {
        super(ErrorCode.TOKEN_CREATION_FAILED);
    }
    
    /**
     * 构造函数（使用默认错误信息）
     * 
     * @param cause 原因异常
     */
    public TokenGenerationException(Throwable cause) {
        super(ErrorCode.TOKEN_CREATION_FAILED, cause);
    }
}