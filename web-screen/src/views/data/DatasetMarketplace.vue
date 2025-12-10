<template>
  <div id="child-page">
    <div class="header-box">
      <span class="header-title">数据标注集市</span>
    </div>
    <div class="dataset-card-container" :style="containerStyle">
      <!-- 轮播展示的卡片列表：将原 datasetList 改为 displayDatasetList -->
      <div class="dataset-card" v-for="item in displayDatasetList" :key="item.id" :style="cardStyle">
        <div class="card-header">
          <h3 class="card-title">{{ item.title }}</h3>
          <span class="card-tag">已标注</span>
        </div>
        <p class="card-subtitle" v-if="item.subtitle && item.subtitle !== '-'">{{ item.subtitle }}</p>
        <!-- 描述区域加滚动，避免内容过多撑高卡片 -->
        <div class="card-desc-wrap" v-if="item.desc">
          <p class="card-desc">{{ item.desc }}</p>
        </div>
        <div class="card-label" v-if="item.label">{{ item.label }}</div>
        <!-- 占位容器，确保页脚始终在底部 -->
        <div class="card-empty-space"></div>
        <div class="card-footer">
          <span class="card-author">
            <i class="icon" v-if="item.author === 'system'">S</i>
            <i class="icon" v-else>U</i>
            {{ item.author }}
          </span>
          <span class="card-meta">{{ item.updateTime }} 更新</span>
          <span class="card-meta">{{ item.heart }} ❤️</span>
          <span class="card-meta">{{ item.view }} 👁️</span>
        </div>
      </div>
      <!-- 轮播提示（数量超出时显示，可选） -->
      <!-- <div v-if="DatasetList.length > carouselLimit" class="carousel-tip">
      <i class="el-icon-info"></i> 数据集数量超{{ carouselLimit }}个，正在顺序轮播展示
    </div> -->
    </div>
  </div>
</template>

<script>
import { getDatasetList } from '../../api/data.js';

