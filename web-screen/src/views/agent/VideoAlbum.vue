<template>
    <div id="child-page">
        <div class="header-box">
            <span class="header-title">视频专辑</span>
        </div>

        <!-- ===== 1. 封面列表（轮播） ===== -->
        <div v-if="mode === 'list'" class="video-album">
            <!-- 左箭头 -->
            <div v-if="maxGroup > 1" class="nav-arrow left" @click="prevGroup">
                <img src="@/assets/icons/arrow-left.png" />
            </div>

            <!-- 封面轨道 -->
            <div class="video-track" :style="trackStyle">
                <div v-for="video in videoList" :key="video.id" class="video-card"
                    :style="{ flex: `0 0 ${100 / visibleCount}%` }" @click="playVideo(video)">
                    <img :src="video.cover" class="video-cover" />
                    <span class="duration">{{ video.duration }}</span>
                    <h4 class="video-title">{{ video.title }}</h4>
                    <span  class="video-desc">{{ video.description }}</span>
                </div>
            </div>

            <!-- 右箭头 -->
            <div v-if="maxGroup > 1" class="nav-arrow right" @click="nextGroup">
                <img src="@/assets/icons/arrow-right.png" />
            </div>
        </div>

        <!-- ===== 2. 播放页 ===== -->
        <div v-else class="player-wrapper">
            <el-button type="text" icon="el-icon-arrow-left" class="back-btn" @click="mode = 'list'">返回列表</el-button>

            <video :src="currentVideo.url" controls autoplay class="video-player"></video>

            <h3 class="player-title">{{ currentVideo.title }}</h3>
            <p class="player-desc">{{ currentVideo.description }}</p>
        </div>
    </div>
</template>

<script>
export default {
    name: 'VideoAlbum',
    props: {
        containerWidth: { type: String, default: '100%' },
        containerHeight: { type: String, default: '100%' },
        visibleCount: { type: Number, default: 4 },        // 一屏几张封面
        videoList: {
            type: Array,
            default: () => [
                {
                    id: 1,
                    title: '团队年度回顾',
                    description: '2025 精彩瞬间',
                    cover: require('@/assets/images/news/tencent.png'),
                    url: 'https://www.w3schools.com/html/mov_bbb.mp4',
                    duration: '03:12'
                },
                {
                    id: 2,
                    title: '新人培训实录',
                    description: '两天一夜训练营',
                    cover: require('@/assets/images/news/tencent.png'),
                    url: 'https://www.w3schools.com/html/movie.mp4',
                    duration: '05:48'
                },
                {
                    id: 3,
                    title: '项目庆功会',
                    description: '上线啦！',
                    cover: require('@/assets/images/news/tencent.png'),
                    url: 'https://www.w3schools.com/html/movie.mp4',
                    duration: '02:35'
                },
                {
                    id: 4,
                    title: 'Tech Day 快闪',
                    description: '技术沙龙花絮',
                    cover: require('@/assets/images/news/tencent.png'),
                    url: 'https://www.w3schools.com/html/movie.mp4',
                    duration: '01:20'
                },
                {
                    id: 5,
                    title: '季度生日会',
                    description: '蛋糕 + 游戏',
                    cover: require('@/assets/images/news/tencent.png'),
                    url: 'https://www.w3schools.com/html/movie.mp4',
                    duration: '04:00'
                },
                {
                    id: 6,
                    title: '季度生日会',
                    description: '蛋糕 + 游戏',
                    cover: require('@/assets/images/news/tencent.png'),
                    url: 'https://www.w3schools.com/html/movie.mp4',
                    duration: '04:00'
                },
                {
                    id: 7,
                    title: '季度生日会',
                    description: '蛋糕 + 游戏',
                    cover: require('@/assets/images/news/tencent.png'),
                    url: 'https://www.w3schools.com/html/movie.mp4',
                    duration: '04:00'
                }
            ]
        }
    },
    data() {
        return {
            mode: 'list',        // list | play
            currentVideo: {},    // 正在播放的视频
            coverIndex: 0        // 封面轮播指针
        }
    },
    computed: {
        // 总组数
        maxGroup() {
            return Math.ceil(this.videoList.length / this.visibleCount)
        },
        // 轨道平移距离
        trackStyle() {
            const unit = 100 / this.visibleCount
            const translateX = -unit * this.coverIndex * this.visibleCount
            return {
                transform: `translateX(${translateX}%)`,
                transition: 'transform .3s ease'
            }
        }
    },
    methods: {
        playVideo(video) {
            this.currentVideo = video
            this.mode = 'play'
        },
        prevGroup() {
            if (this.coverIndex > 0) this.coverIndex--
        },
        nextGroup() {
            if (this.coverIndex < this.maxGroup - 1) this.coverIndex++
        }
    }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

/* ---------- 封面轮播 ---------- */
.video-album {
    position: relative;
    overflow: hidden;
    padding: 0 40px; // 给箭头留位
}

.video-track {
    display: flex;
    will-change: transform;
}

.video-card {
    position: relative;
    padding: 10px;
    box-sizing: border-box;
    cursor: pointer;
    transition: transform .3s;

    &:hover {
        transform: translateY(-4px);
    }
}

.video-cover {
    width: 100%;
    height: 250px;
    object-fit: cover;
    border-radius: 8px;
}

.duration {
    position: absolute;
    top: 18px;
    right: 18px;
    background: rgba(0, 0, 0, .6);
    color: #fff;
    font-size: 12px;
    padding: 2px 6px;
    border-radius: 4px;
}

.video-title {
    margin: 10px 0 0;
    font-size: 15px;
    font-weight: 500;
}

.video-desc {
    font-size: 14px;
    color: #666;
    line-height: 1.6;
}

/* 左右箭头 */
.nav-arrow {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    width: 32px;
    height: 32px;
    cursor: pointer;
    z-index: 10;

    img {
        width: 100%;
        height: 100%;
        object-fit: contain;
    }

    &.left {
        left: 4px;
    }

    &.right {
        right: 4px;
    }
}

/* ---------- 播放页 ---------- */
.player-wrapper {
    height: 100%;
    display: flex;
    flex-direction: column;
    padding: 20px;
    box-sizing: border-box;
}

.back-btn {
    margin-bottom: 12px;
    font-size: 14px;
}

.video-player {
    width: 100%;
    max-height: 250px; // 留给按钮+标题
    object-fit: contain;
    border-radius: 8px;
    outline: none;
}

.player-title {
    margin: 12px 0 6px;
    font-size: 20px;
    font-weight: 600;
}

.player-desc {
    font-size: 14px;
    color: #666;
    line-height: 1.6;
}
</style>