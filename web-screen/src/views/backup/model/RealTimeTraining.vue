<template>
  <div class="training-monitor-container">
    <!-- 标题栏 -->
    <div class="training-header">
      <h3 class="panel-title">实时训练任务监控</h3>
      <div class="filter-tabs">
        <span 
          class="tab-item" 
          :class="{ active: activeFilter === 'all' }"
          @click="activeFilter = 'all'"
        >
          全部任务
        </span>
        <span 
          class="tab-item" 
          :class="{ active: activeFilter === 'running' }"
          @click="activeFilter = 'running'"
        >
          运行中 ({{ runningCount }})
        </span>
        <span 
          class="tab-item" 
          :class="{ active: activeFilter === 'pending' }"
          @click="activeFilter = 'pending'"
        >
          待处理 ({{ pendingCount }})
        </span>
      </div>
    </div>

    <!-- 训练任务卡片列表 -->
    <div class="training-card">
      <div 
        class="training-item" 
        v-for="task in filteredTasks" 
        :key="task.id"
        @mouseenter="showLogPreview(task.id)"
        @mouseleave="hideLogPreview(task.id)"
      >
        <!-- 任务头部：名称 + 状态 -->
        <div class="training-header">
          <div class="training-name">{{ task.name }}</div>
          <div 
            class="training-status" 
            :class="task.status"
          >
            {{ task.status === 'running' ? '运行中' : task.status === 'pending' ? '待处理' : '已失败' }}
          </div>
        </div>

        <!-- 任务信息：模型 + 数据集 -->
        <div class="training-meta">
          <div class="meta-item">
            <i>📦</i> 模型：{{ task.modelType }}
          </div>
          <div class="meta-item">
            <i>📊</i> 数据集：{{ task.dataset }}
          </div>
        </div>

        <!-- 进度条 -->
        <div class="training-progress">
          <div 
            class="progress-bar" 
            :style="{ width: task.progress + '%' }"
          ></div>
        </div>
        <div class="progress-info">
          <span class="progress-percent">{{ task.progress }}%</span>
          <span class="progress-time">
            {{ task.estimatedTime ? `预计剩余: ${task.estimatedTime}` : '计算中...' }}
          </span>
        </div>

        <!-- 资源占用 -->
        <div class="resource-usage">
          <div class="usage-item">
            <i>🖥️</i> GPU：{{ task.gpuUsage }}%
          </div>
          <div class="usage-item">
            <i>💾</i> 内存：{{ task.memoryUsage }}
          </div>
          <div class="usage-item">
            <i>⏱️</i> 已运行：{{ task.runningTime }}
          </div>
        </div>

        <!-- 日志预览（hover显示） -->
        <div class="log-preview" v-if="task.showLog">
          <div class="log-header">最新日志</div>
          <div class="log-content">{{ task.logPreview }}</div>
          <button class="view-log-btn" @click="viewFullLog(task.id)">查看完整日志</button>
        </div>
      </div>

      <div class="empty-tip" v-if="filteredTasks.length === 0">
        暂无符合条件的训练任务
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 模拟训练任务数据
      trainingTasks: [
        { 
          id: 'TR-2025061801', 
          name: 'BERT-Base 预训练', 
          status: 'running', 
          progress: 68, 
          modelType: 'BERT-Base', 
          dataset: 'Wikipedia-2024', 
          estimatedTime: '1h 25m', 
          gpuUsage: 78, 
          memoryUsage: '12.3GB', 
          runningTime: '3h 12m',
          logPreview: '[2025-06-18 14:30:00]  epoch 12/20 - loss: 0.082 - acc: 98.2%',
          showLog: false
        },
        { 
          id: 'TR-2025061802', 
          name: 'YOLOv8 微调', 
          status: 'running', 
          progress: 32, 
          modelType: 'YOLOv8-S', 
          dataset: 'COCO-2017', 
          estimatedTime: '2h 40m', 
          gpuUsage: 65, 
          memoryUsage: '8.7GB', 
          runningTime: '1h 5m',
          logPreview: '[2025-06-18 14:15:00]  epoch 5/15 - mAP: 0.76 - speed: 12ms/frame',
          showLog: false
        },
        { 
          id: 'TR-2025061803', 
          name: 'LSTM 时序预测', 
          status: 'pending', 
          progress: 0, 
          modelType: 'LSTM-2Layer', 
          dataset: 'Weather-2024', 
          estimatedTime: 'N/A', 
          gpuUsage: 0, 
          memoryUsage: '0GB', 
          runningTime: '0m',
          logPreview: '任务等待GPU资源分配...',
          showLog: false
        },
        { 
          id: 'TR-2025061701', 
          name: 'ResNet-50 分类训练', 
          status: 'failed', 
          progress: 45, 
          modelType: 'ResNet-50', 
          dataset: 'ImageNet-Subset', 
          estimatedTime: 'N/A', 
          gpuUsage: 0, 
          memoryUsage: '0GB', 
          runningTime: '2h 30m',
          logPreview: '[2025-06-17 22:10:00]  错误：GPU内存溢出 (OOM)',
          showLog: false
        }
      ],
      activeFilter: 'all', // 筛选：all/running/pending
    };
  },
  computed: {
    // 筛选后的任务
    filteredTasks() {
      if (this.activeFilter === 'all') return this.trainingTasks;
      return this.trainingTasks.filter(task => task.status === this.activeFilter);
    },
    // 运行中任务数
    runningCount() {
      return this.trainingTasks.filter(task => task.status === 'running').length;
    },
    // 待处理任务数
    pendingCount() {
      return this.trainingTasks.filter(task => task.status === 'pending').length;
    }
  },
  mounted() {
    // 模拟进度实时更新（运行中任务）
    this.progressTimer = setInterval(() => {
      this.trainingTasks.forEach(task => {
        if (task.status === 'running' && task.progress < 100) {
          // 随机增长1-3%，避免进度一致
          task.progress = Math.min(task.progress + Math.floor(Math.random() * 3) + 1, 100);
          // 进度100%时标记任务完成（简化逻辑）
          if (task.progress === 100) {
            task.status = 'completed';
            task.estimatedTime = '已完成';
          }
        }
      });
    }, 5000); // 每5秒更新一次
  },
  beforeDestroy() {
    clearInterval(this.progressTimer);
  },
  methods: {
    // 显示日志预览
    showLogPreview(taskId) {
      const task = this.trainingTasks.find(item => item.id === taskId);
      if (task) task.showLog = true;
    },
    // 隐藏日志预览
    hideLogPreview(taskId) {
      const task = this.trainingTasks.find(item => item.id === taskId);
      if (task) task.showLog = false;
    },
    // 查看完整日志（可扩展日志弹窗）
    viewFullLog(taskId) {
      this.$message.info(`打开任务【${this.trainingTasks.find(t => t.id === taskId).name}】的完整日志`);
    }
  }
};
</script>

