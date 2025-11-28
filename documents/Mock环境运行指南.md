# 百惠康大药房系统 - Mock环境运行指南

**文档版本**: v1.0  
**适用场景**: 本地开发、功能测试、演示展示  
**运行时长**: 约15分钟

---

## 📋 环境准备清单

### 必需软件

| 软件 | 版本要求 | 下载地址 | 用途 |
|------|----------|----------|------|
| JDK | 1.8+ | https://www.oracle.com/java/technologies/downloads/ | Java运行环境 |
| Maven | 3.6+ | https://maven.apache.org/download.cgi | 项目构建 |
| MySQL | 8.0+ | https://dev.mysql.com/downloads/mysql/ | 数据库 |
| Redis | 5.0+ | https://redis.io/download/ | 缓存服务 |
| 微信开发者工具 | 最新版 | https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html | 小程序开发 |
| Node.js | 14+ | https://nodejs.org/ | Vue项目运行 |

### 可选软件

| 软件 | 用途 |
|------|------|
| IntelliJ IDEA | Java开发IDE |
| Postman | API测试 |
| Navicat | 数据库管理 |
| VSCode | 代码编辑 |

---

## 🚀 完整运行流程

### 第一步：初始化数据库（5分钟）

#### 1.1 启动MySQL服务

**Windows**:
```bash
# 方式1：服务管理
Win + R → services.msc → 找到MySQL → 启动

# 方式2：命令行
net start MySQL80
```

**Linux/Mac**:
```bash
sudo service mysql start
# 或
brew services start mysql
```

#### 1.2 登录MySQL

```bash
mysql -u root -p
# 输入密码
```

#### 1.3 执行初始化脚本

```sql
-- 执行C端数据库脚本
source C:/Users/20189/Desktop/SE/project/online_market/backend/src/main/resources/sql/schema.sql

-- 执行B端管理员表脚本
source C:/Users/20189/Desktop/SE/project/business_operation/backend/src/main/resources/sql/admin_schema.sql

-- 验证数据库
USE pharmacy_online_market;
SHOW TABLES;
-- 应该看到18张表

-- 验证管理员账号
SELECT * FROM admin_user;
-- 应该看到默认管理员：admin
```

#### 1.4 插入测试数据（重要！）

```sql
-- 插入商品分类
INSERT INTO category (category_name, parent_id, sort_order, icon, status) VALUES
('感冒发烧', 0, 1, '/images/cat1.png', 1),
('肠胃消化', 0, 2, '/images/cat2.png', 1),
('皮肤用药', 0, 3, '/images/cat3.png', 1),
('维生素', 0, 4, '/images/cat4.png', 1);

-- 插入测试商品
INSERT INTO product (product_id, product_name, category_id, specification, unit, price, original_price, is_prescription, main_image, description, indication, status) VALUES
('P00001', '999感冒灵颗粒', 1, '10g*9袋', '盒', 15.80, 18.00, 0, '/images/product1.jpg', '用于感冒引起的头痛，发热，鼻塞，流涕等症状', '感冒、发烧、头痛', 1),
('P00002', '健胃消食片', 2, '0.8g*36片', '瓶', 12.50, 15.00, 0, '/images/product2.jpg', '健胃消食。用于脾胃虚弱所致的食积', '消化不良、食积', 1),
('P00003', '阿莫西林胶囊', 1, '0.25g*24粒', '盒', 8.90, 10.00, 1, '/images/product3.jpg', '用于敏感菌所致的各种感染', '细菌感染', 1),
('P00004', '维生素C片', 4, '100mg*100片', '瓶', 9.90, 12.00, 0, '/images/product4.jpg', '用于预防坏血病，也可用于各种急慢性传染疾病', '维生素C缺乏', 1),
('P00005', '云南白药创可贴', 3, '10片/盒', '盒', 6.50, 8.00, 0, '/images/product5.jpg', '用于小伤口、擦伤的处理', '外伤、擦伤', 1);

-- 插入库存
INSERT INTO inventory (product_id, available_stock, frozen_stock, total_stock, warning_stock, version) VALUES
('P00001', 100, 0, 100, 10, 0),
('P00002', 150, 0, 150, 10, 0),
('P00003', 80, 0, 80, 10, 0),
('P00004', 200, 0, 200, 10, 0),
('P00005', 300, 0, 300, 10, 0);

-- 验证数据
SELECT p.product_name, i.available_stock FROM product p 
LEFT JOIN inventory i ON p.product_id = i.product_id;
```

