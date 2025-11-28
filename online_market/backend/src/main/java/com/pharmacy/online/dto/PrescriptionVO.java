package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
<<<<<<< HEAD
 * 处方详情视图对象
=======
 * 处方视图对象
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
 */
@Data
public class PrescriptionVO {
    
    /**
<<<<<<< HEAD
     * 处方编号
=======
     * 处方ID
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private String prescriptionId;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 患者姓名
     */
    private String patientName;
    
    /**
<<<<<<< HEAD
     * 患者身份证号（脱敏）
     */
    private String patientIdCard;
    
    /**
     * 性别：1-男，2-女
     */
    private Integer gender;
    
    /**
=======
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     * 性别描述
     */
    private String genderDesc;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
<<<<<<< HEAD
     * 体重（kg）
=======
     * 体重
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private BigDecimal weight;
    
    /**
     * 疾病描述
     */
    private String diseaseDesc;
    
    /**
<<<<<<< HEAD
     * 复诊凭证图片列表
=======
     * 复诊凭证图片
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private List<String> evidenceImages;
    
    /**
<<<<<<< HEAD
     * 医生ID
     */
    private String doctorId;
    
    /**
=======
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
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
<<<<<<< HEAD
     * 状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝
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
     * 问诊会话ID
     */
    private String sessionId;
    
    /**
     * 协议同意：0-否，1-是
     */
    private Integer agreementAccepted;
    
    /**
=======
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
}

