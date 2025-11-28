package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 微信登录请求DTO
 */
@Data
public class WechatLoginRequest {
    
    /**
     * 微信登录凭证code
     */
    @NotBlank(message = "登录凭证不能为空")
    private String code;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
}

