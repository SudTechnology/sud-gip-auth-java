package tech.sud.auth.gip.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import tech.sud.auth.gip.auth.model.CodeResponse;
import tech.sud.auth.gip.auth.model.SSTokenResponse;
import tech.sud.auth.gip.auth.model.UidResponse;

/**
 * Complete flow test demonstrating the full authentication workflow:
 * 1. Generate code from UID
 * 2. Parse UID from code 
 * 3. Generate SSToken from UID
 * 4. Verify each step's correctness
 */
@DisplayName("Complete Authentication Flow Test")
public class CompleteFlowTest {
    
    private SudGIPAuth auth;
    private static final String TEST_APP_ID = "test_app_123";
    private static final String TEST_APP_KEY = "test_app_key_456";
    private static final String TEST_UID = "user_12345";
    
    @BeforeEach
    void setUp() {
        auth = new SudGIPAuth(TEST_APP_ID, TEST_APP_KEY);
    }
    
    @Test
    @DisplayName("Complete authentication flow test")
    void testCompleteAuthenticationFlow() {
        System.out.println("=== Starting Complete Authentication Flow Test ===");
        
        // Step 1: Generate code from UID
        System.out.println("\n--- Step 1: Generate Code from UID ---");
        CodeResponse codeResponse = auth.getCode(TEST_UID);
        System.out.println("UID: " + TEST_UID);
        System.out.println("Code Response: " + codeResponse);
        
        assertTrue(codeResponse.isSuccess(), "Code generation should succeed");
        assertNotNull(codeResponse.getCode(), "Generated code should not be null");
        assertFalse(codeResponse.getCode().isEmpty(), "Generated code should not be empty");
        assertTrue(codeResponse.getCode().length() > 10, "Generated code should be reasonably long");
        
        String code = codeResponse.getCode();
        System.out.println("Generated Code: " + code);
        
        // Step 2: Parse UID from code
        System.out.println("\n--- Step 2: Parse UID from Code ---");
        UidResponse uidResponse = auth.getUidByCode(code);
        System.out.println("Original Code: " + code);
        System.out.println("UID Response: " + uidResponse);
        
        assertTrue(uidResponse.isSuccess(), "UID parsing should succeed");
        assertNotNull(uidResponse.getUid(), "Parsed UID should not be null");
        assertEquals(TEST_UID, uidResponse.getUid(), "Parsed UID should match original UID");
        
        String parsedUid = uidResponse.getUid();
        System.out.println("Parsed UID: " + parsedUid);
        
        // Step 3: Generate SSToken from UID
        System.out.println("\n--- Step 3: Generate SSToken from UID ---");
        SSTokenResponse tokenResponse = auth.getSSToken(TEST_UID);
        System.out.println("UID: " + TEST_UID);
        System.out.println("Token Response: " + tokenResponse);
        
        assertTrue(tokenResponse.isSuccess(), "SSToken generation should succeed");
        assertNotNull(tokenResponse.getToken(), "Generated SSToken should not be null");
        assertFalse(tokenResponse.getToken().isEmpty(), "Generated SSToken should not be empty");
        assertTrue(tokenResponse.getToken().length() > 10, "Generated SSToken should be reasonably long");
        
        String ssToken = tokenResponse.getToken();
        System.out.println("Generated SSToken: " + ssToken);
        
        // Step 4: Verify SSToken can be used to get UID
        System.out.println("\n--- Step 4: Verify SSToken ---");
        UidResponse tokenUidResponse = auth.getUidBySSToken(ssToken);
        System.out.println("SSToken: " + ssToken);
        System.out.println("UID Response from Token: " + tokenUidResponse);
        
        assertTrue(tokenUidResponse.isSuccess(), "UID parsing from SSToken should succeed");
        assertNotNull(tokenUidResponse.getUid(), "UID from SSToken should not be null");
        assertEquals(TEST_UID, tokenUidResponse.getUid(), "UID from SSToken should match original UID");
        
        String uidFromToken = tokenUidResponse.getUid();
        System.out.println("UID from SSToken: " + uidFromToken);
        
        // Step 5: Test with different UIDs to ensure consistency
        System.out.println("\n--- Step 5: Test with Different UIDs ---");
        testWithDifferentUid("user_99999");
        testWithDifferentUid("player_abc123");
        testWithDifferentUid("test_user_xyz");
        
        System.out.println("\n=== Complete Authentication Flow Test PASSED ===");
    }
    
