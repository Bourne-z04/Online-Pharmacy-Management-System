// pages/index/index.js
const request = require('../../utils/request');

Page({
  data: {
    banners: [],
    categories: [],
    hotProducts: [],
    recommendProducts: []
  },

  onLoad() {
    this.loadBanners();
    this.loadCategories();
    this.loadHotProducts();
    this.loadRecommendProducts();
  },

  onShow() {
    // 刷新购物车角标
    this.updateCartBadge();
  },

  // 加载轮播图
  loadBanners() {
    // 模拟数据
    this.setData({
      banners: [
        { id: 1, image: '/images/banner1.jpg' },
        { id: 2, image: '/images/banner2.jpg' },
        { id: 3, image: '/images/banner3.jpg' }
      ]
    });
  },

  // 加载分类
  loadCategories() {
    request.get('/category/list').then(res => {
      this.setData({
        categories: res.slice(0, 8) // 只显示8个
      });
    }).catch(err => {
      console.error('加载分类失败', err);
    });
  },

  // 加载热销商品
  loadHotProducts() {
    request.get('/product/hot', { size: 6 }).then(res => {
      this.setData({
        hotProducts: res
      });
    }).catch(err => {
      console.error('加载热销商品失败', err);
    });
  },

  // 加载推荐商品
  loadRecommendProducts() {
    request.get('/product/recommend', { size: 10 }).then(res => {
      this.setData({
        recommendProducts: res
      });
    }).catch(err => {
      console.error('加载推荐商品失败', err);
    });
  },

  // 跳转搜索
  goToSearch() {
    wx.navigateTo({
      url: '/pages/search/search'
    });
  },

  // 跳转分类
  goToCategory(e) {
    const id = e.currentTarget.dataset.id;
    wx.switchTab({
      url: '/pages/category/category'
    });
  },

  // 跳转商品详情
  goToProductDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/product/detail?id=${id}`
    });
  },

  // 添加到购物车
  addToCart(e) {
    const app = getApp();
    if (!app.checkLogin()) {
      app.navigateToLogin();
      return;
    }

    const id = e.currentTarget.dataset.id;
    request.post('/cart', {
      productId: id,
      quantity: 1
    }).then(res => {
      wx.showToast({
        title: '已加入购物车',
        icon: 'success'
      });
      this.updateCartBadge();
    }).catch(err => {
      console.error('添加购物车失败', err);
    });
  },

  // 更新购物车角标
  updateCartBadge() {
    const app = getApp();
    if (!app.checkLogin()) {
      return;
    }

    request.get('/cart/count').then(count => {
      if (count > 0) {
        wx.setTabBarBadge({
          index: 2,
          text: count.toString()
        });
      } else {
        wx.removeTabBarBadge({
          index: 2
        });
      }
    }).catch(err => {
      console.error('获取购物车数量失败', err);
    });
  }
});

