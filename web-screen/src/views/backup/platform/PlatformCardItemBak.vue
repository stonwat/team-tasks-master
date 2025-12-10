<template>
  <div id="child-page" class="platform-management" :style="{ width: containerWidth, height: containerHeight }">
    <div class="header-box">
      <span class="header-title">平台管理中心</span>
      <div class="header-right">
        <button class="view-all-btn" @click="showAllPlatforms">
          查看全部平台
        </button>
        <i class="refresh-icon el-icon-refresh" @click="handleRefresh" />
      </div>
    </div>

    <!-- 平台卡片列表：绑定父组件传递的间距和内边距 -->
    <div class="body-box" :style="{ gap: cardGap, padding: bodyPadding }">
      <div class="platform-card-item" v-for="platform in platforms" :key="platform.id" :style="cardItemStyle">
        <div class="platform-card-inner" :style="{ borderColor: platform.color, height: cardHeight }"
          @click="handleCardClick(platform)">
          <!-- 右上角状态标签 -->
          <div class="platform-status-tag" :class="platform.status">
            {{ platform.status === 'active' ? '运行中' : '维护中' }}
          </div>

          <div class="platform-card-header">
            <span class="platform-card-title">{{ platform.name }}</span>
          </div>
          <div class="platform-icon" :style="{ background: 'linear-gradient(145deg, #1A5CD7 0%, #5B91E8 100%)' }">
            {{ platform.icon }}
          </div>
          <div class="platform-description">{{ platform.description }}</div>
        </div>
      </div>
    </div>
    <!-- 轮播提示文本（绑定父组件传递的阈值） -->
    <div v-if="allPlatforms.length > carouselThreshold" class="carousel-tip">
      <i class="el-icon-info"></i> 平台数量超{{ carouselThreshold }}个，正在随机轮播展示（点击“查看全部”可浏览所有平台）
    </div>
    <div id="child-modal">
      <div class="modal-mask" v-if="allPlatformsVisible" @click="closeAllPlatforms"></div>
      <div class="modal" v-if="allPlatformsVisible">
        <div class="modal-header">
          <h3 class="modal-title">所有平台列表</h3>
          <button class="modal-close" @click="closeAllPlatforms">&times;</button>
        </div>
        <div class="modal-body">
          <div class="all-platforms-list">
            <div v-for="platform in allPlatforms" :key="platform.id" class="all-platform-item">
              <div class="item-icon" :style="{ background: 'linear-gradient(145deg, #1A5CD7 0%, #5B91E8 100%)' }">
                {{ platform.icon }}
              </div>
              <div class="item-info">
                <h4 class="item-name">{{ platform.name }}</h4>
                <p class="item-desc">{{ platform.description }}</p>
                <div class="item-meta">
                  <span :class="['item-status', platform.status]">
                    {{ platform.status === 'active' ? '运行中' : '维护中' }}
                  </span>
                  <span class="item-url">访问地址：{{ platform.url }}</span>
                </div>
              </div>
              <button class="item-enter-btn" @click="handleCardClick(platform)">
                立即进入
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- 带取消选项的跳转确认弹窗 -->
    <div id="child-modal">
      <div class="modal-mask" v-if="confirmVisible" @click="handleConfirmCancel"></div>
      <div class="confirm-modal" v-if="confirmVisible">
        <div class="modal-header">
          <h3 class="modal-title">{{ confirmTitle }}</h3>
          <button class="modal-close" @click="handleConfirmCancel">&times;</button>
        </div>
        <div class="modal-body">
          <p class="confirm-message">{{ confirmMessage }}</p>
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="handleConfirmCancel">取消</button>
          <button class="confirm-btn" @click="handleConfirmOk">确认跳转</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  props: {
    // 组件整体宽高
    containerWidth: {
      type: String,
      default: '100%'
    },
    containerHeight: {
      type: String,
      default: 'auto'
    },
    // 卡片布局参数
    cardColumns: {
      type: Number,
      default: 4
    },
    cardGap: {
      type: String,
      default: '16px'
    },
    cardHeight: {
      type: String,
      default: 'auto'
    },
    // 卡片列表内边距
    bodyPadding: {
      type: String,
      default: '0 8px'
    },
    // 轮播相关配置
    carouselInterval: {
      type: Number,
      default: 5000
    },
    carouselThreshold: {
      type: Number,
      default: 16
    },
    // 控制展示的卡片数量
    showCardCount: {
      type: Number,
      default() {
        return this.carouselThreshold;
      }
    }
  },
  data() {
    return {
      allPlatforms: [
        { id: 1, name: 'AI中台', description: '模型训练与资源管理', url: 'http://136.192.111.150:32088/', icon: '📊', status: 'active', color: '#1A5CD7' },
        { id: 2, name: '智能体平台', description: '实时监控与智能告警系统', url: 'http://136.192.230.61:30050/apps', icon: '🔔', status: 'active', color: '#1A5CD7' },
        { id: 3, name: 'Dify平台', description: '工作流搭建', url: 'http://136.192.230.65:8081/', icon: '🧠', status: 'active', color: '#1A5CD7' },
        { id: 4, name: 'RagFlow', description: '知识库管理', url: 'http://136.192.230.68:8081/', icon: '⚙️', status: 'active', color: '#1A5CD7' },
        { id: 5, name: '数据集集市', description: '海量数据收集与分析', url: 'http://136.192.111.150:30188/micro/modelZoo/maasDataset', icon: '📝', status: 'active', color: '#1A5CD7' },
        { id: 6, name: '模型训练', description: '模型训练', url: 'http://136.192.111.150:30188/micro/modelZoo/maasModel/maasModelTune', icon: '🛡️', status: 'maintenance', color: '#1A5CD7' },
        { id: 7, name: '数据可视化平台', description: '多维度数据图表展示', url: '/data-visual', icon: '📈', status: 'active', color: '#1A5CD7' },
        { id: 8, name: '资源管理', description: '统一身份认证与权限管理', url: 'http://136.192.111.150:30188/micro/modelZoo/maasCluster', icon: '👥', status: 'active', color: '#1A5CD7' }
      ],
      platforms: [],
      carouselTimer: null,
      currentStartIndex: 0,
      allPlatformsVisible: false,
      confirmVisible: false,
      confirmTitle: '跳转确认',
      confirmMessage: '',
      currentPlatform: null
    };
  },
  computed: {
    cardItemStyle() {
      const gapValue = parseFloat(this.cardGap) || 16;
      const width = `calc(100% / ${this.cardColumns} - ${gapValue * (this.cardColumns - 1) / this.cardColumns}px)`;
      return {
        flex: `0 0 ${width}`,
        maxWidth: width
      };
    }
  },
  mounted() {
    this.updateDisplayPlatforms();
    if (this.allPlatforms.length > this.carouselThreshold) {
      this.startCarousel();
    }
  },
  beforeDestroy() {
    this.stopCarousel();
  },
  methods: {
    updateDisplayPlatforms() {
      // 计算结束索引：避免超出总数据长度
      const endIndex = Math.min(this.currentStartIndex + this.showCardCount, this.allPlatforms.length);
      // 按起始索引顺序截取数据，作为当前展示列表
      this.platforms = this.allPlatforms.slice(this.currentStartIndex, endIndex);
    },

    // startCarousel 方法（控制索引一次+1，到达末尾重置）
    startCarousel() {
      this.stopCarousel();
      this.carouselTimer = setInterval(() => {
        // 判断是否到达最后一个窗口（总长度 - 展示数量）
        if (this.currentStartIndex >= this.allPlatforms.length - this.showCardCount) {
          // 到达末尾，重置为初始索引
          this.currentStartIndex = 0;
        } else {
          // 未到达，索引+1（一次滑动一个）
          this.currentStartIndex++;
        }
        // 更新当前展示列表
        this.updateDisplayPlatforms();
      }, this.carouselInterval);
    },

    // handleRefresh 方法（刷新时重置索引到初始状态）
    handleRefresh() {
      this.currentStartIndex = 0; // 刷新后从第一个开始展示
      this.updateDisplayPlatforms();
      this.$message.info('平台数据已刷新');
      if (this.allPlatforms.length > this.carouselThreshold) {
        this.startCarousel();
      }
    },
    stopCarousel() {
      if (this.carouselTimer) {
        clearInterval(this.carouselTimer);
        this.carouselTimer = null;
      }
    },
    handleCardClick(platform) {
      this.currentPlatform = platform;
      if (platform.status === 'active') {
        this.confirmVisible = true;
        this.confirmMessage = `即将跳转到 ${platform.name} 平台：\n${platform.url || '暂无有效访问链接'}`;
      } else {
        this.$message.error(`${platform.name} 平台当前不可用（${platform.status === 'maintenance' ? '维护中' : '未知状态'}）`);
      }
      if (this.allPlatformsVisible) {
        this.allPlatformsVisible = false;
      }
    },
    showAllPlatforms() {
      this.allPlatformsVisible = true;
      this.stopCarousel();
      document.body.style.overflow = 'hidden';
    },
    closeAllPlatforms() {
      this.allPlatformsVisible = false;
      document.body.style.overflow = '';
      if (this.allPlatforms.length > this.carouselThreshold) {
        this.startCarousel();
      }
    },
    handleConfirmOk() {
      this.confirmVisible = false;
      if (this.currentPlatform?.url) {
        window.open(this.currentPlatform.url, '_blank');
      } else {
        this.$message.warning('该平台暂无有效访问链接');
      }
      this.currentPlatform = null;
    },
    handleConfirmCancel() {
      this.confirmVisible = false;
      this.currentPlatform = null;
    }
  }
};
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';
@import '@/assets/scss/components/child-modal.scss';