    private void testWithDifferentUid(String uid) {
        System.out.println("\nTesting with UID: " + uid);
        
        // Generate code
        CodeResponse codeResponse = auth.getCode(uid);
        System.out.println("Code Response: " + codeResponse);
        assertTrue(codeResponse.isSuccess(), "Code generation should succeed for UID: " + uid);
        assertNotNull(codeResponse.getCode(), "Code should not be null for UID: " + uid);
        
        // Parse UID from code
        UidResponse uidResponse = auth.getUidByCode(codeResponse.getCode());
        System.out.println("UID Response: " + uidResponse);
        assertTrue(uidResponse.isSuccess(), "UID parsing should succeed for UID: " + uid);
        assertEquals(uid, uidResponse.getUid(), "Parsed UID should match for UID: " + uid);
        
        // Generate SSToken
        SSTokenResponse tokenResponse = auth.getSSToken(uid);
        System.out.println("Token Response: " + tokenResponse);
        assertTrue(tokenResponse.isSuccess(), "SSToken generation should succeed for UID: " + uid);
        assertNotNull(tokenResponse.getToken(), "SSToken should not be null for UID: " + uid);
        
        // Parse UID from SSToken
        UidResponse tokenUidResponse = auth.getUidBySSToken(tokenResponse.getToken());
        System.out.println("UID Response from Token: " + tokenUidResponse);
        assertTrue(tokenUidResponse.isSuccess(), "UID parsing from SSToken should succeed for UID: " + uid);
        assertEquals(uid, tokenUidResponse.getUid(), "UID from SSToken should match for UID: " + uid);
        
        System.out.println("✅ All operations successful for UID: " + uid);
    }
    
    @Test
    @DisplayName("Test error handling in complete flow")
    void testErrorHandlingInCompleteFlow() {
        System.out.println("\n=== Testing Error Handling ===");
        
        // Test with invalid code
        System.out.println("\n--- Testing Invalid Code ---");
        String invalidCode = "invalid_code_123";
        UidResponse invalidCodeResponse = auth.getUidByCode(invalidCode);
        System.out.println("Response with invalid code: " + invalidCodeResponse);
        assertFalse(invalidCodeResponse.isSuccess(), "Invalid code should fail");
        assertNotNull(invalidCodeResponse.getErrorCode(), "Error code should be present");
        
        // Test with invalid SSToken
        System.out.println("\n--- Testing Invalid SSToken ---");
        String invalidToken = "invalid_token_456";
        UidResponse invalidTokenResponse = auth.getUidBySSToken(invalidToken);
        System.out.println("Response with invalid token: " + invalidTokenResponse);
        assertFalse(invalidTokenResponse.isSuccess(), "Invalid token should fail");
        assertNotNull(invalidTokenResponse.getErrorCode(), "Error code should be present");
        
        // Test with null/empty inputs
        System.out.println("\n--- Testing Null/Empty Inputs ---");
        UidResponse nullCodeResponse = auth.getUidByCode(null);
        System.out.println("Response with null code: " + nullCodeResponse);
        assertFalse(nullCodeResponse.isSuccess(), "Null code should fail");
        
        UidResponse nullTokenResponse = auth.getUidBySSToken("");
        System.out.println("Response with empty token: " + nullTokenResponse);
        assertFalse(nullTokenResponse.isSuccess(), "Empty token should fail");
        
        System.out.println("✅ Error handling test completed");
    }
    
    @Test
    @DisplayName("Test custom expiration times")
    void testCustomExpirationTimes() {
        System.out.println("\n=== Testing Custom Expiration Times ===");
        
        long[] expireSeconds = {60L, 300L, 3600L, 7200L}; // 1min, 5min, 1hour, 2hours
        
        for (long expireSec : expireSeconds) {
            System.out.println("\nTesting with expiration: " + expireSec + " seconds");
            
            // Generate code with custom expiration
            CodeResponse codeResponse = auth.getCode(TEST_UID, expireSec);
            System.out.println("Code Response: " + codeResponse);
            assertTrue(codeResponse.isSuccess(), "Code generation should succeed with expiration: " + expireSec);
            assertNotNull(codeResponse.getExpireDate(), "Expiration date should be set");
            
            // Generate SSToken with custom expiration
            SSTokenResponse tokenResponse = auth.getSSToken(TEST_UID, expireSec);
            System.out.println("Token Response: " + tokenResponse);
            assertTrue(tokenResponse.isSuccess(), "SSToken generation should succeed with expiration: " + expireSec);
            assertNotNull(tokenResponse.getExpireDate(), "Expiration date should be set");
            
            System.out.println("✅ All operations successful with expiration: " + expireSec + " seconds");
        }
        
        System.out.println("✅ Custom expiration times test completed");
    }
}