---

### 第二步：启动Redis服务（1分钟）

#### 2.1 启动Redis

**Windows**:
```bash
# 进入Redis目录
cd C:\Redis
redis-server.exe redis.windows.conf

# 或直接双击 redis-server.exe
```

**Linux/Mac**:
```bash
redis-server
# 或后台运行
redis-server --daemonize yes
```

#### 2.2 验证Redis

打开新终端：
```bash
redis-cli
ping
# 返回：PONG（说明Redis正常）

exit
```

---

### 第三步：配置后端（2分钟）

#### 3.1 配置C端后端

编辑 `online_market/backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pharmacy_online_market
    username: root
    password: your_password  # 👈 改成你的MySQL密码
  
  redis:
    host: localhost
    port: 6379
    password:                # 如果Redis有密码，填写这里
```

#### 3.2 配置B端后端

编辑 `business_operation/backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pharmacy_online_market
    username: root
    password: your_password  # 👈 改成你的MySQL密码
  
  redis:
    host: localhost
    port: 6379
    password:                # 如果Redis有密码，填写这里
```

**注意**: 微信相关配置暂时不用修改，系统已实现Mock模式。

---

### 第四步：启动C端后端（3分钟）

#### 4.1 打开终端

```bash
cd C:\Users\20189\Desktop\SE\project\online_market\backend
```

#### 4.2 启动服务

**方式1：使用Maven**
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

**方式2：使用IDE**
1. 用IDEA打开 `online_market/backend` 项目
2. 找到 `OnlineMarketApplication.java`
3. 右键 → Run

#### 4.3 验证启动成功

看到以下输出：
```
========================================
百惠康大药房在线商城后端服务启动成功！
========================================
```

#### 4.4 测试接口

**浏览器访问**:
```
http://localhost:8080/api/category/list
```

应该返回分类列表JSON数据。

**Postman测试登录**:
```bash
POST http://localhost:8080/api/auth/wechat/login
Content-Type: application/json

{
  "code": "mock_test_code_123",
  "nickname": "测试用户",
  "avatar": "https://example.com/avatar.jpg",
  "gender": 1
}
```

**成功响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": "U1234567890",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "isFirstLogin": true,
    "agreementAccepted": false,
    "nickname": "测试用户"
  }
}
```

**重要**: 复制返回的 `token`，后续测试需要使用！

---

### 第五步：启动B端后端（2分钟）

#### 5.1 打开新终端

```bash
cd C:\Users\20189\Desktop\SE\project\business_operation\backend
```

#### 5.2 启动服务

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

#### 5.3 验证启动成功

看到以下输出：
```
========================================
百惠康大药房业务运营后端服务启动成功！
========================================
```

#### 5.4 测试B端登录

**Postman测试**:
```bash
POST http://localhost:8081/admin-api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "adminId": 1,
    "username": "admin",
    "realName": "系统管理员",
    "role": "ADMIN",
    "token": "eyJhbGc..."
  }
}
```

---

### 第六步：启动C端前端（2分钟）

#### 6.1 打开微信开发者工具

1. 启动微信开发者工具
2. 选择"导入项目"
3. 项目目录：`C:\Users\20189\Desktop\SE\project\online_market\frontend`
4. AppID：使用测试号或留空

#### 6.2 配置后端地址

编辑 `online_market/frontend/app.js`：

```javascript
globalData: {
  baseUrl: 'http://localhost:8080/api',  // 确认后端地址正确
  token: null,
  userInfo: null
}
```

#### 6.3 开启调试模式

在微信开发者工具中：
1. 点击右上角"详情"
2. 勾选"不校验合法域名、web-view（业务域名）、TLS版本以及HTTPS证书"
3. 勾选"不校验安全域名、TLS版本以及HTTPS证书"

#### 6.4 编译运行

点击"编译"按钮，小程序启动成功！

#### 6.5 测试功能

1. 点击首页，查看商品列表
2. 点击搜索，搜索"感冒"
3. 点击商品，查看详情
4. 点击"加入购物车"（会提示先登录）

---

### 第七步：启动B端前端（2分钟）

#### 7.1 安装依赖

```bash
cd C:\Users\20189\Desktop\SE\project\business_operation\frontend
npm install
```

**注意**: 首次安装需要等待几分钟。

#### 7.2 启动开发服务器

```bash
npm run dev
```

#### 7.3 访问系统

浏览器打开：http://localhost:3000

#### 7.4 登录测试

- 账号：`admin`
- 密码：`admin123`

点击"登录"，进入管理后台。

---

## 🧪 Mock环境测试流程

### 测试流程1：C端用户购物流程（完整）

#### 步骤1：微信登录（Mock模式）

在微信开发者工具中，小程序会自动使用Mock登录。

**或使用Postman测试**:
```bash
POST http://localhost:8080/api/auth/wechat/login
Content-Type: application/json

