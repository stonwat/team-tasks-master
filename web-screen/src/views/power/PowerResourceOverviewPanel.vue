<template>
  <div class="resource-monitoring">
    <!-- 顶部标题栏 -->
    <div class="header">
      <span class="title">今日实时数据</span>
      <i class="refresh-icon el-icon-refresh" @click="handleRefresh" />
    </div>

    <!-- 总量统计区域 -->
    <div class="total-stats">
      <div class="stat-item">
        <span class="stat-label">集群总量： {{ clusterSum }}</span>
        <!-- <span class="stat-value">{{ clusterSum }}</span> -->
      </div>
      <div class="stat-item">
        <span class="stat-label">机器总量： {{ nodeSum }}</span>
        <!-- <span class="stat-value">{{ nodeSum }}</span> -->
      </div>
      <div class="stat-item">
        <span class="stat-label">POD总量： {{ podSum }}</span>
        <!-- <span class="stat-value">{{ podSum }}</span> -->
      </div>
    </div>

    <!-- 资源使用卡片区域 -->
    <div class="resource-cards">
      <div class="card" v-for="(resource, index) in resources" :key="index">
        <div class="card-header">
          <span>{{ resource.title }}</span>
          <!-- <button type="text" size="mini" class="detail-btn" @click="showDetail(resource)">详情</button> -->
        </div>
        <div class="card-used-resource">{{ resource.usedResource }}</div>
        <!-- 进度条部分 -->
        <div class="progress-container">
          <div class="progress-fill" :style="{
            width: resource.usedResource == 0 ? 0 : `${(resource.usedResource / resource.totalResource * 100).toFixed(2)}%`,
            backgroundColor: resource.barColor
          }"></div>
          <span class="usage-text">占用{{ resource.usedResource == 0 ? 0 :
            `${(resource.usedResource / resource.totalResource * 100).toFixed(2)}%` }}</span>
        </div>
        <div class="card-meta">总量:{{ resource.totalResource }} 剩余:{{ resource.unusedResource }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPowerDataRealTiming } from '@/api/power.js';

export default {
  name: 'ResourceStatus',
  data() {
    return {
      clusterSum: '0',
      nodeSum: '0',
      podSum: '0',
      resources: [
        {
          title: 'CPU使用量（核）',
          barColor: '#2f54eb',
          // usedResource: '',// 已使用
          // usedRate: 0,// 使用率
          // totalResource: '976',// 总量
          unusedResource: '0'// 剩余
        },
        {
          title: '内存使用量（GB）',
          barColor: '#36cbcb'
        },
        {
          title: 'AI加速卡使用量（卡）',
          barColor: '#18c9c9'
        },
        {
          title: 'NPU使用量（卡）',
          barColor: '#fa8c16'
        },
        {
          title: '海光 DCU K100/未虚拟化(卡)',
          barColor: '#2f54eb'
        }
      ],

    }
  },
  mounted() {
    this.loadResourceStatus();
  },
  methods: {
    async loadResourceStatus() { // 加 async 关键字
      try {
        const response = await getPowerDataRealTiming(); // 加 await
        const apiData = response.data.data[0];
        this.clusterSum = apiData.clusterSum;
        this.nodeSum = apiData.nodeSum;
        this.podSum = apiData.podSum;

        const fieldMap = {
          "CPU使用量（核）": "cpuUsedInfo",
          "内存使用量（GB）": "memoryUsedInfo",
          "AI加速卡使用量（卡）": "gpuUsedInfo",
          "NPU使用量（卡）": "npuUsedInfo",
          "海光 DCU K100/未虚拟化(卡)": "dcuUsedInfo"
        };
        const mergedResources = this.resources.map(item => {
          const fieldName = fieldMap[item.title];
          const apiInfo = apiData[fieldName] || {};
          return { ...item, ...apiInfo };
        });

        this.resources = mergedResources;
      } catch (error) {
        console.error('加载算力资源失败：', error);
        // 重置数据
        this.clusterSum = this.nodeSum = this.podSum = '0';
        this.resources = this.resources.map(item => ({
          ...item,
          usedResource: '0',
          totalResource: '0',
          unusedResource: '0'
        }));
      }
    },
    handleRefresh() {
      this.loadResourceStatus();
      this.$message.info('数据已刷新');
    },
    showDetail(resource) {
      this.$alert(`【${resource.title}】详情：\n使用量：${resource.usedResource}\n总量：${resource.totalResource}\n剩余：${resource.unusedResource}`, '资源详情', {
        confirmButtonText: '确定'
      });
    }
  }
}
</script>

<style scoped>
.resource-monitoring {
  border-radius: 4px;
  padding: 16px;
  box-shadow: 0 0 15px rgba(100, 149, 237, 0.3);
  border: 1px solid rgba(100, 149, 237, 0.5);
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(100, 149, 237, 0.3);
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #cfe2ff;
  margin-right: 12px;
}

.refresh-icon {
  cursor: pointer;
  color: #9bbef5;
  font-size: 16px;
}

.total-stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 24px;
}

.stat-item {
  text-align: center;
  width: 30%;
}

.stat-label {
  display: block;
  font-size: 28px;
  color: #9bbef5;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
}

.stat-usedResource {
  font-size: 24px;
  font-weight: bold;
  color: #cfe2ff;
}

.resource-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.card {
  flex: 1;
  min-width: 200px;
  min-height: 240px;
  border-radius: 4px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(100, 149, 237, 0.2);
  border: 1px solid rgba(100, 149, 237, 0.3);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.card-header span {
  font-size: 16px;
  color: #9bbef5;
}

.detail-btn {
  color: #6495ed !important;
  padding: 0 !important;
}

.card-used-resource {
  font-size: 24px;
  font-weight: bold;
  color: #cfe2ff;
  margin-bottom: 12px;
}

/* 进度条样式 */
.progress-container {
  height: 25px;
  background-color: rgba(100, 149, 237, 0.2);
  border-radius: 8px;
  margin-bottom: 8px;
  position: relative;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 8px;
  transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

/* 进度条光泽动画效果 */
.progress-fill::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 50px;
  height: 100%;
  background: linear-gradient(90deg,
      rgba(255, 255, 255, 0) 0%,
      rgba(255, 255, 255, 0.3) 50%,
      rgba(255, 255, 255, 0) 100%);
  transform: translateX(-100px);
  animation: shine 2.5s infinite;
}

@keyframes shine {
  0% {
    transform: translateX(-100px);
  }

  60%,
  100% {
    transform: translateX(100%);
  }
}

.usage-text {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 14px;
  color: #fff;
  text-shadow: 0 0 2px rgba(0, 0, 0, 0.3);
  font-weight: 500;
}

.card-meta {
  font-size: 14px;
  color: #9bbef5;
}
</style>
