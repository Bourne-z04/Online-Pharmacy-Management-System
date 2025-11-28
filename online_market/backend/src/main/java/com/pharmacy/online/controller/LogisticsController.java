package com.pharmacy.online.controller;

import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.LogisticsVO;
import com.pharmacy.online.service.LogisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 物流控制器
 */
@Slf4j
@RestController
@RequestMapping("/logistics")
public class LogisticsController {
    
    @Autowired
    private LogisticsService logisticsService;
    
    /**
     * 查询物流信息
     */
    @GetMapping("/{orderNo}")
    public Result<LogisticsVO> getLogistics(@RequestAttribute("userId") String userId,
                                             @PathVariable String orderNo) {
        log.info("查询物流信息: userId={}, orderNo={}", userId, orderNo);
        LogisticsVO logistics = logisticsService.getLogistics(userId, orderNo);
        return Result.success(logistics);
    }
}

