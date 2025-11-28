-- 百惠康大药房在线商城数据库初始化脚本

DROP DATABASE IF EXISTS pharmacy_online_market;
CREATE DATABASE pharmacy_online_market DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pharmacy_online_market;

-- 1. 用户表
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `user_id` VARCHAR(50) NOT NULL COMMENT '全局唯一用户ID',
    `open_id` VARCHAR(100) NOT NULL COMMENT '微信OpenID',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号（加密）',
    `agreement_accepted` TINYINT DEFAULT 0 COMMENT '隐私协议同意状态：0-未同意，1-已同意',
    `agreement_time` DATETIME DEFAULT NULL COMMENT '协议同意时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_open_id` (`open_id`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 收货地址表
CREATE TABLE `user_address` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人手机号',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区/县',
    `detail_address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 3. 商品分类表
CREATE TABLE `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `icon` VARCHAR(200) DEFAULT NULL COMMENT '分类图标',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 4. 商品表
CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `product_id` VARCHAR(50) NOT NULL COMMENT '商品编号',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `brand` VARCHAR(100) DEFAULT NULL COMMENT '品牌',
    `specification` VARCHAR(100) DEFAULT NULL COMMENT '规格',
    `unit` VARCHAR(20) DEFAULT NULL COMMENT '单位',
    `approval_number` VARCHAR(100) DEFAULT NULL COMMENT '批准文号',
    `manufacturer` VARCHAR(200) DEFAULT NULL COMMENT '生产厂家',
    `price` DECIMAL(10, 2) NOT NULL COMMENT '销售价格',
    `original_price` DECIMAL(10, 2) DEFAULT NULL COMMENT '原价',
    `cost_price` DECIMAL(10, 2) DEFAULT NULL COMMENT '成本价',
    `is_prescription` TINYINT DEFAULT 0 COMMENT '是否处方药：0-否，1-是',
    `main_image` VARCHAR(500) DEFAULT NULL COMMENT '主图',
    `images` TEXT DEFAULT NULL COMMENT '商品图片，JSON数组',
    `description` TEXT DEFAULT NULL COMMENT '商品描述',
    `indication` TEXT DEFAULT NULL COMMENT '适应症',
    `usage` TEXT DEFAULT NULL COMMENT '用法用量',
    `taboo` TEXT DEFAULT NULL COMMENT '禁忌',
    `sales_volume` INT DEFAULT 0 COMMENT '销量',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_id` (`product_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_product_name` (`product_name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 5. 库存表
CREATE TABLE `inventory` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '库存ID',
    `product_id` VARCHAR(50) NOT NULL COMMENT '商品编号',
    `available_stock` INT NOT NULL DEFAULT 0 COMMENT '可用库存',
    `frozen_stock` INT NOT NULL DEFAULT 0 COMMENT '冻结库存',
    `total_stock` INT NOT NULL DEFAULT 0 COMMENT '总库存',
    `warning_stock` INT DEFAULT 10 COMMENT '预警库存',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 6. 库存变动流水表
CREATE TABLE `inventory_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '流水ID',
    `product_id` VARCHAR(50) NOT NULL COMMENT '商品编号',
    `change_type` VARCHAR(20) NOT NULL COMMENT '变动类型：IN-入库，OUT-出库，FREEZE-冻结，UNFREEZE-解冻',
    `change_quantity` INT NOT NULL COMMENT '变动数量',
    `before_stock` INT NOT NULL COMMENT '变动前库存',
    `after_stock` INT NOT NULL COMMENT '变动后库存',
    `order_id` VARCHAR(50) DEFAULT NULL COMMENT '关联订单号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变动流水表';

-- 7. 购物车表
CREATE TABLE `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
    `product_id` VARCHAR(50) NOT NULL COMMENT '商品编号',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `selected` TINYINT DEFAULT 1 COMMENT '是否选中：0-否，1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 8. 订单主表
