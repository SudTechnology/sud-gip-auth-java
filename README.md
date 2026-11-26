# Sud GIP Auth Java SDK

## Introduction

The Sud GIP Auth Java SDK is an authentication system library designed specifically for Java developers, providing complete JWT (JSON Web Token) authentication functionality. This SDK supports generating authentication codes, server-to-server tokens (SSTokens), token verification, and user ID retrieval, enabling developers to easily integrate Sud GIP authentication services into Java projects.

## Features

* **Token Generation**: Supports generating authentication codes and SSTokens with configurable expiration times

* **Token Verification**: Validates the validity of authentication codes and SSTokens

* **User ID Retrieval**: Retrieves user IDs through authentication codes or SSTokens

* **Java 8 Compatible**: Compatible with Java 8 and higher versions

* **Thread Safe**: Supports concurrent usage in multi-threaded environments

* **Exception Handling**: Comprehensive exception handling mechanism and error code definitions

## System Requirements

* Java 8 or higher

* Maven 3.x or Gradle 4.x+

## Installation

### Maven

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>tech.sud.gip</groupId>
    <artifactId>sud-gip-auth-java</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle` file:

```gradle
implementation 'tech.sud.gip:sud-gip-auth-java:1.1.0'
```

## Quick Start

### Initialization

First, you need to instantiate the `SudGIPAuth` class and pass in your application ID and application secret:

```java
import tech.sud.auth.gip.auth.SudGIPAuth;

public class Example {
    public static void main(String[] args) {
        String appId = "your_app_id";
        String appSecret = "your_app_secret";
        
        SudGIPAuth auth = new SudGIPAuth(appId, appSecret);
    }
}
```

### Get Authentication Code

```java
import tech.sud.auth.gip.auth.model.CodeResponse;

String uid = "user_id";
CodeResponse codeResponse = auth.getCode(uid);

if (codeResponse.isSuccess()) {
    System.out.println("Code: " + codeResponse.getCode());
    System.out.println("Expire Date: " + codeResponse.getExpireDate());
} else {
    System.out.println("Error Code: " + codeResponse.getErrorCode());
}
```

### Get Server-to-Server Token (SSToken)

```java
import tech.sud.auth.gip.auth.model.SSTokenResponse;

String uid = "user_id";
SSTokenResponse ssTokenResponse = auth.getSSToken(uid);

if (ssTokenResponse.isSuccess()) {
    System.out.println("SSToken: " + ssTokenResponse.getToken());
    System.out.println("Expire Date: " + ssTokenResponse.getExpireDate());
} else {
    System.out.println("Error Code: " + ssTokenResponse.getErrorCode());
}
```

### Get User ID by Authentication Code

```java
import tech.sud.auth.gip.auth.model.UidResponse;

String code = "your_auth_code";
UidResponse uidResponse = auth.getUidByCode(code);

if (uidResponse.isSuccess()) {
    System.out.println("User ID: " + uidResponse.getUid());
} else {
    System.out.println("Error Code: " + uidResponse.getErrorCode());
}
```

### Get User ID by SSToken

```java
String ssToken = "your_sstoken";
UidResponse uidResponse = auth.getUidBySSToken(ssToken);

if (uidResponse.isSuccess()) {
    System.out.println("User ID: " + uidResponse.getUid());
} else {
    System.out.println("Error Code: " + uidResponse.getErrorCode());
}
```

## Advanced Usage

### Custom Expiration Time

You can set custom expiration times for authentication codes and SSTokens:

```java
// Set authentication code expiration time to 30 minutes (1800 seconds)
CodeResponse codeResponse = auth.getCode(uid, 1800);

// Set SSToken expiration time to 4 hours (14400 seconds)
SSTokenResponse ssTokenResponse = auth.getSSToken(uid, 14400);
```

### Exception Handling

```java
import tech.sud.auth.gip.auth.exception.SudGIPAuthException;
import tech.sud.auth.gip.auth.exception.TokenGenerationException;
import tech.sud.auth.gip.auth.exception.TokenValidationException;

try {
    CodeResponse response = auth.getCode(uid);
    // Handle successful response
} catch (TokenGenerationException e) {
    System.err.println("Token generation failed: " + e.getMessage());
    System.err.println("Error code: " + e.getErrorCode());
} catch (SudGIPAuthException e) {
    System.err.println("Authentication error: " + e.getMessage());
}
```

### Thread-Safe Usage

The `SudGIPAuth` instance is thread-safe and can be shared in multi-threaded environments:

```java
public class AuthService {
    private static final SudGIPAuth auth = new SudGIPAuth(appId, appSecret);
    
    public CodeResponse generateCode(String uid) {
        return auth.getCode(uid);
    }
    
