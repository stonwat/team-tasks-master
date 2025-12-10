<template>
    <div class="Model-card-container" :style="containerStyle">
        <!-- 轮播展示的卡片列表 -->
        <div class="Model-card" v-for="item in displayModelList" :key="item.id" :style="cardStyle">
            <div class="card-header">
                <h3 class="card-title">{{ item.name }}</h3>
                <span class="card-tag">{{ item.type || '预置' }}</span>
            </div>
            <!-- 模型版本 -->
            <p class="card-subtitle" v-if="item.version">{{ item.version }}</p>
            <!-- 描述区域（带滚动） -->
            <div class="card-desc-wrap" v-if="item.description">
                <p class="card-desc">{{ item.description }}</p>
            </div>
            <!-- 分类标签 -->
            <div class="card-tags" v-if="item.categories && item.categories.length">
                <span class="tag-item" v-for="tag in item.categories" :key="tag">{{ tag }}</span>
            </div>
            <!-- 占位容器（推页脚到底部） -->
            <div class="card-empty-space"></div>
            <div class="card-footer">
                <span class="card-author">
                    <i class="icon" v-if="item.creatorType === 'system'">S</i>
                    <i class="icon" v-else>A</i>
                    {{ item.creator || '未知' }}
                </span>
                <span class="card-meta">{{ item.updateTime }} 更新</span>
                <span class="card-meta">{{ item.likeCount }} <img src="@/assets/images/love.png" alt="喜欢"
                        class="icon-img"></span>
                <span class="card-meta">{{ item.viewCount }} <img src="@/assets/images/eyes.png" alt="浏览"
                        class="icon-img"></span>
            </div>
        </div>
        <!-- 轮播提示（数量超出时显示） -->
        <!-- <div v-if="ModelList.length > carouselLimit" class="carousel-tip">
            <i class="el-icon-info"></i> 模型数量超{{ carouselLimit }}个，正在顺序轮播展示
        </div> -->
    </div>
</template>

<script>
import { getModelList } from '../../../api/model.js';

