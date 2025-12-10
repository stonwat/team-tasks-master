<template>
  <div class="app-overview">
    <el-tabs style="background: #F5F8FA" type="border-card" v-model="activeName" @tab-click="onTabClick">
      <el-tab-pane label="项目概览" name="first">


        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>项目信息</span>
          </div>
          <el-descriptions title="">
            <el-descriptions-item label="项目名称">{{ projectInfo.projectName }}</el-descriptions-item>
            <el-descriptions-item label="项目负责人">{{ projectInfo.userName }}</el-descriptions-item>
          </el-descriptions>
          <el-descriptions title="">
            <el-descriptions-item label="计划完成时间">{{ plannedStartDate }} 至 {{ plannedEndDate }}
            </el-descriptions-item>
            <!--            <el-descriptions-item label="工期">{{projectInfo.duration}}</el-descriptions-item>-->
          </el-descriptions>
          <el-descriptions title="">
            <el-descriptions-item label="实际完成时间">{{ actualStartDate }} 至 {{ actualEndDate }}
            </el-descriptions-item>
            <!--            <el-descriptions-item label="工期">{{projectInfo.duration}}</el-descriptions-item>-->
          </el-descriptions>
          <el-descriptions title="">
            <el-descriptions-item label="预测完成时间">{{ predictingStartDate }} 至 {{ predictingEndDate }}
            </el-descriptions-item>
            <!--            <el-descriptions-item label="工期">{{projectInfo.duration}}</el-descriptions-item>-->
          </el-descriptions>
          <el-descriptions title="">
            <el-descriptions-item label="项目管理员">
              {{ projectInfo.projectManagers }}
            </el-descriptions-item>
          </el-descriptions>
          <el-descriptions title="">
            <el-descriptions-item label="项目成员">
              {{ projectInfo.projectMembers }}
            </el-descriptions-item>
          </el-descriptions>
          <el-descriptions title="">
            <el-descriptions-item label="项目描述">
              {{ projectInfo.description }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>项目概况</span>
          </div>

          <el-row :gutter="1">
            <el-col :span="10">
              <div class="project-info">
                <el-row type="flex" class="row-bg row-bg-1">
                  <el-col :span="8">
                    <div class="grid-content grid-content-1">
                      <div class="top-half">{{ actualProgress }}%</div>
                      <div class="bottom-half">实际进度</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content grid-content-1">
                      <div class="top-half">{{ planProgress }}%</div>
                      <div class="bottom-half">计划进度</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content ">
                      <div class="top-half">{{ progressDeterioration }}%</div>
                      <div class="bottom-half">进度偏差</div>
                    </div>
                  </el-col>
                </el-row>

                <el-row type="flex" class="row-bg row-bg-1">
                  <el-col :span="8">
                    <div class="grid-content grid-content-1">
                      <div class="top-half">{{ taskCount }}</div>
                      <div class="bottom-half">任务数量</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content grid-content-1">
                      <div class="top-half">{{ taskCompletedCount }}</div>
                      <div class="bottom-half">完成数量</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content">
                      <div class="top-half">{{ taskCompletedPercent }}%</div>
                      <div class="bottom-half">完成百分比</div>
                    </div>
                  </el-col>
                </el-row>
                <el-row type="flex" class="row-bg ">
                  <el-col :span="8">
                    <div class="grid-content grid-content-1">
                      <div class="top-half">{{ taskOverdueCount }}</div>
                      <div class="bottom-half">逾期任务数量</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content grid-content-1">
                      <div class="top-half">{{ taskOverdueCompletedCount }}</div>
                      <div class="bottom-half">逾期完成数量</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content">
                      <div class="top-half">{{ taskOverduePercent }}%</div>
                      <div class="bottom-half">逾期百分比</div>
                    </div>
                  </el-col>
                </el-row>

              </div>
            </el-col>
            <el-col :span="14">
              <div id="bin-tu">

              </div>
            </el-col>
          </el-row>

        </el-card>

        <el-card class="box-card">
          <div id="ran-jin-tu"></div>
        </el-card>


      </el-tab-pane>


      <el-tab-pane style=";width: 100%;" label="甘特图" name="second">


        <gantt-elastic
          v-if="tasks.length>0"
          :options="ganttOptions"
          :dynamic-style="dynamicStyle"
          :tasks="tasks">
          <gantt-header :options="ganttHeaderOptions" slot="header">
          </gantt-header>
        </gantt-elastic>


      </el-tab-pane>
      <el-tab-pane style="width: 100%;" label="项目动态" name="third">
        <div>

        </div>
      </el-tab-pane>
      <el-tab-pane style="background: #1ab394;width: 100%;height: 100%" label="资源报告" name="fourth">
        <div style="width: 100%">

        </div>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<script>
import Vue from 'vue';
import 'element-ui/lib/theme-chalk/display.css';
import {
  getProject,
} from "@/api/pms/project/project";
import {
  listManager
} from "@/api/pms/project/resources";
import {
  listTask, getTaskBurnDownChartData
} from "@/api/pms/project/task";
import GanttElastic from "gantt-elastic";
import GanttHeader from "gantt-elastic-header";
import moment from "moment/moment";
import * as echarts from "echarts";


export default {
  name: "Project",
  components: {GanttElastic, GanttHeader},
  props: {
    // 定义一个名为id的prop，类型为String
    projectId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      ganttHeaderOptions: {
        title: {
          label: "<button class='export-btn'  style='background: rgb(149, 165, 166); border: none; outline: none; cursor: pointer; color: white; border-radius: 3px; margin-right: 27px; font-size: 16px; padding: 8px 12px;'>导出</button>",
          html: true
        },
        locale: {
          Now: "现在",
          "X-Scale": "缩放-X",
          "Y-Scale": "缩放-Y",
          "Task list width": "任务列表",
          "Before/After": "展开",
          "Display task list": "显示任务列表"
        }
      },
      ganttOptions: {
        taskMapping: {
          progress: "percent",
        },
        scope: {
          before: 20,
          after: 20
        },
        times: {
          timeZoom: 21
        },
        maxRows: 1000,
        maxHeight: 600,
        row: {
          height: 20
        },
        calendar: {
          workingDays: [1, 2, 3, 4, 5], //*
          gap: 6, //*
          height: 0,
          strokeWidth: 1,
          hour: {
            height: 20, //*
            display: true, //*
            widths: [],
            maxWidths: {short: 0, medium: 0, long: 0},
            formatted: {
              long: [],
              medium: [],
              short: []
            },
            format: {
              //*
              long(date) {
                return date.format('HH:mm');
              },
              medium(date) {
                return date.format('HH:mm');
              },
              short(date) {
                return date.format('HH');
              }
            }
          },
          day: {
            height: 20, //*
            display: true, //*
            widths: [],
            maxWidths: {short: 0, medium: 0, long: 0},
            format: {
              long(date) {
                return date.format('DD dddd');
              },
              medium(date) {
                return date.format('DD ddd');
              },
              short(date) {
                return date.format('DD');
              }
            }
          },
          month: {
            height: 20, //*
            display: true, //*
            widths: [],
            maxWidths: {short: 0, medium: 0, long: 0},
            format: {
              //*
              short(date) {
                return date.format('MM');
              },
              medium(date) {
                return date.format("'YY年MMM");
              },
              long(date) {
                return date.format('YYYY年MMMM');
              }
            }
          }
        },
        chart: {
          progress: {
            bar: true
          },
          expander: {
            display: false
          }
        },
        taskList: {
          expander: {
            straight: true
          },
          percent: 100,
          columns: [
            {
              id: 0,
              label: "任务",
              value: "taskName",
              expander: true,
              width: 200,
              html: true,
            },
            {
              id: 1,
              label: "工期",
              value: "durationStr",
              percent: 0,
              width: 68,
              html: true,
              style: {
                "task-list-header-label": {
                  "text-align": "center",
                  width: "100%"
                },
                "task-list-item-value-container": {
                  "text-align": "center",
                  width: "100%"
                }
              }
            },
            {
              id: 2,
              label: "开始",
              value: "startDate",
              width: 78,
              html: true,
              style: {
                "task-list-header-label": {
                  "text-align": "center",
                  width: "100%"
                },
                "task-list-item-value-container": {
                  "text-align": "center",
                  width: "100%"
                }
              }
            },
            {
              id: 3,
              label: "结束",
              value: "endDate",
              width: 78,
              html: true,
              style: {
                "task-list-header-label": {
                  "text-align": "center",
                  width: "100%"
                },
                "task-list-item-value-container": {
                  "text-align": "center",
                  width: "100%"
                }
              }
            },
            {
              id: 4,
              label: "指派给",
              value: "user",
              width: 110,
              html: true,
              style: {
                "task-list-header-label": {
                  "text-align": "center",
                  width: "100%"
                },
                "task-list-item-value-container": {
                  "text-align": "center",
                  width: "100%"
                }
              }
            },
            {
              id: 5,
              label: "%",
              value: "percentStr",
              width: 35,
              html: true,
              style: {
                "task-list-header-label": {
                  "text-align": "center",
                  width: "100%"
                },
                "task-list-item-value-container": {
                  "text-align": "center",
                  width: "100%"
                }
              }
            }
          ]
        },
        locale: {
          //*
          name: 'en',
          weekdays: '星期日_星期一_星期二_星期三_星期四_星期五_星期六'.split('_'),
          weekdaysShort: '星期日_星期一_星期二_星期三_星期四_星期五_星期六'.split('_'),
          weekdaysMin: '星期日_星期一_星期二_星期三_星期四_星期五_星期六'.split('_'),
          months: '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
          monthsShort: '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
          weekStart: 1,
          relativeTime: {
            future: 'in %s',
            past: '%s ago',
            s: 'a few seconds',
            m: 'a minute',
            mm: '%d minutes',
            h: 'an hour',
            hh: '%d hours',
            d: 'a day',
            dd: '%d days',
            M: 'a month',
            MM: 'months',
            y: 'a year',
            yy: 'years'
          },
          formats: {
            LT: 'HH:mm',
            LTS: 'HH:mm:ss',
            L: 'DD/MM/YYYY',
            LL: 'D MMMM YYYY',
            LLL: 'D MMMM YYYY HH:mm',
            LLLL: 'dddd, D MMMM YYYY HH:mm'
          },
          ordinal: n => {
            const s = ['th', 'st', 'nd', 'rd'];
            const v = n % 100;
            return `[${n}${s[(v - 20) % 10] || s[v] || s[0]}]`;
          }
        },

      },
      dynamicStyle: {
        'chart-row-progress-bar-solid': {
          fill: '#3498db',
          height: '25%'
        }
      },
      tasks: [],
      ganttTitle: undefined,
      projectId: undefined,
      projectInfo: {},

// 实际进度，表示当前任务的完成百分比（0-100）
      actualProgress: 0,
// 计划进度，表示按计划任务应该完成的百分比（0-100）
      planProgress: 0,
// 进度恶化情况，表示实际进度与计划进度之间的差异（通常是实际进度 - 计划进度）
      progressDeterioration: 0,
// 总任务数，表示所有任务的总数量
      taskCount: 0,
// 已完成任务数，表示已经完成的任务数量
      taskCompletedCount: 0,
// 逾期任务数，表示已经超过截止日期但尚未完成的任务数量
      taskOverdueCount: 0,
// 逾期已完成任务数，表示已经超过截止日期但最终完成的任务数量
      taskOverdueCompletedCount: 0,
// 任务完成百分比，表示已完成任务占总任务数的百分比
      taskCompletedPercent: 0,
// 任务逾期百分比，表示逾期任务占总任务数的百分比
      taskOverduePercent: 0,


      activeName: 'first',
      plannedStartDate: '',
      plannedEndDate: '',
      actualStartDate: '',
      actualEndDate: '',
      predictingStartDate: '',
      predictingEndDate: '',


      // 遮罩层
      loading: true,
      // 项目表格数据
      projectList: [],
      managerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
    };
  },
  created() {
    // const projectId = this.projectId;
    // this.projectId = projectId;

  },
  mounted() {

    this.bindExportEvent();
    getProject(this.projectId).then(response => {
      response.data.startDate = moment(new Date(response.data.startDate)).format('YYYY-MM-DD');
      response.data.endDate = moment(new Date(response.data.endDate)).format('YYYY-MM-DD');
      this.projectInfo = response.data
    })

    listManager(this.projectId).then(response => {
      this.managerList = response.data;
    })

    let pm = {};
    pm.projectId = this.projectId;
    listTask(pm).then(response => {
      // 总任务数
      this.taskCount = response.data.length;
      //完成任务
      this.taskCompletedCount = 0
      //未完成任务
      let taskUnCompletedCount = 0
      //完成百分比
      this.taskCompletedPercent = 0
      //逾期未完成
      let taskOverdueUnCompletedCount = 0
      //逾期完成
      this.taskOverdueCompletedCount = 0
      //总预期
      this.taskOverdueCount = 0
      //总逾百分比
      this.taskOverduePercent = 0
      this.taskOverduePercent = 0

      if (this.taskCount !== 0) {
        //完成任务
        this.taskCompletedCount = response.data.filter(item => item.progress === 100).length;
        //未完成任务
        taskUnCompletedCount = response.data.filter(item => item.progress !== 100).length;
        //完成百分比
        this.taskCompletedPercent = (this.taskCompletedCount / this.taskCount) * 100;
        this.taskCompletedPercent = Math.round(this.taskCompletedPercent);
        //逾期未完成
        taskOverdueUnCompletedCount = response.data.filter(item => item.progress !== 100 && new Date(item.endDate) < new Date()).length;
        //逾期完成
        this.taskOverdueCompletedCount = response.data.filter(item => item.completionDate !== null && new Date(item.endDate) < new Date(item.completionDate)).length;
        //总预期
        this.taskOverdueCount = taskOverdueUnCompletedCount + this.taskOverdueCompletedCount;
        //总逾百分比
        this.taskOverduePercent = (this.taskOverdueCount / this.taskCount) * 100;
        this.taskOverduePercent = Math.round(this.taskOverduePercent);
      }


      let chartDom = document.getElementById('bin-tu');
      let myChart = echarts.init(chartDom);
      let option = {
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              {value: this.taskCompletedCount - this.taskOverdueCompletedCount, name: '完成'},
              {value: taskUnCompletedCount - taskOverdueUnCompletedCount, name: '待办'},
              {value: taskOverdueUnCompletedCount, name: '逾期待办'},
              {value: this.taskOverdueCompletedCount, name: '逾期完成'}
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            // 添加 label 配置
            label: {
              show: true, // 显示标签
              formatter: '{b}: {c} ({d}%)', // 格式化显示内容：名称: 数值 (百分比)
              fontSize: 12 // 字体大小
            },
            labelLine: {
              show: true // 显示引导线
            }
          }
        ]
      };
      option && myChart.setOption(option);

      let tree = this.listToTree(response.data, 'id', 'pId', 'children');
      let root = {
        id: null,
        name: '根节点',
        children: tree
      }
      this.traverseUpFromLeaf(root, node => {
        // console.info(node.taskName)
        // 检查是否有子节点
        if (!node.children || node.children.length === 0) {
          return;
        }
        // 检查任务类型是否为 "1"
        if (node.taskType === "1") {
          return;
        }
        // 获取最早开始时间和最晚结束时间
        const minStartDate = this.getEarliestStartDateNode(node.children).startDate;
        const maxEndDate = this.getLatestEndDateNode(node.children).endDate;
        const duration = this.sumChildrenDurations(node.children)
        //取node.children 中 progress字段 的平均值
        // node.progress = this.getAverageProgress(node.children);
        let p = node.children.map(a => a.duration * (a.progress)).reduce((a, b) => a + b, 0);
        const progress = p / duration
        node.progress = Math.round(progress);
        node.duration = duration;
        node.startDate = minStartDate;
        node.endDate = maxEndDate;
        // console.log("node", node)
      });

      let list = this.treeToList(root.children);

      let pIds = list.map(item => item.pId);
      list.forEach(element => {
        let task = {};
        task.id = element.id;
        task.taskName = element.taskName;
        task.label = element.taskName;
        task.user = element.assignUserName;
        task.start = new Date(element.startDate).getTime();
        task.end = new Date(element.endDate).getTime();
        let timeDifference = new Date(element.endDate).getTime() - new Date(element.startDate).getTime();
        task.duration = timeDifference;
        task.durationStr = element.duration;
        task.startDate = moment(new Date(element.startDate)).format('YYYY-MM-DD');
        task.endDate = moment(new Date(element.endDate)).format('YYYY-MM-DD');
        //计算 startDate 至 endDate 之间有多少天
        task.percent = element.progress
        task.percentStr = task.percent;
        task.type = "task";
        //判断element的ID是否 在 pIds里面
        if (pIds.includes(element.id)) {
          task.taskName = "<label style='color:black;'>" + task.taskName + "</label>";
          task.durationStr = "<label style='color:black;'>" + task.durationStr + "</label>";
          task.startDate = "<label style='color:black;'>" + task.startDate + "</label>";
          task.endDate = "<label style='color:black;'>" + task.endDate + "</label>";
          task.percentStr = "<label style='color:black;'>" + task.percentStr + "</label>";
          task.type = "milestone";
          task.collapsed = false;
          task.user = "";
          task.style = {
            base: {
              fill: "#1EBC61",
              stroke: "#0EAC51"
            }
          }
        }
        if (element.pId !== null) {
          task.parentId = element.pId;
        }
        if (element.predecessorId !== null) {
          task.dependentOn = [];
          task.dependentOn[0] = element.predecessorId;
        }
        this.tasks.push(task)
      })


    });

    getTaskBurnDownChartData(this.projectId).then(response => {
      this.actualProgress = response.data.actualProgress;
      this.planProgress = response.data.planProgress;
      this.progressDeterioration = response.data.progressDeterioration;
      let chartDom1 = document.getElementById('ran-jin-tu');
      let myChart = echarts.init(chartDom1);
      let option1 = {
        title: {
          text: '燃尽图',
          textStyle: {
            fontSize: 14
          }
        },
        legend: {
          data: ['计划', '实际', '预测']
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          axisLabel: {
            show: true
          },
          data: response.data.xAxisData
        },
        yAxis: {
          type: 'value',
          axisLine: {
            show: true
          },
          axisLabel: {
            show: true
          }
        },

        grid: {
          top: '30',
          left: '20',
          right: '20',
          bottom: '5',
          containLabel: true
        },
        series: [
          {
            name: '计划',
            showSymbol: false,
            data: response.data.plannedProgressData,
            type: 'line',
            smooth: true,
          },
          {
            name: '实际',
            showSymbol: false,
            data: response.data.actualProgressData,
            type: 'line',
            smooth: true,
          },
          {
            name: '预测',
            showSymbol: false,
            data: response.data.predictingProgress,
            type: 'line',
            smooth: true,
          }
        ]
      };
      option1 && myChart.setOption(option1);


      this.plannedStartDate = response.data.xAxisData[0]
      this.plannedEndDate = response.data.xAxisData[response.data.plannedProgressData.length - 1]
      this.actualStartDate = response.data.xAxisData[0]
      if (this.actualProgress === 100) {
        this.actualEndDate = response.data.xAxisData[response.data.actualProgressData.length - 1]
        this.predictingStartDate = ''
        this.predictingEndDate = ''
      } else {
        this.actualEndDate = ''
        this.predictingStartDate = response.data.xAxisData[0]
        this.predictingEndDate = response.data.xAxisData[response.data.predictingProgress.length - 1]
      }


    });


  },
  methods: {


    bindExportEvent() {
      // 使用事件委托监听按钮点击
      this.$el.addEventListener('click', (event) => {
        if (event.target.closest('.export-btn')) {
          this.export();
        }
      });
    },

    export: function () {
      this.pmsDownload('pms/task/export?projectId='+this.projectInfo.id, this.projectInfo.projectName + '.xlsx')
    },
    onTabClick: function (tab, event) {
      console.log(tab, event);
    },
    getTask: function () {
      return this.tasks
    },
    tasksUpdate(tasks) {
      this.tasks = tasks;
    },
    optionsUpdate(options) {
      this.options = options;
    },
    styleUpdate(style) {
      this.dynamicStyle = style;
    },

    treeToList(treeData, childKey = 'children') {
      const arr = []
      const expanded = data => {
        if (data && data.length > 0) {
          data.filter(d => d).forEach(e => {
            arr.push(e)
            expanded(e[childKey] || [])
          })
        }
      }
      expanded(treeData)
      arr.forEach(item => {
        item.children = [];
        item.child = [];
      });
      return arr
    },
    listToTree(list, idKey = 'id', parentKey = 'predecessorId', childKey = 'children') {
      const nodeMap = new Map();
      const roots = [];

      // 创建新节点以避免修改原始数据
      const newNodes = list.map(node => {
        const newNode = {...node};
        newNode[childKey] = []; // 初始化子节点数组
        nodeMap.set(newNode[idKey], newNode);
        return newNode;
      });

      for (const node of newNodes) {
        const parentId = node[parentKey];
        if (parentId == null) { // 父键为 null/undefined 时作为根节点
          roots.push(node);
        } else {
          const parent = nodeMap.get(parentId);
          if (parent) {
            parent[childKey].push(node);
          } else {
            // 父节点不存在，当前节点作为根节点
            roots.push(node);
          }
        }
      }

      return roots;
    },


    traverseUpFromLeaf(node, callback) {
      if (!node.children || node.children.length === 0) {
        // 如果当前节点没有子节点，调用回调函数
        callback(node);
      } else {
        // 遍历当前节点的子节点
        node.children.forEach(child => {
          // 递归调用自身，向下遍历子节点
          this.traverseUpFromLeaf(child, callback);
        });
        // 遍历完所有子节点后，调用回调函数
        callback(node);
      }
    },
    //辅助函数：获取最早的开始时间节点
    getEarliestStartDateNode(nodes) {
      return nodes.reduce((earliest, current) => {
        if (!earliest || (current.startDate && earliest.startDate > current.startDate)) {
          return current;
        }
        return earliest;
      }, null);
    },
    //辅助函数：获取最晚的结束时间节点
    getLatestEndDateNode(nodes) {
      return nodes.reduce((latest, current) => {
        if (!latest || (current.endDate && latest.endDate < current.endDate)) {
          return current;
        }
        return latest;
      }, null);
    },
    sumChildrenDurations(children) {
      // 安全获取子节点数组，处理不存在或非数组的情况
      // 使用reduce累加duration，处理无效值为0
      return children.reduce((total, child) => {
        const duration = Number(child?.duration) || 0;
        return total + duration;
      }, 0);
    },


  },
  watch: {}
}