{
  "code": "mock_code_001",
  "nickname": "张三",
  "gender": 1
}
```

**获得Token**: 复制响应中的 `token`

#### 步骤2：浏览商品

```bash
# 获取分类列表
GET http://localhost:8080/api/category/list

# 获取商品列表（第1页，每页20条）
GET http://localhost:8080/api/product/list?page=1&size=20

# 获取商品详情
GET http://localhost:8080/api/product/1

# 搜索商品
POST http://localhost:8080/api/product/search
Content-Type: application/json

{
  "keyword": "感冒"
}

# 获取热销商品
GET http://localhost:8080/api/product/hot?size=6
```

#### 步骤3：添加到购物车

```bash
POST http://localhost:8080/api/cart
Authorization: Bearer {你的token}
Content-Type: application/json

{
  "productId": "P00001",
  "quantity": 2
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "添加成功"
}
```

#### 步骤4：查看购物车

```bash
# 获取购物车列表
GET http://localhost:8080/api/cart/list
Authorization: Bearer {你的token}

# 获取购物车汇总
GET http://localhost:8080/api/cart/summary
Authorization: Bearer {你的token}
```

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "totalCount": 1,
    "selectedCount": 1,
    "totalAmount": 31.60,
    "hasPrescription": false,
    "allSelected": true
  }
}
```

#### 步骤5：添加收货地址

```bash
POST http://localhost:8080/api/address
Authorization: Bearer {你的token}
Content-Type: application/json

{
  "receiverName": "张三",
  "receiverPhone": "13800138000",
  "province": "浙江省",
  "city": "杭州市",
  "district": "西湖区",
  "detailAddress": "文三路XXX号XXX室",
  "isDefault": 1
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": 1
}
```

#### 步骤6：创建订单

```bash
POST http://localhost:8080/api/order/create
Authorization: Bearer {你的token}
Content-Type: application/json

{
  "addressId": 1,
  "deliveryMethod": "COURIER",
  "deliveryTimeSlot": "09:00-12:00",
  "cartIds": [1]
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderNo": "BD2024A1B2C3D4",
    "payableAmount": 36.60,
    "paymentTimeout": 1732187200000
  }
}
```

**验证库存已锁定**:
```sql
SELECT product_id, available_stock, frozen_stock FROM inventory WHERE product_id = 'P00001';
-- available_stock应该减少，frozen_stock应该增加
```

#### 步骤7：查询订单

```bash
# 订单列表
GET http://localhost:8080/api/order/list?page=1&size=10
Authorization: Bearer {你的token}

# 订单详情
GET http://localhost:8080/api/order/BD2024A1B2C3D4
Authorization: Bearer {你的token}
```

#### 步骤8：取消订单（测试库存释放）

```bash
PUT http://localhost:8080/api/order/BD2024A1B2C3D4/cancel
Authorization: Bearer {你的token}
Content-Type: application/json

{
  "reason": "不想买了"
}
```

**验证库存已释放**:
```sql
SELECT product_id, available_stock, frozen_stock FROM inventory WHERE product_id = 'P00001';
-- 库存应该恢复原值
```

---

### 测试流程2：B端管理员操作流程

#### 步骤1：管理员登录

