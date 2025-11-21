# 百惠康大药房在线商城后端系统

## 项目概述

百惠康大药房网上药店经营系统C端后端服务，采用Spring Boot + MyBatis Plus + Redis + MySQL技术栈，实现前后端分离的RESTful API服务。

## 技术栈

- **框架**: Spring Boot 2.7.18
- **ORM**: MyBatis Plus 3.5.3.1
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **认证**: JWT
- **文件存储**: 阿里云OSS
- **第三方**: 微信小程序SDK、微信支付SDK

## 项目结构

```
online_market/backend/
├── src/main/java/com/pharmacy/online/
│   ├── common/              # 通用类
│   │   ├── Result.java      # 统一响应结果
│   │   └── PageResult.java  # 分页响应结果
│   ├── config/              # 配置类
│   │   ├── RedisConfig.java
│   │   ├── WebMvcConfig.java
│   │   └── MyBatisPlusConfig.java
│   ├── controller/          # 控制器层
│   │   ├── AuthController.java          # 认证控制器
│   │   ├── UserController.java          # 用户控制器
│   │   ├── AddressController.java       # 地址控制器
│   │   ├── ProductController.java       # 商品控制器
│   │   ├── CartController.java          # 购物车控制器
│   │   ├── OrderController.java         # 订单控制器
│   │   ├── PrescriptionController.java  # 处方控制器
│   │   └── AfterSaleController.java     # 售后控制器
│   ├── dto/                 # 数据传输对象
│   ├── entity/              # 实体类
│   ├── exception/           # 异常处理
│   ├── interceptor/         # 拦截器
│   ├── mapper/              # Mapper接口
│   ├── service/             # 服务接口
│   │   └── impl/            # 服务实现
│   └── util/                # 工具类
├── src/main/resources/
│   ├── application.yml      # 配置文件
│   ├── mapper/              # MyBatis XML
│   └── sql/                 # SQL脚本
│       └── schema.sql       # 数据库初始化脚本
└── pom.xml                  # Maven配置

## 核心功能模块

### 1. 登录验证子系统
- 微信快捷登录
- 隐私协议管理
- JWT Token认证
- 权限拦截

### 2. 用户信息管理子系统
- 个人信息查询
- 个人信息编辑
- 头像上传
- 收货地址管理（增删改查、设置默认地址、地址上限20条）

### 3. 商品浏览子系统
- 分类导航
- 商品列表（分页加载、每页20条）
- 智能搜索（模糊匹配、拼音联想、历史记录）
- 商品详情
- 处方药标识

### 4. 购物车管理子系统
- 添加商品到购物车
- 购物车列表
- 数量修改
- 批量选择和删除
- 实时价格计算

### 5. 订单结算与订单处理子系统
- 订单创建
- 地址选择
- 配送方式选择（骑手配送、到店自提）
- 金额计算（商品总价、运费、优惠）
- 支付流程（微信支付、支付超时2小时自动关闭）
- 订单查询（按状态筛选）
- 订单取消

### 6. 处方服务子系统
- 患者信息采集（实名信息、病情描述、复诊凭证）
- 在线问诊（医生匹配、图文会话、10分钟超时）
- 处方开具（5分钟超时）
- 处方审核

### 7. 配送服务子系统
- 物流查询
- 配送状态同步
- 配送进度推送
- 微信即时配送对接

### 8. 售后服务子系统
- 售后申请（退货退款）
- 售后审核（48小时超时自动同意）
- 退款处理

### 9. 库存管理
- 库存查询
- 库存锁定（乐观锁）
- 库存扣减
- 库存释放
- 库存变动流水

## 数据库设计

系统包含16张核心数据表：

1. `user` - 用户表
2. `user_address` - 收货地址表
3. `category` - 商品分类表
4. `product` - 商品表
5. `inventory` - 库存表
6. `inventory_log` - 库存变动流水表
7. `cart` - 购物车表
8. `order_main` - 订单主表
9. `order_item` - 订单明细表
10. `prescription` - 处方表
11. `consultation_session` - 问诊会话表
12. `consultation_message` - 问诊消息表
13. `logistics` - 物流信息表
14. `logistics_track` - 物流轨迹表
15. `after_sale` - 售后表
16. `search_history` - 搜索历史表

详见：`src/main/resources/sql/schema.sql`

## API接口设计

### 认证接口
- POST `/api/auth/wechat/login` - 微信登录
- POST `/api/auth/agreement/accept` - 确认隐私协议
- GET `/api/auth/agreement/content` - 获取隐私协议

### 用户接口
- GET `/api/user/info` - 获取用户信息
- PUT `/api/user/info` - 更新用户信息
- POST `/api/user/avatar` - 上传头像
- POST `/api/user/logout` - 退出登录

### 地址接口
- GET `/api/address/list` - 获取地址列表
- GET `/api/address/{id}` - 获取地址详情
- POST `/api/address` - 添加地址
- PUT `/api/address/{id}` - 更新地址
- DELETE `/api/address/{id}` - 删除地址
- PUT `/api/address/{id}/default` - 设置默认地址
- GET `/api/address/default` - 获取默认地址

### 商品接口
- GET `/api/product/list` - 商品列表
- GET `/api/product/{id}` - 商品详情
- POST `/api/product/search` - 搜索商品
- GET `/api/category/list` - 分类列表

### 购物车接口
- GET `/api/cart/list` - 购物车列表
- POST `/api/cart` - 添加到购物车
- PUT `/api/cart/{id}` - 更新购物车商品
- DELETE `/api/cart/{id}` - 删除购物车商品
- POST `/api/cart/batch-delete` - 批量删除

### 订单接口
- POST `/api/order/create` - 创建订单
- GET `/api/order/list` - 订单列表
- GET `/api/order/{orderNo}` - 订单详情
- PUT `/api/order/{orderNo}/cancel` - 取消订单
- POST `/api/order/pay` - 发起支付

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pharmacy_online_market
    username: root
    password: root
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

### 微信配置
```yaml
wechat:
  miniapp:
    app-id: your-miniapp-appid
    secret: your-miniapp-secret
  pay:
    mch-id: your-mch-id
    mch-key: your-mch-key
