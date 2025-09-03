package com.sud.gip.auth.model;

/**
 * 用户ID响应类
 * 用于返回通过令牌获取的用户ID
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class UidResponse extends BaseResponse {
    
    /**
     * 用户ID
     */
    private String uid;
    
    /**
     * 默认构造函数
     */
    public UidResponse() {
        super();
    }
    
    /**
     * 成功响应构造函数
     * 
     * @param uid 用户ID
     */
    public UidResponse(String uid) {
        super();
        this.uid = uid;
    }
    
    /**
     * 错误响应构造函数
     * 
     * @param errorCode 错误码
     */
    public UidResponse(int errorCode) {
        super(errorCode);
    }
    
    /**
     * 完整构造函数
     * 
     * @param isSuccess 是否成功
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @param uid 用户ID
     */
    public UidResponse(boolean isSuccess, int errorCode, String errorMessage, String uid) {
        super(isSuccess, errorCode, errorMessage);
        this.uid = uid;
    }
    
    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public String getUid() {
        return uid;
    }
    
    /**
     * 设置用户ID
     * 
     * @param uid 用户ID
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    /**
     * 创建成功响应
     * 
     * @param uid 用户ID
     * @return UidResponse实例
     */
    public static UidResponse success(String uid) {
        return new UidResponse(uid);
    }
    
    /**
     * 创建错误响应
     * 
     * @param errorCode 错误码
     * @return UidResponse实例
     */
    public static UidResponse error(int errorCode) {
        return new UidResponse(errorCode);
    }
    
    /**
     * 创建错误响应
     * 
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @return UidResponse实例
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