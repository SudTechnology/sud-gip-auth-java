package tech.sud.gip.auth;

import tech.sud.gip.auth.constant.ErrorCode;
import tech.sud.gip.auth.model.CodeResponse;
import tech.sud.gip.auth.model.SSTokenResponse;
import tech.sud.gip.auth.model.UidResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SudGIPAuth unit test class
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SudGIPAuthTest {
    
    private static final String TEST_APP_ID = "test_app_id";
    private static final String TEST_APP_SECRET = "test_app_secret";
    private static final String TEST_UID = "test_user_123";
    
    private SudGIPAuth auth;
    
    @Before
    public void setUp() {
        auth = new SudGIPAuth(TEST_APP_ID, TEST_APP_SECRET);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(auth);
        assertEquals(TEST_APP_ID, auth.getAppId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAppId() {
        new SudGIPAuth(null, TEST_APP_SECRET);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyAppId() {
        new SudGIPAuth("", TEST_APP_SECRET);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAppSecret() {
        new SudGIPAuth(TEST_APP_ID, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyAppSecret() {
        new SudGIPAuth(TEST_APP_ID, "");
    }
    
    @Test
    public void testGetCodeSuccess() {
        CodeResponse response = auth.getCode(TEST_UID);
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ErrorCode.SUCCESS, response.getErrorCode());
        assertNotNull(response.getCode());
        assertNotNull(response.getExpireDate());
        assertFalse(response.getCode().isEmpty());
    }
    
    @Test
    public void testGetCodeWithCustomExpireTime() {
        long customExpireSeconds = 1800L; // 30 minutes
        CodeResponse response = auth.getCode(TEST_UID, customExpireSeconds);
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ErrorCode.SUCCESS, response.getErrorCode());
        assertNotNull(response.getCode());
        assertNotNull(response.getExpireDate());
        
        // Verify expiration time is approximately correct (allow a few seconds error)
        long expectedExpireTime = System.currentTimeMillis() + (customExpireSeconds * 1000);
        long actualExpireTime = response.getExpireDate().getTime();
        assertTrue(Math.abs(actualExpireTime - expectedExpireTime) < 5000); // 5 seconds error
    }
    
    @Test
    public void testGetCodeWithNullUid() {
        CodeResponse response = auth.getCode(null);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.APP_DATA_INVALID, response.getErrorCode());
        assertNull(response.getCode());
    }
    
    @Test
    public void testGetCodeWithEmptyUid() {
        CodeResponse response = auth.getCode("");
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.APP_DATA_INVALID, response.getErrorCode());
        assertNull(response.getCode());
    }
    
    @Test
    public void testGetCodeWithInvalidExpireTime() {
        CodeResponse response = auth.getCode(TEST_UID, -1);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.APP_DATA_INVALID, response.getErrorCode());
        assertNull(response.getCode());
    }
    
    @Test
    public void testGetSSTokenSuccess() {
        SSTokenResponse response = auth.getSSToken(TEST_UID);
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ErrorCode.SUCCESS, response.getErrorCode());
        assertNotNull(response.getToken());
        assertNotNull(response.getExpireDate());
        assertFalse(response.getToken().isEmpty());
    }
    
    @Test
    public void testGetSSTokenWithCustomExpireTime() {
        long customExpireSeconds = 3600L; // 1 hour
        SSTokenResponse response = auth.getSSToken(TEST_UID, customExpireSeconds);
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(ErrorCode.SUCCESS, response.getErrorCode());
        assertNotNull(response.getToken());
        assertNotNull(response.getExpireDate());
        
        // Verify expiration time is approximately correct (allow a few seconds error)
        long expectedExpireTime = System.currentTimeMillis() + (customExpireSeconds * 1000);
        long actualExpireTime = response.getExpireDate().getTime();
        assertTrue(Math.abs(actualExpireTime - expectedExpireTime) < 5000); // 5 seconds error
    }
    
    @Test
    public void testGetSSTokenWithNullUid() {
        SSTokenResponse response = auth.getSSToken(null);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.APP_DATA_INVALID, response.getErrorCode());
        assertNull(response.getToken());
    }
    
    @Test
    public void testGetSSTokenWithEmptyUid() {
        SSTokenResponse response = auth.getSSToken("");
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.APP_DATA_INVALID, response.getErrorCode());
        assertNull(response.getToken());
    }
    
    @Test
    public void testGetUidByCodeSuccess() {
        // First generate an authentication code
        CodeResponse codeResponse = auth.getCode(TEST_UID);
        assertTrue(codeResponse.isSuccess());
        
        // Get user ID through authentication code
        UidResponse uidResponse = auth.getUidByCode(codeResponse.getCode());
        
        assertNotNull(uidResponse);
        assertTrue(uidResponse.isSuccess());
        assertEquals(ErrorCode.SUCCESS, uidResponse.getErrorCode());
        assertEquals(TEST_UID, uidResponse.getUid());
    }
    
    @Test
    public void testGetUidByCodeWithNullCode() {
        UidResponse response = auth.getUidByCode(null);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.TOKEN_INVALID, response.getErrorCode());
        assertNull(response.getUid());
    }
    
    @Test
    public void testGetUidByCodeWithEmptyCode() {
        UidResponse response = auth.getUidByCode("");
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.TOKEN_INVALID, response.getErrorCode());
        assertNull(response.getUid());
    }
    
    @Test
    public void testGetUidByCodeWithInvalidCode() {
        UidResponse response = auth.getUidByCode("invalid_code");
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotEquals(ErrorCode.SUCCESS, response.getErrorCode());
        assertNull(response.getUid());
    }
    
    @Test
    public void testGetUidBySSTokenSuccess() {
        // First generate an SSToken
        SSTokenResponse tokenResponse = auth.getSSToken(TEST_UID);
        assertTrue(tokenResponse.isSuccess());
        
        // Get user ID through SSToken
        UidResponse uidResponse = auth.getUidBySSToken(tokenResponse.getToken());
        
        assertNotNull(uidResponse);
        assertTrue(uidResponse.isSuccess());
        assertEquals(ErrorCode.SUCCESS, uidResponse.getErrorCode());
        assertEquals(TEST_UID, uidResponse.getUid());
    }
    
    @Test
    public void testGetUidBySSTokenWithNullToken() {
        UidResponse response = auth.getUidBySSToken(null);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.TOKEN_INVALID, response.getErrorCode());
        assertNull(response.getUid());
    }
    
    @Test
    public void testGetUidBySSTokenWithEmptyToken() {
        UidResponse response = auth.getUidBySSToken("");
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(ErrorCode.TOKEN_INVALID, response.getErrorCode());
        assertNull(response.getUid());
    }
    
    @Test
    public void testGetUidBySSTokenWithInvalidToken() {
        UidResponse response = auth.getUidBySSToken("invalid_token");
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotEquals(ErrorCode.SUCCESS, response.getErrorCode());
        assertNull(response.getUid());
    }
    
    @Test
    public void testIsTokenExpiredWithValidToken() {
        // Generate a valid authentication code
        CodeResponse codeResponse = auth.getCode(TEST_UID, 3600); // 1 hour
        assertTrue(codeResponse.isSuccess());
        
        // Check if expired
        boolean expired = auth.isTokenExpired(codeResponse.getCode());
        assertFalse(expired);
    }
    
    @Test
    public void testIsTokenExpiredWithExpiredToken() {
        // Generate an authentication code that expires quickly
        CodeResponse codeResponse = auth.getCode(TEST_UID, 1); // 1 second
        assertTrue(codeResponse.isSuccess());
        
        // Wait for token to expire
        try {
            Thread.sleep(2000); // Wait 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check if expired
        boolean expired = auth.isTokenExpired(codeResponse.getCode());
        assertTrue(expired);
    }
    
    @Test
    public void testIsTokenExpiredWithNullToken() {
        boolean expired = auth.isTokenExpired(null);
        assertTrue(expired);
    }
    
    @Test
    public void testIsTokenExpiredWithEmptyToken() {
        boolean expired = auth.isTokenExpired("");
        assertTrue(expired);
    }
    
    @Test
    public void testCodeAndSSTokenAreDifferent() {
        CodeResponse codeResponse = auth.getCode(TEST_UID);
        SSTokenResponse tokenResponse = auth.getSSToken(TEST_UID);
        
        assertTrue(codeResponse.isSuccess());
        assertTrue(tokenResponse.isSuccess());
        
        // Authentication code and SSToken should be different (even though user ID is the same)
        assertNotEquals(codeResponse.getCode(), tokenResponse.getToken());
    }
    
    @Test
    public void testToString() {
        String str = auth.toString();
        assertNotNull(str);
        assertTrue(str.contains(TEST_APP_ID));
        assertTrue(str.contains("SudGIPAuth"));
    }
}