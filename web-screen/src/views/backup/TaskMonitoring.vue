<template>
    <div id="task-monitoring">
        <h3 class="panel-title">算力任务监控</h3>
        <div class="table-container">
            <table class="task-table">
                <thead>
                    <tr>
                        <th>任务ID</th>
                        <th>任务名称</th>
                        <th>GPU数</th>
                        <th>状态</th>
                        <th>开始时间</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(task, index) in taskList" :key="index">
                        <td>{{ task.taskId }}</td>
                        <td>{{ task.taskName }}</td>
                        <td>{{ task.usedGpu }} 张</td>
                        <td>
                            <span class="status-tag" :class="task.status === '运行中' ? 'running' : 'pending'">
                                {{ task.status }}
                            </span>
                        </td>
                        <td>{{ task.startTime }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return{
            taskList: [
                { taskId: "T20250618001", taskName: "ResNet50训练", usedGpu: 4, status: "运行中", startTime: "2025-06-18 09:00", expectEndTime: "2025-06-19 18:00" },
                { taskId: "T20250618002", taskName: "用户行为分析", usedGpu: 2, status: "运行中", startTime: "2025-06-18 10:30", expectEndTime: "2025-06-18 16:30" },
                { taskId: "T20250618003", taskName: "目标检测优化", usedGpu: 8, status: "待处理", startTime: "2025-06-18 14:00", expectEndTime: "2025-06-20 10:00" }
            ]
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
    flex: 1;
    overflow-x: auto;
    margin: 0 2.5%;

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

.task-table {
    width: 100%;
    border-collapse: collapse;
    min-width: 350px;

    th,
    td {
        padding: 10px 8px;
        text-align: center;
        color: #d3d6dd;
        border-bottom: 1px solid #333;
        font-size: 13px;
    }

    th {
        color: #fff;
        background-color: #171c33;
        font-weight: 500;
    }

    tr:hover {
        background-color: #171c33;
    }

    .status-tag {
        padding: 2px 6px;
        border-radius: 10px;
        font-size: 11px;
    }

    .running {
        background-color: rgba(51, 206, 160, 0.2);
        color: #33cea0;
    }

    .pending {
        background-color: rgba(255, 152, 0, 0.2);
        color: #ff9800;
    }
}
</style>