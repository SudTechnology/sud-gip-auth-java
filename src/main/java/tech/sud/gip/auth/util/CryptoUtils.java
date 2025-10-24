package tech.sud.gip.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.sud.gip.auth.constant.ErrorCode;
import tech.sud.gip.auth.exception.TokenGenerationException;
import tech.sud.gip.auth.exception.TokenValidationException;

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
 * Cryptographic utility class
 * Provides JWT processing, HMAC signing, Base64 encoding/decoding and other functions
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public final class CryptoUtils {
    
    /**
     * HMAC-SHA256 algorithm name
     */
    private static final String HMAC_SHA256 = "HmacSHA256";
    
    /**
     * JWT header information
     */
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    
    /**
     * JSON object mapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    /**
     * Private constructor to prevent instantiation
     */
    private CryptoUtils() {
        throw new AssertionError("CryptoUtils class should not be instantiated");
    }
    
    /**
     * Generate JWT token
     * 
     * @param uid User ID
     * @param appId Application ID
     * @param expireTime Expiration time (millisecond timestamp)
     * @param secret Secret key
     * @return JWT token
     * @throws TokenGenerationException Token generation exception
     */
    public static String generateJWT(String uid, String appId, long expireTime, String secret) 
            throws TokenGenerationException {
        try {
            // Build JWT header
            String header = base64UrlEncode(JWT_HEADER.getBytes(StandardCharsets.UTF_8));
            
            // Build JWT payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("uid", uid);
            payload.put("app_id", appId);
            payload.put("exp", expireTime / 1000); // JWT uses second-level timestamp
            payload.put("iat", System.currentTimeMillis() / 1000); // Issued time
            
            String payloadJson = OBJECT_MAPPER.writeValueAsString(payload);
            String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
            
            // Build string to be signed
            String signData = header + "." + encodedPayload;
            
            // Generate signature
            String signature = hmacSha256(signData, secret);
            String encodedSignature = base64UrlEncode(signature.getBytes(StandardCharsets.UTF_8));
            
            // Assemble JWT
            return signData + "." + encodedSignature;
            
        } catch (Exception e) {
            throw new TokenGenerationException("Failed to generate JWT token", e);
        }
    }
    
    /**
     * Verify and parse JWT token
     * 
     * @param token JWT token
     * @param secret Secret key
     * @return Parsed payload information
     * @throws TokenValidationException Token validation exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> verifyAndParseJWT(String token, String secret) 
            throws TokenValidationException {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new TokenValidationException("Token is null or empty");
            }
            
            // Split JWT
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new TokenValidationException("Invalid JWT format");
            }
            
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];
            
            // Verify signature
            String signData = header + "." + payload;
            String expectedSignature = hmacSha256(signData, secret);
            String expectedEncodedSignature = base64UrlEncode(expectedSignature.getBytes(StandardCharsets.UTF_8));
            
            if (!signature.equals(expectedEncodedSignature)) {
                throw new TokenValidationException("Invalid token signature");
            }
            
            // Parse payload
            byte[] payloadBytes = base64UrlDecode(payload);
            String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);
            Map<String, Object> payloadMap = OBJECT_MAPPER.readValue(payloadJson, Map.class);
            
            // Check expiration time
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
     * HMAC-SHA256 signature
     * 
     * @param data Data to be signed
     * @param secret Secret key
     * @return Signature result (hexadecimal string)
     * @throws TokenGenerationException Signature exception
     */
    public static String hmacSha256(String data, String secret) throws TokenGenerationException {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] signBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // Convert to hexadecimal string
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
     * Base64 URL-safe encoding
     * 
     * @param data Data to be encoded
     * @return Encoding result
     */
    public static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
    
    /**
     * Base64 URL-safe decoding
     * 
     * @param data Data to be decoded
     * @return Decoding result
     */
    public static byte[] base64UrlDecode(String data) {
        return Base64.getUrlDecoder().decode(data);
    }
    
    /**
     * Generate authentication code
     * 
     * @param uid User ID
     * @param appId Application ID
     * @param expireSeconds Expiration time (seconds)
     * @param secret Secret key
     * @return Authentication code
     * @throws TokenGenerationException Token generation exception
     */
    public static String generateCode(String uid, String appId, long expireSeconds, String secret) 
            throws TokenGenerationException {
        long expireTime = System.currentTimeMillis() + (expireSeconds * 1000);
        return generateJWT(uid, appId, expireTime, secret);
    }
    
    /**
     * Generate SSToken
     * 
     * @param uid User ID
     * @param appId Application ID
     * @param expireSeconds Expiration time (seconds)
     * @param secret Secret key
     * @return SSToken
     * @throws TokenGenerationException Token generation exception
     */
    public static String generateSSToken(String uid, String appId, long expireSeconds, String secret) 
            throws TokenGenerationException {
        long expireTime = System.currentTimeMillis() + (expireSeconds * 1000);
        return generateJWT(uid, appId, expireTime, secret);
    }
    
    /**
     * Extract user ID from token
     * 
     * @param token Token
     * @param secret Secret key
     * @return User ID
     * @throws TokenValidationException Token validation exception
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
     * Check if token is expired
     * 
     * @param token Token
     * @param secret Secret key
     * @return Whether expired
     */
    public static boolean isTokenExpired(String token, String secret) {
        try {
            verifyAndParseJWT(token, secret);
            return false;
        } catch (TokenValidationException e) {
            // For invalid tokens, we also consider them as "expired" (unavailable)
            return true;
        }
    }
}