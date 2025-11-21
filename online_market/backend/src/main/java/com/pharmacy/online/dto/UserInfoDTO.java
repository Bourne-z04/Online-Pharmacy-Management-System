package com.pharmacy.online.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 用户信息DTO
 */
@Data
public class UserInfoDTO {
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 手机号（脱敏）
     */
    private String phone;
    
    /**
     * 真实姓名
     */
    private String realName;
}

