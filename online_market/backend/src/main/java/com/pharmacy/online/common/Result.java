package com.pharmacy.online.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果类
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> Result<T> fail() {
        return new Result<>(500, "操作失败", null);
    }
    
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }
    
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未授权，请先登录", null);
    }
    
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }
    
    /**
     * 禁止访问
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "无权访问", null);
    }
    
    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound() {
        return new Result<>(404, "资源不存在", null);
    }
    
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }
}

