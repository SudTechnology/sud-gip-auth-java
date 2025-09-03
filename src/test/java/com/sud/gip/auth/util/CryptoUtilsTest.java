package com.sud.gip.auth.util;

import com.sud.gip.auth.exception.TokenGenerationException;
import com.sud.gip.auth.exception.TokenValidationException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * CryptoUtils 单元测试类
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class CryptoUtilsTest {
    
    private static final String TEST_UID = "test_user_123";
    private static final String TEST_APP_ID = "test_app_id";
    private static final String TEST_SECRET = "test_secret_key";
    
    @Test
    public void testGenerateJWT() throws TokenGenerationException {
        long expireTime = System.currentTimeMillis() + 3600000; // 1小时后
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        
        assertNotNull(jwt);
        assertFalse(jwt.isEmpty());
        
        // JWT应该包含三个部分，用点分隔
        String[] parts = jwt.split("\\.");
        assertEquals(3, parts.length);
    }
    
    @Test
    public void testVerifyAndParseJWT() throws TokenGenerationException, TokenValidationException {
        long expireTime = System.currentTimeMillis() + 3600000; // 1小时后
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        
        Map<String, Object> payload = CryptoUtils.verifyAndParseJWT(jwt, TEST_SECRET);
        
        assertNotNull(payload);
        assertEquals(TEST_UID, payload.get("uid"));
        assertEquals(TEST_APP_ID, payload.get("app_id"));
        assertNotNull(payload.get("exp"));
        assertNotNull(payload.get("iat"));
    }
    
    @Test(expected = TokenValidationException.class)
    public void testVerifyJWTWithWrongSecret() throws TokenGenerationException, TokenValidationException {
        long expireTime = System.currentTimeMillis() + 3600000;
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        
        // 使用错误的密钥验证
        CryptoUtils.verifyAndParseJWT(jwt, "wrong_secret");
    }
    
    @Test(expected = TokenValidationException.class)
    public void testVerifyExpiredJWT() throws TokenGenerationException, TokenValidationException {
        long expireTime = System.currentTimeMillis() - 1000; // 1秒前过期
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        
        CryptoUtils.verifyAndParseJWT(jwt, TEST_SECRET);
    }
    
    @Test(expected = TokenValidationException.class)
    public void testVerifyInvalidJWTFormat() throws TokenValidationException {
        CryptoUtils.verifyAndParseJWT("invalid.jwt", TEST_SECRET);
    }
    
    @Test(expected = TokenValidationException.class)
    public void testVerifyNullJWT() throws TokenValidationException {
        CryptoUtils.verifyAndParseJWT(null, TEST_SECRET);
    }
    
    @Test(expected = TokenValidationException.class)
    public void testVerifyEmptyJWT() throws TokenValidationException {
        CryptoUtils.verifyAndParseJWT("", TEST_SECRET);
    }
    
    @Test
    public void testHmacSha256() throws TokenGenerationException {
        String data = "test_data";
        String signature = CryptoUtils.hmacSha256(data, TEST_SECRET);
        
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
        
        // 相同的数据和密钥应该产生相同的签名
        String signature2 = CryptoUtils.hmacSha256(data, TEST_SECRET);
        assertEquals(signature, signature2);
        
        // 不同的数据应该产生不同的签名
        String signature3 = CryptoUtils.hmacSha256("different_data", TEST_SECRET);
        assertNotEquals(signature, signature3);
    }
    
    @Test
    public void testBase64UrlEncode() {
        String data = "Hello, World!";
        String encoded = CryptoUtils.base64UrlEncode(data.getBytes());
        
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        
        // Base64 URL编码不应该包含填充字符
        assertFalse(encoded.contains("="));
    }
    
    @Test
    public void testBase64UrlDecode() {
        String data = "Hello, World!";
        String encoded = CryptoUtils.base64UrlEncode(data.getBytes());
        byte[] decoded = CryptoUtils.base64UrlDecode(encoded);
        
        assertNotNull(decoded);
        assertEquals(data, new String(decoded));
    }
    
    @Test
    public void testGenerateCode() throws TokenGenerationException {
        long expireSeconds = 3600;
        String code = CryptoUtils.generateCode(TEST_UID, TEST_APP_ID, expireSeconds, TEST_SECRET);
        
        assertNotNull(code);
        assertFalse(code.isEmpty());
        
        // 验证生成的认证码是有效的JWT
        String[] parts = code.split("\\.");
        assertEquals(3, parts.length);
    }
    
    @Test
    public void testGenerateSSToken() throws TokenGenerationException {
        long expireSeconds = 7200;
        String token = CryptoUtils.generateSSToken(TEST_UID, TEST_APP_ID, expireSeconds, TEST_SECRET);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // 验证生成的SSToken是有效的JWT
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }
    
    @Test
    public void testExtractUidFromToken() throws TokenGenerationException, TokenValidationException {
        String code = CryptoUtils.generateCode(TEST_UID, TEST_APP_ID, 3600, TEST_SECRET);
        String extractedUid = CryptoUtils.extractUidFromToken(code, TEST_SECRET);
        
        assertEquals(TEST_UID, extractedUid);
    }
    
    @Test(expected = TokenValidationException.class)
    public void testExtractUidFromInvalidToken() throws TokenValidationException {
        CryptoUtils.extractUidFromToken("invalid_token", TEST_SECRET);
    }
    
    @Test
    public void testIsTokenExpiredWithValidToken() throws TokenGenerationException {
        String code = CryptoUtils.generateCode(TEST_UID, TEST_APP_ID, 3600, TEST_SECRET);
        boolean expired = CryptoUtils.isTokenExpired(code, TEST_SECRET);
        
        assertFalse(expired);
    }
    
    @Test
    public void testIsTokenExpiredWithExpiredToken() throws TokenGenerationException {
        // 生成一个已经过期的令牌
        long expireTime = System.currentTimeMillis() - 1000; // 1秒前过期
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        boolean expired = CryptoUtils.isTokenExpired(jwt, TEST_SECRET);
        
        assertTrue(expired);
    }
    
    @Test
    public void testIsTokenExpiredWithInvalidToken() {
        boolean expired = CryptoUtils.isTokenExpired("invalid_token", TEST_SECRET);
        assertTrue(expired);
    }
    
    @Test
    public void testJWTRoundTrip() throws TokenGenerationException, TokenValidationException {
        // 完整的JWT生成和验证流程
        long expireTime = System.currentTimeMillis() + 3600000;
        
        // 生成JWT
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        
        // 验证和解析JWT
        Map<String, Object> payload = CryptoUtils.verifyAndParseJWT(jwt, TEST_SECRET);
        
        // 验证载荷内容
        assertEquals(TEST_UID, payload.get("uid"));
        assertEquals(TEST_APP_ID, payload.get("app_id"));
        
        // 提取用户ID
        String extractedUid = CryptoUtils.extractUidFromToken(jwt, TEST_SECRET);
        assertEquals(TEST_UID, extractedUid);
        
        // 检查过期状态
        assertFalse(CryptoUtils.isTokenExpired(jwt, TEST_SECRET));
    }
}