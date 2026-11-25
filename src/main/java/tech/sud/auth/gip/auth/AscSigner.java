package tech.sud.auth.gip.auth;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/** Calculates App Server Config (ASC) signatures independently of request signing. */
public final class AscSigner {
    private AscSigner() {
    }

    /**
     * Calculates the ASC HMAC-MD5 signature as 32 lowercase hexadecimal characters.
     * The original UTF-8 app ID is the message; the original UTF-8 secret is the key.
     * Do not append a newline, trim, normalize, or pre-hash either input.
     * Existing whitespace is preserved. This helper performs no HTTP I/O or caching.
     * Business requests and callbacks use RequestSigner (HMAC-SHA1) instead.
     * The JWT HS256 32-byte key minimum does not apply to ASC signing.
     *
     * @param appId original application ID
     * @param appSecret original application secret
     * @return ASC signature
     * @throws IllegalArgumentException if an input is null, empty, or invalid Unicode
     * @throws IllegalStateException if HMAC-MD5 is unavailable
     */
    public static String ascSign(String appId, String appSecret) {
        byte[] message = utf8(appId);
        byte[] key = utf8(appSecret);
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(new SecretKeySpec(key, "HmacMD5"));
            byte[] digest = mac.doFinal(message);
            char[] hex = "0123456789abcdef".toCharArray();
            StringBuilder result = new StringBuilder(32);
            for (byte value : digest) {
                result.append(hex[(value & 0xff) >>> 4]);
                result.append(hex[value & 0x0f]);
            }
            return result.toString();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("ASC signing is unavailable", exception);
        }
    }

    private static byte[] utf8(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Invalid ASC signing input");
        }
        try {
            // A strict encoder rejects unpaired surrogates instead of replacing them.
            ByteBuffer buffer = StandardCharsets.UTF_8.newEncoder().encode(CharBuffer.wrap(value));
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return bytes;
        } catch (CharacterCodingException exception) {
            throw new IllegalArgumentException("Invalid ASC signing input", exception);
        }
    }
}
