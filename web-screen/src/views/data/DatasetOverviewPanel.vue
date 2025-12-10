<template>
    <div id="child-page">
        <div class="header-box">
            <span class="header-title">数据集数量统计</span>
        </div>
        <div class="dataset-statistics">
            <!-- 总量统计区域 -->
            <div class="total-stats">
                <div class="stat-item">
                    <span class="stat-label">数据集总数：{{ datasetTotal }}</span>
                    <!-- <span class="stat-value">{{ datasetTotal }}</span> -->
                </div>
            </div>

            <!-- 模型分类统计卡片区域 -->
            <div class="dataset-cards">
                <div class="card" v-for="(dataset, index) in datasetTypes" :key="index">
                    <div class="card-header">
                        <span>{{ dataset.title }}</span>
                    </div>
                    <div class="card-count">{{ dataset.count }}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'DatasetOverviewPanel',
    data() {
        return {
            datasetTotal: '22',
            datasetTypes: [
                {
                    title: '文本',
                    count: '10' 
                },
                {
                    title: '图片',
                    count: '8'
                },
                {
                    title: '音频',
                    count: '2'
                },
                {
                    title: '视频',
                    count: '1'
                }
            ]
        }
    },
    mounted() {
    },
    methods: {

    }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.dataset-statistics {
    border-radius: 4px;
    padding: 16px;
    box-shadow: 0 0 15px rgba(100, 149, 237, 0.3);
    border: 1px solid rgba(100, 149, 237, 0.5);
}


.total-stats {
    display: flex;
    justify-content: center;
    margin-bottom: 24px;
}

.stat-item {
    text-align: center;
    width: 100%;
}

.stat-label {
    display: block;
    font-size: 28px;
    color: #9bbef5;
    margin-bottom: 8px;
}

.stat-value {
    font-size: 36px;
    font-weight: bold;
    color: #cfe2ff;
}

.dataset-cards {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
}

.card {
    flex: 1;
    min-width: 200px;
    min-height: 100px;
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

.card-count {
    font-size: 32px;
    font-weight: bold;
    color: #cfe2ff;
    margin-bottom: 12px;
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
</style>