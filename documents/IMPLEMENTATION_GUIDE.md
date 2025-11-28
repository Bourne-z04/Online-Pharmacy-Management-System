# 百惠康大药房在线商城 - 完整实现指南

## 系统架构说明

本系统采用**前后端分离架构**，包含以下部分：

### 1. C端（在线商城）
- **前端**: 微信小程序（online_market/frontend）
- **后端**: Spring Boot REST API（online_market/backend）

### 2. B端（业务运营）
- **前端**: Vue3 + Element Plus（business_operation/frontend）
- **后端**: Spring Boot REST API（business_operation/backend）

## 已完成模块

### ✅ C端后端基础架构
1. Maven项目配置（pom.xml）
2. Spring Boot主配置（application.yml）
3. 数据库初始化脚本（schema.sql）
4. 核心实体类（User, Product, Cart, Order等）
5. Mapper接口层
6. 统一响应结果（Result）
7. 全局异常处理
8. JWT认证机制
9. 登录验证子系统
10. 用户信息管理子系统（Controller + Service接口）

## 核心实现模式

### 1. 三层架构
```
Controller层（接收请求、参数校验）
    ↓
Service层（业务逻辑处理）
    ↓
Mapper层（数据访问）
```

### 2. 统一响应格式
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1234567890
}
```

### 3. 认证机制
```
1. 用户登录 → 生成JWT Token
2. 请求携带Token → AuthInterceptor验证
3. 验证通过 → 提取userId并注入到请求
4. Controller通过@RequestAttribute获取userId
```

## 快速实现指南

### Service层实现模板

以UserService为例：

```java
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserInfoDTO getUserInfo(String userId) {
        // 1. 查询数据
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(wrapper);
        
        // 2. 校验
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 3. 转换DTO
        UserInfoDTO dto = new UserInfoDTO();
        BeanUtils.copyProperties(user, dto);
        
        // 4. 数据脱敏
        if (user.getPhone() != null) {
            dto.setPhone(user.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }
        
        return dto;
    }
}
```

### Controller层实现模板

```java
@RestController
@RequestMapping("/api/xxx")
public class XxxController {
    
    @Autowired
    private XxxService xxxService;
    
    @GetMapping("/list")
    public Result<List<XxxVO>> getList(@RequestAttribute("userId") String userId,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "20") Integer size) {
        List<XxxVO> list = xxxService.getList(userId, page, size);
        return Result.success(list);
    }
    
    @PostMapping
    public Result<Void> create(@RequestAttribute("userId") String userId,
                                @Valid @RequestBody XxxRequest request) {
        xxxService.create(userId, request);
        return Result.success("创建成功");
    }
}
```

## 各子系统实现要点

### 1. 商品浏览子系统

**关键功能**:
- 分页查询（每页20条）
- 搜索（模糊匹配、拼音联想）
- 分类筛选
- 处方药标识

**实现要点**:
```java
// 商品列表查询
@Override
public PageResult<ProductVO> getProductList(Long categoryId, Integer page, Integer size) {
    Page<Product> pageParam = new Page<>(page, size);
    LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Product::getStatus, 1); // 只查上架商品
    if (categoryId != null) {
        wrapper.eq(Product::getCategoryId, categoryId);
    }
    wrapper.orderByDesc(Product::getSalesVolume); // 按销量排序
    
    Page<Product> result = productMapper.selectPage(pageParam, wrapper);
    // 转换VO并返回
}

// 智能搜索
@Override
public List<ProductVO> searchProduct(String keyword) {
    // 1. 保存搜索历史
    // 2. 模糊查询商品名称
    // 3. 如果结果为空，推荐热销商品
    // 4. 返回结果
}
```

### 2. 购物车管理子系统

**关键功能**:
- 添加商品（防重复、数量累加）
- 数量修改（1-99限制）
- 实时计算总价
- 批量操作

**实现要点**:
```java
// 添加到购物车
@Override
public void addToCart(String userId, String productId, Integer quantity) {
    // 1. 检查商品是否存在且上架
    // 2. 检查库存是否充足
    // 3. 查询购物车是否已有该商品
    // 4. 如果有则累加数量，否则新增
    // 5. 限制数量不超过99
}

// 计算总价
@Override
public CartSummary getCartSummary(String userId) {
    // 1. 查询已选中的购物车商品
    // 2. 关联查询商品信息
    // 3. 计算总价
    // 4. 识别是否有处方药
}
```

### 3. 订单结算子系统

**关键功能**:
- 订单创建（生成订单号、锁定库存）
- 金额计算（商品总价+运费-优惠）
- 支付流程（微信支付）
- 超时自动关闭（2小时）

**实现要点**:
```java
// 创建订单
@Override
@Transactional
public String createOrder(String userId, OrderCreateRequest request) {
    // 1. 生成订单号: "BD" + 年份 + 8位随机数
    // 2. 验证地址是否存在
    // 3. 验证商品信息和库存
    // 4. 计算金额
    // 5. 锁定库存（乐观锁）
    // 6. 创建订单主记录
    // 7. 创建订单明细记录
    // 8. 清空购物车
    // 9. 设置支付超时时间（2小时后）
    // 10. 返回订单号
}

