package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
<<<<<<< HEAD
 * 创建问诊会话请求DTO
=======
 * 创建问诊请求DTO
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
 */
@Data
public class ConsultationCreateRequest {
    
    /**
     * 处方ID
     */
    @NotBlank(message = "处方ID不能为空")
    private String prescriptionId;
    
    /**
     * 科室
     */
    @NotBlank(message = "科室不能为空")
    private String department;
}