CREATE TABLE `order_main` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
    `order_type` VARCHAR(20) DEFAULT 'NORMAL' COMMENT '订单类型：NORMAL-普通，PRESCRIPTION-处方',
    `status` VARCHAR(20) NOT NULL COMMENT '订单状态',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '商品总价',
    `freight` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '运费',
    `discount_amount` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '优惠金额',
    `payable_amount` DECIMAL(10, 2) NOT NULL COMMENT '应付金额',
    `actual_amount` DECIMAL(10, 2) DEFAULT NULL COMMENT '实付金额',
    `payment_method` VARCHAR(20) DEFAULT NULL COMMENT '支付方式：WECHAT-微信支付',
    `payment_no` VARCHAR(100) DEFAULT NULL COMMENT '支付流水号',
    `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `delivery_method` VARCHAR(20) NOT NULL COMMENT '配送方式：COURIER-骑手配送，PICKUP-到店自提',
    `receiver_name` VARCHAR(50) DEFAULT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) DEFAULT NULL COMMENT '收货人手机',
    `receiver_province` VARCHAR(50) DEFAULT NULL COMMENT '收货省份',
    `receiver_city` VARCHAR(50) DEFAULT NULL COMMENT '收货城市',
    `receiver_district` VARCHAR(50) DEFAULT NULL COMMENT '收货区县',
    `receiver_address` VARCHAR(200) DEFAULT NULL COMMENT '收货详细地址',
    `delivery_time_slot` VARCHAR(50) DEFAULT NULL COMMENT '配送时段',
    `store_id` BIGINT DEFAULT NULL COMMENT '自提门店ID',
    `prescription_id` VARCHAR(50) DEFAULT NULL COMMENT '处方ID',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `cancel_reason` VARCHAR(500) DEFAULT NULL COMMENT '取消原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `payment_timeout` DATETIME DEFAULT NULL COMMENT '支付超时时间',
    `confirm_time` DATETIME DEFAULT NULL COMMENT '确认收货时间',
    `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 9. 订单明细表
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `product_id` VARCHAR(50) NOT NULL COMMENT '商品编号',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `specification` VARCHAR(100) DEFAULT NULL COMMENT '规格',
    `unit_price` DECIMAL(10, 2) NOT NULL COMMENT '单价',
    `quantity` INT NOT NULL COMMENT '数量',
    `subtotal` DECIMAL(10, 2) NOT NULL COMMENT '小计',
    `is_prescription` TINYINT DEFAULT 0 COMMENT '是否处方药：0-否，1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_no` (`order_no`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 10. 处方表
