<template>
    <div id="compute-allocation">
        <h3 class="panel-title">算力分配管理</h3>
        <!-- 滚动容器：隐藏垂直滚动条，仅JS控制滚动 -->
        <div class="table-container" ref="tableContainer">
            <table class="allocate-table">
                <!-- 固定表头 -->
                <thead class="fixed-header">
                    <tr>
                        <th>序号</th>
                        <th>项目名称</th>
                        <th>GPU数</th>
                        <th>算力</th>
                        <th>负责人</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <!-- 滚动内容：使用复制的数据实现无缝循环 -->
                <tbody ref="tableBody" class="scroll-body">
                    <tr v-for="(item, index) in displayList" :key="index">
                        <td>{{ ++index % allocateList.length }}</td>
                        <td>{{ item.projectName }}</td>
                        <td>{{ item.gpuCount }} 张</td>
                        <td>{{ item.computePower }} GFLOPS</td>
                        <td>{{ item.manager }}</td>
                        <td>
                            <button class="operate-btn edit-btn"
                                @click="$emit('edit', item, index % allocateList.length)">编辑</button>
                            <button class="operate-btn revoke-btn"
                                @click="$emit('revoke', item, index % allocateList.length)">回收</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            allocateList: [
                { projectName: "AI模型训练A", gpuCount: 4, computePower: 1200, manager: "张工", validPeriod: "2025-06-18至2025-07-18" },
                { projectName: "大数据分析B", gpuCount: 2, computePower: 600, manager: "李工", validPeriod: "2025-06-10至2025-06-30" },
                { projectName: "计算机视觉C", gpuCount: 8, computePower: 3200, manager: "王工", validPeriod: "2025-06-01至2025-08-01" },
                { projectName: "AI模型训练D", gpuCount: 4, computePower: 1200, manager: "张工", validPeriod: "2025-06-18至2025-07-18" },
                { projectName: "大数据分析E", gpuCount: 2, computePower: 600, manager: "李工", validPeriod: "2025-06-10至2025-06-30" },
                { projectName: "计算机视觉F", gpuCount: 8, computePower: 3200, manager: "王工", validPeriod: "2025-06-01至2025-08-01" },
                { projectName: "计算机视觉G", gpuCount: 8, computePower: 3200, manager: "王工", validPeriod: "2025-06-01至2025-08-01" },
                { projectName: "AI模型训练H", gpuCount: 4, computePower: 1200, manager: "张工", validPeriod: "2025-06-18至2025-07-18" },
                { projectName: "大数据分析I", gpuCount: 2, computePower: 600, manager: "李工", validPeriod: "2025-06-10至2025-06-30" },
                { projectName: "计算机视觉J", gpuCount: 8, computePower: 3200, manager: "王工", validPeriod: "2025-06-01至2025-08-01" },
                { projectName: "自然语言处理K", gpuCount: 6, computePower: 1800, manager: "赵工", validPeriod: "2025-06-05至2025-07-05" }
            ],
            scrollTimer: null,
            scrollSpeed: 30, // 滚动速度（数值越大越慢）
            rowHeight: 45 // 每行固定高度（需与样式一致）
        };
    },
    computed: {
        // 数据超过10条时，复制一份用于无缝循环
        displayList() {
            return this.allocateList.length > 8
                ? [...this.allocateList, ...this.allocateList]
                : this.allocateList;
        }
    },
    mounted() {
        if (this.allocateList.length > 8) {
            this.startScroll();
            // 鼠标悬停暂停，离开继续
            const container = this.$refs.tableContainer;
            container.addEventListener('mouseenter', this.stopScroll);
            container.addEventListener('mouseleave', this.startScroll);
        }
    },
    beforeDestroy() {
        this.stopScroll();
        const container = this.$refs.tableContainer;
        if (container) {
            container.removeEventListener('mouseenter', this.stopScroll);
            container.removeEventListener('mouseleave', this.startScroll);
        }
    },
    methods: {
        startScroll() {
            if (this.scrollTimer) return;

            const container = this.$refs.tableContainer;
            // 原始数据总高度（滚动到此处时重置，实现循环）
            const maxScrollTop = this.allocateList.length * this.rowHeight;

            this.scrollTimer = setInterval(() => {
                // 每次滚动1px
                container.scrollTop += 1;

                // 当滚动距离达到原始数据总高度时，重置为0（无缝循环）
                if (container.scrollTop >= maxScrollTop) {
                    container.scrollTop = 0;
                }
            }, this.scrollSpeed);
        },
        stopScroll() {
            if (this.scrollTimer) {
                clearInterval(this.scrollTimer);
                this.scrollTimer = null;
            }
        }
    }
};
</script>

<style lang="scss" scoped>
.panel-title {
    color: #fff;
    font-size: 16px;
    margin: 10px 0;
    padding-left: 10px;
    border-left: 3px solid #1890ff;
}

.table-container {
    width: 95%;
    margin: 10px 2.5%;
    max-height: 360px;
    /* 固定高度：10行内容（45*10）+ 表头（不占内容高度） */
    overflow-x: auto;
    /* 保留水平滚动条（内容过宽时） */
    overflow-y: hidden;
    /* 隐藏垂直滚动条（核心） */
    position: relative;

    // 优化水平滚动条样式
    &::-webkit-scrollbar {
        height: 6px;
    }

    &::-webkit-scrollbar-track {
        background: #171c33;
        border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
        background: #1890ff;
        border-radius: 3px;
    }
}

.allocate-table {
    width: 100%;
    border-collapse: collapse;
    min-width: 350px;

    // 固定表头
    .fixed-header {
        position: sticky;
        top: 0;
        z-index: 2;
        /* 确保表头在内容上方 */
    }

    th,
    td {
        padding: 10px 8px;
        text-align: center;
        color: #d3d6dd;
        border-bottom: 1px solid #333;
        font-size: 13px;
        height: 25px;
        /* 加上padding总高度=45px（与rowHeight一致） */
        white-space: nowrap;
        /* 禁止换行，保证行高稳定 */
    }

    th {
        color: #fff;
        background-color: #171c33;
        font-weight: 500;
        border-bottom: 2px solid #333;
        /* 表头下边框加粗，区分内容 */
    }

    .scroll-body tr:hover {
        background-color: #171c33;
    }

    .operate-btn {
        border: none;
        padding: 4px 8px;
        border-radius: 3px;
        cursor: pointer;
        margin: 0 3px;
        font-size: 12px;
    }

    .edit-btn {
        background-color: #1890ff;
        color: #fff;
    }

    .revoke-btn {
        background-color: #ff4d4f;
        color: #fff;
    }
}
</style>