package tech.sud.auth.gip.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/** Independent fixtures exercise exact bytes, header safety, and protocol interoperability. */
class RequestSignerTest {
    private final RequestSigner signer = new RequestSigner("test-app", "test-secret");
    private static final String TIME = "1688464949817";
    private static final String NONCE = "fixed-nonce";

    @Test
    void independentVectors() {
        check(new byte[0], "cb6d64fd0a3dfd78b19da2c7625ec2317b05ef8d");
        check(
                "{\r\n \"message\":\"用户\", \"n\":1\r\n}".getBytes(StandardCharsets.UTF_8),
                "0e6f9c9c7ba300bb03ec32277b497b67a62054bc");
        check(new byte[] {0, (byte) 255, 10}, "d2d001cfd566df480128991a0a124ea6b373727d");
    }

    private void check(byte[] body, String expected) {
        assertEquals(expected, signer.generateSignature(TIME, NONCE, body));
        assertTrue(signer.verifySignature("test-app", TIME, NONCE, body, expected));
    }

    @Test
    void authorizationFormat() {
        assertEquals(
                "Sud-Auth app_id=\"test-app\",timestamp=\"1688464949817\","
                    + "nonce=\"fixed-nonce\",signature=\"cb6d64fd0a3dfd78b19da2c7625ec2317b05ef8d\"",
                signer.generateAuthorization(TIME, NONCE, new byte[0]));
    }

    @Test
    void tamperingAndMalformedInputsFailClosed() {
        byte[] body = "{}".getBytes(StandardCharsets.UTF_8);
        String signature = signer.generateSignature(TIME, NONCE, body);
        assertFalse(signer.verifySignature("other-app", TIME, NONCE, body, signature));
        assertFalse(signer.verifySignature("test-app", TIME + "1", NONCE, body, signature));
        assertFalse(signer.verifySignature("test-app", TIME, NONCE + "1", body, signature));
        assertFalse(
                signer.verifySignature(
                        "test-app",
                        TIME,
                        NONCE,
                        "{} ".getBytes(StandardCharsets.UTF_8),
                        signature));
        assertFalse(
                new RequestSigner("test-app", "other-secret")
                        .verifySignature("test-app", TIME, NONCE, body, signature));
        for (String invalid :
                new String[] {null, "", "bad", signature.toUpperCase(), signature + "0"}) {
            assertFalse(signer.verifySignature("test-app", TIME, NONCE, body, invalid));
        }
        assertFalse(signer.verifySignature("test-app", TIME, NONCE, null, signature));
    }

    @Test
    void unsafeMetadataCannotInjectHeadersOrSignedLines() {
        for (String invalid :
                new String[] {null, "", " ", "a\nb", "a\rb", "a\"b", "a\\b", "a,b", "用户"}) {
            assertThrows(
                    IllegalArgumentException.class, () -> new RequestSigner(invalid, "secret"));
            assertThrows(
                    IllegalArgumentException.class,
                    () -> signer.generateAuthorization(invalid, NONCE, new byte[0]));
            assertThrows(
                    IllegalArgumentException.class,
                    () -> signer.generateSignature(TIME, invalid, new byte[0]));
            assertFalse(
                    signer.verifySignature(
                            "test-app",
                            invalid,
                            NONCE,
                            new byte[0],
                            "0000000000000000000000000000000000000000"));
        }
        assertThrows(
                IllegalArgumentException.class, () -> signer.generateSignature(TIME, NONCE, null));
        assertThrows(IllegalArgumentException.class, () -> new RequestSigner("app", " "));
    }

    @Test
    void secretIsNotTrimmedOrTransformed() {
        RequestSigner padded = new RequestSigner("test-app", " test-secret ");
        assertNotEquals(
                signer.generateSignature(TIME, NONCE, new byte[0]),
                padded.generateSignature(TIME, NONCE, new byte[0]));
        RequestSigner utf8 = new RequestSigner("test-app", "密钥");
        String signature = utf8.generateSignature(TIME, NONCE, new byte[0]);
        assertTrue(utf8.verifySignature("test-app", TIME, NONCE, new byte[0], signature));
    }
}
