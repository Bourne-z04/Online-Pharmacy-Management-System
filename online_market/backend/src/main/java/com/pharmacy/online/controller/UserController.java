package com.pharmacy.online.controller;

import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.UserInfoDTO;
import com.pharmacy.online.dto.UserUpdateRequest;
import com.pharmacy.online.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoDTO> getUserInfo(@RequestAttribute("userId") String userId) {
        log.info("获取用户信息: userId={}", userId);
        UserInfoDTO userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestAttribute("userId") String userId,
                                        @Valid @RequestBody UserUpdateRequest request) {
        log.info("更新用户信息: userId={}, request={}", userId, request);
        userService.updateUserInfo(userId, request);
        return Result.success("更新成功");
    }
    
    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestAttribute("userId") String userId,
                                        @RequestParam("file") MultipartFile file) {
        log.info("上传头像: userId={}, fileName={}", userId, file.getOriginalFilename());
        String avatarUrl = userService.uploadAvatar(userId, file);
        return Result.success("上传成功", avatarUrl);
    }
    
    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute("userId") String userId) {
        log.info("用户退出登录: userId={}", userId);
        // 可以在这里清理缓存等操作
        return Result.success("退出成功");
    }
}