**Postman测试**:
```bash
POST http://localhost:8081/admin-api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**或在浏览器中登录**:
访问 http://localhost:3000

#### 步骤2：查看商品列表

```bash
GET http://localhost:8081/admin-api/product/list
Authorization: Bearer {B端token}
```

#### 步骤3：上架/下架商品

```bash
# 下架商品
PUT http://localhost:8081/admin-api/product/1/offline
Authorization: Bearer {B端token}

# 上架商品
PUT http://localhost:8081/admin-api/product/1/online
Authorization: Bearer {B端token}
```

---

## 📝 完整测试用例集合

### 用例1：完整购物流程

```bash
# 1. 登录
POST http://localhost:8080/api/auth/wechat/login
{
  "code": "test_001",
  "nickname": "买家A"
}
# 👉 获取token1

# 2. 浏览商品
GET http://localhost:8080/api/product/list?page=1&size=20

# 3. 添加购物车
POST http://localhost:8080/api/cart
Authorization: Bearer {token1}
{
  "productId": "P00001",
  "quantity": 2
}

# 4. 再添加一个商品
POST http://localhost:8080/api/cart
Authorization: Bearer {token1}
{
  "productId": "P00002",
  "quantity": 1
}

# 5. 查看购物车
GET http://localhost:8080/api/cart/list
Authorization: Bearer {token1}

# 6. 修改数量
PUT http://localhost:8080/api/cart/1
Authorization: Bearer {token1}
{
  "quantity": 3
}

# 7. 添加地址
POST http://localhost:8080/api/address
Authorization: Bearer {token1}
{
  "receiverName": "张三",
  "receiverPhone": "13800138000",
  "province": "浙江省",
  "city": "杭州市",
  "district": "西湖区",
  "detailAddress": "文三路123号456室",
  "isDefault": 1
}

# 8. 创建订单
POST http://localhost:8080/api/order/create
Authorization: Bearer {token1}
{
  "addressId": 1,
  "deliveryMethod": "COURIER",
  "deliveryTimeSlot": "09:00-12:00",
  "cartIds": [1, 2]
}
# 👉 获取orderNo

# 9. 查看订单
GET http://localhost:8080/api/order/list
Authorization: Bearer {token1}
```

### 用例2：处方药购买流程

```bash
# 1. 提交患者信息
POST http://localhost:8080/api/prescription/patient-info
Authorization: Bearer {token1}
{
  "patientName": "李四",
  "idCard": "330106199001011234",
  "gender": 1,
  "age": 34,
  "weight": 70.5,
  "diseaseDesc": "感冒发烧，咳嗽，需要消炎药",
  "department": "内科",
  "agreementAccepted": 1
}
# 👉 获取prescriptionId

# 2. 创建问诊会话
POST http://localhost:8080/api/prescription/consultation/create
Authorization: Bearer {token1}
{
  "prescriptionId": "{获取的prescriptionId}",
  "department": "内科"
}

# 3. 查询处方状态
GET http://localhost:8080/api/prescription/{prescriptionId}/status
Authorization: Bearer {token1}
```

### 用例3：售后申请流程

```bash
# 1. 创建售后申请
POST http://localhost:8080/api/after-sale/create
Authorization: Bearer {token1}
{
  "orderNo": "{订单号}",
  "type": "REFUND",
  "reason": "商品有质量问题",
  "refundAmount": 31.60
}
# 👉 获取afterSaleNo

# 2. 查看售后列表
GET http://localhost:8080/api/after-sale/list?page=1&size=10
Authorization: Bearer {token1}

# 3. 查看售后详情
GET http://localhost:8080/api/after-sale/{afterSaleNo}
Authorization: Bearer {token1}
```

---

## 🔍 常见问题与解决方案

### Q1: 后端启动失败 - 数据库连接错误

**错误信息**:
```
Communications link failure
```

**解决方案**:
1. 检查MySQL是否启动：`mysql -u root -p`
2. 检查 `application.yml` 中的密码是否正确
3. 检查数据库名是否存在：`SHOW DATABASES;`
4. 检查端口3306是否被占用

### Q2: 后端启动失败 - Redis连接错误

**错误信息**:
```
Unable to connect to Redis
```

**解决方案**:
1. 启动Redis：`redis-server`
2. 测试连接：`redis-cli ping`
3. 检查端口6379是否被占用
4. 如果不需要Redis，可临时注释配置

### Q3: 端口被占用

**错误信息**:
```
Port 8080 was already in use
```

**解决方案**:
修改 `application.yml` 中的端口：
```yaml
server:
  port: 8082  # 改为其他端口
