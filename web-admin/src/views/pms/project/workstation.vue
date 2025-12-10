<template>
  <div class="app-container workstation-container">

    <el-row :gutter="10">

      <el-col :span="16">
        <div class="top">
          <el-row>
            <el-col :span="24">
              <el-col :span="4">
                <div class="grid-content grid-content-1">
                  <div class="top-half">{{ countInfo.projectsParticipated }}</div>
                  <div class="bottom-half">参与项目数</div>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content grid-content-1">
                  <div class="top-half">{{ countInfo.projectsCompleted }}</div>
                  <div class="bottom-half">完成项目数</div>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content grid-content-1">
                  <div style="color: #E6A23C" class="top-half">{{ countInfo.projectsPending }}</div>
                  <div class="bottom-half">待办项目数</div>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content grid-content-1">
                  <div class="top-half">{{ countInfo.tasksCompleted }}</div>
                  <div class="bottom-half">完成任务数量</div>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content grid-content-1">
                  <div style="color: #E6A23C" class="top-half">{{ countInfo.tasksPending }}</div>
                  <div class="bottom-half">待办任务数量</div>
                </div>
              </el-col>
              <el-col :span="4">
                <div class="grid-content grid-content-1">
                  <div style="color: #F56C6C" class="top-half">{{ countInfo.tasksOverdue }}</div>
                  <div class="bottom-half">逾期任务数量</div>
                </div>
              </el-col>
            </el-col>
          </el-row>
        </div>
        <div class="bottom">
          <el-row style="height:100%" :gutter="10">
            <el-col :span="10" style="height:100%; ">
              <div class="project-list">
                <el-table
                  class="hide-scrollbar"
                  height="100%"
                  :show-header=true
                  highlight-current-row
                  :data="projectList"
                  @row-click="handleProjectListRowClick"
                  style="width:100%;">
                  <el-table-column prop="projectName" :show-overflow-tooltip="true" label="项目列表">
                    <template slot="header" slot-scope="scope">
                      <div
                        style="color: #606266 ;display: flex; align-items: center ; justify-content:  flex-start; height: 80px; font-size: 20px; margin-left: 10px">
                        <span>项目列表</span>
                      </div>
                    </template>

                    <template slot-scope="scope">
                      <div>
                        <span class="project-name" style="font-size: medium;color: #606266">{{
                            scope.row.projectName
                          }}</span>
                        <br/>
                        <span class="project-desc">{{ scope.row.description }}</span>
                      </div>
                    </template>

                  </el-table-column>
                  <el-table-column prop="progress" label="进度" width="120px">
                    <template slot="header" slot-scope="scope">
                      <div
                        style="color: #909399 ;display: flex; align-items: center; justify-content: right; height: 80px; font-size: 17px; margin-right: 10px">
                        <!--                        <router-link :to="'/pms/project/index/'">-->
                        <!--                          <span>项目管理</span>-->
                        <!--                        </router-link>-->
                      </div>
                    </template>
                    <template slot-scope="scope">
                      <el-progress :percentage="scope.row.progress"></el-progress>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
            <el-col :span="14" style="height:100%;">
              <div class="task-list-header">
                <el-row>
                  <el-col :span="15">
                    <el-radio-group size="medium" v-model="tabPosition" @input="handleTaskListTabPositionChange">
                      <el-radio-button label="我的">我的</el-radio-button>
                      <el-radio-button label="协助">协助</el-radio-button>
                      <el-radio-button label="全部">全部</el-radio-button>
                    </el-radio-group>
                    <br/>
                    <el-radio-group style="margin-top: 10px" v-model="tabPosition1"
                                    @input="handleTaskListTabPositionChange">
                      <el-radio-button label="待办">待办</el-radio-button>
                      <el-radio-button label="逾期">逾期</el-radio-button>
                      <el-radio-button label="完成">完成</el-radio-button>
                      <el-radio-button label="全部">全部</el-radio-button>
                    </el-radio-group>
                  </el-col>
                  <el-col :span="9">
                    <el-select
                      size="medium"
                      style="margin-left:5px"
                      v-model="assignUserIds"
                      v-if="tabPosition==='全部'"
                      @change="handleAssignUserChange"
                      multiple
                      collapse-tags
                      placeholder="指派给" clearable>
                      <el-option
                        v-for="item in users"
                        :key="item.userId"
                        :label="item.nickName"
                        :value="item.userId">
                        <span style="float: left">{{ item.nickName }}</span>
                      </el-option>
                    </el-select>
                  </el-col>
                </el-row>
              </div>
              <div class="task-list">
                <el-table
                  :show-header="false"
                  class="hide-scrollbar"
                  height="100%"
                  row-key="id"
                  :data="taskList"
                  :tree-props="{children: 'children'}"
                  :expand-row-keys="projectExpandRowKeys[projectId]"
                  @expand-change="handleTaskListExpandChange"
                  style="width: 100%">
                  <el-table-column
                    prop="taskName"
                    label="任务列表">
                    <template slot="header" slot-scope="scope">
                      <el-row>
                        <el-col :span="24">
                          <el-radio-group v-model="tabPosition" @input="handleTaskListTabPositionChange">
                            <el-radio-button label="待办">待办</el-radio-button>
                            <el-radio-button label="我的">我的</el-radio-button>
                            <el-radio-button label="协助">协助</el-radio-button>
                            <el-radio-button label="全部">全部</el-radio-button>
                          </el-radio-group>
                        </el-col>
                      </el-row>
                    </template>
                    <template slot-scope="scope" style="display: inline">
                      <el-tooltip class="item" effect="dark" placement="right">
                        <div slot="content">
                          开始时间：{{ scope.row.startDate }}
                          <br/>
                          结束时间：{{ scope.row.endDate }}
                          <br/>
                          工期：{{ scope.row.duration }}
                          <br/>
                          进度：{{ scope.row.progress }}%
                        </div>
                        <el-button
                          v-if="!scope.row.isBranch"
                          size="mini"
                          style="color: #606266"
                          @click="handleShowTaskDialog(scope.row)" type="text"
                          v-hasPermi="['pms:project:list']">
                          {{ scope.row.taskName }}
                        </el-button>
                        <span style="color: black;font-weight: bold" v-if="scope.row.isBranch ">
                       {{ scope.row.taskName }}
                      </span>
                      </el-tooltip>
                    </template>
                  </el-table-column>
                  <el-table-column
                    width="120px"
                    prop="assignUserName"
                    align="center"
                    label="指派给">
                    <template slot="header" slot-scope="scope">
                      <el-select
                        v-model="assignUserIds"
                        v-if="tabPosition==='全部'"
                        @change="handleAssignUserChange"
                        multiple
                        collapse-tags
                        placeholder="指派给" clearable>
                        <el-option
                          v-for="item in users"
                          :key="item.userId"
                          :label="item.nickName"
                          :value="item.userId">
                          <span style="float: left">{{ item.nickName }}</span>
                        </el-option>
                      </el-select>
                    </template>
                    <template slot-scope="scope">
                      <el-progress :percentage="scope.row.progress"></el-progress>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="top">
          <el-row>
            <el-col :span="24">
              <el-row>
                <el-col :span="8">
                  <div class="grid-content grid-content-1">
                    <div class="top-half">{{ planProgress }}%</div>
                    <div class="bottom-half">计划进度</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content grid-content-1">
                    <div class="top-half">{{ actualProgress }}%</div>
                    <div class="bottom-half">实际进度</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="grid-content grid-content-1">
                    <div style="color: #E6A23C" class="top-half">{{ progressDeterioration }}%</div>
                    <div class="bottom-half">进度偏差</div>
                  </div>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </div>
        <div class="bottom">
          <el-row style="height:100%" :gutter="10">
            <el-col :span="24" style="height:100%;">

              <div id="tu1" class="project-info">

              </div>


              <div class="project-dynamics">
                <el-table
                  class="hide-scrollbar"
                  height="100%"
                  highlight-current-row
                  :data="dynamicsList"
                  style="width:100%;">
                  <el-table-column prop="content" label="项目动态">
                    <template slot-scope="scope">
                      <div>
                        <span class="dynamics-createtime">{{ scope.row.createTime }}</span>
                        <br/>
                        <span class="dynamics-context">{{ scope.row.content }}</span>
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </div>

            </el-col>
          </el-row>
        </div>
      </el-col>

    </el-row>


    <el-dialog
      :title="'任务信息'"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="1200px">
      <div id="dialog">

        <div class="dialog-task-info">
          <el-card class="box-card" shadow="never">

            <el-descriptions :colon="false" :column="3">
              <el-descriptions-item label="任务名称：">{{ form.taskName }}</el-descriptions-item>
            </el-descriptions>

            <el-descriptions :colon="false" :column="3">
              <el-descriptions-item label="开始时间：">{{ form.startDate }}</el-descriptions-item>
              <el-descriptions-item label="结束时间：">{{ form.endDate }}</el-descriptions-item>
              <el-descriptions-item label="工期：">{{ form.duration }}</el-descriptions-item>
            </el-descriptions>

            <el-descriptions :colon="false" :column="1">
              <el-descriptions-item label="指派给：">{{ form.assignUserName }}</el-descriptions-item>
            </el-descriptions>

            <el-descriptions :colon="false" :column="1">
              <el-descriptions-item label="协作人：">{{ form.collaboratorUserNames }}</el-descriptions-item>
            </el-descriptions>

            <el-descriptions :colon="false" :column="1">
              <el-descriptions-item label="进度：">
                <el-slider
                  style="width: 100%"
                  v-model="form.progress"
                  :marks="progressMarks"
                  @change="handleProgressChange"
                  show-input>
                </el-slider>
              </el-descriptions-item>
            </el-descriptions>

            <el-divider></el-divider>

            <el-descriptions :colon="false" :column="1">
              <el-descriptions-item label="任务描述：">
                <div class="ql-editor" v-html="form.description"></div>
              </el-descriptions-item>
            </el-descriptions>

          </el-card>

        </div>

        <ul class="dialog-comment-info">
          <li v-for="row in commentList" class="infinite-list-item">
            <div class="comment-left">
              <el-avatar shape="square" :size="40">
                {{ userLogo }}
              </el-avatar>
            </div>
            <div class="comment-right">
              <div class="comment-right-title">
                <span style="color: #606266">{{ row.userName }} </span> <span style="color: #909399;margin-left: 10px"> {{
                  row.createTime
                }}</span>
              </div>
              <div class="comment-right-body">
                <div class="ql-editor" v-html="row.content"></div>
              </div>
              <div class="comment-right-footer">
                <el-button
                  style="font-size: small;color: #409EFF" @click="handleDeleteComment(row)"
                  type="text"
                  v-hasPermi="['pms:project:delete']">
                  删除
                </el-button>
              </div>


            </div>
          </li>
        </ul>

        <div class="dialog-comment-input">
          <div class="comment-left">
            <el-avatar shape="square" :size="40">
              {{ userLogo }}
            </el-avatar>
          </div>
          <div class="comment-right">
            <editor v-model="comment" :height="192"/>
            <br/>
            <el-button type="primary" style="width: 100px" @click="commentSubmit">备注</el-button>
          </div>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="open= false">关闭</el-button>
      </div>

    </el-dialog>


  </div>