export default {
  name: 'DatasetCard',
  props: {
    columns: {
      type: Number,
      default: 2
    },
    gap: {
      type: String,
      default: '20px'
    },
    padding: {
      type: String,
      default: '20px'
    },
    maxCardWidth: {
      type: String,
      default: 'none'
    },
    cardHeight: {
      type: String,
      default: '240px'
    },
    // 新增：轮播配置（和 DatasetCard 一致）
    carouselLimit: {
      type: Number,
      default: 6 // 页面最大显示数量
    },
    carouselInterval: {
      type: Number,
      default: 5000 // 轮播间隔（毫秒）
    }
  },
  computed: {
    containerStyle() {
      return {
        gap: this.gap,
        padding: this.padding
      }
    },
    cardStyle() {
      const gapValue = parseFloat(this.gap) || 20;
      const baseWidth = `calc(100% / ${this.columns} - ${gapValue * (this.columns - 1) / this.columns}px)`;
      return {
        width: baseWidth,
        maxWidth: this.maxCardWidth,
        height: this.cardHeight
      }
    }
  },
  data() {
    return {
      defaultDatasetList: [
        {
          id: 1,
          title: '动物分类-已标注（平台预置）',
          subtitle: '-',
          desc: '',
          label: '',
          author: 'system',
          updateTime: '2025-08-15 18:07:47 更新',
          heart: 0,
          view: 17
        },
        {
          id: 2,
          title: '车辆检测-已标注（平台预置）',
          subtitle: '-',
          desc: '',
          label: '',
          author: 'system',
          updateTime: '2025-08-15 00:00:00 更新',
          heart: 0,
          view: 7
        }
      ],
      // 新增：轮播相关数据（和 DatasetCard 逻辑对齐）
      DatasetList: [], // 存储所有数据集（父组件传递 + 默认数据）
      displayDatasetList: [], // 当前展示的卡片（轮播窗口）
      currentStartIndex: 0, // 轮播窗口起始索引
      carouselTimer: null // 轮播定时器
    }
  },
  created() {
    this.loadDatasetData();
    // 初始化轮播
    this.initCarousel();
  },
  beforeDestroy() {
    // 组件销毁时清除定时器
    this.stopCarousel();
  },
  methods: {
    // 加载模型数据
    async loadDatasetData() {
      try {
        const datasetResponse = await getDatasetList();
        const datasetRes = datasetResponse.data.data.records;
        console.log(datasetRes, '数据标注集市')
        if (Array.isArray(datasetRes) && datasetRes.length > 0) {
          // 映射接口数据为组件所需格式
          this.DatasetList = datasetRes.map(item => ({
            id: item.id,
            title: item.datasetName || '未命名数据集',
            subtitle: item.datasetNameEn || '-',
            desc: item.datasetDesc || '',
            label: item.applyTpe && item.applyTpe.length > 0 ? item.applyTpe[0].name : '',
            author: item.username || '未知',
            updateTime: item.gmtModified || '未知时间',
            heart: item.stats?.LIKE || 0,
            view: item.stats?.VIEW || 0
          }));
          console.log(this.DatasetList, 'oipipiopi')
        } else {
          // 接口无数据时使用默认数据
          this.DatasetList = this.defaultDatasetList;
          console.warn('接口返回无模型数据，使用默认数据');
        }
        // 初始化轮播
        this.initCarousel();
      } catch (error) {
        // 接口失败时使用默认数据
        this.DatasetList = this.defaultDatasetList;
        this.initCarousel();
        console.error('加载模型数据失败:', error);
      }
    },
    // 新增：初始化轮播（和 DatasetCard 一致）
    initCarousel() {
      this.updateDisplayDatasets();
      // 只有当总数超过上限时才启动轮播
      if (this.DatasetList.length > this.carouselLimit) {
        this.startCarousel();
      }
    },
    // 新增：更新当前展示的数据集（滑动窗口）
    updateDisplayDatasets() {
      const endIndex = Math.min(
        this.currentStartIndex + this.carouselLimit,
        this.DatasetList.length
      );
      this.displayDatasetList = this.DatasetList.slice(this.currentStartIndex, endIndex);
    },
    // 新增：启动轮播定时器（if-else 版本，和 DatasetCard 对齐）
    startCarousel() {
      this.stopCarousel(); // 先清除旧定时器
      this.carouselTimer = setInterval(() => {
        // 计算最后一个有效起始索引（总数量 - 最大显示数,如10：0-9，最大显示数为6，那么最后一个有效索引为4）
        const lastValidIndex = this.DatasetList.length - this.carouselLimit;

        // 判断是否到达最后一个有效索引
        if (this.currentStartIndex >= lastValidIndex) {
          // 到达末尾，重置为初始索引0
          this.currentStartIndex = 0;
        } else {
          // 未到达，索引+1（一次滑动一个卡片）
          this.currentStartIndex++;
        }

        // 更新当前展示的卡片列表
        this.updateDisplayDatasets();
      }, this.carouselInterval);
    },
    // 新增：停止轮播定时器
    stopCarousel() {
      if (this.carouselTimer) {
        clearInterval(this.carouselTimer);
        this.carouselTimer = null;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.dataset-card-container {
  display: flex;
  flex-wrap: wrap;
  box-sizing: border-box;
  width: 100%;
  margin: 0;
}

.dataset-card {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
  transition: transform 0.2s ease;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.dataset-card:hover {
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  flex-wrap: wrap;
  gap: 8px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  word-break: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-tag {
  background-color: #2563eb;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
}

.card-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 8px 0;
}

.card-desc-wrap {
  max-height: 60px;
  overflow-y: auto;
  margin-bottom: 12px;
}

.card-desc-wrap::-webkit-scrollbar {
  width: 4px;
}

.card-desc-wrap::-webkit-scrollbar-thumb {
  background-color: #e5e7eb;
  border-radius: 2px;
}

.card-desc {
  font-size: 14px;
  color: #4b5563;
  margin: 0;
  line-height: 1.5;
}

.card-label {
  display: inline-block;
  background-color: #fef3c7;
  color: #92400e;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  margin-bottom: 8px;
}

.card-empty-space {
  flex: 1;
}

.card-footer {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #6b7280;
  gap: 12px;
  flex-wrap: wrap;
  max-height: 40px;
  overflow: hidden;
}

.card-author .icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background-color: #3b82f6;
  color: white;
  text-align: center;
  line-height: 16px;
  margin-right: 4px;
}

/* 新增：轮播提示样式（和 DatasetCard 一致） */
.carousel-tip {
  margin-top: 16px;
  padding: 8px 12px;
  font-size: 14px;
  color: #6b7280;
  background-color: rgba(243, 244, 246, 0.5);
  border-radius: 4px;
  text-align: center;
}

@media (max-width: 768px) {
  .card-footer {
    gap: 8px;
    font-size: 11px;
  }

  .card-title {
    font-size: 16px;
  }

  .card-desc-wrap {
    max-height: 48px;
  }
}
</style>