// 库存锁定（乐观锁）
@Override
public void lockInventory(String productId, Integer quantity) {
    for (int i = 0; i < 3; i++) {  // 最多重试3次
        Inventory inventory = inventoryMapper.selectOne(...);
        if (inventory.getAvailableStock() < quantity) {
            throw new BusinessException("库存不足");
        }
        int rows = inventoryMapper.freezeStock(
            productId, quantity, inventory.getVersion()
        );
        if (rows > 0) {
            return; // 成功
        }
    }
    throw new BusinessException("库存锁定失败，请稍后重试");
}
```

### 4. 处方服务子系统

**关键功能**:
- 患者信息校验（身份证、体重范围1-300kg）
- 医生匹配（按科室、负载均衡）
- 问诊超时控制（10分钟）
- 处方开具（5分钟超时）

**实现要点**:
```java
// 创建问诊会话
@Override
public ConsultationSession createSession(String userId, PatientInfoDTO patientInfo) {
    // 1. 验证患者信息
    // 2. 匹配医生（查询在线医生，选择队列最短的）
    // 3. 计算预计等待时间
    // 4. 创建会话记录
    // 5. 启动10分钟倒计时
    // 6. 返回会话信息
}

// 医生匹配算法
private Doctor matchDoctor(String department) {
    // 1. 查询该科室在线医生
    // 2. 排除队列已满（>5人）的医生
    // 3. 按评分、响应速度排序
    // 4. 选择排名第一的医生
}
```

### 5. 库存管理

**乐观锁实现**:
```xml
<!-- InventoryMapper.xml -->
<update id="freezeStock">
    UPDATE inventory 
    SET available_stock = available_stock - #{quantity},
        frozen_stock = frozen_stock + #{quantity},
        version = version + 1
    WHERE product_id = #{productId} 
      AND available_stock >= #{quantity}
      AND version = #{version}
</update>
```

### 6. 定时任务

**支付超时自动关闭**:
```java
@Scheduled(cron = "0 */5 * * * ?")  // 每5分钟执行
public void closeTimeoutOrders() {
    // 1. 查询支付超时的订单（status=PAYING且超时2小时）
    // 2. 关闭订单
    // 3. 释放冻结库存
    // 4. 记录日志
}
```

## B端系统实现

### B端与C端的区别

1. **认证方式**: 账号密码登录（不是微信登录）
2. **权限管理**: 需要角色权限控制
3. **数据范围**: 可以查看所有数据

### B端核心功能

1. **商品管理子系统**
   - 商品增删改查
   - 上下架管理
   - 库存同步

2. **订单处理子系统**
   - 订单列表（按状态筛选）
   - 发货处理
   - 售后审核

3. **统计分析子系统**
   - 销售报表（日/周/月/年）
   - 业绩统计（排行榜、提成计算）
   - 商品销售统计（TOP100、类别占比）

### B端数据库表

需要额外创建：
```sql
-- B端用户表
CREATE TABLE `admin_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL COMMENT '账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `role` VARCHAR(20) NOT NULL COMMENT '角色',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='B端用户表';
```

## 前端实现

### C端微信小程序

**目录结构**:
```
online_market/frontend/
├── pages/
│   ├── index/          # 首页
│   ├── category/       # 分类
│   ├── cart/           # 购物车
│   ├── user/           # 个人中心
│   └── order/          # 订单
├── components/         # 组件
├── utils/             # 工具类
│   ├── request.js     # 请求封装
│   └── auth.js        # 认证工具
├── app.js
├── app.json
└── app.wxss
```

**API调用封装**:
```javascript
// utils/request.js
const request = (url, method, data) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: 'http://localhost:8080/api' + url,
      method: method,
      data: data,
      header: {
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data.data);
        } else if (res.data.code === 401) {
          // 未授权，跳转登录
          wx.navigateTo({ url: '/pages/login/login' });
        } else {
          wx.showToast({
            title: res.data.message,
            icon: 'none'
          });
          reject(res.data.message);
        }
      },
      fail: reject
    });
  });
};
```

### B端Vue管理后台

**技术栈**:
- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router
- Pinia

**目录结构**:
```
business_operation/frontend/
├── src/
│   ├── api/            # API接口
│   ├── components/     # 组件
│   ├── views/          # 页面
│   ├── router/         # 路由
│   ├── store/          # 状态管理
│   ├── utils/          # 工具类
│   ├── App.vue
│   └── main.js
├── public/
├── index.html
├── vite.config.js
└── package.json
```

## 部署说明

### 开发环境
```bash
# 后端
cd online_market/backend
mvn spring-boot:run

# 微信小程序
微信开发者工具打开 online_market/frontend

# Vue管理后台
cd business_operation/frontend
npm install
npm run dev
```

### 生产环境
```bash
# 后端打包
mvn clean package

# 部署jar包
java -jar online-market-backend-1.0.0.jar

# Vue打包
npm run build
# 将dist目录部署到Nginx
```

## 测试账号

### C端
- 使用微信小程序扫码登录

### B端
- 账号: admin
- 密码: admin123

## 注意事项

1. **微信配置**: 需要在微信公众平台配置AppID和AppSecret
2. **支付配置**: 需要开通微信支付并配置商户号
3. **OSS配置**: 需要开通阿里云OSS服务
4. **Redis**: 确保Redis服务已启动
5. **MySQL**: 先执行schema.sql初始化数据库

## 性能优化

1. **缓存**: Redis缓存商品信息、分类信息
2. **索引**: 关键字段建立索引
3. **分页**: 使用延迟关联优化
4. **连接池**: Druid连接池配置
5. **乐观锁**: 库存并发控制

## 安全措施

1. **SQL注入**: MyBatis参数化查询
2. **XSS**: 输入过滤
3. **CSRF**: Token验证
4. **密码**: BCrypt加密
5. **敏感数据**: AES加密存储

---

**开发进度**: 基础架构完成，核心模块已实现框架代码，需补充完整业务逻辑

**下一步**: 
1. 补充Service层完整实现
2. 集成微信支付
3. 实现定时任务
4. 完成前端页面开发
5. 联调测试

**估算工作量**: 
- 后端剩余开发: 3-5天
- 前端开发: 5-7天
- 测试联调: 2-3天

