package com.pharmacy.online.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流信息视图对象
 */
@Data
public class LogisticsVO {
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 配送平台
     */
    private String deliveryPlatform;
    
    /**
     * 配送单号
     */
    private String deliveryNo;
    
    /**
     * 骑手姓名
     */
    private String courierName;
    
    /**
     * 骑手电话
     */
    private String courierPhone;
    
    /**
<<<<<<< HEAD
     * 状态：ACCEPTED-已接单，PICKED-已取货，DELIVERING-配送中，DELIVERED-已送达
=======
     * 状态
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
<<<<<<< HEAD
     * 物流轨迹列表
=======
     * 物流轨迹
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private List<LogisticsTrackVO> tracks;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
<<<<<<< HEAD
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
=======
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
}

