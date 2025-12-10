<template>
  <div id="child-page" class="project-duration">
    <div class="header-box">
      <span class="header-title">项目列表</span>
    </div>

    <div class="bg-color-black">
      <div class="d-flex jc-center body-box">
        <!-- 数据不为空时，渲染滚动表格 -->
        <dv-scroll-board v-if="config.data.length > 0" class="dv-scr-board" :config="config" />
        <!-- 数据为空时，显示“暂无工作排期” -->
        <div v-else class="empty-tip">
          暂无工作排期
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { getProjectList } from '@/api/admin.js';
export default {
  data() {
    return {
      config: {
        header: ['具体任务', '部门', '目标分数', '当前分数'],
        data: [
          ['组件1', 'dev-1', '2025-09-30', "<span class='colorGrass'>↑75%</span>"],
          ['组件2', 'dev-2', '2025-09-30', "<span class='colorRed'>↓33%</span>"],
          ['组件3', 'dev-3', '2025-09-30', "<span class='colorGrass'>↑100%</span>"],
          ['组件4', 'rea-1', '2025-09-30', "<span class='colorGrass'>↑94%</span>"],
          ['组件5', 'rea-2', '2025-09-30', "<span class='colorGrass'>↑95%</span>"],
          ['组件6', 'fix-2', '2025-09-30', "<span class='colorGrass'>↑63%</span>"],
          ['组件7', 'fix-4', '2025-09-30', "<span class='colorGrass'>↑84%</span>"],
          ['组件8', 'fix-7', '2025-09-30', "<span class='colorRed'>↓46%</span>"],
          ['组件9', 'dev-2', '2025-09-30', "<span class='colorRed'>↓13%</span>"],
          ['组件10', 'dev-9', '2025-09-30', "<span class='colorGrass'>↑76%</span>"],
          ['组件1', 'dev-1', '2025-09-30', "<span class='colorGrass'>↑75%</span>"],
          ['组件2', 'dev-2', '2025-09-30', "<span class='colorRed'>↓33%</span>"],
          ['组件3', 'dev-3', '2025-09-30', "<span class='colorGrass'>↑100%</span>"],
          ['组件4', 'rea-1', '2025-09-30', "<span class='colorGrass'>↑94%</span>"],
          ['组件5', 'rea-2', '2025-09-30', "<span class='colorGrass'>↑95%</span>"],
          ['组件6', 'fix-2', '2025-09-30', "<span class='colorGrass'>↑63%</span>"],
          ['组件7', 'fix-4', '2025-09-30', "<span class='colorGrass'>↑84%</span>"],
          ['组件8', 'fix-7', '2025-09-30', "<span class='colorRed'>↓46%</span>"],
          ['组件9', 'dev-2', '2025-09-30', "<span class='colorRed'>↓13%</span>"],
          ['组件10', 'dev-9', '2025-09-30', "<span class='colorGrass'>↑76%</span>"]
        ],
        rowNum: 15,
        headerHeight: 30,
        headerBGC: '#0f1325',
        oddRowBGC: '#0f1325',
        evenRowBGC: '#171c33',
        index: true,
        columnWidth: [45, 210, 110],
        align: ['center']
      }
    }

  },
  mounted() {
    this.loadProjectData();
  },
  methods: {
    async loadProjectData() {
      try {
        const params = {
          pageSize: 50,
          PageNum: 1
        }
        // 获取项目列表
        const projectResponse = await getProjectList(params);
        this.config = {
          ...this.config,
          data: projectResponse.rows.map(item => [
            item.application,
            item.department,
            item.target,
            item.score
          ])
        };
      } catch (error) {
        // console.error('加载数据失败:', error);
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';
@import "@/assets/scss/components/child-modal.scss";

.project-duration {
    position: absolute;
  right: 10px;
  padding: 0 10px;
  width: 560px;
  height: 866px;
  border-radius: 5px;
  

  .bg-color-black {
    height: 860px;
    border-radius: 10px;
  }

  .body-box {
    border-radius: 10px;
    overflow: hidden;

    .dv-scr-board {
      width: 560px;
      height: 860px;
    }

    .empty-tip {
      width: 560px;
      height: 866px;
      text-align: center;
      color: #c3cbde;
      font-size: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #0f1325;
      border-radius: 10px;
    }
  }
}
</style>
