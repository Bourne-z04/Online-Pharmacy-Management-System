# 百惠康大药房在线商城 - 微信小程序前端

## 项目说明

本项目是百惠康大药房网上药店经营系统的C端微信小程序前端，基于微信云电商模板进行开发。

## 技术栈

- **框架**: 微信小程序原生开发
- **UI**: WeUI组件库
- **状态管理**: 全局App对象
- **网络请求**: 封装wx.request

## 项目结构

```
online_market/frontend/
├── pages/                  # 页面目录
│   ├── index/              # 首页
│   │   ├── index.js
│   │   ├── index.wxml
│   │   ├── index.wxss
│   │   └── index.json
│   ├── category/           # 分类页
│   ├── cart/               # 购物车
│   ├── user/               # 个人中心
│   ├── product/            # 商品详情
│   ├── order/              # 订单
│   ├── address/            # 地址管理
│   └── login/              # 登录
├── components/             # 组件目录
│   ├── product-card/       # 商品卡片组件
│   ├── cart-item/          # 购物车项组件
│   └── address-card/       # 地址卡片组件
├── utils/                  # 工具类
│   ├── request.js          # 请求封装
│   └── util.js             # 通用工具
├── images/                 # 图片资源
├── app.js                  # 小程序入口
├── app.json                # 全局配置
├── app.wxss                # 全局样式
├── sitemap.json            # 站点地图
└── project.config.json     # 项目配置
```

## 核心功能

### 1. 首页
- 轮播图展示
- 分类导航
- 热销商品
- 推荐商品
- 快速加入购物车

### 2. 分类页
- 左侧分类导航
- 右侧商品列表
- 下拉刷新、上拉加载

### 3. 商品详情
- 商品图片轮播
- 详细信息展示
- 处方药标识
- 加入购物车
- 立即购买
- 在线问诊（处方药）

### 4. 购物车
- 商品列表
- 数量修改
- 批量选择删除
- 实时计算总价
- 去结算

### 5. 订单
- 订单列表（待支付、待收货、已完成、售后）
- 订单详情
- 取消订单
- 申请售后
- 物流查询

### 6. 个人中心
- 用户信息展示
- 我的订单
- 收货地址管理
- 客服联系
- 退出登录

## 开发说明

### 1. 配置后端地址

修改 `app.js` 中的 `baseUrl`：

```javascript
globalData: {
  baseUrl: 'http://localhost:8080/api',  // 修改为实际后端地址
  token: null,
  userInfo: null
}
```

### 2. 配置AppID

在 `project.config.json` 中配置微信小程序AppID。

### 3. 运行项目

1. 安装微信开发者工具
2. 导入项目
3. 编译运行

### 4. 真机调试

1. 开启"不校验合法域名"
2. 或在微信公众平台配置服务器域名

## API接口说明

所有接口统一返回格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1234567890
}
```

### 认证接口

```
POST /auth/wechat/login     # 微信登录
POST /auth/agreement/accept # 确认隐私协议
```

### 商品接口

```
GET /product/list           # 商品列表
GET /product/{id}           # 商品详情
POST /product/search        # 搜索商品
GET /category/list          # 分类列表
```

### 购物车接口

```
GET /cart/list              # 购物车列表
POST /cart                  # 添加到购物车
PUT /cart/{id}              # 更新购物车
DELETE /cart/{id}           # 删除购物车商品
GET /cart/count             # 购物车数量
```

### 订单接口

```
POST /order/create          # 创建订单
GET /order/list             # 订单列表
GET /order/{orderNo}        # 订单详情
PUT /order/{orderNo}/cancel # 取消订单
POST /order/pay             # 支付订单
```

### 地址接口

```
GET /address/list           # 地址列表
POST /address               # 添加地址
PUT /address/{id}           # 更新地址
DELETE /address/{id}        # 删除地址
PUT /address/{id}/default   # 设置默认地址
```

## 样式规范

### 1. 颜色规范
- 主色调: `#3cc51f` (绿色)
- 文字颜色: `#333` (深色), `#999` (浅色)
- 背景色: `#f5f5f5`
- 边框色: `#e5e5e5`
- 价格色: `#ff4444`

### 2. 尺寸规范
- 基础间距: `20rpx`, `30rpx`
- 圆角: `10rpx`, `20rpx`
- 字体大小: `24rpx`, `28rpx`, `32rpx`, `36rpx`

### 3. 组件规范
- 使用flex布局
- 统一使用rpx单位
- 图片使用mode="aspectFit"或"aspectFill"

## 注意事项

1. **处方药处理**: 
   - 处方药需显示明显标识
   - 购买处方药需先问诊开方

2. **登录状态**:
   - 浏览商品无需登录
   - 加入购物车、下单需要登录

3. **地址管理**:
   - 最多保存20个地址
   - 详细地址必须包含门牌号
   - 手机号需脱敏显示

4. **订单处理**:
   - 支付超时2小时自动关闭
   - 7天自动确认收货

5. **错误处理**:
   - 401自动跳转登录
   - 网络错误提示用户

## 部署上线

1. 完善隐私协议
2. 配置合法服务器域名
3. 提交代码审核
4. 发布上线

## 测试账号

使用微信扫码登录即可。

## 联系方式

如有问题，请联系开发团队。

---
**百惠康大药房 © 2024**
