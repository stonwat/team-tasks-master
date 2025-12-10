<template>
  <div class="model-card-container" :style="containerStyle">
    <div class="model-card" v-for="item in modelList" :key="item.id" :style="cardStyle">
      <div class="card-header">
        <h3 class="card-title">{{ item.title }}</h3>
        <span class="card-tag">预置</span>
      </div>
      <p class="card-subtitle">{{ item.subtitle }}</p>
      <div class="card-desc-wrap" v-if="item.desc">
        <p class="card-desc">{{ item.desc }}</p>
      </div>
      <div class="card-tags">
        <span class="tag" v-for="tag in item.tags" :key="tag" :class="tagClassMap[tag]">
          {{ tag }}
        </span>
      </div>
      <div class="card-footer">
        <span class="card-author">
          <i class="icon" v-if="item.authorType === 'system'">A</i>
          {{ item.author }}
        </span>
        <span class="card-meta">{{ item.updateTime }}</span>
        <span class="card-meta">{{ item.view }} 👁️</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ModelCard',
  props: {
    modelList: {
      type: Array,
      default: () => []
    },
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
    cardHeight: {
      type: String,
      default: '240px'
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
        height: this.cardHeight,
        border: '1px solid #e5e7eb',
        borderRadius: '8px',
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.05)'
      }
    }
  },
  data() {
    return {
      tagClassMap: {
        'PyTorch': 'tag-pytorch',
        '物体检测': 'tag-detection',
        '文本生成': 'tag-text',
        '视觉问答': 'tag-vision',
        '图像分类': 'tag-classification'
      },
      defaultModelList: [
        {
          id: 1,
          title: 'yolov8火焰模型',
          subtitle: 'yolov8',
          desc: '',
          tags: ['PyTorch', '物体检测'],
          author: 'admin',
          authorType: 'system',
          updateTime: '2025-10-11 18:36:04 更新',
          view: 84
        },
        {
          id: 2,
          title: '深度求索DeepSeek-R1-70B',
          subtitle: 'deepseek-70b',
          desc: '',
          tags: ['PyTorch', '文本生成'],
          author: 'admin',
          authorType: 'system',
          updateTime: '2025-10-13 16:07:32 更新',
          view: 12
        },
        {
          id: 3,
          title: '通义千问Qwen3-8B',
          subtitle: 'Qwen3-8B',
          desc: 'Qwen3 是 Qwen 系列的最新一代大型语言模型，提供了一套完整的密集型和混合专家（MoE）模型。本模型为Qwen3系列的8B版本。',
          tags: ['PyTorch', '文本生成'],
          author: 'admin',
          authorType: 'system',
          updateTime: '2025-10-14 15:34:10 更新',
          view: 9
        },
        {
          id: 4,
          title: '通义千问Qwen2.5-VL-7B-Instruct.模型镜像一体版',
          subtitle: 'Qwen2.5-VL-7B-Instruct.integrated',
          desc: '2025年1月推出的一款视觉语言模型，基于Qwen2.5系列，具有70亿参数。它经过了指令微调，能够更好地理解和遵循用户的指令，在图像和文本理解方面表现出色。',
          tags: ['PyTorch', '视觉问答'],
          author: 'admin',
          authorType: 'system',
          updateTime: '2025-10-13 17:56:17 更新',
          view: 2
        },
        {
          id: 5,
          title: '深度求索DeepSeek-R1-32B',
          subtitle: 'DeepSeek-R1-Distill-Qwen-32B',
          desc: '基于Qwen-2.5-32B模型通过知识蒸馏技术开发的320亿参数推理优化模型，它利用DeepSeek-R1生成的80万条推理数据进行监督微调，在数学、代码等复杂任务上性能接近...',
          tags: ['PyTorch', '文本生成'],
          author: 'admin',
          authorType: 'system',
          updateTime: '2025-10-14 11:29:45 更新',
          view: 1
        },
        {
          id: 6,
          title: 'ResNet',
          subtitle: 'ResNet',
          desc: 'ResNet模型是一种深度卷积神经网络，它通过引入残差学习解决了深层网络训练中的退化问题，使得构建和训练非常深的网络成为可能。',
          tags: ['PyTorch', '图像分类'],
          author: 'admin',
          authorType: 'system',
          updateTime: '2025-10-14 10:59:18 更新',
          view: 1
        }
      ]
    }
  },
  created() {
    if (this.modelList.length === 0) {
      this.modelList = this.defaultModelList
    }
  }
}
</script>

<style scoped>
.model-card-container {
  display: flex;
  flex-wrap: wrap;
  box-sizing: border-box;
  width: 100%;
  margin: 0;
}

.model-card {
  padding: 16px;
  box-sizing: border-box;
  transition: transform 0.2s ease;
  display: flex;
  flex-direction: column;
}

.model-card:hover {
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  word-break: break-word;
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
  margin-bottom: 12px;
}

.card-desc {
  font-size: 14px;
  color: #4b5563;
  margin: 0;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 12px;
}

.tag {
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  color: white;
}

.tag-pytorch {
  background-color: #ff5252;
}

.tag-detection {
  background-color: #2196f3;
}

.tag-text {
  background-color: #f50057;
}

.tag-vision {
  background-color: #4caf50;
}

.tag-classification {
  background-color: #ff9800;
}

.card-footer {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #6b7280;
  gap: 16px;
  margin-top: auto;
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
  font-style: normal;
}

@media (max-width: 768px) {
  .card-footer {
    gap: 8px;
    font-size: 11px;
  }

  .card-title {
    font-size: 16px;
  }
}
</style>