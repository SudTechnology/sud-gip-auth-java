package tech.sud.auth.gip.auth;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Thread-safe signing of Sud HTTP requests and verification of callbacks. This protocol is separate
 * from token signing. It does not enforce timestamp freshness or replay protection; those policies
 * belong to the receiving service.
 */
public final class RequestSigner {
    private static final char[] HEX = "0123456789abcdef".toCharArray();
    private final String appId;
    private final byte[] secret;

    /**
     * Creates a signer without trimming, hashing, or otherwise changing the secret.
     *
     * @param appId application ID, safe for a quoted HTTP header value
     * @param appSecret original application secret, encoded as UTF-8
     */
    public RequestSigner(String appId, String appSecret) {
        requireMetadata(appId, "appId");
        if (appSecret == null || appSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("appSecret must not be blank");
        }
        this.appId = appId;
        this.secret = appSecret.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Signs appId + LF + timestamp + LF + nonce + LF + rawBody + LF using HMAC-SHA1. Never
     * deserialize, reserialize, trim, or normalize the body before calling this method. For
     * outgoing requests, serialize once, sign those bytes, and send exactly those bytes.
     *
     * @param timestamp request timestamp as supplied by the protocol (Unix milliseconds)
     * @param nonce request nonce
     * @param rawBody original body bytes; use an empty array for an empty body
     * @return lowercase hexadecimal signature
     */
    public String generateSignature(String timestamp, String nonce, byte[] rawBody) {
        requireMetadata(timestamp, "timestamp");
        requireMetadata(nonce, "nonce");
        if (rawBody == null) {
            throw new IllegalArgumentException("rawBody must not be null");
        }
        byte[] digest = digest(timestamp, nonce, rawBody);
        char[] hex = new char[digest.length * 2];
        for (int i = 0; i < digest.length; i++) {
            hex[i * 2] = HEX[(digest[i] & 0xff) >>> 4];
            hex[i * 2 + 1] = HEX[digest[i] & 0x0f];
        }
        return new String(hex);
    }

    /**
     * Checks the configured application ID and compares signature digests in constant time. Invalid
     * inputs return false. The caller must supply ORIGINAL received body bytes before any JSON
     * conversion and reject duplicate or ambiguous HTTP headers in its HTTP adapter.
     *
     * @param suppliedAppId application ID from the callback headers
     * @param timestamp timestamp from the callback headers
     * @param nonce nonce from the callback headers
     * @param rawBody unmodified received body bytes
     * @param signature lowercase hexadecimal signature from the callback headers
     * @return whether the signature matches
     */
    public boolean verifySignature(
            String suppliedAppId,
            String timestamp,
            String nonce,
            byte[] rawBody,
            String signature) {
        if (!appId.equals(suppliedAppId)
                || !validMetadata(timestamp)
                || !validMetadata(nonce)
                || rawBody == null
                || signature == null
                || !signature.matches("[0-9a-f]{40}")) {
            return false;
        }
        byte[] supplied = new byte[20];
        for (int i = 0; i < supplied.length; i++) {
            supplied[i] = (byte) Integer.parseInt(signature.substring(i * 2, i * 2 + 2), 16);
        }
        return MessageDigest.isEqual(digest(timestamp, nonce, rawBody), supplied);
    }

    /**
     * Builds the single-line Authorization header value for an outgoing Sud API request. Callback
     * requests instead carry the four separate Sud-* headers.
     *
     * @param timestamp request timestamp in Unix milliseconds
     * @param nonce request nonce
     * @param rawBody exact bytes that will be sent
     * @return Sud-Auth header value, without the Authorization header name
     */
    public String generateAuthorization(String timestamp, String nonce, byte[] rawBody) {
        String signature = generateSignature(timestamp, nonce, rawBody);
        return "Sud-Auth app_id=\""
                + appId
                + "\",timestamp=\""
                + timestamp
                + "\",nonce=\""
                + nonce
                + "\",signature=\""
                + signature
                + "\"";
    }

    private byte[] digest(String timestamp, String nonce, byte[] rawBody) {
        try {
            // Mac is mutable: create a separate instance for each operation.
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(secret, "HmacSHA1"));
            mac.update(
                    (appId + "\n" + timestamp + "\n" + nonce + "\n")
                            .getBytes(StandardCharsets.UTF_8));
            mac.update(rawBody);
            // Append the protocol LF even if rawBody already ends in a newline.
            return mac.doFinal(new byte[] {'\n'});
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Request signature calculation failed", exception);
        }
    }

    private static void requireMetadata(String value, String name) {
        if (!validMetadata(value)) {
            throw new IllegalArgumentException(name + " must be nonempty, header-safe ASCII");
        }
    }

    private static boolean validMetadata(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < 33 || c > 126 || c == '"' || c == '\\' || c == ',') {
                return false;
            }
        }
        return true;
    }
}
