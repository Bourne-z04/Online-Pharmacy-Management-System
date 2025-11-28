import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/Index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', icon: 'House' }
      },
      {
        path: 'product/list',
        name: 'ProductList',
        component: () => import('@/views/product/List.vue'),
        meta: { title: '商品管理', icon: 'Goods' }
      },
      {
        path: 'order/list',
        name: 'OrderList',
        component: () => import('@/views/order/List.vue'),
        meta: { title: '订单管理', icon: 'ShoppingCart' }
      },
      {
        path: 'after-sale/list',
        name: 'AfterSaleList',
        component: () => import('@/views/after-sale/List.vue'),
        meta: { title: '售后管理', icon: 'Service' }
      },
      {
        path: 'statistics/sales',
        name: 'SalesStatistics',
        component: () => import('@/views/statistics/Sales.vue'),
        meta: { title: '销售统计', icon: 'DataLine' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const token = authStore.token

  if (to.path === '/login') {
    next()
  } else {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router

