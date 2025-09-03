package com.sud.gip.auth.constant;

/**
 * Sud GIP Auth 错误码常量定义
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public final class ErrorCode {
    
    /**
     * 成功
     */
    public static final int SUCCESS = 0;
    
    /**
     * 令牌创建失败
     */
    public static final int TOKEN_CREATION_FAILED = 1001;
    
    /**
     * 令牌验证失败
     */
    public static final int TOKEN_VERIFICATION_FAILED = 1002;
    
    /**
     * 令牌解码失败
     */
    public static final int TOKEN_DECODING_FAILED = 1003;
    
    /**
     * 令牌无效
     */
    public static final int TOKEN_INVALID = 1004;
    
    /**
     * 令牌已过期
     */
    public static final int TOKEN_EXPIRED = 1005;
    
    /**
     * 应用数据无效
     */
    public static final int APP_DATA_INVALID = 1101;
    
    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 9999;
    
    /**
     * 私有构造函数，防止实例化
     */
    private ErrorCode() {
        throw new AssertionError("ErrorCode class should not be instantiated");
    }
    
    /**
     * 获取错误码对应的描述信息
     * 
     * @param errorCode 错误码
     * @return 错误描述
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
     * 检查错误码是否表示成功
     * 
     * @param errorCode 错误码
     * @return 是否成功
     */
    public static boolean isSuccess(int errorCode) {
        return errorCode == SUCCESS;
    }
}