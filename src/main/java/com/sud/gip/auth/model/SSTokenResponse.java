package com.sud.gip.auth.model;

import java.util.Date;

/**
 * 服务器间令牌响应类
 * 用于返回生成的SSToken和相关信息
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SSTokenResponse extends BaseResponse {
    
    /**
     * 生成的SSToken
     */
    private String token;
    
    /**
     * SSToken过期时间
     */
    private Date expireDate;
    
    /**
     * 默认构造函数
     */
    public SSTokenResponse() {
        super();
    }
    
    /**
     * 成功响应构造函数
     * 
     * @param token SSToken
     * @param expireDate 过期时间
     */
    public SSTokenResponse(String token, Date expireDate) {
        super();
        this.token = token;
        this.expireDate = expireDate;
    }
    
    /**
     * 错误响应构造函数
     * 
     * @param errorCode 错误码
     */
    public SSTokenResponse(int errorCode) {
        super(errorCode);
    }
    
    /**
     * 完整构造函数
     * 
     * @param isSuccess 是否成功
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @param token SSToken
     * @param expireDate 过期时间
     */
    public SSTokenResponse(boolean isSuccess, int errorCode, String errorMessage, String token, Date expireDate) {
        super(isSuccess, errorCode, errorMessage);
        this.token = token;
        this.expireDate = expireDate;
    }
    
    /**
     * 获取SSToken
     * 
     * @return SSToken
     */
    public String getToken() {
        return token;
    }
    
    /**
     * 设置SSToken
     * 
     * @param token SSToken
     */
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * 获取过期时间
     * 
     * @return 过期时间
     */
    public Date getExpireDate() {
        return expireDate;
    }
    
    /**
     * 设置过期时间
     * 
     * @param expireDate 过期时间
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    
    /**
     * 创建成功响应
     * 
     * @param token SSToken
     * @param expireDate 过期时间
     * @return SSTokenResponse实例
     */
    public static SSTokenResponse success(String token, Date expireDate) {
        return new SSTokenResponse(token, expireDate);
    }
    
    /**
     * 创建错误响应
     * 
     * @param errorCode 错误码
     * @return SSTokenResponse实例
     */
    public static SSTokenResponse error(int errorCode) {
        return new SSTokenResponse(errorCode);
    }
    
    /**
     * 创建错误响应
     * 
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @return SSTokenResponse实例
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