</template>

<script>


import {getWorkStationCount, listProject,} from "@/api/pms/project/project";


import {getTask, getTaskBurnDownChartData, listTask, updateProgress} from "@/api/pms/project/task";
import {getProjectDynamics} from "@/api/pms/project/dynamics";
import {detailResources, listResources} from "@/api/pms/project/resources";
import {deleteComment, listComment, saveComment} from "@/api/pms/project/comment";

import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';

import moment from "moment";
import * as echarts from 'echarts';


export default {
  name: "Data",
  data() {
    return {
      dynamicsList: [],
      progressMarks: {
        0: '',
        10: '分析',
        20: '设计',
        30: '开发',
        80: '发布',
        90: '测试',
        100: '',
      },
      countInfo: {},
      comment: "",
      commentList: [],
      reverse: true,
      actualProgress: 0,
      planProgress: 0,
      progressDeterioration: 0,
      count: 100,
      users: [],
      userLogo: undefined,
      user: {},
      assignUserIds: [],
      assignUserId: undefined,
      collaboratorUserId: undefined,
      projectId: undefined,
      projectList: [],
      taskList: [],
      tabPosition: "全部",
      tabPosition1: "待办",
      projectExpandRowKeys: {},
      // 遮罩层
      loading: true,

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskName: undefined,
        projectId: undefined
      },
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        },
          {
            text: '一周后',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
      },
      // 表单参数
      form: {},
      // 表单校验
    };
  },
  created() {

    detailResources().then(response => {
      this.user = response.data;
      let str = response.data.nickName
      this.userLogo = str.charAt(0);
    });

    listResources().then(response => {
      this.users = response.data;
    });


    getWorkStationCount().then(response => {
      this.countInfo = response.data;
    })

    this.debouncedGetTaskList = this.debounce(this.getTaskList, 300);
    this.debouncedgetDynamicsList = this.debounce(this.getDynamicsList, 300);

  },
  mounted() {
    this.getProjectList();
  },
  methods: {
    debounce(func, delay) {
      let timer;
      return function () {
        const context = this;
        const args = arguments;
        clearTimeout(timer);
        timer = setTimeout(() => {
          func.apply(context, args);
        }, delay);
      };
    },
    listToTree(list, idKey = 'id', parentKey = 'pId', childKey = 'children') {
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
    getProjectList() {
      this.loading = true;
      this.queryParams.type = this.type;
      let queryParams = {};
      queryParams.type = '2';
      queryParams.pageNum = 1;
      queryParams.pageSize = 1000;
      queryParams.projectStatus = '1';
      listProject(queryParams).then(response => {
        this.projectList = response.data.rows;
        this.loading = false;
      });
    },
    getLeafPaths(tree3) {
      let root = {};
      root.taskName = "root"
      root.id = "0";
      root.children = tree3;

      let leafPaths = [];
      getLeafPaths1(root);
      leafPaths.forEach(item => {
        item.shift();
      });


      return leafPaths;

      function getLeafPaths1(node, path = []) {
        path.push(node);
        if (node.children.length === 0) {
          // let treeData = JSON.parse(JSON.stringify(path));
          leafPaths.push(path);
        } else {
          // 如果不是叶节点，则继续遍历子节点
          node.children.forEach(child => {
            getLeafPaths1(child, [...path]);
          });
        }
      }
    },
    formatDatYYYYMMDD(input) {
      // 如果输入是字符串，转换为 Date 对象
      let date = typeof input === 'string' ? new Date(input) : input;
      // 确保 date 是一个有效的 Date 对象
      if (!(date instanceof Date) || isNaN(date)) {
        throw new Error('Invalid date input');
      }
      // 设置时间为 00:00:00.000
      //date.setHours(0, 0, 0, 0);
      // 提取年、月、日、小时、分钟
      let year = date.getFullYear();
      let month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从 0 开始，需要加 1
      let day = String(date.getDate()).padStart(2, '0');
      // 返回格式化后的字符串
      return `${year}-${month}-${day}`;
    },
    findPaths(node, currentPath = [], paths = []) {
      // 添加当前节点到路径
      currentPath.push(node.id);
      // 如果当前节点是叶节点（没有子节点），则将当前路径添加到结果中
      if (node.isBranch === false) {
        paths.push([...currentPath]);
      } else {
        // 递归遍历子节点
        for (const child of node.children) {
          this.findPaths(child, currentPath, paths);
        }
      }
      // 回溯：移除当前节点，以便继续遍历其他分支
      currentPath.pop();
      return paths;
    },
    getTaskList() {
      if (!this.projectId) {
        return;
      }
      listTask({projectId: this.projectId}).then(response => {

          // //判断 projectExpand 中 是否有   this.projectId 属性
          // if (!this.projectExpandRowKeys.hasOwnProperty(this.projectId)) {
          //   this.projectExpandRowKeys[this.projectId] = [];
          // }
          //
          // if (this.projectExpandRowKeys[this.projectId].length === 0) {
          //   this.projectExpandRowKeys[this.projectId] = list.map(item => item.pId);
          // }

          response.data.forEach(item => {
            item.startDate = this.formatDatYYYYMMDD(item.startDate);
            item.endDate = this.formatDatYYYYMMDD(item.endDate);
          })

          let pIds = response.data.map(item => item.pId);
          pIds = pIds.filter(item => item !== null);
          //去重
          pIds = [...new Set(pIds)];
          // console.info("pIds",pIds)

          response.data.forEach(item => {
            if (pIds.includes(item.id)) {
              item.isBranch = true
            } else {
              item.isBranch = false
            }
          })


          //所有叶节点
          let allLeafNode = response.data.filter(item => !pIds.includes(item.id))
          let filterList = [];
          allLeafNode.forEach(item => {

            if ("全部" === this.tabPosition) {
              // console.log("----------------------11")
              // console.log(item.assignUserId)
              // console.log(this.assignUserIds)

              if (this.assignUserIds.length > 0 && !this.assignUserIds.includes(item.assignUserId)) {
                filterList.push(item);
              }

            }

            if ("我的" === this.tabPosition) {
              if (this.assignUserId !== item.assignUserId) {
                filterList.push(item);
              }
            }

            if ("协助" === this.tabPosition) {
              let collaboratorUserIds = item.collaboratorUserList.map(item => item.userId);
              if (!collaboratorUserIds.includes(this.collaboratorUserId)) {
                filterList.push(item);
              }
            }


            if ('待办' === this.tabPosition1) {
              if (item.progress === 100) {
                filterList.push(item);
              }
            }

            if ('全部' === this.tabPosition1) {

            }

            if ('逾期' === this.tabPosition1) {
              if (!(item.progress < 100 && new Date(item.endDate) < new Date())) {
                filterList.push(item);
              }
            }

            if ('完成' === this.tabPosition1) {
              if (item.progress !== 100) {
                filterList.push(item);
              }
            }

          })
          // console.info("filterList",filterList)
          let filterIds = filterList.map(item => item.id);
          // console.info("filterIds",filterIds)
          response.data = response.data.filter(item => !filterIds.includes(item.id));
          let tempTree = this.listToTree(response.data, 'id', 'pId', 'children');

          let root = {
            id: null,
            name: '根节点',
            children: tempTree
          }
          let paths = this.findPaths(root)
          paths = paths.flat();
          paths = [...new Set(paths)];
          paths = paths.filter(item => item !== null);
          // console.info("paths", paths)
          response.data = response.data.filter(item => paths.includes(item.id));
          if (!this.projectExpandRowKeys.hasOwnProperty(this.projectId)) {
            this.projectExpandRowKeys[this.projectId] = [];
          }

          if (this.projectExpandRowKeys.length === 0) {
            this.projectExpandRowKeys[this.projectId] = response.data.map(item => item.pId);
          }

          this.taskList = this.listToTree(response.data, 'id', 'pId', 'children');

          //汇总时间
          root.children = this.taskList

          // console.info("taskList", this.taskList)
          // console.info("root", root)
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


        }
      )
      ;


      getTaskBurnDownChartData(this.projectId).then(response => {
        this.actualProgress = response.data.actualProgress;
        this.planProgress = response.data.planProgress;
        this.progressDeterioration = response.data.progressDeterioration;
        let chartDom1 = document.getElementById('tu1');
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
              show: false
            },
            axisLabel: {
              show: false
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

      });

    },

    getDynamicsList() {

      if (!this.projectId) {
        return
      }

      getProjectDynamics(this.projectId).then(response => {

        response.data.forEach(item => {
          item.createTime = moment(new Date(item.createTime)).format('YYYY-MM-DD HH:mm:ss');
        })

        this.dynamicsList = response.data;
      });

    }
    ,

    commentSubmit() {
      this.count++;
      let saveCommentDm = {};
      saveCommentDm.moduleId = this.form.id;
      saveCommentDm.comment = this.comment;
      saveComment(saveCommentDm).then(response => {
        listComment(this.form.id).then(response => {
          this.commentList = response.data;
          this.$nextTick(() => {
            let table = document.getElementById("dialog");
            table.scrollTop = table.scrollHeight;
            this.comment = "";
          })
        })
      });
    }
    ,

    handleDeleteComment(row) {
      deleteComment(row.id).then(response => {
        listComment(this.form.id).then(response => {
          this.commentList = response.data;
        })
      });
    }
    ,

    handleProjectListRowClick(row, column, event) {
      this.projectId = row.id;

      if ("待办" === this.tabPosition) {
        this.assignUserId = this.user.userId;
      }

      if ("我的" === this.tabPosition) {
        this.assignUserId = this.user.userId;
      }

      if ("协助" === this.tabPosition) {
        this.collaboratorUserId = this.user.userId;
      }

      if ("全部" === this.tabPosition) {
        this.assignUserId = undefined;
        this.collaboratorUserId = undefined;
      }

      this.debouncedGetTaskList();
      this.debouncedgetDynamicsList();
    },

    handleTaskListExpandChange(row, e) {
      if (e) {
        this.projectExpandRowKeys[this.projectId].push(row.id);
      } else {
        if (row) {
          this.projectExpandRowKeys[this.projectId] = this.projectExpandRowKeys[this.projectId].filter(item => item !== row.id);
        }
      }

    },

    handleAssignUserChange(values) {
      if (this.projectId) {
        this.getTaskList()
      }
    },

    handleShowTaskDialog(row) {
      getTask(row.id).then(response => {
        response.data.startDate = moment(new Date(response.data.startDate)).format('YYYY-MM-DD');
        response.data.endDate = moment(new Date(response.data.endDate)).format('YYYY-MM-DD');
        let array = response.data.collaboratorUserList.map(item => item.nickName);
        response.data.collaboratorUserNames = array.join(", ");
        this.form = response.data;
        this.open = true;
        this.title = "任务信息";
        listComment(row.id).then(response => {
          this.commentList = response.data;
          this.$nextTick(() => {
            let table = document.getElementById("dialog");
            table.scrollTop = 0;
          })
        })
      });
    },

    handleTaskListTabPositionChange(label) {
      if ("待办" === this.tabPosition) {
        this.assignUserId = this.user.userId;
      }

      if ("我的" === this.tabPosition) {
        this.assignUserId = this.user.userId;
      }

      if ("协助" === this.tabPosition) {
        this.collaboratorUserId = this.user.userId;
      }

      if ("全部" === this.tabPosition) {
        this.assignUserId = undefined;
        this.collaboratorUserId = undefined;
      }

      this.debouncedGetTaskList();
      this.debouncedgetDynamicsList();
    },

    handleProgressChange() {
      let dm = {};
      dm.id = this.form.id;
      dm.progress = this.form.progress;
      updateProgress(dm).then(response => {
        this.getTaskList()
      })
    },

// 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    }
    ,

// 表单重置
    reset() {
      this.form = {
        startDate: new Date(),
        endDate: new Date(),
        description: '',
      };
      this.resetForm("form");
    }
    ,
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    }
    ,

  },
  watch: {}
  ,
  filters: {}
}