    public UidResponse validateCode(String code) {
        return auth.getUidByCode(code);
    }
}
```

## Error Codes

| Error Code | Description | Suggested Handling |
| ---- | ------ | ----------- |
| 0    | Success     | Process returned data normally    |
| 1001 | Token creation failed | Check application credentials and network connection |
| 1002 | Token verification failed | Confirm token format and validity  |
| 1003 | Token decoding failed | Check token integrity     |
| 1004 | Token is invalid   | Obtain a new valid token    |
| 1005 | Token has expired  | Generate a new token      |
| 1101 | Application data is invalid | Verify application ID and secret   |
| 9999 | Unknown error   | Contact technical support      |

## Best Practices

1. **Credential Security**: Do not hardcode application ID and secret in code, use environment variables or configuration files
2. **Instance Reuse**: `SudGIPAuth` instance creation is expensive, recommend using as singleton
3. **Error Handling**: Always check the `isSuccess()` status of responses
4. **Logging**: Enable appropriate logging in production environments



## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.

## Support

* 📖 [API Documentation](./docs/api.md)

* 🐛 [Issue Reporting](https://github.com/SudTechnology/sud-gip-auth-java/issues)

* 💬 [Discussions](https://github.com/SudTechnology/sud-gip-auth-java/discussions)

* 📧 Technical Support: <dev@sud.tech>

## Changelog

### v1.0.0 (2024-01-XX)

* 🎉 Initial release

* ✅ Complete JWT authentication functionality

* ✅ JDK 1.8 compatibility



## HTTP request signing and callback verification

Version 1.1.0 adds `RequestSigner` without changing existing token APIs or encoding.

```java
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import tech.sud.auth.gip.auth.RequestSigner;

RequestSigner signer = new RequestSigner(appId, appSecret);
String timestamp = Long.toString(System.currentTimeMillis());
String nonce = UUID.randomUUID().toString();
// Serialize ONCE, then sign and send exactly the same byte array.
byte[] rawBody = "{\"example\":true}".getBytes(StandardCharsets.UTF_8);
String authorization = signer.generateAuthorization(timestamp, nonce, rawBody);
// Set the outgoing Authorization header to authorization and send rawBody unchanged.
String signature = signer.generateSignature(timestamp, nonce, rawBody);
boolean valid = signer.verifySignature(appId, timestamp, nonce, rawBody, signature);
```

Outgoing API requests use a single header:

```text
Authorization: Sud-Auth app_id="...",timestamp="...",nonce="...",signature="..."
```

Incoming callbacks use `Sud-AppId`, `Sud-Timestamp`, `Sud-Nonce`, and `Sud-Signature`.
Read these headers in your HTTP adapter and pass their values plus the **original received
body bytes** to `verifySignature`. Reject ambiguous duplicate headers in the adapter.
Only parse JSON or bind a DTO **after** verification succeeds. Never deserialize/reserialize,
trim whitespace, reorder fields, normalize line endings, or decode/re-encode the received body.

The signed content is `appId + LF + timestamp + LF + nonce + LF + rawBody + LF`.
The final LF is required even if the body already ends in a newline. The algorithm is
HMAC-SHA1 with the original UTF-8 application secret and a lowercase 40-character hex result.
Use `new byte[0]` for an empty body; null is invalid. Metadata must be nonempty printable ASCII
without whitespace, quotes, backslashes, or commas. Generation throws `IllegalArgumentException`
for invalid arguments; verification returns false for invalid inputs or a mismatched application ID.

This HTTP signing protocol is separate from token signing. The signer does not alter the secret,
make HTTP requests, check timestamp freshness, store nonces, or enforce replay protection.
Apply those service policies separately. URL, method, and query parameters are not signed.
The class is thread-safe and compatible with Java 8.


## App Server Config signature (ASC)

Available since **1.2.0**, with Java 8 compatibility:

```java
import tech.sud.auth.gip.auth.AscSigner;

String sign = AscSigner.ascSign(appId, appSecret);
```

ASC means **App Server Config**. This helper returns **32 lowercase hexadecimal
characters** using HMAC-MD5. The message is the original UTF-8 app ID and the key
is the original UTF-8 secret. **No newline is appended.** Do not trim, normalize,
or pre-hash either input. Existing whitespace is preserved.

Null, empty, or invalid Unicode inputs throw `IllegalArgumentException`.
The JWT HS256 32-byte key minimum does not apply to ASC signing.

This helper only calculates the ASC sign. It does not construct URLs, perform
HTTP requests, fetch configuration, cache results, or refresh configuration.
It is separate from `RequestSigner` (HMAC-SHA1 business request/callback signing)
and JWT authentication.
