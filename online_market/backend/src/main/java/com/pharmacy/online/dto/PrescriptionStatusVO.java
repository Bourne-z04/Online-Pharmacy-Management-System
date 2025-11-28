package com.pharmacy.online.dto;

import lombok.Data;
<<<<<<< HEAD
import java.time.LocalDateTime;
=======
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874

/**
 * 处方状态视图对象
 */
@Data
public class PrescriptionStatusVO {
    
    /**
<<<<<<< HEAD
     * 处方编号
=======
     * 处方ID
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private String prescriptionId;
    
    /**
<<<<<<< HEAD
     * 订单号
     */
    private String orderNo;
    
    /**
=======
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     * 状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 审核意见
     */
    private String auditOpinion;
    
    /**
<<<<<<< HEAD
     * 审核时间
     */
    private LocalDateTime auditTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
=======
     * 处方图片
     */
    private String prescriptionImage;
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
}

