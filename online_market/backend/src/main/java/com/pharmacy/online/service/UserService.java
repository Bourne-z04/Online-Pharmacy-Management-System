package com.pharmacy.online.service;

import com.pharmacy.online.dto.UserInfoDTO;
import com.pharmacy.online.dto.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 获取用户信息
     */
    UserInfoDTO getUserInfo(String userId);
    
    /**
     * 更新用户信息
     */
    void updateUserInfo(String userId, UserUpdateRequest request);
    
    /**
     * 上传头像
     */
    String uploadAvatar(String userId, MultipartFile file);
}

