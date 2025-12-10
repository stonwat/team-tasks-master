<template>
    <div id="child-page">
        <div class="header-box">
            <span class="header-title">AI资讯</span>
        </div>
        <div class="news-carousel" :style="{ width: containerWidth, height: containerHeight }">
            <!-- 轮播容器 -->
            <div class="carousel-wrapper">
                <div class="carousel-track" :style="{
                    transform: `translateX(-${currentIndex * 100}%)`,
                    transition: 'transform 0.5s ease'
                }">
                    <div class="news-item" v-for="news in newsList" :key="news.id">
                        <a :href="news.link" target="_blank">
                            <img :src="news.imgUrl" alt="新闻图片" class="news-img">
                        </a>
                        <div class="news-content">
                            <h3 class="news-title">{{ news.title }}</h3>
                            <p class="news-desc">{{ news.description }}</p>
                            <div class="news-meta">
                                <span>{{ news.source }}</span>
                                <span>{{ news.publishTime }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 左右切换：用div承载本地图标，替代button -->
            <div class="nav-btn prev" @click="prevSlide" title="上一条新闻">
                <!-- 本地图标引用：替换为你的图标路径，支持相对/绝对路径 -->
                <img src="@/assets/icons/arrow-left.png" alt="上一页" class="arrow-icon">
            </div>
            <div class="nav-btn next" @click="nextSlide" title="下一条新闻">
                <img src="@/assets/icons/arrow-right.png" alt="下一页" class="arrow-icon">
            </div>

            <!-- 指示器 -->
            <div class="indicators">
                <span class="indicator" v-for="(item, index) in newsList.length" :key="index"
                    :class="{ active: index === currentIndex }" @click="goToSlide(index)"></span>
            </div>
        </div>
    </div>
</template>

<script>
// 脚本部分完全不变
export default {
    name: 'NewsCarousel',
    props: {
        containerWidth: { type: String, default: '100%' },
        containerHeight: { type: String, default: '100%' },
        interval: { type: Number, default: 5000 },
        newsList: {
            type: Array,
            default: () => [
                {
                    id: 1,
                    title: 'ChatGPT 群聊功能上线部分地区',
                    description: 'ChatGPT 群聊功能上线部分地区：人机共同决策讨论、不会主动“打扰”你',
                    imgUrl: require('/src/assets/images/news/chatgpt.png'),
                    link: 'https://www.ithome.com/0/897/382.htm',
                    source: 'IT之家',
                    publishTime: '2025-11-14'

                },
                {
                    id: 2,
                    title: '腾讯开源百亿参数模型KaLM-Embedding，斩获MTEB多语言榜单全球第一！',
                    description: '腾讯开源百亿参数模型KaLM-Embedding，斩获MTEB多语言榜单全球第一！',
                    imgUrl: require('/src/assets/images/news/tencent.png'),
                    link: 'https://mp.weixin.qq.com/s/IPIEu7IaaRFO-gTsJT6MHw',
                    source: '腾讯开源',
                    publishTime: '2025-11-12'
                },
                {
                    id: 3,
                    title: '英伟达投的AI创企又融资了！284亿估值',
                    description: '智东西10月30日消息，今天，据外媒《福布斯》和TFN援引知情人士报道，英国AI视频生成独角兽Synthesia完成新一轮2亿美元（折合人民币约14亿元）融资，估值达到40亿美元（折合人民币约284亿元）。',
                    imgUrl: require('/src/assets/images/news/zhidongxi.png'),
                    link: 'https://zhidx.com/p/512053.html',
                    source: '智东西',
                    publishTime: '2025-10-30'
                }
            ]
        }
    },
    data() {
        return { currentIndex: 0, autoplayTimer: null };
    },
    mounted() {
        this.startAutoplay();
        this.$el.addEventListener('mouseenter', this.stopAutoplay);
        this.$el.addEventListener('mouseleave', this.startAutoplay);
    },
    beforeDestroy() {
        this.stopAutoplay();
        this.$el.removeEventListener('mouseenter', this.stopAutoplay);
        this.$el.removeEventListener('mouseleave', this.startAutoplay);
    },
    methods: {
        nextSlide() {
            this.currentIndex = (this.currentIndex + 1) % this.newsList.length;
        },
        prevSlide() {
            this.currentIndex = (this.currentIndex - 1 + this.newsList.length) % this.newsList.length;
        },
        goToSlide(index) {
            this.currentIndex = index;
        },
        startAutoplay() {
            this.stopAutoplay();
            this.autoplayTimer = setInterval(() => this.nextSlide(), this.interval);
        },
        stopAutoplay() {
            if (this.autoplayTimer) {
                clearInterval(this.autoplayTimer);
                this.autoplayTimer = null;
            }
        }
    }
}
</script>

<style scoped>
@import '@/assets/scss/components/child-page.scss';

.news-carousel {
    position: relative;
    overflow: hidden;
    box-sizing: border-box;
}

.carousel-wrapper {
    width: 100%;
    height: 100%;
    overflow: hidden;
}

.carousel-track {
    display: flex;
    height: 100%;
}

.news-item {
    flex-shrink: 0;
    width: 100%;
    height: 100%;
    padding: 20px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
}

.news-img {
    width: 100%;
    height: 200px;
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 16px;
}

.news-content {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.news-title {
    margin: 0 0 12px 0;
    font-size: 20px;
    font-weight: 600;
    color: #1f2937;
}

.news-desc {
    margin: 0 0 16px 0;
    font-size: 14px;
    color: #6b7280;
    line-height: 1.6;
    flex: 1;
}

.news-meta {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: #9ca3af;
}

/* 导航按钮：div样式，适配本地图标 */
.nav-btn {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    /* 点击区域大小：根据图标调整，确保易点击 */
    width: 40px;
    height: 40px;
    /* 去掉原button的圆圈背景，仅保留图标点击区 */
    background: transparent;
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 10;
    transition: opacity 0.3s;
    opacity: 0.7;
    /* 避免文字选中干扰 */
    user-select: none;
}

/* 本地图标样式：控制图标大小，避免变形 */
.arrow-icon {
    width: 24px;
    height: 24px;
    object-fit: contain;
    /* 图标颜色（若图标是白色，可删除；若需深色，可加filter） */
    /* filter: invert(0.2);  // 如需深色图标，可开启此属性调整明暗 */
}

/* hover效果：提升透明度，增强交互反馈 */
.nav-btn:hover {
    opacity: 1;
    background: transparent;
    transform: translateY(-50%);
}

/* 位置调整：贴近边缘，不遮挡内容 */
.prev {
    left: 15px;
}

.next {
    right: 15px;
}

.indicators {
    position: absolute;
    bottom: 20px;
    left: 0;
    right: 0;
    display: flex;
    justify-content: center;
    gap: 8px;
}

.indicator {
    display: inline-block;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 0.5);
    cursor: pointer;
    transition: all 0.3s;
}

.indicator.active {
    width: 24px;
    border-radius: 4px;
    background-color: #fff;
}

@media (max-width: 768px) {
    .news-title {
        font-size: 18px;
    }

    .news-img {
        height: 50%;
    }

    /* 移动端适配：缩小点击区和图标 */
    .nav-btn {
        width: 32px;
        height: 32px;
    }

    .arrow-icon {
        width: 20px;
        height: 20px;
    }

    .prev {
        left: 10px;
    }

    .next {
        right: 10px;
    }
}
</style>