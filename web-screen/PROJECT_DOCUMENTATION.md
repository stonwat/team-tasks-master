# AI Office 数据大屏项目文档

## 项目概述

AI Office 是一个基于 Vue2 的智能化办公数据可视化大屏项目。通过整合 ECharts、DataV 等数据可视化框架，实现动态数据展示和多维度业务数据监控，为企业提供直观的数据分析和决策支持。

## 技术架构

### 核心技术栈
- **前端框架**: Vue.js 2.6.11
- **路由管理**: Vue Router 3.1.5
- **状态管理**: Vuex 3.1.2
- **数据可视化框架**: ECharts 4.6.0 + @jiaminghi/data-view 2.7.3
- **UI组件库**: Element UI 2.15.14
- **图标库**: Vue-awesome 4.0.2
- **构建工具**: Vue CLI 5.0.8
- **样式预处理**: Sass/SCSS

### 开发环境
- **Node.js**: v18
- **包管理器**: pnpm
- **npm**: 9.x
- **webpack**: 4.0

## 项目结构

```
src/
├── assets/                   # 静态资源目录
│   ├── images/               # 图片资源
│   ├── icons/                # 图标文件
│   └── scss/                 # 样式文件
│       ├── index.scss        # 主页面样式
│       ├── style.scss        # 全局样式
│       ├── admin.scss        # 管理后台样式
│       ├── agent.scss        # 代理管理样式
│       ├── data.scss         # 数据平台样式
│       ├── model.scss        # 模型中心样式
│       ├── platform.scss     # 计算平台样式
│       └── power.scss        # 电源监控样式
├── components/               # 公共组件
│   └── echart/              # ECharts图表组件库
│       ├── centerLeft/       # 左侧面板图表
│       ├── centerRight/      # 右侧面板图表
│       ├── bottomLeft/       # 左下角图表
│       └── bottomRight/      # 右下角图表
├── common/                   # 公共工具和封装
│   └── echart/              # ECharts通用封装组件
│       ├── index.vue        # 图表基础封装组件
│       └── theme.json       # 默认图表主题
├── views/                    # 页面组件
│   ├── admin/               # 管理后台模块
│   ├── agent/               # 代理管理模块
│   ├── data/                # 数据平台模块
│   ├── model/               # 模型中心模块
│   ├── platform/            # 计算平台模块
│   ├── power/               # 电源监控模块
│   └── backup/              # 备份和演示文件
├── router/                   # 路由配置
│   └── index.js             # 路由配置主文件
├── store/                    # Vuex状态管理
│   └── index.js             # 状态管理主文件
└── utils/                    # 工具函数
    ├── userDraw.js          # 屏幕适配工具
    └── resizeMixins.js      # 窗口变化监听混合
```

## 核心功能模块

### 1. 管理后台 (Admin)
**路径**: `/admin`

#### 功能组件
- **AdminOverviewPanel**: 管理员概览面板，显示系统整体运行状态
- **AdminKeyTaskProgress**: 关键任务进度监控，跟踪重要任务完成情况
- **AdminProjectList**: 项目列表展示，管理所有项目的基本信息
- **AdminTodoList**: 管理员待办事项清单

#### 数据展示
- 系统状态概览
- 任务完成统计
- 项目进度监控
- 待办事项提醒

### 2. 模型中心 (Model)
**路径**: `/model`

#### 功能组件
- **ModelLibrary**: 模型库管理，管理所有AI模型
- **ModelMarketplace**: 模型市场，展示可选的AI模型
- **RealTimeTraining**: 实时训练监控，显示模型训练状态
- **ModelPerformanceChart**: 模型性能图表，展示模型性能指标

#### 数据展示
- 模型数量统计
- 模型类型饼图
- 训练进度监控
- 性能指标趋势

### 3. 数据平台 (Data)
**路径**: `/data`

#### 功能组件
- **Data**: 数据平台主界面，展示数据总体情况
- **DatasetOverviewPanel**: 数据集概览面板
- **DatasetMarketplace**: 数据集市场，提供各种数据集
- **DatasetDetail**: 数据集详情展示

#### 数据展示
- 数据集分类统计
- 数据量趋势图
- 数据集质量评估
- 数据使用统计

### 4. 代理管理 (Agent)
**路径**: `/agent`

#### 功能组件
- **Agent**: 代理管理主界面，展示代理总体情况
- **AgentStatisticPieChart**: 代理统计饼图，显示代理分布
- **AgentStatisticMap**: 代理统计地图，地理分布展示

#### 数据展示
- 代理数量统计
- 代理类型分布
- 地理分布地图
- 代理状态监控

### 5. 计算平台 (Platform)
**路径**: `/platform`

#### 功能组件
- **Platform**: 计算平台主界面
- **PlatformCardItem**: 平台卡片项目展示，显示计算资源

#### 数据展示
- 计算资源使用情况
- 平台性能指标
- 资源分配统计

### 6. 电源监控 (Power)
**路径**: `/power`

#### 功能组件
- **Power**: 电源监控主界面
- **PowerResourceMonitoringLineChart**: 电源资源监控线图
- **PowerResourceOverviewPanel**: 电源资源概览面板

