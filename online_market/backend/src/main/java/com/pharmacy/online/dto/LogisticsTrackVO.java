package com.pharmacy.online.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 物流轨迹视图对象
 */
@Data
public class LogisticsTrackVO {
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

