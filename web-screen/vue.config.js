const path = require('path')
const resolve = dir => {
  return path.join(__dirname, dir)
}
module.exports = {
  publicPath: './',
  transpileDependencies: [],
  chainWebpack: config => {
    config.resolve.alias
      .set('_c', resolve('src/components')) // key,value自行定义，比如.set('@@', resolve('src/components'))
  },
  configureWebpack: {
    module: {
      rules: [
        {
          test: /src\/common\/map\/.*\.js$/, // 匹配地图目录下的所有 js 文件
          use: "amd-loader" // 用 amd-loader 解析 AMD 模块
        }
      ]
    }
  }
}