.platform-management {
  width: 100%;
  box-sizing: border-box;
}

.header-right {
  position: absolute;
  right: 0;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
}

.view-all-btn {
  padding: 0 12px;
  border: 1px solid #6495ed;
  border-radius: 4px;
  background-color: transparent;
  color: #9bbef5;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;

  &:hover {
    background-color: rgba(100, 149, 237, 0.2);
    color: #cfe2ff;
  }
}

.refresh-icon {
  cursor: pointer;
  color: #9bbef5;
  font-size: 16px;
}

.body-box {
  display: flex;
  flex-wrap: wrap;
  box-sizing: border-box;
}

.platform-card-item {
  box-sizing: border-box;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 16px rgba(100, 149, 237, 0.3);
  }
}

.platform-card-inner {
  border-radius: 4px;
  padding: 10px;
  border: 1px solid;
  background-color: rgba(15, 19, 37, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
  position: relative;
  /* 为状态标签提供定位基准 */
}

// 右上角状态标签样式
.platform-status-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  color: #fff;
  z-index: 1;
  /* 确保标签在其他内容上方 */

  &.active {
    background-color: #1A5CD7;
  }

  &.maintenance {
    background-color: #ff9800;
  }
}

.platform-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;

  .platform-card-title {
    font-size: 14px;
    color: #9bbef5;
  }
}

.platform-icon {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  margin-bottom: 12px;
  color: #fff;
  font-size: 20px;
}

.platform-description {
  font-size: 14px;
  color: #d3d6dd;
  margin-bottom: 16px;
  line-height: 1.5;
}

.carousel-tip {
  margin-top: 16px;
  padding: 8px 12px;
  font-size: 14px;
  color: #9bbef5;
  background-color: rgba(100, 149, 237, 0.1);
  border-radius: 4px;
  text-align: center;
}
</style>