package tech.sud.gip.auth.util;

import tech.sud.gip.auth.exception.TokenGenerationException;
import tech.sud.gip.auth.exception.TokenValidationException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * CryptoUtils unit test class
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
        long expireTime = System.currentTimeMillis() + 3600000; // 1 hour later
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        
        assertNotNull(jwt);
        assertFalse(jwt.isEmpty());
        
        // JWT should contain three parts, separated by dots
        String[] parts = jwt.split("\\.");
        assertEquals(3, parts.length);
    }
    
    @Test
    public void testVerifyAndParseJWT() throws TokenGenerationException, TokenValidationException {
        long expireTime = System.currentTimeMillis() + 3600000; // 1 hour later
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
        
        // Verify with wrong secret key
        CryptoUtils.verifyAndParseJWT(jwt, "wrong_secret");
    }
    
    @Test(expected = TokenValidationException.class)
    public void testVerifyExpiredJWT() throws TokenGenerationException, TokenValidationException {
        long expireTime = System.currentTimeMillis() - 1000; // Expired 1 second ago
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
        
        // Same data and key should produce same signature
        String signature2 = CryptoUtils.hmacSha256(data, TEST_SECRET);
        assertEquals(signature, signature2);
        
        // Different data should produce different signature
        String signature3 = CryptoUtils.hmacSha256("different_data", TEST_SECRET);
        assertNotEquals(signature, signature3);
    }
    
    @Test
    public void testBase64UrlEncode() {
        String data = "Hello, World!";
        String encoded = CryptoUtils.base64UrlEncode(data.getBytes());
        
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        
        // Base64 URL encoding should not contain padding characters
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
        
        // Verify that the generated authentication code is a valid JWT
        String[] parts = code.split("\\.");
        assertEquals(3, parts.length);
    }
    
    @Test
    public void testGenerateSSToken() throws TokenGenerationException {
        long expireSeconds = 7200;
        String token = CryptoUtils.generateSSToken(TEST_UID, TEST_APP_ID, expireSeconds, TEST_SECRET);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify that the generated SSToken is a valid JWT
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
        // Generate an already expired token
        long expireTime = System.currentTimeMillis() - 1000; // Expired 1 second ago
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
        // Complete JWT generation and verification process
        long expireTime = System.currentTimeMillis() + 3600000;
        
        // Generate JWT
        String jwt = CryptoUtils.generateJWT(TEST_UID, TEST_APP_ID, expireTime, TEST_SECRET);
        
        // Verify and parse JWT
        Map<String, Object> payload = CryptoUtils.verifyAndParseJWT(jwt, TEST_SECRET);
        
        // Verify payload content
        assertEquals(TEST_UID, payload.get("uid"));
        assertEquals(TEST_APP_ID, payload.get("app_id"));
        
        // Extract user ID
        String extractedUid = CryptoUtils.extractUidFromToken(jwt, TEST_SECRET);
        assertEquals(TEST_UID, extractedUid);
        
        // Check expiration status
        assertFalse(CryptoUtils.isTokenExpired(jwt, TEST_SECRET));
    }
}