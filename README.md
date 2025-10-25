# Sud GIP Auth Java SDK

<mcreference link="https://github.com/SudTechnology/sud-gip-auth-php" index="0">0</mcreference>

## Introduction

The Sud GIP Auth Java SDK is an authentication system library designed specifically for Java developers, providing complete JWT (JSON Web Token) authentication functionality. This SDK is fully ported from the PHP version and supports generating authentication codes, server-to-server tokens (SSTokens), token verification, and user ID retrieval, enabling developers to easily integrate Sud GIP authentication services into Java projects.

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
    <groupId>tech.sud</groupId>
    <artifactId>sud-gip-auth-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle` file:

```gradle
implementation 'tech.sud:sud-gip-auth-java:1.0.0'
```

> **Note**: This SDK will be published to Maven Central Repository. For publishing instructions, see [PUBLISHING.md](./PUBLISHING.md).

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
import tech.sud.gip.auth.exception.SudGIPAuthException;
import tech.sud.gip.auth.exception.TokenGenerationException;
import tech.sud.gip.auth.exception.TokenValidationException;

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

* 📧 Technical Support: <support@sud.tech>

## Changelog

### v1.0.0 (2024-01-XX)

* 🎉 Initial release

* ✅ Complete JWT authentication functionality

* ✅ JDK 1.8 compatibility