export default {
    name: 'ModelCard',
    props: {
        // 布局配置
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
        // 轮播配置
        carouselLimit: {
            type: Number,
            default: 6 // 页面最大显示数量
        },
        carouselInterval: {
            type: Number,
            default: 5000 // 轮播间隔（毫秒）
        }
    },
    data() {
        return {
            ModelList: [], // 所有模型数据
            displayModelList: [], // 当前展示的卡片（轮播窗口）
            currentStartIndex: 0, // 轮播窗口起始索引
            carouselTimer: null, // 轮播定时器
            // 默认模型数据（接口失败时兜底）
            defaultModelList: [
                {
                    id: 1,
                    name: 'yolov8火焰模型',
                    version: 'yolov8',
                    description: '',
                    categories: ['PyTorch', '物体检测'],
                    type: '预置',
                    creator: 'admin',
                    creatorType: 'system',
                    updateTime: '2025-10-11 18:36:04',
                    likeCount: 12,
                    viewCount: 84
                },
                {
                    id: 2,
                    name: '深度求索DeepSeek-R1-70B',
                    version: 'deepseek-70b',
                    description: '',
                    categories: ['PyTorch', '文本生成'],
                    type: '预置',
                    creator: 'admin',
                    creatorType: 'system',
                    updateTime: '2025-10-13 16:07:32',
                    likeCount: 5,
                    viewCount: 12
                },
                {
                    id: 3,
                    name: '通义千问Qwen3-8B',
                    version: 'Qwen3-8B',
                    description: 'Qwen3 是 Qwen 系列的最新一代大型语言模型，提供了一套完整的密集型和混合专家（MoE）模型。',
                    categories: ['PyTorch', '文本生成'],
                    type: '预置',
                    creator: 'admin',
                    creatorType: 'system',
                    updateTime: '2025-10-14 15:34:10',
                    likeCount: 8,
                    viewCount: 9
                },
                {
                    id: 4,
                    name: 'Qwen2.5-VL-7B-Instruct',
                    version: 'Qwen2.5-VL-7B-Instruct',
                    description: '2025年1月推出的视觉语言模型，基于Qwen2.5系列，具有70亿参数。',
                    categories: ['PyTorch', '视觉问答'],
                    type: '预置',
                    creator: 'admin',
                    creatorType: 'system',
                    updateTime: '2025-10-13 17:56:17',
                    likeCount: 3,
                    viewCount: 2
                },
                {
                    id: 5,
                    name: 'DeepSeek-R1-32B',
                    version: 'DeepSeek-R1-Distill-Qwen-32B',
                    description: '基于Qwen-2.5-32B模型通过知识蒸馏技术开发的320亿参数推理优化模型。',
                    categories: ['PyTorch', '文本生成'],
                    type: '预置',
                    creator: 'admin',
                    creatorType: 'system',
                    updateTime: '2025-10-14 11:29:45',
                    likeCount: 1,
                    viewCount: 1
                },
                {
                    id: 6,
                    name: 'ResNet',
                    version: 'ResNet',
                    description: 'ResNet模型通过引入残差学习解决了深层网络训练中的退化问题。',
                    categories: ['PyTorch', '图像分类'],
                    type: '预置',
                    creator: 'admin',
                    creatorType: 'system',
                    updateTime: '2025-10-14 10:59:18',
                    likeCount: 0,
                    viewCount: 1
                },
                {
                    id: 7,
                    name: 'MobileNet',
                    version: 'MobileNet-v2',
                    description: '轻量级卷积神经网络，适用于移动设备和嵌入式系统。',
                    categories: ['TensorFlow', '图像分类'],
                    type: '预置',
                    creator: 'admin',
                    creatorType: 'system',
                    updateTime: '2025-10-15 09:23:45',
                    likeCount: 2,
                    viewCount: 5
                }
            ]
        };
    },
    computed: {
        // 容器样式（间距和内边距）
        containerStyle() {
            return {
                gap: this.gap,
                padding: this.padding
            };
        },
        // 卡片样式（宽度和高度）
        cardStyle() {
            const gapValue = parseFloat(this.gap) || 20;
            const baseWidth = `calc(100% / ${this.columns} - ${gapValue * (this.columns - 1) / this.columns}px)`;
            return {
                width: baseWidth,
                maxWidth: this.maxCardWidth,
                height: this.cardHeight
            };
        }
    },
    created() {
        this.loadModelData();
    },
    beforeDestroy() {
        // 组件销毁时清除定时器
        this.stopCarousel();
    },
    methods: {
        // 加载模型数据
        async loadModelData() {
            try {
                const modelResponse = await getModelList();
                const modelRes = modelResponse.data.data[0].records;
                if (Array.isArray(modelRes) && modelRes.length > 0) {
                    // 映射接口数据为组件所需格式
                    this.ModelList = modelRes.map(item => ({
                        id: item.id || Date.now() + Math.random(),
                        name: item.modelCnName || '未命名模型',
                        version: item.modelName || '未知版本',
                        description: item.modelDesc || '',
                        categories: [item.modelFrameworkName, item.groupName] || [],
                        type: '预置',
                        creator: item.createBy || '未知',
                        creatorType: item.creatorType || 'system',
                        updateTime: item.gmtModified || '未知时间',
                        likeCount: item.stats?.LIKE || 0,
                        viewCount: item.stats?.VIEW || 0
                    }));
                } else {
                    // 接口无数据时使用默认数据
                    this.ModelList = this.defaultModelList;
                    console.warn('接口返回无模型数据，使用默认数据');
                }
                // 初始化轮播
                this.initCarousel();
            } catch (error) {
                // 接口失败时使用默认数据
                this.ModelList = this.defaultModelList;
                this.initCarousel();
                console.error('加载模型数据失败:', error);
            }
        },
        // 初始化轮播
        initCarousel() {
            this.updateDisplayModels();
            // 只有当总数超过上限时才启动轮播
            if (this.ModelList.length > this.carouselLimit) {
                this.startCarousel();
            }
        },
        // 更新当前展示的卡片（滑动窗口）
        updateDisplayModels() {
            const endIndex = Math.min(
                this.currentStartIndex + this.carouselLimit,
                this.ModelList.length
            );
            this.displayModelList = this.ModelList.slice(this.currentStartIndex, endIndex);
        },
        // 启动轮播定时器
        startCarousel() {
            this.stopCarousel(); // 先清除旧定时器
            this.carouselTimer = setInterval(() => {
                // 索引后移（到达末尾则重置为0）
                this.currentStartIndex = (this.currentStartIndex + 1) % this.ModelList.length;
                this.updateDisplayModels();
            }, this.carouselInterval);
        },
        // 停止轮播定时器
        stopCarousel() {
            if (this.carouselTimer) {
                clearInterval(this.carouselTimer);
                this.carouselTimer = null;
            }
        }
    }
};
</script>

<style scoped>
.Model-card-container {
    display: flex;
    flex-wrap: wrap;
    box-sizing: border-box;
    width: 100%;
    margin: 0;
}

.Model-card {
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

.Model-card:hover {
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

.card-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    margin-bottom: 8px;
}

.tag-item {
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 4px;
    background-color: #f3f4f6;
    color: #4b5563;
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

.icon-img {
    width: 16px;
    height: 16px;
    vertical-align: middle;
    margin-left: 2px;
}

/* 轮播提示样式 */
.carousel-tip {
    margin-top: 16px;
    padding: 8px 12px;
    font-size: 14px;
    color: #6b7280;
    background-color: rgba(243, 244, 246, 0.5);
    border-radius: 4px;
    text-align: center;
}

/* 响应式适配 */
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