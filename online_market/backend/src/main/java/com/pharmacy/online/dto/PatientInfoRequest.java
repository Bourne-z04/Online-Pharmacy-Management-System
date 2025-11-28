package com.pharmacy.online.dto;

import lombok.Data;
<<<<<<< HEAD
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
=======
import javax.validation.constraints.*;
import java.math.BigDecimal;
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874

/**
 * 患者信息请求DTO
 */
@Data
public class PatientInfoRequest {
    
    /**
<<<<<<< HEAD
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    
    /**
     * 患者姓名
     */
    @NotBlank(message = "患者姓名不能为空")
    private String patientName;
    
    /**
     * 患者身份证号
     */
    @NotBlank(message = "患者身份证号不能为空")
    private String patientIdCard;
=======
     * 患者姓名
     */
    @NotBlank(message = "患者姓名不能为空")
    @Size(max = 50, message = "患者姓名长度不能超过50个字符")
    private String patientName;
    
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", 
             message = "身份证号格式不正确")
    private String idCard;
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
    
    /**
     * 性别：1-男，2-女
     */
    @NotNull(message = "性别不能为空")
<<<<<<< HEAD
=======
    @Min(value = 1, message = "性别参数错误")
    @Max(value = 2, message = "性别参数错误")
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
    private Integer gender;
    
    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
<<<<<<< HEAD
=======
    @Min(value = 1, message = "年龄不能小于1")
    @Max(value = 150, message = "年龄不能大于150")
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
    private Integer age;
    
    /**
     * 体重（kg）
     */
    @NotNull(message = "体重不能为空")
<<<<<<< HEAD
=======
    @DecimalMin(value = "1.0", message = "体重不能小于1kg")
    @DecimalMax(value = "300.0", message = "体重不能大于300kg")
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
    private BigDecimal weight;
    
    /**
     * 疾病描述
     */
    @NotBlank(message = "疾病描述不能为空")
<<<<<<< HEAD
    private String diseaseDesc;
    
    /**
     * 复诊凭证图片列表
     */
    private List<String> evidenceImages;
=======
    @Size(min = 10, max = 500, message = "疾病描述长度应为10-500个字符")
    private String diseaseDesc;
    
    /**
     * 科室
     */
    @NotBlank(message = "科室不能为空")
    private String department;
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
    
    /**
     * 协议同意：0-否，1-是
     */
<<<<<<< HEAD
    @NotNull(message = "请同意相关协议")
=======
    @NotNull(message = "请同意《互联网医院问诊协议》")
    @Min(value = 1, message = "请同意《互联网医院问诊协议》")
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
    private Integer agreementAccepted;
}