</script>
<style lang="scss">

.workstation-container {
  height: calc(100vh - 84px);
  background: #F5F8FA;
  min-width: 1366px;

  .el-row {
    height: 100%;
  }

  .el-col {
    height: 100%;
  }

  .top {
    height: calc(100% * 0.13);
    margin-bottom: 10px;
    background: white;
  }

  .bottom {

    height: calc(100% * 0.85);

    el-row {
      padding: 50px;
    }

    el-col {
      padding: 50px;
    }

    .project-list {
      height: 100%;

    }

    .task-list {
      height: calc(100% - 100px);
    }

    .task-list-header {
      height: 100px;
      background: white;
      padding: 10px;
      //下边框
      border-bottom: 1px solid #EBEEF5;
    }

    .project-info {
      height: 25%;
      background: white;
    }

    .project-dynamics {
      height: 75%;
      padding-top: 10px;

      .dynamics-createtime {
        color: #909399;
      }

      .dynamics-context {
        color: #606266;
      }

    }
  }

  .like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
  }

  .hide-scrollbar ::-webkit-scrollbar {
    display: none; /* Chrome, Safari, Opera*/
  }

  .hide-scrollbar {
    -ms-overflow-style: none; /* IE, Edge */
    scrollbar-width: none; /* Firefox */
  }

  //表头颜色
  .el-table {
    .el-table__header-wrapper, .el-table__fixed-header-wrapper {
      th {
        background-color: white;
      }
    }
  }

  .project-name {
    font-size: 17px;
    display: inline-block;
  }

  .project-desc {
    font-size: 15px;
    display: inline-block;
    color: #909399;
  }

  .row-bg {
    width: 100%;
    height: 33.333%;
  }

  .row-bg-1 {
    //  border-bottom: 1px solid gray; /* 设置底部边框为1像素宽度，实线样式，黑色 */
  }

  .grid-content {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;

    justify-content: center; /* 水平居中 */
    align-items: center;
  }

  .grid-content-1 {
    //border-right: 1px solid gray; /* 设置底部边框为1像素宽度，实线样式，黑色 */
  }

  .top-half {
    display: flex;
    width: 100%;
    font-weight: bold;
    justify-content: center; /* 水平居中 */
    align-items: flex-end;
    color: #409EFF;
    font-size: 20px;
    padding: 5px;
  }

  .bottom-half {
    display: flex;
    width: 100%;
    color: #606266;
    font-weight: bold;
    justify-content: center; /* 水平居中 */
    align-items: flex-start;
    padding: 5px;

  }

  .el-button--mini {
    padding: 0;
    font-size: 14px;
  }

  #dialog {
    height: calc(100vh * 0.7);
    overflow: auto;
    padding: 10px;
  }

  .dialog-task-info {
    .el-descriptions-item__container .el-descriptions-item__label, .el-descriptions-item__container .el-descriptions-item__content {
      align-items: center;
      font-size: 16px;
    }
  }


  .dialog-comment-info {
    margin: 0px;
    padding: 0px;
    list-style: none;

    .infinite-list-item {
      display: flex;
      margin-bottom: 10px;
    }
  }


  .dialog-comment-input {
    width: 100%;
    display: flex;
    flex-direction: row;
  }

  .comment-left {
    width: 50px;
  }

  .comment-right {
    width: calc(100% - 60px);
    display: flex;
    flex-direction: column;

    .comment-right-title {
      height: 40px;
      align-content: center;
      font-size: 16px;
    }

    .comment-right-body {
    }

    .comment-right-footer {

    }
  }

  .el-avatar {
    font-size: 25px !important
  }


  .box-card {
    margin-bottom: 10px;
  }


}

</style>
