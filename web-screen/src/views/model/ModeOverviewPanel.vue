<template>
    <div id="child-page">
        <div class="header-box">
            <span class="header-title">模型数量统计</span>
        </div>
        <div class="model-statistics">
            <!-- 总量统计区域 -->
            <div class="total-stats">
                <div class="stat-item">
                    <span class="stat-label">模型总数：{{ modelTotal }}</span>
                    <!-- <span class="stat-value">{{ modelTotal }}</span> -->
                </div>
            </div>

            <!-- 模型分类统计卡片区域 -->
            <div class="model-cards">
                <div class="card" v-for="(model, index) in modelTypes" :key="index">
                    <div class="card-header">
                        <span>{{ model.title }}</span>
                    </div>
                    <div class="card-count">{{ model.count }}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'ModelOverviewPanel',
    data() {
        return {
            modelTotal: '11',
            modelTypes: [
                {
                    title: '多模态模型',
                    barColor: '#2f54eb',
                    count: '1' 
                },
                {
                    title: '计算机视觉模型',
                    barColor: '#36cbcb',
                    count: '4'
                },
                {
                    title: '自然语言处理模型',
                    barColor: '#18c9c9',
                    count: '5'
                },
                {
                    title: '音频模型',
                    barColor: '#fa8c16',
                    count: '1'
                }
            ]
        }
    },
    mounted() {
    },
    methods: {
        handleRefresh() {
            this.loadModelData();
            this.$message.info('模型统计数据已刷新');
        }
    }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.model-statistics {
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

.model-cards {
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

.ratio-text {
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