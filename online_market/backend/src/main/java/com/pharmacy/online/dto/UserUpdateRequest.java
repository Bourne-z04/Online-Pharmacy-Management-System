package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 用户信息更新请求DTO
 */
@Data
public class UserUpdateRequest {
    
    /**
     * 昵称
     */
    @Size(min = 2, max = 20, message = "昵称长度应为2-20个字符")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$", message = "昵称只能包含汉字、字母、数字")
    private String nickname;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;
}

