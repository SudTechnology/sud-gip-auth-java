package tech.sud.gip.auth.constant;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ErrorCode unit test class
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class ErrorCodeTest {
    
    @Test
    public void testErrorCodeConstants() {
        assertEquals(0, ErrorCode.SUCCESS);
        assertEquals(1001, ErrorCode.TOKEN_CREATION_FAILED);
        assertEquals(1002, ErrorCode.TOKEN_VERIFICATION_FAILED);
        assertEquals(1003, ErrorCode.TOKEN_DECODING_FAILED);
        assertEquals(1004, ErrorCode.TOKEN_INVALID);
        assertEquals(1005, ErrorCode.TOKEN_EXPIRED);
        assertEquals(1101, ErrorCode.APP_DATA_INVALID);
        assertEquals(9999, ErrorCode.UNKNOWN_ERROR);
    }
    
    @Test
    public void testGetErrorMessage() {
        assertEquals("Success", ErrorCode.getErrorMessage(ErrorCode.SUCCESS));
        assertEquals("Token creation failed", ErrorCode.getErrorMessage(ErrorCode.TOKEN_CREATION_FAILED));
        assertEquals("Token verification failed", ErrorCode.getErrorMessage(ErrorCode.TOKEN_VERIFICATION_FAILED));
        assertEquals("Token decoding failed", ErrorCode.getErrorMessage(ErrorCode.TOKEN_DECODING_FAILED));
        assertEquals("Token is invalid", ErrorCode.getErrorMessage(ErrorCode.TOKEN_INVALID));
        assertEquals("Token has expired", ErrorCode.getErrorMessage(ErrorCode.TOKEN_EXPIRED));
        assertEquals("App data is invalid", ErrorCode.getErrorMessage(ErrorCode.APP_DATA_INVALID));
        assertEquals("Unknown error", ErrorCode.getErrorMessage(ErrorCode.UNKNOWN_ERROR));
    }
    
    @Test
    public void testGetErrorMessageWithUnknownCode() {
        int unknownCode = 8888;
        String message = ErrorCode.getErrorMessage(unknownCode);
        assertTrue(message.contains("Unknown error code"));
        assertTrue(message.contains(String.valueOf(unknownCode)));
    }
    
    @Test
    public void testIsSuccess() {
        assertTrue(ErrorCode.isSuccess(ErrorCode.SUCCESS));
        assertFalse(ErrorCode.isSuccess(ErrorCode.TOKEN_CREATION_FAILED));
        assertFalse(ErrorCode.isSuccess(ErrorCode.TOKEN_VERIFICATION_FAILED));
        assertFalse(ErrorCode.isSuccess(ErrorCode.TOKEN_DECODING_FAILED));
        assertFalse(ErrorCode.isSuccess(ErrorCode.TOKEN_INVALID));
        assertFalse(ErrorCode.isSuccess(ErrorCode.TOKEN_EXPIRED));
        assertFalse(ErrorCode.isSuccess(ErrorCode.APP_DATA_INVALID));
        assertFalse(ErrorCode.isSuccess(ErrorCode.UNKNOWN_ERROR));
        assertFalse(ErrorCode.isSuccess(8888)); // Unknown error code
    }
    
    @Test
    public void testConstructorIsPrivate() {
        // ErrorCode class should not be instantiated, constructor is private
        // Here we just verify the design intent of the class, not actually call the private constructor
        try {
            java.lang.reflect.Constructor<ErrorCode> constructor = ErrorCode.class.getDeclaredConstructor();
            assertTrue("Constructor should be private", java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("Private constructor should exist");
        }
    }
}