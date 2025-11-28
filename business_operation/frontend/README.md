# 百惠康大药房 - 业务运营管理后台

## 项目说明

本项目是百惠康大药房网上药店经营系统的B端管理后台，使用Vue 3 + Vite + Element Plus开发。

## 技术栈

- **框架**: Vue 3
- **构建工具**: Vite 4
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP**: Axios
- **图表**: ECharts 5

## 项目结构

```
business_operation/frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/                # API接口
│   ├── assets/             # 资源文件
│   ├── components/         # 公共组件
│   ├── layout/             # 布局组件
│   ├── router/             # 路由配置
│   ├── store/              # 状态管理
│   ├── utils/              # 工具类
│   ├── views/              # 页面组件
│   │   ├── Login.vue       # 登录页
│   │   ├── Dashboard.vue   # 首页
│   │   ├── product/        # 商品管理
│   │   ├── order/          # 订单管理
│   │   ├── after-sale/     # 售后管理
│   │   └── statistics/     # 统计分析
│   ├── App.vue             # 根组件
│   └── main.js             # 入口文件
├── index.html              # HTML模板
├── vite.config.js          # Vite配置
└── package.json            # 依赖配置
```

## 核心功能

### 1. 登录系统
- 账号密码登录
- JWT Token认证
- 自动跳转拦截

### 2. 商品管理
- 商品列表（查询、筛选）
- 商品增删改查
- 商品上下架

### 3. 订单管理
- 订单列表（分状态查看）
- 订单详情
- 发货处理

### 4. 售后管理
- 售后申请列表
- 审核处理（同意/拒绝）
- 自动处理（48小时）

### 5. 统计分析
- 销售报表（日/周/月/年）
- 业绩统计
- TOP10商品
- Excel导出

## 开发说明

### 1. 安装依赖

```bash
cd business_operation/frontend
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问：http://localhost:3000

### 3. 构建生产版本

```bash
npm run build
```

构建产物在 `dist` 目录。

### 4. 后端配置

确保后端服务已启动（端口8081）：

```bash
cd business_operation/backend
mvn spring-boot:run
```

## 默认账号

- **账号**: admin
- **密码**: admin123

## 已完成功能

- [x] 项目基础架构
- [x] 登录页面
- [x] 主布局（侧边栏+顶部导航）
- [x] 首页数据看板
- [x] 商品管理页面框架
- [x] 订单管理页面框架
- [x] 售后管理页面框架
- [x] 销售统计页面框架
- [x] Axios请求封装
- [x] Pinia状态管理
- [x] 路由守卫

## 待补充功能

- [ ] API接口联调
- [ ] 图表数据可视化
- [ ] 表单验证
- [ ] 文件上传
- [ ] Excel导出实现
- [ ] 权限控制

## 注意事项

1. **后端地址配置**：在 `vite.config.js` 中配置代理
2. **Token存储**：使用 localStorage 存储
3. **路由守卫**：未登录自动跳转登录页
4. **统一错误处理**：Axios拦截器处理

---
**百惠康大药房 © 2024**

