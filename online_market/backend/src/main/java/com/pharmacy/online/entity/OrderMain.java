package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 */
@Data
@TableName("order_main")
public class OrderMain implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 订单类型：NORMAL-普通，PRESCRIPTION-处方
     */
    private String orderType;
    
    /**
     * 订单状态
     */
    private String status;
    
    /**
     * 商品总价
     */
    private BigDecimal totalAmount;
    
    /**
     * 运费
     */
    private BigDecimal freight;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 应付金额
     */
    private BigDecimal payableAmount;
    
    /**
     * 实付金额
     */
    private BigDecimal actualAmount;
    
    /**
     * 支付方式：WECHAT-微信支付
     */
    private String paymentMethod;
    
    /**
     * 支付流水号
     */
    private String paymentNo;
    
    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;
    
    /**
     * 配送方式：COURIER-骑手配送，PICKUP-到店自提
     */
    private String deliveryMethod;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人手机
     */
    private String receiverPhone;
    
    /**
     * 收货省份
     */
    private String receiverProvince;
    
    /**
     * 收货城市
     */
    private String receiverCity;
    
    /**
     * 收货区县
     */
    private String receiverDistrict;
    
    /**
     * 收货详细地址
     */
    private String receiverAddress;
    
    /**
     * 配送时段
     */
    private String deliveryTimeSlot;
    
    /**
     * 自提门店ID
     */
    private Long storeId;
    
    /**
     * 处方ID
     */
    private String prescriptionId;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 取消原因
     */
    private String cancelReason;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 支付超时时间
     */
    private LocalDateTime paymentTimeout;
    
    /**
     * 确认收货时间
     */
    private LocalDateTime confirmTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;
}