```

### Q4: Maven依赖下载慢

**解决方案**:
配置阿里云镜像，在 `~/.m2/settings.xml` 添加：
```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

### Q5: 小程序请求失败

**错误信息**:
```
request:fail
```

**解决方案**:
1. 确认后端已启动（http://localhost:8080/api/category/list 可访问）
2. 检查 `app.js` 中的 `baseUrl`
3. 确认已勾选"不校验合法域名"

### Q6: Vue项目npm install失败

**解决方案**:
使用国内镜像：
```bash
npm config set registry https://registry.npmmirror.com
npm install
```

### Q7: 商品列表为空

**原因**: 未插入测试数据

**解决方案**:
执行"第一步 1.4"中的测试数据插入SQL。

---

## 🎯 Mock环境特性说明

### 1. 微信登录Mock

系统已实现Mock模式，不需要真实微信AppID：

```java
// AuthServiceImpl.java中的Mock代码
if (wxMaService != null) {
    // 真实微信登录
    WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
    openId = session.getOpenid();
} else {
    // Mock模式（开发环境）
    openId = "test_openid_" + System.currentTimeMillis();
    log.warn("未配置微信服务，使用模拟OpenID: {}", openId);
}
```

**说明**: 任何code都可以登录成功，系统会生成模拟OpenID。

### 2. 微信支付Mock

支付功能返回模拟数据：

```java
// OrderServiceImpl.java中的Mock代码
PaymentVO payment = new PaymentVO();
payment.setPrepayId("mock_prepay_id");
payment.setPaySign("mock_sign");
```

**说明**: 支付流程可以走通，但不会真实扣款。

### 3. 文件上传Mock

文件上传到本地目录：

```yaml
# application.yml
file:
  upload:
    path: upload/  # 本地目录
```

**说明**: 不需要配置阿里云OSS，文件保存在本地。

### 4. 物流查询Mock

返回模拟物流信息：

```java
// LogisticsServiceImpl.java中的Mock代码
List<LogisticsTrackVO> tracks = new ArrayList<>();
track1.setDescription("骑手已接单");
track2.setDescription("骑手配送中");
```

**说明**: 物流轨迹为模拟数据。

---

## 📊 Mock环境数据流向图

```
用户操作 → C端小程序 → C端后端API → MySQL
                              ↓
                           Redis缓存
                              ↓
                         返回Mock数据

管理员操作 → Vue后台 → B端后端API → MySQL
                              ↓
                         返回数据
```

---

## 🎮 完整演示脚本

### 场景：完整购物演示（15分钟）

#### 准备工作（5分钟）
1. ✅ 启动MySQL
2. ✅ 启动Redis
3. ✅ 启动C端后端
4. ✅ 启动B端后端
5. ✅ 打开微信开发者工具
6. ✅ 打开浏览器（B端）

#### C端演示（5分钟）
1. **登录**：小程序自动Mock登录
2. **浏览商品**：首页查看商品列表
3. **搜索**：搜索"感冒"
4. **加购物车**：添加2个商品
5. **修改数量**：修改为3个
6. **添加地址**：添加收货地址
7. **创建订单**：提交订单
8. **查看订单**：订单列表

#### B端演示（5分钟）
1. **登录**：http://localhost:3000，admin/admin123
2. **查看首页**：数据看板
3. **商品管理**：查看商品列表
4. **订单管理**：查看订单
5. **统计分析**：查看销售数据

---

## 📝 Postman测试集合

### 导入Postman集合

创建 `pharmacy-test-collection.json`：