#### 数据展示
- 电源使用情况线图
- 功率消耗统计
- 电源设备状态
- 能耗趋势分析

## 技术特点

### 1. 响应式设计
- **屏幕适配**: 采用 CSS3 `scale` 缩放适配方案
- **基准尺寸**: 1920x1080px（支持同比例屏幕100%填充）
- **自适应算法**: 非同比例屏幕自动计算比例居中填充
- **实现文件**: `src/utils/userDraw.js`

### 2. 组件化开发
- **图表封装**: 所有 ECharts 图表基于 `common/echart/index.vue` 封装
- **动态渲染**: 监听数据和屏幕变化，实时更新图表
- **防抖优化**: 使用防抖函数控制更新频率，节省性能
- **主题配置**: 统一图表主题文件 `common/echart/theme.json`

### 3. 可视化实现
- **混合布局**: 结合 ECharts 图形和 DataV 边框组件
- **交互效果**: 支持图表切换、数据筛选、实时刷新
- **自定义组件**: 封装地图组件、饼图、折线图、柱状图等

### 4. 模块化架构
- **功能分离**: 各业务模块独立开发、互不影响
- **路由懒加载**: 使用动态导入优化加载性能
- **按需引入**: Vue-awesome 图标按需加载，减小包体积

## 开发指南

### 环境准备
```bash
# 安装 Node.js (推荐 v18)
# 全局安装 pnpm
npm install -g pnpm
```

### 项目初始化
```bash
# 克隆项目
git clone [项目地址]

# 进入项目目录
cd vue-big-screen

# 安装依赖
pnpm install
```

### 开发命令
```bash
# 启动开发服务器
npm run serve

# 构建生产版本
npm run build

# 代码检查和修复
npm run lint
```

### 项目配置
- **开发端口**: 默认 8080
- **打包路径**: 相对路径 (`./`)
- **地图模块**: AMD 模块加载器专门处理
- **浏览器兼容**: 支持主流浏览器最新两个版本

## 图表组件说明

### ECharts 封装组件
**路径**: `src/common/echart/index.vue`

#### 支持参数
| 参数名 | 类型 | 说明 | 必需 |
|-------|------|------|------|
| id | String | 图表 DOM 元素 ID | 否 |
| className | String | 自定义样式类名 | 否 |
| options | Object | ECharts 配置项 | 是 |
| height | String | 图表高度 | 建议填写 |
| width | String | 图表宽度 | 建议填写 |

#### 使用示例
```vue
<template>
  <Echart :options="chartOptions" id="myChart" height="300px" width="100%" />
</template>

<script>
import Echart from '@/common/echart'

export default {
  components: { Echart },
  data() {
    return {
      chartOptions: {
        // ECharts 配置项
      }
    }
  }
}
</script>
```

### 响应式设计
使用 `resizeMixins.js` 实现图表自适应，自动监听窗口大小变化并调整图表尺寸。

## 模块开发指南

### 添加新图表
1. 在对应的 `components/echart/` 目录下创建组件文件夹
2. 创建 `index.vue` 和 `chart.vue` 文件
3. `index.vue` 负责数据处理，`chart.vue` 负责图表渲染
4. 在对应的业务模块中引入使用

### 添加新模块
1. 在 `views/` 目录下创建新模块文件夹
2. 创建模块主组件文件
3. 在 `router/index.js` 中配置路由
4. 更新导航菜单等入口组件

### 样式规范
- 每个模块使用独立的 SCSS 文件，路径在 `assets/scss/`
- 全局通用样式使用 `style.scss`
- 遵循 BEM 命名规范
- 使用变量管理常用颜色和尺寸

## 性能优化

### 1. 打包优化
- Vue-awesome 改为按需引入
- 组件按需加载（路由懒加载）
- 静态资源优化处理

### 2. 运行时优化
- 图表防抖更新
- 响应式监听优化
- 内存泄漏处理

### 3. 适配优化
- rem 改为 px 单位 + scale 缩放方案
- 提升渲染性能和兼容性

## 维护建议

### 代码规范
1. 按照现有项目风格编写代码
2. 遵循 Vue 单文件组件规范
3. SCSS 样式模块化分组
4. 重要逻辑添加注释说明

### 性能监控
1. 定期检查图表渲染性能
2. 监控内存使用情况
3. 优化大数据展示处理

### 数据接口
项目目前为静态数据展示，实际需要对接后端时可：
- 在 `main.js` 中配置 axios
- 添加统一的数据请求管理
- 实现数据的实时更新

### 开发建议
1. 全屏展示效果最佳（F11）
2. 使用现代浏览器开发调试
3. 注意不同屏幕尺寸适配测试
4. 合理使用图表动画效果

---

## 更新历史
- v1.5.1: 优化适配方案，改进性能，添加新模块
- 兼容高版本环境配置

## 相关链接
- [Vue 官方文档](https://cn.vuejs.org/v2/guide/)
- [DataV 官方文档](http://datav.jiaminghi.com/guide/)
- [ECharts 实例](https://echarts.apache.org/examples/zh/index.html)
- [ECharts API 文档](https://echarts.apache.org/zh/api.html)

**项目Git仓库**: [git仓库地址]
**文档更新日期**: 2024年

---

*最后更新时间: $(date)*