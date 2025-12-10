# TaskViz Pro - 企业级任务管理与数据可视化平台

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.5.15-green" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Vue.js-2.6.12-blue" alt="Vue.js" />
  <img src="https://img.shields.io/badge/MySQL-5.7-orange" alt="MySQL" />
  <img src="https://img.shields.io/badge/license-MIT-yellow" alt="MIT" />
</div>

## 🎯 项目简介

TaskViz Pro 是一个企业级任务管理与数据可视化平台，采用前后端分离架构，提供项目任务管理、团队协作、甘特图展示以及6大模块的可视化大屏幕展示功能。系统基于Spring Boot + Vue.js技术栈开发，集成了ECharts等主流可视化组件，适用于企业项目管理和数据展示场景。

## ✨ 核心特性

### 🔧 任务管理功能
- **项目管理** - 支持项目创建、编辑、归档、删除等全生命周期管理
- **任务分配** - 灵活的任务分配机制，支持指派负责人和协作成员
- **甘特图展示** - 基于弹性甘特图库，直观展示项目进度和任务依赖关系
- **进度跟踪** - 实时任务状态更新，支持多种进度展示方式
- **团队协作** - 完善的用户权限管理，支持RBAC权限模型

### 📊 数据可视化大屏（AI Office）
- **管理后台监控** - 系统状态和运行指标实时监控
- **模型中心展示** - AI模型管理可视化界面
- **数据平台分析** - 数据集统计分析和趋势展示
- **代理地理分布** - 代理商地理分布可视化
- **计算平台监控** - 资源使用情况和性能指标
- **电源能耗分析** - 能耗趋势分析

### 🚀 技术亮点
- **企业级架构** - 基于Spring Boot的企业级框架设计
- **响应式UI** - 支持多终端适配，1080p基准分辨率
- **丰富图表库** - 集成ECharts、DataV等专业可视化组件
- **定时调度** - 内置Quartz定时任务调度框架
- **代码生成** - 基于Velocity模板的代码生成功能
- **API文档** - 集成Swagger 3.0，提供完善的API文档

## 🏗️ 技术架构

### 后端技术栈
- **框架**: Spring Boot 2.5.15
- **数据访问**: MyBatis Plus 3.5.1
- **安全框架**: Spring Security + JWT
- **定时任务**: Quartz 2.3.2
- **API文档**: Swagger 3.0
- **数据库连接池**: Druid 1.2.8
- **构建工具**: Maven 3.8+

### 前端管理端 (web-admin)
- **主框架**: Vue.js 2.6.12
- **UI组件库**: Element UI 2.15.14
- **状态管理**: Vuex 3.6.0
- **路由管理**: Vue Router 3.4.9
- **图表库**: ECharts 5.4.0
- **甘特图**: 弹性甘特图组件
- **构建工具**: Vue CLI 4.4.6

### 数据大屏端 (web-screen)
- **主框架**: Vue.js 2.6.11
- **可视化库**: ECharts 4.6.0
- **数据可视化组件**: DataV 2.7.3
- **屏幕适配**: CSS3 Scale缩放
- **设计基准**: 1920x1080分辨率

### 数据存储
- **主数据库**: MySQL 5.7
- **缓存**: 支持Redis集成
- **连接池**: Druid连接池管理

## 📦 项目结构
```
plus-tasks-master/
├── backend/                    # 后端服务
│   ├── src/main/java/          # Java源代码
│   │   ├── com.plus.tasks/     # 核心业务代码
│   │   ├── controller/         # 控制器层
│   │   ├── service/            # 业务逻辑层
│   │   ├── mapper/             # 数据访问层
│   │   ├── entity/             # 实体类
│   │   ├── config/             # 配置类
│   │   └── utils/              # 工具类
│   ├── src/main/resources/     # 资源文件
│   │   ├── mapper/             # MyBatis映射文件
│   │   ├── application.yml     # 应用配置
│   │   └── logback-spring.xml  # 日志配置
│   └── pom.xml                 # Maven依赖配置
├── web-admin/                  # 管理后台前端
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── assets/             # 静态资源
│   │   ├── components/         # 公共组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理
│   │   ├── utils/              # 工具函数
│   │   └── views/              # 页面组件
│   ├── vue.config.js           # Vue配置
│   └── package.json            # 依赖配置
├── web-screen/                 # 数据大屏前端
│   ├── src/
│   │   ├── components/         # 可视化组件
│   │   ├── views/              # 大屏页面
│   │   └── utils/              # 工具函数
│   └── package.json            # 依赖配置
└── sql/                        # 数据库文件
    └── taskviz-pro.sql         # 数据库初始化脚本
```

## 🚀 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Node.js 14+
- npm 6+

### 1. 数据库初始化
```sql
# 创建数据库
CREATE DATABASE IF NOT EXISTS taskviz_pro DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库结构
mysql -u root -p taskviz_pro < sql/taskviz-pro.sql
```

