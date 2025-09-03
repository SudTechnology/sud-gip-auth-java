package com.sud.gip.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sud.gip.auth.constant.ErrorCode;
import com.sud.gip.auth.exception.TokenGenerationException;
import com.sud.gip.auth.exception.TokenValidationException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密工具类
 * 提供JWT处理、HMAC签名、Base64编解码等功能
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public final class CryptoUtils {
    
    /**
     * HMAC-SHA256算法名称
     */
    private static final String HMAC_SHA256 = "HmacSHA256";
    
    /**
     * JWT头部信息
     */
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    
    /**
     * JSON对象映射器
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    /**
     * 私有构造函数，防止实例化
     */
    private CryptoUtils() {
        throw new AssertionError("CryptoUtils class should not be instantiated");
    }
    
    /**
     * 生成JWT令牌
     * 
     * @param uid 用户ID
     * @param appId 应用ID
     * @param expireTime 过期时间（毫秒时间戳）
     * @param secret 密钥
     * @return JWT令牌
     * @throws TokenGenerationException 令牌生成异常
     */
    public static String generateJWT(String uid, String appId, long expireTime, String secret) 
            throws TokenGenerationException {
        try {
            // 构建JWT头部
            String header = base64UrlEncode(JWT_HEADER.getBytes(StandardCharsets.UTF_8));
            
            // 构建JWT载荷
            Map<String, Object> payload = new HashMap<>();
            payload.put("uid", uid);
            payload.put("app_id", appId);
            payload.put("exp", expireTime / 1000); // JWT使用秒级时间戳
            payload.put("iat", System.currentTimeMillis() / 1000); // 签发时间
            
            String payloadJson = OBJECT_MAPPER.writeValueAsString(payload);
            String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
            
            // 构建待签名字符串
            String signData = header + "." + encodedPayload;
            
            // 生成签名
            String signature = hmacSha256(signData, secret);
            String encodedSignature = base64UrlEncode(signature.getBytes(StandardCharsets.UTF_8));
            
            // 组装JWT
            return signData + "." + encodedSignature;
            
        } catch (Exception e) {
            throw new TokenGenerationException("Failed to generate JWT token", e);
        }
    }
    
    /**
     * 验证并解析JWT令牌
     * 
     * @param token JWT令牌
     * @param secret 密钥
     * @return 解析后的载荷信息
     * @throws TokenValidationException 令牌验证异常
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> verifyAndParseJWT(String token, String secret) 
            throws TokenValidationException {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new TokenValidationException("Token is null or empty");
            }
            
            // 分割JWT
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new TokenValidationException("Invalid JWT format");
            }
            
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];
            
            // 验证签名
            String signData = header + "." + payload;
            String expectedSignature = hmacSha256(signData, secret);
            String expectedEncodedSignature = base64UrlEncode(expectedSignature.getBytes(StandardCharsets.UTF_8));
            
            if (!signature.equals(expectedEncodedSignature)) {
                throw new TokenValidationException("Invalid token signature");
            }
            
            // 解析载荷
            byte[] payloadBytes = base64UrlDecode(payload);
            String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);
            Map<String, Object> payloadMap = OBJECT_MAPPER.readValue(payloadJson, Map.class);
            
            // 检查过期时间
            Object expObj = payloadMap.get("exp");
            if (expObj != null) {
                long exp = ((Number) expObj).longValue();
                long currentTime = System.currentTimeMillis() / 1000;
                if (currentTime > exp) {
                    throw new TokenValidationException("Token has expired");
                }
            }
            
            return payloadMap;
            
        } catch (TokenValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new TokenValidationException("Failed to verify and parse JWT token", e);
        }
    }
    
    /**
     * HMAC-SHA256签名
     * 
     * @param data 待签名数据
     * @param secret 密钥
     * @return 签名结果（十六进制字符串）
     * @throws TokenGenerationException 签名异常
     */
    public static String hmacSha256(String data, String secret) throws TokenGenerationException {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] signBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // 转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : signBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new TokenGenerationException("Failed to generate HMAC-SHA256 signature", e);
        }
    }
    
    /**
     * Base64 URL安全编码
     * 
     * @param data 待编码数据
     * @return 编码结果
     */
    public static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
    
    /**
     * Base64 URL安全解码
     * 
     * @param data 待解码数据
     * @return 解码结果
     */
    public static byte[] base64UrlDecode(String data) {
        return Base64.getUrlDecoder().decode(data);
    }
    
    /**
     * 生成认证码
     * 
     * @param uid 用户ID
     * @param appId 应用ID
     * @param expireSeconds 过期时间（秒）
     * @param secret 密钥
     * @return 认证码
     * @throws TokenGenerationException 令牌生成异常
     */
    public static String generateCode(String uid, String appId, long expireSeconds, String secret) 
            throws TokenGenerationException {
        long expireTime = System.currentTimeMillis() + (expireSeconds * 1000);
        return generateJWT(uid, appId, expireTime, secret);
    }
    
    /**
     * 生成SSToken
     * 
     * @param uid 用户ID
     * @param appId 应用ID
     * @param expireSeconds 过期时间（秒）
     * @param secret 密钥
     * @return SSToken
     * @throws TokenGenerationException 令牌生成异常
     */
    public static String generateSSToken(String uid, String appId, long expireSeconds, String secret) 
            throws TokenGenerationException {
        long expireTime = System.currentTimeMillis() + (expireSeconds * 1000);
        return generateJWT(uid, appId, expireTime, secret);
    }
    
    /**
     * 从令牌中提取用户ID
     * 
     * @param token 令牌
     * @param secret 密钥
     * @return 用户ID
     * @throws TokenValidationException 令牌验证异常
     */
    public static String extractUidFromToken(String token, String secret) throws TokenValidationException {
        Map<String, Object> payload = verifyAndParseJWT(token, secret);
        Object uid = payload.get("uid");
        if (uid == null) {
            throw new TokenValidationException("UID not found in token");
        }
        return uid.toString();
    }
    
    /**
     * 检查令牌是否过期
     * 
     * @param token 令牌
     * @param secret 密钥
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token, String secret) {
        try {
            verifyAndParseJWT(token, secret);
            return false;
        } catch (TokenValidationException e) {
            // 对于无效令牌，我们也认为它是"过期"的（不可用的）
            return true;
        }
    }
}