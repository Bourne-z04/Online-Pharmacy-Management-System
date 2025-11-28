package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问诊会话实体类
 */
@Data
@TableName("consultation_session")
public class ConsultationSession implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 会话ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 会话编号
     */
    private String sessionId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 科室
     */
    private String department;
    
    /**
     * 状态：WAITING-等待，CONSULTING-问诊中，FINISHED-已结束，TIMEOUT-超时
     */
    private String status;
    
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
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

