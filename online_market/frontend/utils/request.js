// 请求封装
const app = getApp();

/**
 * 发起请求
 */
function request(url, method = 'GET', data = {}) {
  return new Promise((resolve, reject) => {
    wx.showLoading({
      title: '加载中...',
      mask: true
    });

    wx.request({
      url: app.globalData.baseUrl + url,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': app.globalData.token ? 'Bearer ' + app.globalData.token : ''
      },
      success(res) {
        wx.hideLoading();
        
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data.data);
          } else if (res.data.code === 401) {
            // 未授权，跳转登录
            wx.showToast({
              title: '请先登录',
              icon: 'none',
              duration: 2000
            });
            setTimeout(() => {
              app.navigateToLogin();
            }, 2000);
            reject(res.data);
          } else {
            wx.showToast({
              title: res.data.message || '请求失败',
              icon: 'none',
              duration: 2000
            });
            reject(res.data);
          }
        } else {
          wx.showToast({
            title: '网络错误',
            icon: 'none'
          });
          reject(res);
        }
      },
      fail(err) {
        wx.hideLoading();
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
}

/**
 * GET请求
 */
function get(url, data = {}) {
  return request(url, 'GET', data);
}

/**
 * POST请求
 */
function post(url, data = {}) {
  return request(url, 'POST', data);
}

/**
 * PUT请求
 */
function put(url, data = {}) {
  return request(url, 'PUT', data);
}

/**
 * DELETE请求
 */
function del(url, data = {}) {
  return request(url, 'DELETE', data);
}

/**
 * 上传文件
 */
function uploadFile(url, filePath) {
  return new Promise((resolve, reject) => {
    wx.showLoading({
      title: '上传中...'
    });

    wx.uploadFile({
      url: app.globalData.baseUrl + url,
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': 'Bearer ' + app.globalData.token
      },
      success(res) {
        wx.hideLoading();
        const data = JSON.parse(res.data);
        if (data.code === 200) {
          resolve(data.data);
        } else {
          wx.showToast({
            title: data.message || '上传失败',
            icon: 'none'
          });
          reject(data);
        }
      },
      fail(err) {
        wx.hideLoading();
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
}

module.exports = {
  request,
  get,
  post,
  put,
  del,
  uploadFile
};

