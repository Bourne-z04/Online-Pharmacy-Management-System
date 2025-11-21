// app.js
App({
  globalData: {
    baseUrl: 'http://localhost:8080/api',
    token: null,
    userInfo: null
  },

  onLaunch() {
    // 检查登录状态
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
      this.getUserInfo();
    }
  },

  // 获取用户信息
  getUserInfo() {
    const that = this;
    wx.request({
      url: this.globalData.baseUrl + '/user/info',
      header: {
        'Authorization': 'Bearer ' + this.globalData.token
      },
      success(res) {
        if (res.data.code === 200) {
          that.globalData.userInfo = res.data.data;
        }
      }
    });
  },

  // 检查登录
  checkLogin() {
    return this.globalData.token != null;
  },

  // 跳转登录
  navigateToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  }
});
