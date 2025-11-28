package com.pharmacy.online.service;

import com.pharmacy.online.dto.LogisticsVO;

/**
 * 物流服务接口
 */
public interface LogisticsService {
    
    /**
     * 查询物流信息
     */
    LogisticsVO getLogistics(String userId, String orderNo);
}

