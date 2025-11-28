package com.pharmacy.online.controller;

import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.AfterSaleCreateRequest;
import com.pharmacy.online.dto.AfterSaleVO;
import com.pharmacy.online.service.AfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 售后控制器
 */
@Slf4j
@RestController
@RequestMapping("/after-sale")
public class AfterSaleController {
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    /**
     * 创建售后申请
     */
    @PostMapping("/create")
    public Result<String> createAfterSale(@RequestAttribute("userId") String userId,
                                           @Valid @RequestBody AfterSaleCreateRequest request) {
        log.info("创建售后申请: userId={}, request={}", userId, request);
        String afterSaleNo = afterSaleService.createAfterSale(userId, request);
        return Result.success("申请成功", afterSaleNo);
    }
    
    /**
     * 上传售后凭证
     */
    @PostMapping("/evidence")
    public Result<List<String>> uploadEvidence(@RequestAttribute("userId") String userId,
                                                @RequestParam("files") MultipartFile[] files,
                                                @RequestParam("afterSaleNo") String afterSaleNo) {
        log.info("上传售后凭证: userId={}, afterSaleNo={}, count={}", 
                userId, afterSaleNo, files.length);
        List<String> urls = afterSaleService.uploadEvidence(userId, afterSaleNo, files);
        return Result.success("上传成功", urls);
    }
    
    /**
     * 获取售后列表
     */
    @GetMapping("/list")
    public Result<PageResult<AfterSaleVO>> getAfterSaleList(
            @RequestAttribute("userId") String userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取售后列表: userId={}, page={}", userId, page);
        PageResult<AfterSaleVO> result = afterSaleService.getAfterSaleList(userId, page, size);
        return Result.success(result);
    }
    
    /**
     * 获取售后详情
     */
    @GetMapping("/{afterSaleNo}")
    public Result<AfterSaleVO> getAfterSaleDetail(@RequestAttribute("userId") String userId,
                                                   @PathVariable String afterSaleNo) {
        log.info("获取售后详情: userId={}, afterSaleNo={}", userId, afterSaleNo);
        AfterSaleVO detail = afterSaleService.getAfterSaleDetail(userId, afterSaleNo);
        return Result.success(detail);
    }
    
    /**
     * 取消售后申请
     */
    @PutMapping("/{afterSaleNo}/cancel")
    public Result<Void> cancelAfterSale(@RequestAttribute("userId") String userId,
                                         @PathVariable String afterSaleNo) {
        log.info("取消售后申请: userId={}, afterSaleNo={}", userId, afterSaleNo);
        afterSaleService.cancelAfterSale(userId, afterSaleNo);
        return Result.success("取消成功");
    }
}

