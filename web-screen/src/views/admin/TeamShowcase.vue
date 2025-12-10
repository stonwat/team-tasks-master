<template>
    <div id="child-page">
        <div class="header-box">
            <span class="header-title">团队风采</span>
        </div>

        <div class="team-showcase" :style="{ width: containerWidth, height: containerHeight }">
            <!-- 轮播轨道 -->
            <div class="carousel-wrapper">
                <div class="carousel-track" :style="{
                    transform: `translateX(-${currentIndex * 100}%)`,
                    transition: 'transform 0.5s ease'
                }">
                    <div class="activity-item" v-for="item in activityList" :key="item.id">
                        <img :src="item.cover" alt="活动封面" class="activity-cover" />
                        <div class="activity-info">
                            <h3 class="activity-title">{{ item.title }}</h3>
                            <p class="activity-desc">{{ item.description }}</p>
                            <span class="activity-time">{{ item.date }}</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 左右箭头 -->
            <div class="nav-btn prev" @click="prevSlide" title="上一活动">
                <img src="@/assets/icons/arrow-left.png" class="arrow-icon" />
            </div>
            <div class="nav-btn next" @click="nextSlide" title="下一活动">
                <img src="@/assets/icons/arrow-right.png" class="arrow-icon" />
            </div>

            <!-- 指示器 -->
            <!-- <div class="indicators">
                <span v-for="(item, index) in activityList.length" :key="index" class="indicator"
                    :class="{ active: index === currentIndex }" @click="goToSlide(index)"></span>
            </div> -->
        </div>
    </div>
</template>

<script>
export default {
    name: 'TeamShowcase',
    props: {
        containerWidth: { type: String, default: '100%' },
        containerHeight: { type: String, default: '100%' },
        interval: { type: Number, default: 5000 },
        activityList: {
            type: Array,
            default: () => [
                {
                    id: 1,
                    title: '春季团建 · 南山露营',
                    description: '一起烧烤、飞盘、看星星，放松心情，增进默契。',
                    cover: require('@/assets/images/news/chatgpt.png'),
                    date: '2025-04-12'
                },
                {
                    id: 2,
                    title: '技术分享马拉松',
                    description: '全天 8 场闪电演讲，覆盖前端、后端、AI 与运维。',
                    cover: require('/src/assets/images/news/chatgpt.png'),
                    date: '2025-05-18'
                },
                {
                    id: 3,
                    title: '公益日 · 走进敬老院',
                    description: '为老人送温暖，陪聊、义剪、包饺子，传递爱心。',
                    cover: require('/src/assets/images/news/chatgpt.png'),
                    date: '2025-06-20'
                }
            ]
        }
    },
    data() {
        return {
            currentIndex: 0,
            autoplayTimer: null
        }
    },
    mounted() {
        this.startAutoplay()
        this.$el.addEventListener('mouseenter', this.stopAutoplay)
        this.$el.addEventListener('mouseleave', this.startAutoplay)
    },
    beforeDestroy() {
        this.stopAutoplay()
        this.$el.removeEventListener('mouseenter', this.stopAutoplay)
        this.$el.removeEventListener('mouseleave', this.startAutoplay)
    },
    methods: {
        nextSlide() {
            this.currentIndex = (this.currentIndex + 1) % this.activityList.length
        },
        prevSlide() {
            this.currentIndex = (this.currentIndex - 1 + this.activityList.length) % this.activityList.length
        },
        goToSlide(index) {
            this.currentIndex = index
        },
        startAutoplay() {
            this.stopAutoplay()
            this.autoplayTimer = setInterval(() => this.nextSlide(), this.interval)
        },
        stopAutoplay() {
            if (this.autoplayTimer) {
                clearInterval(this.autoplayTimer)
                this.autoplayTimer = null
            }
        }
    }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.team-showcase {
    position: relative;
    overflow: hidden;
    box-sizing: border-box;
    border-radius: 12px;
    background: #fff;
}

.carousel-wrapper {
    width: 100%;
    height: 100%;
}

.carousel-track {
    display: flex;
    height: 100%;
}

.activity-item {
    flex-shrink: 0;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
}

.activity-cover {
    width: 100%;
    // height: 0;
    // padding-bottom: 56.25%;
    /* 16:9 比例占位 */
    object-fit: cover;
    border-radius: 12px 12px 0 0;
}

.activity-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 5px 24px;
    text-align: center;
}

.activity-title {
    margin: 0 0 6px;
    font-size: 20px;
    font-weight: 600;
    color: #1f2937;
}

.activity-desc {
    margin: 0 0 2px;
    font-size: 14px;
    color: #6b7280;
    line-height: 1.4;
}

.activity-time {
    font-size: 12px;
    color: #9ca3af;
}

/* 箭头 & 指示器 与 NewsCarousel 保持一致 */
.nav-btn {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    width: 40px;
    height: 40px;
    background: rgba(0, 0, 0, 0.3);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    opacity: 0.7;
    transition: opacity 0.3s;
    z-index: 10;
    user-select: none;
}

.nav-btn:hover {
    opacity: 1;
}

.prev {
    left: 15px;
}

.next {
    right: 15px;
}

.arrow-icon {
    width: 24px;
    height: 24px;
    object-fit: contain;
    filter: invert(1);
}

.indicators {
    position: absolute;
    bottom: 10px;
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
    background: rgba(255, 255, 255, 0.6);
    cursor: pointer;
    transition: all 0.3s;
}

.indicator.active {
    width: 24px;
    border-radius: 4px;
    background: #fff;
}

/* 移动端 */
@media (max-width: 768px) {
    .activity-title {
        font-size: 18px;
    }

    .activity-desc {
        font-size: 13px;
    }

    .nav-btn {
        width: 32px;
        height: 32px;
    }

    .arrow-icon {
        width: 20px;
        height: 20px;
    }
}
</style>