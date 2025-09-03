package com.sud.gip.auth.example;

import com.sud.gip.auth.SudGIPAuth;
import com.sud.gip.auth.model.CodeResponse;
import com.sud.gip.auth.model.SSTokenResponse;
import com.sud.gip.auth.model.UidResponse;

/**
 * Sud GIP Auth Java SDK 使用示例
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public class SudGIPAuthExample {
    
    public static void main(String[] args) {
        // 初始化SDK（请替换为您的实际应用ID和密钥）
        String appId = "your_app_id";
        String appSecret = "your_app_secret";
        SudGIPAuth auth = new SudGIPAuth(appId, appSecret);
        
        String userId = "user_123";
        
        System.out.println("=== Sud GIP Auth Java SDK 示例 ===");
        System.out.println();
        
        // 1. 生成认证码
        System.out.println("1. 生成认证码：");
        CodeResponse codeResponse = auth.getCode(userId);
        if (codeResponse.isSuccess()) {
            System.out.println("认证码: " + codeResponse.getCode());
            System.out.println("过期时间: " + codeResponse.getExpireDate());
            
            // 验证认证码
            System.out.println("\n验证认证码：");
            UidResponse uidResponse = auth.getUidByCode(codeResponse.getCode());
            if (uidResponse.isSuccess()) {
                System.out.println("用户ID: " + uidResponse.getUid());
            } else {
                System.out.println("验证失败: " + uidResponse.getErrorMessage());
            }
        } else {
            System.out.println("生成失败: " + codeResponse.getErrorMessage());
        }
        
        System.out.println();
        
        // 2. 生成SSToken
        System.out.println("2. 生成SSToken：");
        SSTokenResponse tokenResponse = auth.getSSToken(userId);
        if (tokenResponse.isSuccess()) {
            System.out.println("SSToken: " + tokenResponse.getToken());
            System.out.println("过期时间: " + tokenResponse.getExpireDate());
            
            // 验证SSToken
            System.out.println("\n验证SSToken：");
            UidResponse uidResponse = auth.getUidBySSToken(tokenResponse.getToken());
            if (uidResponse.isSuccess()) {
                System.out.println("用户ID: " + uidResponse.getUid());
            } else {
                System.out.println("验证失败: " + uidResponse.getErrorMessage());
            }
        } else {
            System.out.println("生成失败: " + tokenResponse.getErrorMessage());
        }
        
        System.out.println();
        
        // 3. 自定义过期时间
        System.out.println("3. 自定义过期时间（30分钟）：");
        CodeResponse customCodeResponse = auth.getCode(userId, 1800); // 30分钟
        if (customCodeResponse.isSuccess()) {
            System.out.println("认证码: " + customCodeResponse.getCode());
            System.out.println("过期时间: " + customCodeResponse.getExpireDate());
            
            // 检查令牌是否过期
            boolean expired = auth.isTokenExpired(customCodeResponse.getCode());
            System.out.println("是否过期: " + expired);
        } else {
            System.out.println("生成失败: " + customCodeResponse.getErrorMessage());
        }
        
        System.out.println();
        
        // 4. 错误处理示例
        System.out.println("4. 错误处理示例：");
        
        // 无效的用户ID
        CodeResponse errorResponse1 = auth.getCode("");
        System.out.println("空用户ID: " + errorResponse1.getErrorMessage());
        
        // 无效的认证码
        UidResponse errorResponse2 = auth.getUidByCode("invalid_code");
        System.out.println("无效认证码: " + errorResponse2.getErrorMessage());
        
        // 无效的SSToken
        UidResponse errorResponse3 = auth.getUidBySSToken("invalid_token");
        System.out.println("无效SSToken: " + errorResponse3.getErrorMessage());
        
        System.out.println();
        System.out.println("=== 示例完成 ===");
    }
}