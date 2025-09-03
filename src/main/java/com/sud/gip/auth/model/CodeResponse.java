package com.sud.gip.auth.model;

import java.util.Date;

/**
 * 认证码响应类
 * 用于返回生成的认证码和相关信息
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class CodeResponse extends BaseResponse {
    
    /**
     * 生成的认证码
     */
    private String code;
    
    /**
     * 认证码过期时间
     */
    private Date expireDate;
    
    /**
     * 默认构造函数
     */
    public CodeResponse() {
        super();
    }
    
    /**
     * 成功响应构造函数
     * 
     * @param code 认证码
     * @param expireDate 过期时间
     */
    public CodeResponse(String code, Date expireDate) {
        super();
        this.code = code;
        this.expireDate = expireDate;
    }
    
    /**
     * 错误响应构造函数
     * 
     * @param errorCode 错误码
     */
    public CodeResponse(int errorCode) {
        super(errorCode);
    }
    
    /**
     * 完整构造函数
     * 
     * @param isSuccess 是否成功
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @param code 认证码
     * @param expireDate 过期时间
     */
    public CodeResponse(boolean isSuccess, int errorCode, String errorMessage, String code, Date expireDate) {
        super(isSuccess, errorCode, errorMessage);
        this.code = code;
        this.expireDate = expireDate;
    }
    
    /**
     * 获取认证码
     * 
     * @return 认证码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 设置认证码
     * 
     * @param code 认证码
     */
    public void setCode(String code) {
        this.code = code;
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
     * @param code 认证码
     * @param expireDate 过期时间
     * @return CodeResponse实例
     */
    public static CodeResponse success(String code, Date expireDate) {
        return new CodeResponse(code, expireDate);
    }
    
    /**
     * 创建错误响应
     * 
     * @param errorCode 错误码
     * @return CodeResponse实例
     */
    public static CodeResponse error(int errorCode) {
        return new CodeResponse(errorCode);
    }
    
    /**
     * 创建错误响应
     * 
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @return CodeResponse实例
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