<style lang="scss" scoped>
.training-monitor-container {
  padding: 15px;
  height: 400px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

// 标题栏 + 筛选
.training-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .filter-tabs {
    display: flex;
    gap: 15px;

    .tab-item {
      color: #d3d6dd;
      cursor: pointer;
      font-size: 13px;
      padding: 5px 10px;
      border-radius: 4px;

      &.active {
        background-color: #1890ff;
        color: #fff;
      }

      &:hover:not(.active) {
        background-color: #171c33;
      }
    }
  }
}

// 训练任务卡片（复用主组件样式，补充细节）
.training-card {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  flex: 1;
  overflow-y: auto;
  padding-bottom: 10px;

  .training-item {
    width: calc(33.33% - 10px);
    background-color: #171c33;
    border-radius: 8px;
    padding: 15px;
    border: 1px solid #333;
    transition: transform 0.3s, box-shadow 0.3s;
    position: relative;

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
    }

    // 任务状态样式
    .training-status {
      font-size: 12px;
      padding: 2px 6px;
      border-radius: 10px;

      &.running {
        background-color: rgba(24, 144, 255, 0.2);
        color: #1890ff;
      }

      &.pending {
        background-color: rgba(255, 152, 0, 0.2);
        color: #ff9800;
      }

      &.failed {
        background-color: rgba(255, 77, 79, 0.2);
        color: #ff4d4f;
      }

      &.completed {
        background-color: rgba(51, 206, 160, 0.2);
        color: #33cea0;
      }
    }

    // 元信息
    .training-meta {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      color: #999;
      margin: 8px 0;
    }

    // 进度条
    .training-progress {
      width: 100%;
      height: 8px;
      background-color: #2c3147;
      border-radius: 4px;
      margin: 8px 0;
      overflow: hidden;

      .progress-bar {
        height: 100%;
        border-radius: 4px;
        background: linear-gradient(to right, #1890ff, #568aea);
        transition: width 1s ease;
      }
    }

    // 进度信息
    .progress-info {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      color: #d3d6dd;
      margin-bottom: 10px;
    }

    // 资源占用
    .resource-usage {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      color: #999;
      flex-wrap: wrap;
      gap: 8px;
    }

    // 日志预览（悬浮显示）
    .log-preview {
      position: absolute;
      top: 100%;
      left: 0;
      width: 100%;
      background-color: #0f1325;
      border-radius: 0 0 8px 8px;
      padding: 10px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
      z-index: 10;
      border-top: none;

      .log-header {
        font-size: 13px;
        color: #1890ff;
        margin-bottom: 5px;
        font-weight: 500;
      }

      .log-content {
        font-size: 12px;
        color: #d3d6dd;
        height: 60px;
        overflow-y: auto;
        margin-bottom: 8px;
        white-space: pre-wrap;
      }

      .view-log-btn {
        width: 100%;
        padding: 5px 0;
        background-color: #1a5cd7;
        color: #fff;
        border: none;
        border-radius: 4px;
        font-size: 12px;
        cursor: pointer;

        &:hover {
          background-color: #1890ff;
        }
      }
    }
  }

  // 空数据提示
  .empty-tip {
    width: 100%;
    text-align: center;
    color: #999;
    padding: 60px 0;
    font-size: 14px;
  }
}

// 响应式适配
@media (max-width: 1200px) {
  .training-card .training-item {
    width: calc(50% - 10px);
  }
}

@media (max-width: 768px) {
  .training-card .training-item {
    width: 100%;
  }
}
</style>