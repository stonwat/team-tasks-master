<template>
    <div id="child-page">
        <div class="header-box">
            <span class="header-title">数据集展示</span>
        </div>
        <div class="tab-container">
            <div class="tab-item" v-for="(tab, index) in tabs" :key="index" :class="{ active: currentTab === index }"
                @click="handleTabClick(index)">
                {{ tab.name }}
            </div>
        </div>
        <div class="images-carousel" :style="{ width: containerWidth, height: containerHeight }">
            <!-- 轮播容器 -->
            <div class="carousel-wrapper">
                <div class="carousel-track" :style="{
                    transform: `translateX(-${currentIndex * 100}%)`,
                    transition: 'transform 0.5s ease'
                }">
                    <div class="images-item" v-for="images in imagesList" :key="images.id">
                        <img :src="images.imgUrl" alt="标注图片" class="images-img">
                        <!-- <div class="images-content">
                            <h3 class="images-title">{{ images.title }}</h3>
                        </div> -->
                    </div>
                </div>
            </div>

            <div class="nav-btn prev" @click="prevSlide" title="上一条新闻">
                <img src="@/assets/icons/arrow-left.png" alt="上一页" class="arrow-icon">
            </div>
            <div class="nav-btn next" @click="nextSlide" title="下一条新闻">
                <img src="@/assets/icons/arrow-right.png" alt="下一页" class="arrow-icon">
            </div>

            <div class="indicators">
                <span class="indicator" v-for="(item, index) in imagesList.length" :key="index"
                    :class="{ active: index === currentIndex }" @click="goToSlide(index)"></span>
            </div>
        </div>
    </div>
</template>

<script>
// 脚本部分完全不变
export default {
    name: 'DataDetail',
    props: {
        containerWidth: { type: String, default: '100%' },
        containerHeight: { type: String, default: '100%' },
        interval: { type: Number, default: 2000 },

    },
    data() {
        return {
            tabs: [
                { name: '动物分类', key: 'animals' },
                { name: '车辆检测', key: 'cars' },
                { name: '交通标识', key: 'traffic' },
                { name: '安全帽', key: 'hard-hat' },
                { name: '水果分类', key: 'fruit' }
            ],
            currentTab: 0,
            currentIndex: 0,
            autoplayTimer: null,
            imagesList: [],
            animalsList:
                [
                    { id: 1, imgUrl: require('@/assets/images/animals/dog_158.jpg') },
                    { id: 2, imgUrl: require('@/assets/images/animals/bird_273.jpg') },
                    { id: 3, imgUrl: require('@/assets/images/animals/elephant_264.jpg') },
                    { id: 4, imgUrl: require('@/assets/images/animals/bird_257.jpg') },
                    { id: 5, imgUrl: require('@/assets/images/animals/dog_166.jpg') },
                    { id: 6, imgUrl: require('@/assets/images/animals/cat_431.jpg') },
                    { id: 7, imgUrl: require('@/assets/images/animals/bird_263.jpg') },
                    { id: 8, imgUrl: require('@/assets/images/animals/elephant_258.jpg') },
                    { id: 9, imgUrl: require('@/assets/images/animals/cat_446.jpg') },
                    { id: 10, imgUrl: require('@/assets/images/animals/dog_157.jpg') },
                    { id: 11, imgUrl: require('@/assets/images/animals/cat_437.jpg') }
                ],
            carsList: [
                { id: 1, imgUrl: require('@/assets/images/cars/car_001.jpg') },
                { id: 2, imgUrl: require('@/assets/images/cars/car_002.jpg') },
                { id: 3, imgUrl: require('@/assets/images/cars/car_003.jpg') }
            ],
            trafficList: [
                { id: 1, imgUrl: require('@/assets/images/traffic/traffic_001.png') },
                { id: 2, imgUrl: require('@/assets/images/traffic/traffic_002.png') },
                { id: 3, imgUrl: require('@/assets/images/traffic/traffic_003.png') }
            ],
            fruitList: [
                { id: 1, imgUrl: require('@/assets/images/fruit/fruit_001.jpg') },
            ],
            hardHatList: [
                { id: 1, imgUrl: require('@/assets/images/hard_hat/hard_hat_001.png') },
            ]
        };
    },
    mounted() {
        this.imagesList = this.animalsList;
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
        handleTabClick(index) {
            this.currentTab = index;
            switch (index) {
                case 0:
                    this.imagesList = this.animalsList;
                    break;
                case 1:
                    this.imagesList = this.carsList;
                    break;
                case 2:
                    this.imagesList = this.trafficList;
                    break;
                case 3:
                    this.imagesList = this.hardHatList;
                    break;
                case 4:
                    this.imagesList = this.fruitList;
                    break;
                default:
                    this.imagesList = this.animalsList;
            }
            this.currentIndex = 0;
        },
        nextSlide() {
            this.currentIndex = (this.currentIndex + 1) % this.imagesList.length;
        },
        prevSlide() {
            this.currentIndex = (this.currentIndex - 1 + this.imagesList.length) % this.imagesList.length;
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

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.tab-container {
    display: flex;
    margin-bottom: 20px;
    border-bottom: 1px solid #eee;
}

.tab-item {
    padding: 10px 20px;
    cursor: pointer;
    color: #666;
    transition: all 0.3s;
    margin-right: 10px;
}

.tab-item.active {
    color: #2c3e50;
    font-weight: bold;
    border-bottom: 2px solid #3498db;
}

.tab-item:hover {
    color: #3498db;
}

.images-carousel {
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

.images-item {
    flex-shrink: 0;
    width: 100%;
    height: 100%;
    padding: 20px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
}

.images-img {
    width: 100%;
    height: 90%;
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 16px;
}

.images-content {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.images-title {
    margin: 0 0 12px 0;
    font-size: 20px;
    font-weight: 600;
    color: #1f2937;
}

.images-meta {
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
    .images-title {
        font-size: 18px;
    }

    .images-img {
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