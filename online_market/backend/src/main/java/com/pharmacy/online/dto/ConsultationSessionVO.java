package com.pharmacy.online.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 问诊会话视图对象
 */
@Data
public class ConsultationSessionVO {
    
    /**
<<<<<<< HEAD
     * 会话编号
=======
     * 会话ID
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private String sessionId;
    
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 医生姓名
     */
    private String doctorName;
    
    /**
<<<<<<< HEAD
=======
     * 医生头像
     */
    private String doctorAvatar;
    
    /**
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     * 科室
     */
    private String department;
    
    /**
     * 状态：WAITING-等待，CONSULTING-问诊中，FINISHED-已结束，TIMEOUT-超时
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 排队号
     */
    private Integer queueNumber;
    
    /**
     * 预计等待时间（分钟）
     */
    private Integer estimatedWaitTime;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
<<<<<<< HEAD
     * 创建时间
     */
    private LocalDateTime createTime;
=======
     * 剩余时间（秒）
     */
    private Integer remainingTime;
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
}