```

### JWT配置
```yaml
jwt:
  secret: your-secret-key
  expiration: 604800000  # 7天
```

## 运行说明

### 1. 初始化数据库
执行 `src/main/resources/sql/schema.sql` 脚本初始化数据库。

### 2. 配置参数
修改 `application.yml` 中的数据库、Redis、微信等配置。

### 3. 启动项目
```bash
mvn spring-boot:run
```

### 4. 访问接口
默认地址：http://localhost:8080/api

## 开发规范

### 1. 代码规范
- 遵循阿里巴巴Java开发规范
- 使用Lombok简化代码
- 统一使用Result包装响应
- 统一异常处理

### 2. 数据库规范
- 所有表使用逻辑删除
- 时间字段使用LocalDateTime
- 使用乐观锁处理并发
- 敏感数据加密存储

### 3. 安全规范
- 接口鉴权使用JWT
- 密码使用BCrypt加密
- SQL防注入
- XSS防护

## 待完成功能

由于代码量较大，以下功能已完成架构设计，需要补充完整实现：

1. 服务层完整实现（UserService、AddressService、ProductService等）
2. 微信支付集成
3. 阿里云OSS文件上传
4. 定时任务（支付超时、自动确认收货等）
5. 消息推送
6. 统计分析功能

## 扩展说明

系统采用模块化设计，易于扩展：

- 支持多种支付方式扩展
- 支持多种配送方式扩展
- 支持营销活动模块扩展
- 支持会员体系扩展
- 支持医保结算扩展

## 联系方式

如有问题，请联系开发团队。

---
**百惠康大药房 © 2024**