```json
{
  "info": {
    "name": "百惠康大药房API测试",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "C端接口",
      "item": [
        {
          "name": "微信登录",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": "http://localhost:8080/api/auth/wechat/login",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"code\": \"mock_001\",\n  \"nickname\": \"测试用户\"\n}"
            }
          }
        },
        {
          "name": "商品列表",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/product/list?page=1&size=20"
          }
        },
        {
          "name": "添加购物车",
          "request": {
            "method": "POST",
            "header": [
              {"key": "Content-Type", "value": "application/json"},
              {"key": "Authorization", "value": "Bearer {{token}}"}
            ],
            "url": "http://localhost:8080/api/cart",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"productId\": \"P00001\",\n  \"quantity\": 2\n}"
            }
          }
        }
      ]
    },
    {
      "name": "B端接口",
      "item": [
        {
          "name": "管理员登录",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": "http://localhost:8081/admin-api/auth/login",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\"\n}"
            }
          }
        }
      ]
    }
  ]
}
```

---

## ✅ Mock环境验收清单

### 系统启动验收

- [ ] MySQL服务已启动
- [ ] Redis服务已启动
- [ ] C端后端启动成功（端口8080）
- [ ] B端后端启动成功（端口8081）
- [ ] C端小程序编译成功
- [ ] B端Vue页面打开成功

### 功能验收

- [ ] C端可以Mock登录
- [ ] C端可以查看商品列表
- [ ] C端可以搜索商品
- [ ] C端可以添加购物车
- [ ] C端可以创建订单
- [ ] C端订单创建后库存正确锁定
- [ ] C端取消订单后库存正确释放
- [ ] B端可以账号密码登录
- [ ] B端可以查看商品列表
- [ ] B端可以查看订单

---

## 🎓 数据验证SQL

### 验证用户创建

```sql
SELECT user_id, nickname, open_id, create_time FROM user ORDER BY create_time DESC LIMIT 5;
```

### 验证库存变化

```sql
SELECT product_id, available_stock, frozen_stock, version FROM inventory;
```

### 验证订单创建

```sql
SELECT order_no, status, payable_amount, create_time FROM order_main ORDER BY create_time DESC LIMIT 5;
```

### 验证购物车

```sql
SELECT c.id, c.user_id, p.product_name, c.quantity 
FROM cart c 
LEFT JOIN product p ON c.product_id = p.product_id;
```

---

## 💡 Mock环境优势

1. **无需外部依赖** - 不需要微信AppID、支付账号
2. **快速测试** - 可以立即测试所有功能
3. **数据可控** - 可以自由添加测试数据
4. **易于调试** - 本地环境，方便查看日志
5. **演示友好** - 适合功能演示和展示

---

## 🔄 从Mock到生产环境

### 需要配置的真实服务

#### 1. 微信小程序
在 `application.yml` 中配置：
```yaml
wechat:
  miniapp:
    app-id: wx1234567890abcdef  # 真实AppID
    secret: your-secret-key      # 真实Secret
```

#### 2. 微信支付
```yaml
wechat:
  pay:
    mch-id: 1234567890          # 商户号
    mch-key: your-mch-key       # 商户密钥
    key-path: classpath:cert/apiclient_cert.p12
    notify-url: https://your-domain.com/api/payment/notify
```

#### 3. 阿里云OSS
```yaml
aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    bucket-name: pharmacy-online-market
```

#### 4. 短信服务（可选）
配置阿里云短信或腾讯云短信服务。

---

## 📞 技术支持

遇到问题时：

1. **查看日志文件**
   - C端：`online_market/backend/logs/online-market.log`
   - B端：`business_operation/backend/logs/business-operation.log`

2. **检查配置文件**
   - `application.yml` 中的数据库配置
   - `application.yml` 中的Redis配置

3. **验证数据**
   - 使用SQL查询验证数据是否正确

4. **参考文档**
   - 查看 `documents/快速启动指南.md`
   - 查看各模块README文档

---

## 🎉 Mock环境运行成功标志

✅ C端后端日志显示"启动成功"  
✅ B端后端日志显示"启动成功"  
✅ 访问 http://localhost:8080/api/category/list 有数据  
✅ 访问 http://localhost:3000 能打开登录页  
✅ Postman测试登录接口返回token  
✅ 微信小程序编译无错误  

---

**恭喜！Mock环境已成功运行！🎊**

现在您可以：
- 测试所有API接口
- 演示系统功能
- 进行二次开发
- 学习代码实现

---

**百惠康大药房 © 2024**

**最后更新**: 2024年11月21日







