package com.sud.gip.auth;

import com.sud.gip.auth.constant.ErrorCode;
import com.sud.gip.auth.exception.SudGIPAuthException;
import com.sud.gip.auth.exception.TokenGenerationException;
import com.sud.gip.auth.exception.TokenValidationException;
import com.sud.gip.auth.model.CodeResponse;
import com.sud.gip.auth.model.SSTokenResponse;
import com.sud.gip.auth.model.UidResponse;
import com.sud.gip.auth.util.CryptoUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Date;

/**
 * Sud GIP Auth Java SDK 主类
 * 提供完整的JWT认证功能，包括令牌生成、验证和用户ID获取
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SudGIPAuth {
    
    /**
     * 默认认证码过期时间（秒）
     */
    private static final long DEFAULT_CODE_EXPIRE_SECONDS = 3600L; // 1小时
    
    /**
     * 默认SSToken过期时间（秒）
     */
    private static final long DEFAULT_SSTOKEN_EXPIRE_SECONDS = 7200L; // 2小时
    
    /**
     * 应用ID
     */
    private final String appId;
    
    /**
     * 应用密钥
     */
    private final String appSecret;
    
    /**
     * 基础URL（预留用于未来扩展）
     */
    private final String baseUrl;
    
    /**
     * HTTP客户端（预留用于未来扩展）
     */
    private final HttpClient httpClient;
    
    /**
     * 构造函数
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @throws IllegalArgumentException 参数无效异常
     */
    public SudGIPAuth(String appId, String appSecret) {
        this(appId, appSecret, "https://api.sud.tech/gip", HttpClients.createDefault());
    }
    
    /**
     * 构造函数（自定义基础URL）
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param baseUrl 基础URL
     * @throws IllegalArgumentException 参数无效异常
     */
    public SudGIPAuth(String appId, String appSecret, String baseUrl) {
        this(appId, appSecret, baseUrl, HttpClients.createDefault());
    }
    
    /**
     * 构造函数（自定义HTTP客户端）
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param httpClient HTTP客户端
     * @throws IllegalArgumentException 参数无效异常
     */
    public SudGIPAuth(String appId, String appSecret, HttpClient httpClient) {
        this(appId, appSecret, "https://api.sud.tech/gip", httpClient);
    }
    
    /**
     * 完整构造函数
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param baseUrl 基础URL
     * @param httpClient HTTP客户端
     * @throws IllegalArgumentException 参数无效异常
     */
    public SudGIPAuth(String appId, String appSecret, String baseUrl, HttpClient httpClient) {
        if (appId == null || appId.trim().isEmpty()) {
            throw new IllegalArgumentException("App ID cannot be null or empty");
        }
        if (appSecret == null || appSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("App Secret cannot be null or empty");
        }
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null or empty");
        }
        if (httpClient == null) {
            throw new IllegalArgumentException("HTTP Client cannot be null");
        }
        
        this.appId = appId.trim();
        this.appSecret = appSecret.trim();
        this.baseUrl = baseUrl.trim();
        this.httpClient = httpClient;
    }
    
    /**
     * 生成认证码（使用默认过期时间）
     * 
     * @param uid 用户ID
     * @return 认证码响应
     */
    public CodeResponse getCode(String uid) {
        return getCode(uid, DEFAULT_CODE_EXPIRE_SECONDS);
    }
    
    /**
     * 生成认证码（自定义过期时间）
     * 
     * @param uid 用户ID
     * @param expireSeconds 过期时间（秒）
     * @return 认证码响应
     */
    public CodeResponse getCode(String uid, long expireSeconds) {
        try {
            // 参数验证
            if (uid == null || uid.trim().isEmpty()) {
                return CodeResponse.error(ErrorCode.APP_DATA_INVALID, "User ID cannot be null or empty");
            }
            
            if (expireSeconds <= 0) {
                return CodeResponse.error(ErrorCode.APP_DATA_INVALID, "Expire seconds must be positive");
            }
            
            // 生成认证码
            String code = CryptoUtils.generateCode(uid.trim(), appId, expireSeconds, appSecret);
            Date expireDate = new Date(System.currentTimeMillis() + (expireSeconds * 1000));
            
            return CodeResponse.success(code, expireDate);
            
        } catch (TokenGenerationException e) {
            return CodeResponse.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return CodeResponse.error(ErrorCode.UNKNOWN_ERROR, "Failed to generate code: " + e.getMessage());
        }
    }
    
    /**
     * 生成SSToken（使用默认过期时间）
     * 
     * @param uid 用户ID
     * @return SSToken响应
     */
    public SSTokenResponse getSSToken(String uid) {
        return getSSToken(uid, DEFAULT_SSTOKEN_EXPIRE_SECONDS);
    }
    
    /**
     * 生成SSToken（自定义过期时间）
     * 
     * @param uid 用户ID
     * @param expireSeconds 过期时间（秒）
     * @return SSToken响应
     */
    public SSTokenResponse getSSToken(String uid, long expireSeconds) {
        try {
            // 参数验证
            if (uid == null || uid.trim().isEmpty()) {
                return SSTokenResponse.error(ErrorCode.APP_DATA_INVALID, "User ID cannot be null or empty");
            }
            
            if (expireSeconds <= 0) {
                return SSTokenResponse.error(ErrorCode.APP_DATA_INVALID, "Expire seconds must be positive");
            }
            
            // 生成SSToken
            String token = CryptoUtils.generateSSToken(uid.trim(), appId, expireSeconds, appSecret);
            Date expireDate = new Date(System.currentTimeMillis() + (expireSeconds * 1000));
            
            return SSTokenResponse.success(token, expireDate);
            
        } catch (TokenGenerationException e) {
            return SSTokenResponse.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            return SSTokenResponse.error(ErrorCode.UNKNOWN_ERROR, "Failed to generate SSToken: " + e.getMessage());
        }
    }
    
    /**
     * 通过认证码获取用户ID
     * 
     * @param code 认证码
     * @return 用户ID响应
     */
    public UidResponse getUidByCode(String code) {
        try {
            // 参数验证
            if (code == null || code.trim().isEmpty()) {
                return UidResponse.error(ErrorCode.TOKEN_INVALID, "Code cannot be null or empty");
            }
            
            // 提取用户ID
            String uid = CryptoUtils.extractUidFromToken(code.trim(), appSecret);
            
            return UidResponse.success(uid);
            
        } catch (TokenValidationException e) {
            // 根据异常信息确定具体错误码
            int errorCode = determineErrorCode(e.getMessage());
            return UidResponse.error(errorCode, e.getMessage());
        } catch (Exception e) {
            return UidResponse.error(ErrorCode.UNKNOWN_ERROR, "Failed to get UID by code: " + e.getMessage());
        }
    }
    
    /**
     * 通过SSToken获取用户ID
     * 
     * @param ssToken SSToken
     * @return 用户ID响应
     */
    public UidResponse getUidBySSToken(String ssToken) {
        try {
            // 参数验证
            if (ssToken == null || ssToken.trim().isEmpty()) {
                return UidResponse.error(ErrorCode.TOKEN_INVALID, "SSToken cannot be null or empty");
            }
            
            // 提取用户ID
            String uid = CryptoUtils.extractUidFromToken(ssToken.trim(), appSecret);
            
            return UidResponse.success(uid);
            
        } catch (TokenValidationException e) {
            // 根据异常信息确定具体错误码
            int errorCode = determineErrorCode(e.getMessage());
            return UidResponse.error(errorCode, e.getMessage());
        } catch (Exception e) {
            return UidResponse.error(ErrorCode.UNKNOWN_ERROR, "Failed to get UID by SSToken: " + e.getMessage());
        }
    }
    
    /**
     * 根据异常信息确定错误码
     * 
     * @param message 异常信息
     * @return 错误码
     */
    private int determineErrorCode(String message) {
        if (message == null) {
            return ErrorCode.UNKNOWN_ERROR;
        }
        
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("expired")) {
            return ErrorCode.TOKEN_EXPIRED;
        } else if (lowerMessage.contains("invalid") || lowerMessage.contains("format")) {
            return ErrorCode.TOKEN_INVALID;
        } else if (lowerMessage.contains("signature")) {
            return ErrorCode.TOKEN_VERIFICATION_FAILED;
        } else if (lowerMessage.contains("decode") || lowerMessage.contains("parse")) {
            return ErrorCode.TOKEN_DECODING_FAILED;
        } else {
            return ErrorCode.TOKEN_VERIFICATION_FAILED;
        }
    }
    
    /**
     * 检查令牌是否过期
     * 
     * @param token 令牌（认证码或SSToken）
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        if (token == null || token.trim().isEmpty()) {
            return true;
        }
        
        return CryptoUtils.isTokenExpired(token.trim(), appSecret);
    }
    
    /**
     * 获取应用ID
     * 
     * @return 应用ID
     */
    public String getAppId() {
        return appId;
    }
    
    /**
     * 获取基础URL
     * 
     * @return 基础URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }
    
    /**
     * 获取HTTP客户端
     * 
     * @return HTTP客户端
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }
    
    @Override
    public String toString() {
        return "SudGIPAuth{" +
                "appId='" + appId + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                '}';
    }
}