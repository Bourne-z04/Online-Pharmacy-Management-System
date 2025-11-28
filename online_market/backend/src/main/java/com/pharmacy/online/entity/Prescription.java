package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 处方实体类
 */
@Data
@TableName("prescription")
public class Prescription implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 处方ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 处方编号
     */
    private String prescriptionId;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 患者姓名
     */
    private String patientName;
    
    /**
     * 患者身份证号（加密）
     */
    private String patientIdCard;
    
    /**
     * 性别：1-男，2-女
     */
    private Integer gender;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 体重（kg）
     */
    private BigDecimal weight;
    
    /**
     * 疾病描述
     */
    private String diseaseDesc;
    
    /**
     * 复诊凭证图片，JSON数组
     */
    private String evidenceImages;
    
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 医生姓名
     */
    private String doctorName;
    
    /**
     * 处方图片
     */
    private String prescriptionImage;
    
    /**
     * 审核意见
     */
    private String auditOpinion;
    
    /**
     * 状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝
     */
    private String status;
    
    /**
     * 问诊会话ID
     */
    private String sessionId;
    
    /**
     * 协议同意：0-否，1-是
     */
    private Integer agreementAccepted;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

