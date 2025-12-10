<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <!-- <h2>优加任务管理系统(PlusTasks)</h2>
        <p>
          几年前我一直想找一款既方便做计划、又能让团队一起协作的项目管理软件。市面上虽然有不少优秀的开源产品，但总感觉差点意思。像MS Project做计划确实专业，但没法满足多人协作的需求。纠结了很久，最后决定干脆自己开发一套新系统。 </p>
        <p>
          <p>
            优加任务管理系统(PlusTasks) 既有微软Project那样专业的任务排期功能（比如拖拽调整任务顺序、自动计算工期），又有多人实时协作。它能根据团队的工作日历自动避开节假日安排任务，支持两种灵活模式：要么手动设定时间自动算工期，要么设好任务依赖关系让系统自动排期。所有成员都能在自己的工作台看到待办任务，管理者还能通过燃尽图一眼看清项目进度是否正常，自动预警延期风险。 <p>
        </p>
            <b>当前版本:</b> <span>v{{ version }}</span>
        <p>
          <el-tag type="danger">&yen;免费开源</el-tag>
        </p> -->
        <!-- <p>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTarget('https://gitee.com/ning_jiang5855/PlusTasks')"
            >访问码云</el-button
          >
        </p> -->
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>RuoYi v3.8.7</li>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>RuoYi-Vue v3.8.7</li>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>

    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="24" :lg="24">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-user-solid"></i> 省公司<a
              href="javascript:;"> AI行动办公室 </a>
            </p>
            <p>
              <i class="el-icon-chat-dot-round"></i> 联系电话：<a
                href="javascript:;"
                >12345678910</a>
            </p>

          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-divider />

  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "1.1.0"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

