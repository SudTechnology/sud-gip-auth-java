package tech.sud.auth.gip.auth;

import tech.sud.auth.gip.auth.constant.ErrorCode;
import tech.sud.auth.gip.auth.exception.TokenGenerationException;
import tech.sud.auth.gip.auth.exception.TokenValidationException;
import tech.sud.auth.gip.auth.model.CodeResponse;
import tech.sud.auth.gip.auth.model.SSTokenResponse;
import tech.sud.auth.gip.auth.model.UidResponse;
import tech.sud.auth.gip.auth.util.CryptoUtils;


import java.util.Date;

/**
 * Sud GIP Auth Java SDK main class
 * Provides complete JWT authentication functionality including token generation, validation and user ID retrieval
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SudGIPAuth {
    
    /**
     * Default authentication code expiration time (seconds)
     */
    private static final long DEFAULT_CODE_EXPIRE_SECONDS = 3600L; // 1 hour
    
    /**
     * Default SSToken expiration time (seconds)
     */
    private static final long DEFAULT_SSTOKEN_EXPIRE_SECONDS = 7200L; // 2 hours
    
    /**
     * Application ID
     */
    private final String appId;
    
    /**
     * Application secret
     */
    private final String appSecret;
    

    /**
     * Constructor
     * 
     * @param appId Application ID
     * @param appSecret Application secret
     * @throws IllegalArgumentException Invalid parameter exception
     */
    public SudGIPAuth(String appId, String appSecret) {
        if (appId == null || appId.trim().isEmpty()) {
            throw new IllegalArgumentException("App ID cannot be null or empty");
        }
        if (appSecret == null || appSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("App Secret cannot be null or empty");
        }
        
        this.appId = appId.trim();
        this.appSecret = appSecret.trim();
    }
    
    /**
     * Generate authentication code (using default expiration time)
     * 
     * @param uid User ID
     * @return Authentication code response
     */
    public CodeResponse getCode(String uid) {
        return getCode(uid, DEFAULT_CODE_EXPIRE_SECONDS);
    }
    
    /**
     * Generate authentication code (with custom expiration time)
     * 
     * @param uid User ID
     * @param expireSeconds Expiration time (seconds)
     * @return Authentication code response
     */
    public CodeResponse getCode(String uid, long expireSeconds) {
        try {
            // Parameter validation
            if (uid == null || uid.trim().isEmpty()) {
                return CodeResponse.error(ErrorCode.APP_DATA_INVALID, "User ID cannot be null or empty");
            }
            
            if (expireSeconds <= 0) {
                return CodeResponse.error(ErrorCode.APP_DATA_INVALID, "Expire seconds must be positive");
            }
            
            // Generate authentication code
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
     * Generate SSToken (using default expiration time)
     * 
     * @param uid User ID
     * @return SSToken response
     */
    public SSTokenResponse getSSToken(String uid) {
        return getSSToken(uid, DEFAULT_SSTOKEN_EXPIRE_SECONDS);
    }
    
    /**
     * Generate SSToken (with custom expiration time)
     * 
     * @param uid User ID
     * @param expireSeconds Expiration time (seconds)
     * @return SSToken response
     */
    public SSTokenResponse getSSToken(String uid, long expireSeconds) {
        try {
            // Parameter validation
            if (uid == null || uid.trim().isEmpty()) {
                return SSTokenResponse.error(ErrorCode.APP_DATA_INVALID, "User ID cannot be null or empty");
            }
            
            if (expireSeconds <= 0) {
                return SSTokenResponse.error(ErrorCode.APP_DATA_INVALID, "Expire seconds must be positive");
            }
            
            // Generate SSToken
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
     * Get user ID by authentication code
     * 
     * @param code Authentication code
     * @return User ID response
     */
    public UidResponse getUidByCode(String code) {
        try {
            // Parameter validation
            if (code == null || code.trim().isEmpty()) {
                return UidResponse.error(ErrorCode.TOKEN_INVALID, "Code cannot be null or empty");
            }
            
            // Extract user ID
            String uid = CryptoUtils.extractUidFromToken(code.trim(), appSecret);
            
            return UidResponse.success(uid);
            
        } catch (TokenValidationException e) {
            // Determine specific error code based on exception message
            int errorCode = determineErrorCode(e.getMessage());
            return UidResponse.error(errorCode, e.getMessage());
        } catch (Exception e) {
            return UidResponse.error(ErrorCode.UNKNOWN_ERROR, "Failed to get UID by code: " + e.getMessage());
        }
    }
    
    /**
     * Get user ID by SSToken
     * 
     * @param ssToken SSToken
     * @return User ID response
     */
    public UidResponse getUidBySSToken(String ssToken) {
        try {
            // Parameter validation
            if (ssToken == null || ssToken.trim().isEmpty()) {
                return UidResponse.error(ErrorCode.TOKEN_INVALID, "SSToken cannot be null or empty");
            }
            
            // Extract user ID
            String uid = CryptoUtils.extractUidFromToken(ssToken.trim(), appSecret);
            
            return UidResponse.success(uid);
            
        } catch (TokenValidationException e) {
            // Determine specific error code based on exception message
            int errorCode = determineErrorCode(e.getMessage());
            return UidResponse.error(errorCode, e.getMessage());
        } catch (Exception e) {
            return UidResponse.error(ErrorCode.UNKNOWN_ERROR, "Failed to get UID by SSToken: " + e.getMessage());
        }
    }
    
    /**
     * Determine error code based on exception message
     * 
     * @param message Exception message
     * @return Error code
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
     * Check if token is expired
     * 
     * @param token Token (authentication code or SSToken)
     * @return Whether expired
     */
    public boolean isTokenExpired(String token) {
        if (token == null || token.trim().isEmpty()) {
            return true;
        }
        
        return CryptoUtils.isTokenExpired(token.trim(), appSecret);
    }
    
    /**
     * Get application ID
     * 
     * @return Application ID
     */
    public String getAppId() {
        return appId;
    }
    

    @Override
    public String toString() {
        return "SudGIPAuth{" +
                "appId='" + appId + '\'' +
                '}';
    }
}