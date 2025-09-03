package com.sud.gip.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sud.gip.auth.exception.SudGIPAuthException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP客户端工具类
 * 提供HTTP请求发送和响应处理功能
 * 
 * @author Sud Technology
 * @version 1.0.0
 */
public final class HttpUtils {
    
    /**
     * 默认连接超时时间（毫秒）
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    
    /**
     * 默认读取超时时间（毫秒）
     */
    private static final int DEFAULT_READ_TIMEOUT = 30000;
    
    /**
     * JSON内容类型
     */
    private static final String CONTENT_TYPE_JSON = "application/json";
    
    /**
     * 表单内容类型
     */
    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    
    /**
     * JSON对象映射器
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    /**
     * 私有构造函数，防止实例化
     */
    private HttpUtils() {
        throw new AssertionError("HttpUtils class should not be instantiated");
    }
    
    /**
     * 发送GET请求
     * 
     * @param url 请求URL
     * @return 响应内容
     * @throws SudGIPAuthException HTTP请求异常
     */
    public static String sendGet(String url) throws SudGIPAuthException {
        return sendGet(url, null);
    }
    
    /**
     * 发送GET请求
     * 
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应内容
     * @throws SudGIPAuthException HTTP请求异常
     */
    public static String sendGet(String url, Map<String, String> headers) throws SudGIPAuthException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        
        try {
            // 设置请求头
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            
            // 发送请求
            HttpResponse response = httpClient.execute(httpGet);
            
            // 处理响应
            return handleResponse(response);
            
        } catch (IOException e) {
            throw new SudGIPAuthException(9999, "HTTP GET request failed: " + e.getMessage(), e);
        } finally {
            httpGet.releaseConnection();
        }
    }
    
    /**
     * 发送POST请求（JSON格式）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @return 响应内容
     * @throws SudGIPAuthException HTTP请求异常
     */
    public static String sendPostJson(String url, String jsonData) throws SudGIPAuthException {
        return sendPostJson(url, jsonData, null);
    }
    
    /**
     * 发送POST请求（JSON格式）
     * 
     * @param url 请求URL
     * @param jsonData JSON数据
     * @param headers 请求头
     * @return 响应内容
     * @throws SudGIPAuthException HTTP请求异常
     */
    public static String sendPostJson(String url, String jsonData, Map<String, String> headers) 
            throws SudGIPAuthException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        
        try {
            // 设置请求体
            if (jsonData != null) {
                StringEntity entity = new StringEntity(jsonData, StandardCharsets.UTF_8);
                entity.setContentType(CONTENT_TYPE_JSON);
                httpPost.setEntity(entity);
            }
            
            // 设置请求头
            httpPost.setHeader("Content-Type", CONTENT_TYPE_JSON);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);
            
            // 处理响应
            return handleResponse(response);
            
        } catch (IOException e) {
            throw new SudGIPAuthException(9999, "HTTP POST request failed: " + e.getMessage(), e);
        } finally {
            httpPost.releaseConnection();
        }
    }
    
    /**
     * 发送POST请求（表单格式）
     * 
     * @param url 请求URL
     * @param formData 表单数据
     * @return 响应内容
     * @throws SudGIPAuthException HTTP请求异常
     */
    public static String sendPostForm(String url, String formData) throws SudGIPAuthException {
        return sendPostForm(url, formData, null);
    }
    
    /**
     * 发送POST请求（表单格式）
     * 
     * @param url 请求URL
     * @param formData 表单数据
     * @param headers 请求头
     * @return 响应内容
     * @throws SudGIPAuthException HTTP请求异常
     */
    public static String sendPostForm(String url, String formData, Map<String, String> headers) 
            throws SudGIPAuthException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        
        try {
            // 设置请求体
            if (formData != null) {
                StringEntity entity = new StringEntity(formData, StandardCharsets.UTF_8);
                entity.setContentType(CONTENT_TYPE_FORM);
                httpPost.setEntity(entity);
            }
            
            // 设置请求头
            httpPost.setHeader("Content-Type", CONTENT_TYPE_FORM);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            
            // 发送请求
            HttpResponse response = httpClient.execute(httpPost);
            
            // 处理响应
            return handleResponse(response);
            
        } catch (IOException e) {
            throw new SudGIPAuthException(9999, "HTTP POST request failed: " + e.getMessage(), e);
        } finally {
            httpPost.releaseConnection();
        }
    }
    
    /**
     * 处理HTTP响应
     * 
     * @param response HTTP响应
     * @return 响应内容
     * @throws SudGIPAuthException HTTP响应处理异常
     */
    private static String handleResponse(HttpResponse response) throws SudGIPAuthException {
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            
            // 检查HTTP状态码
            if (statusCode < 200 || statusCode >= 300) {
                throw new SudGIPAuthException(9999, "HTTP request failed with status code: " + statusCode);
            }
            
            // 获取响应内容
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                return "";
            }
            
        } catch (IOException e) {
            throw new SudGIPAuthException(9999, "Failed to handle HTTP response: " + e.getMessage(), e);
        }
    }
    
    /**
     * 构建查询字符串
     * 
     * @param params 参数映射
     * @return 查询字符串
     */
    public static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        
        return sb.toString();
    }
    
    /**
     * 将对象转换为JSON字符串
     * 
     * @param obj 对象
     * @return JSON字符串
     * @throws SudGIPAuthException 转换异常
     */
    public static String toJson(Object obj) throws SudGIPAuthException {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new SudGIPAuthException(9999, "Failed to convert object to JSON: " + e.getMessage(), e);
        }
    }
    
    /**
     * 将JSON字符串转换为对象
     * 
     * @param json JSON字符串
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 转换后的对象
     * @throws SudGIPAuthException 转换异常
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws SudGIPAuthException {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new SudGIPAuthException(9999, "Failed to convert JSON to object: " + e.getMessage(), e);
        }
    }
    
    /**
     * URL编码
     * 
     * @param value 待编码的值
     * @return 编码后的值
     */
    public static String urlEncode(String value) {
        try {
            return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return value;
        }
    }
    
    /**
     * URL解码
     * 
     * @param value 待解码的值
     * @return 解码后的值
     */
    public static String urlDecode(String value) {
        try {
            return java.net.URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return value;
        }
    }
}