CREATE TABLE `prescription` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '处方ID',
    `prescription_id` VARCHAR(50) NOT NULL COMMENT '处方编号',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
    `patient_name` VARCHAR(50) NOT NULL COMMENT '患者姓名',
    `patient_id_card` VARCHAR(100) NOT NULL COMMENT '患者身份证号（加密）',
    `gender` TINYINT NOT NULL COMMENT '性别：1-男，2-女',
    `age` INT NOT NULL COMMENT '年龄',
    `weight` DECIMAL(5, 2) DEFAULT NULL COMMENT '体重（kg）',
    `disease_desc` TEXT NOT NULL COMMENT '疾病描述',
    `evidence_images` TEXT DEFAULT NULL COMMENT '复诊凭证图片，JSON数组',
    `doctor_id` VARCHAR(50) DEFAULT NULL COMMENT '医生ID',
    `doctor_name` VARCHAR(50) DEFAULT NULL COMMENT '医生姓名',
    `prescription_image` VARCHAR(500) DEFAULT NULL COMMENT '处方图片',
    `audit_opinion` TEXT DEFAULT NULL COMMENT '审核意见',
    `status` VARCHAR(20) NOT NULL COMMENT '状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝',
    `session_id` VARCHAR(50) DEFAULT NULL COMMENT '问诊会话ID',
    `agreement_accepted` TINYINT DEFAULT 0 COMMENT '协议同意：0-否，1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_prescription_id` (`prescription_id`),
    KEY `idx_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='处方表';

-- 11. 问诊会话表
CREATE TABLE `consultation_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `session_id` VARCHAR(50) NOT NULL COMMENT '会话编号',
    `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
    `doctor_id` VARCHAR(50) NOT NULL COMMENT '医生ID',
    `department` VARCHAR(50) NOT NULL COMMENT '科室',
    `status` VARCHAR(20) NOT NULL COMMENT '状态：WAITING-等待，CONSULTING-问诊中，FINISHED-已结束，TIMEOUT-超时',
    `queue_number` INT DEFAULT NULL COMMENT '排队号',
    `estimated_wait_time` INT DEFAULT NULL COMMENT '预计等待时间（分钟）',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_session_id` (`session_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问诊会话表';

-- 12. 问诊消息表
CREATE TABLE `consultation_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `session_id` VARCHAR(50) NOT NULL COMMENT '会话编号',
    `sender_id` VARCHAR(50) NOT NULL COMMENT '发送者ID',
    `sender_type` VARCHAR(20) NOT NULL COMMENT '发送者类型：USER-用户，DOCTOR-医生',
    `message_type` VARCHAR(20) NOT NULL COMMENT '消息类型：TEXT-文本，IMAGE-图片',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问诊消息表';

-- 13. 物流信息表
CREATE TABLE `logistics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '物流ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `delivery_platform` VARCHAR(50) NOT NULL COMMENT '配送平台',
    `delivery_no` VARCHAR(100) NOT NULL COMMENT '配送单号',
    `courier_name` VARCHAR(50) DEFAULT NULL COMMENT '骑手姓名',
    `courier_phone` VARCHAR(20) DEFAULT NULL COMMENT '骑手电话',
    `status` VARCHAR(20) NOT NULL COMMENT '状态：ACCEPTED-已接单，PICKED-已取货，DELIVERING-配送中，DELIVERED-已送达',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_delivery_no` (`delivery_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息表';

-- 14. 物流轨迹表
CREATE TABLE `logistics_track` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '轨迹ID',
    `logistics_id` BIGINT NOT NULL COMMENT '物流ID',
    `status` VARCHAR(20) NOT NULL COMMENT '状态',
    `description` VARCHAR(500) NOT NULL COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_logistics_id` (`logistics_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹表';

-- 15. 售后表
CREATE TABLE `after_sale` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '售后ID',
    `after_sale_no` VARCHAR(50) NOT NULL COMMENT '售后单号',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
    `type` VARCHAR(20) NOT NULL COMMENT '类型：REFUND-退款，RETURN-退货退款',
    `reason` VARCHAR(500) NOT NULL COMMENT '原因',
    `evidence_images` TEXT DEFAULT NULL COMMENT '凭证图片，JSON数组',
    `refund_amount` DECIMAL(10, 2) NOT NULL COMMENT '退款金额',
    `status` VARCHAR(20) NOT NULL COMMENT '状态：PENDING-待审核，APPROVED-已同意，REJECTED-已拒绝，REFUNDED-已退款',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `reject_reason` VARCHAR(500) DEFAULT NULL COMMENT '拒绝原因',
    `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_after_sale_no` (`after_sale_no`),
    KEY `idx_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='售后表';

-- 16. 搜索历史表
CREATE TABLE `search_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '历史ID',
    `user_id` VARCHAR(50) NOT NULL COMMENT '用户ID',
    `keyword` VARCHAR(100) NOT NULL COMMENT '搜索关键词',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';

-- 创建索引优化查询
CREATE INDEX idx_order_user_status ON order_main(user_id, status);
CREATE INDEX idx_order_create_time ON order_main(create_time DESC);
CREATE INDEX idx_product_category_status ON product(category_id, status);
CREATE INDEX idx_product_name_fulltext ON product(product_name);