</script>


<style scoped lang="scss">
.app-overview {
  background: #F5F8FA;


  .box-card {
    width: 1000px;
    margin-bottom: 20px;
  }


  .project-name {
    font-size: 20px;
    display: inline-block;
  }

  .project-desc {
    font-size: 15px;
    display: inline-block;
    color: #909399;
  }


  .row-bg {
    width: 100%;
    height: 100px;
  }

  .row-bg-1 {
    border-bottom: 1px solid gray; /* 设置底部边框为1像素宽度，实线样式，黑色 */
  }


  .grid-content {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    background: white;
  }

  .grid-content-1 {
    border-right: 1px solid gray; /* 设置底部边框为1像素宽度，实线样式，黑色 */
  }

  .top-half {
    display: flex;
    align-items: flex-end;
    justify-content: center;
    text-align: center;
    width: 100%;
    flex: 1;
    padding: 5px;
    font-weight: bold;
    color: #409EFF;
    font-size: 20px;
  }

  .bottom-half {
    display: flex;
    align-items: flex-start;
    justify-content: center;
    width: 100%;
    flex: 1;
    color: #606266;
    //加粗
    font-weight: bold;
    padding: 5px;
  }

  #bin-tu {
    padding-left: 20px;
    height: 280px;
    width: 580px;
  }

  #ran-jin-tu {
    height: 300px;
    width: 1000px;
  }

  #ran-jin-tu2 {
    height: 300px;
    width: 1000px;
  }

  .app-overview {

  }

  .el-tabs__content {
    height: 700px;
  }

}
</style>