### 2. 后端服务配置
```bash
# 进入后端目录
cd backend

# 修改配置文件 src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/taskviz_pro?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password

# 安装依赖并运行
mvn clean install
mvn spring-boot:run
```

### 3. 前端管理端配置
```bash
# 进入管理端目录
cd web-admin

# 安装依赖
npm install

# 开发环境运行
npm run dev

# 构建生产版本
npm run build
```

### 4. 数据大屏配置
```bash
# 进入大屏目录
cd web-screen

# 安装依赖
npm install

# 开发环境运行
npm run dev

# 构建生产版本
npm run build
```

## 📋 功能模块

### 工作台
- 个人待办事项
- 项目进度概览
- 快捷入口
- 数据统计卡片

### 项目管理
- 项目创建与编辑
- 项目成员管理
- 项目状态跟踪
- 项目归档功能
- 项目删除与恢复

### 任务管理
- 任务创建与分配
- 任务优先级设置
- 任务进度更新
- 任务甘特图展示
- 任务依赖关系
- 任务评论与附件

### 用户管理
- 用户注册与登录
- 角色权限管理
- 用户信息维护
- 密码修改功能
- 在线状态监控

### 数据可视化大屏
- **AI Office总览** - 6大模块统一入口
- **管理后台监控** - 系统运行状态
- **模型中心** - AI模型管理展示
- **数据平台** - 数据分析和统计
- **代理管理** - 地理分布可视化
- **计算平台** - 资源使用监控
- **电源监控** - 能耗趋势分析

## 🔑 API文档

系统提供完整的API文档，可通过以下地址访问：

- Swagger文档: http://localhost:8080/swagger-ui/index.html
- API测试: http://localhost:8080/swagger-ui/index.html

主要API模块包括：
- **用户管理API** - 用户注册、登录、权限验证
- **项目管理API** - 项目CRUD操作
- **任务管理API** - 任务分配、状态更新
- **数据可视化API** - 图表数据获取
- **系统管理API** - 用户管理、配置管理

## 🛠️ 开发指南

### 统一返回格式
```java
{
  "code": 200,
  "msg": "success",
  "data": {},
  "total": 0,
  "success": true
}
```

### 异常处理
系统采用全局异常处理机制，所有异常都会被捕获并返回标准格式的错误信息。

### 日志规范
- 使用SLF4J + Logback日志框架
- 日志级别：ERROR > WARN > INFO > DEBUG
- 日志文件按天轮转存储

### 代码规范
- 遵循阿里巴巴Java开发手册
- 前端遵循Vue.js官方风格指南
- 统一代码格式和注释规范

## 🧪 测试

### 单元测试
```bash
# 运行后端单元测试
mvn test

# 生成测试报告
mvn test jacoco:report
```

### 前端测试
```bash
# 运行前端测试
cd web-admin
npm run test

# 运行端到端测试
npm run test:e2e
```

## 📈 性能优化

### 后端优化
- 数据库连接池优化配置
- Redis缓存集成支持
- MyBatis SQL性能优化
- JVM参数调优建议

### 前端优化
- Vue Router懒加载
- 组件按需加载
- ECharts图表性能优化
- 静态资源压缩
- CDN加速支持

## 🔒 安全说明

### 身份认证
- JWT Token认证
- Session会话管理
- 密码加密存储（BCrypt）

### 权限控制
- RBAC权限模型
- 基于角色的访问控制
- API接口权限验证

### 数据安全
- SQL注入防护
- XSS攻击防护
- CSRF攻击防护
- 敏感数据脱敏

## 🤝 贡献指南

1. **Fork项目仓库**
2. **创建特性分支** (`git checkout -b feature/AmazingFeature`)
3. **提交变更** (`git commit -m 'Add some AmazingFeature'`)
4. **推送分支** (`git push origin feature/AmazingFeature`)
5. **创建Pull Request**

### 提交规范
- feat: 新功能开发
- fix: 问题修复
- docs: 文档更新
- style: 代码格式调整
- refactor: 代码重构
- test: 测试相关
- chore: 构建过程或辅助工具的变动

## 📞 支持与联系

如在使用中遇到问题，可以通过以下方式联系：

- 📧 邮箱支持: support@taskviz.com
- 💬 社区讨论: https://github.com/your-org/taskviz-pro/discussions
- 🐛 问题反馈: https://github.com/your-org/taskviz-pro/issues

## 📄 许可证

本项目基于MIT许可证开源，详见 [LICENSE](LICENSE) 文件。

## 🙏 致谢

感谢以下开源项目的贡献：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [ECharts](https://echarts.apache.org/)
- [Element UI](https://element.eleming.io/)
- [DataV](http://datav.jiaminghi.com/)

---

<div align="center">
  <p>⭐ 如果这个项目对你有帮助，请给个星标 ! ⭐</p>
  <p>Made with ❤️ by TaskViz Team</p>
</div>