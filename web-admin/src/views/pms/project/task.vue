<template>
  <div class="app-container task-container">
    <div class="top">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd(undefined)"
            v-hasPermi="['pms:project:save']"
          >新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-plus"
            size="mini"
            :disabled="single"
            @click="handleAbstract"
            v-hasPermi="['pms:project:save']"
          >摘要
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDeleteRows"
            v-hasPermi="['pms:project:delete']"
          >删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['pms:project:save']"
          >导出
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-check"
            size="mini"
            @click="handleSave"
            v-hasPermi="['pms:project:save']"
          >保存
          </el-button>
        </el-col>
        <!--        <el-col :span="1.5">-->
        <!--          <el-switch-->
        <!--            style="padding-top: 5px"-->
        <!--            v-model="autoSave"-->
        <!--            active-text="自动保存"-->
        <!--            :active-value="true"-->
        <!--            :nactive-value="false"-->
        <!--          >-->
        <!--          </el-switch>-->
        <!--        </el-col>-->


        <!--        <el-col :span="1.5">-->
        <!--          <el-button-->
        <!--            type="warning"-->
        <!--            plain-->
        <!--            icon="el-icon-close"-->
        <!--            size="mini"-->
        <!--            @click="handleClose"-->
        <!--          >关闭-->
        <!--          </el-button>-->
        <!--        </el-col>-->
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
    </div>
    <div class="bottom">
      <el-table ref="myTable" id="table" row-key="_id" :height="'100%'" :key="tableKey" v-loading="loading"
                :data="dataTree"
                highlight-current-row
                :row-class-name="headerStyle"
                :indent="32"
                :expand-row-keys="expandRowKeys"
                @expand-change="onExpandChange"
                :tree-props="{children: 'children'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="任务名称" align="left" prop="taskName" :show-overflow-tooltip='true'>
          <template slot-scope="scope">
    <span :class="getRowClass(scope.row)">
        {{ scope.row.taskName }}
    </span>
          </template>
        </el-table-column>
        <!--        <el-table-column label="任务编号" align="center" prop="sortNum"/>-->

        <el-table-column label="任务模式" align="center" prop="taskType" width="120">
          <template slot-scope="scope">
            <span :class="getRowClass(scope.row)" v-show="scope.row.taskType === '0'">自动</span>
            <span :class="getRowClass(scope.row)" v-show="scope.row.taskType === '1'">手动</span>
          </template>
        </el-table-column>

        <el-table-column label="工期" align="center" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.duration" class="centered-input"
                      size="mini"
                      @focus="onFocus($event)"
                      :tabindex="(scope.row.sortNum+100000) +''"
                      v-show="!(scope.row.taskType==='1'|| scope.row.children.length>0) "
                      @change="updateTaskPlan(scope.row)"
                      @input="(v)=>(scope.row.duration=v.replace(/[^\d|\.]/g, ''))" clearable>
            </el-input>
            <span :class="getRowClass(scope.row)"
                  v-show="(scope.row.taskType==='1'|| scope.row.children.length>0)">{{ scope.row.duration }}</span>
          </template>
        </el-table-column>

        <el-table-column label="开始时间" align="center" prop="startDate" width="150">
          <template slot-scope="scope">
            <el-date-picker clearable
                            style="width:100%"
                            :tabindex="(scope.row.sortNum+200000) +''"
                            v-model="scope.row.startDate"
                            v-show="scope.row.taskType==='1'"
                            @change="updateTaskPlan(scope.row)"
                            size="mini"
                            type="date"
                            value-format="yyyy-MM-dd"
                            :picker-options="pickerOptions"
                            format="yyyy-MM-dd "
                            placeholder="请选择">
            </el-date-picker>
            <span :class="getRowClass(scope.row)" v-show="scope.row.taskType==='0'">{{
                scope.row.startDate|formatDate
              }}</span>
          </template>
        </el-table-column>

        <el-table-column label="结束时间" align="center" prop="endDate" width="150">
          <template slot-scope="scope">
            <el-date-picker clearable
                            style="width:100%"
                            v-model="scope.row.endDate"
                            :tabindex="(scope.row.sortNum+300000) +''"
                            v-show="scope.row.taskType==='1'"
                            @change="updateTaskPlan(scope.row)"
                            size="mini"
                            type="date"
                            value-format="yyyy-MM-dd"
                            :picker-options="pickerOptions"
                            format="yyyy-MM-dd "
                            placeholder="请选择">
            </el-date-picker>
            <span :class="getRowClass(scope.row)" v-show="scope.row.taskType==='0'">{{
                scope.row.endDate|formatDate
              }}</span>
          </template>
        </el-table-column>

        <el-table-column label="前置任务" align="center" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.predecessor" class="centered-input"
                      :tabindex="(scope.row.sortNum+400000) +''"
                      @focus="onFocus($event)"
                      size="mini"
                      v-show="scope.row.children.length===0&&scope.row.taskType!=='1'"
                      @change="updateTaskPlan(scope.row)"
                      @input="(v)=>(scope.row.predecessor=v.replace(/[^\d]/g,''))" clearable>
            </el-input>
          </template>
        </el-table-column>

        <el-table-column label="指派给" prop="assignUserId" align="center" width="150">
          <template slot-scope="scope">
            <el-select
              size="mini"
              v-show="scope.row.children.length===0"
              :tabindex="(scope.row.sortNum+500000) +''"
              @change="onAssignUserChange(scope.row)"
              v-model="scope.row.assignUserId" placeholder="请选择" filterable>
              <el-option
                v-for="item in users"
                :key="item.userId"
                :label="item.nickName"
                :value="item.userId">
                <span style="float: left">{{ item.nickName }}</span>
              </el-option>
            </el-select>

          </template>
        </el-table-column>

        <el-table-column label="进度" prop="progress" align="center" width="70">
          <template slot-scope="scope">
            <span :class="getRowClass(scope.row)" v-show="scope.row.taskType === '0'">{{ scope.row.progress }}%</span>
            <span :class="getRowClass(scope.row)" v-show="scope.row.taskType === '1'">{{ scope.row.progress }}%</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" fixed="right" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['pms:project:save']"
            >增加
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['pms:project:save']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['pms:project:delete']"
            >删除
            </el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      :lock-scroll="false"
      append-to-body
      width="980px">

      <el-form ref="form" inline :model="form" :rules="rules" label-width="80px">

        <el-form-item label="任务名称" prop="taskName">
          <el-input style="width: 835px" v-model="form.taskName" placeholder="请输入任务名称"/>
        </el-form-item>

        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="form.taskType">
            <el-option label="自动" value="0"></el-option>
            <el-option label="手动" value="1"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="工期" prop="duration" v-if="form.taskType==='0'">
          <el-input
            style="width: 220px"
            v-model="form.duration"
            @input="(v)=>(form.duration=v.replace(/[^\d|\.]/g, ''))"
            clearable
            placeholder="请输入工期"/>
        </el-form-item>

        <el-form-item label="开始时间" prop="startDate" v-if="form.taskType==='1'">
          <el-date-picker clearable
                          v-model="form.startDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          format="yyyy-MM-dd "
                          :picker-options="pickerOptions"
                          placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="结束时间" prop="endDate" v-if="form.taskType==='1'">
          <el-date-picker clearable
                          v-model="form.endDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          format="yyyy-MM-dd"
                          :picker-options="pickerOptions"
                          placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item v-show="form.taskType==='0'">
          <span style="width: 200px;display: block"></span>
        </el-form-item>

        <el-form-item label="指派给" prop="assignUserId">
          <el-select v-model="form.assignUserId" placeholder="请选择" filterable clearable>
            <el-option
              v-for="item in users"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId">
              <span style="float: left">{{ item.nickName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.deptName }}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="协作人" prop="collaboratorUserIds">
          <el-select
            style="width: 835px"
            multiple
            v-model="form.collaboratorUserIds"
            placeholder="请选择"
            filterable
            clearable>
            <el-option
              v-for="item in users"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId">
              <span style="float: left">{{ item.nickName }}</span>
              <!--              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.deptName }}</span>-->
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="进度" prop="progress">
          <el-slider
            style="width: 820px"
            v-model="form.progress"
            :marks="progressMarks"
            show-input
          >
          </el-slider>
        </el-form-item>

        <el-form-item label="任务描述" prop="description">
          <editor v-model="form.description" :min-height="192"/>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {listTask, saveTask} from "@/api/pms/project/task";
import {getProjectCalendarInfo} from "@/api/pms/project/calendar";
import {calculateEndTime, calculateWorkingDuration} from "@/utils/pms/calculate";
import {listResources} from "@/api/pms/project/resources";

import {getProject} from "@/api/pms/project/project";


import Sortable from 'sortablejs'
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';


export default {
  name: "Data",
  props: {
    // 定义一个名为id的prop，类型为String
    projectId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      selectNum: 0,
      projectName: "",
      progressMarks: {
        0: '',
        10: '分析',
        20: '设计',
        30: '开发',
        80: '发布',
        90: '测试',
        100: '',
      },
      leafPaths: [],
      r: "",
      timeNum: 0,
      expandRowKeys: [],
      tableKey: Math.random(),
      taskNameColumnWidth: "380",
      users: [],
      sortNum: 0,
      // 遮罩层
      loading: true,
      autoSave: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: false,
      // 总条数
      total: 0,
      // list数据格式
      idList: [],
      dataList: [],
      dataMap: {},
      dataTree: [],
      predecessorDataTree: [],
      paths: [],
      //日历信息
      calendarInfo: {},
      // 默认字典类型
      defaultDictType: "",
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据标签回显样式
      listClassOptions: [
        {
          value: "default",
          label: "默认"
        },
        {
          value: "primary",
          label: "主要"
        },
        {
          value: "success",
          label: "成功"
        },
        {
          value: "info",
          label: "信息"
        },
        {
          value: "warning",
          label: "警告"
        },
        {
          value: "danger",
          label: "危险"
        }
      ],
      // 类型数据字典
      typeOptions: [],
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
      rules: {
        projectId: [
          {required: true, message: "任务Id不能为空", trigger: "blur"}
        ],
        taskName: [
          {required: true, message: "任务名字不能为空", trigger: "blur"}
        ],
        taskType: [
          {required: true, message: "任务类型不能为空", trigger: "blur"}
        ],
        startDate: [
          {required: true, message: "开始时间不能为空", trigger: "blur"}
        ],
        endDate: [
          {required: true, message: "结束时间不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    //获取我是从那个路由进入
  },
  mounted() {
    this.load();
  },
  methods: {
    load() {
      this.queryParams.projectId = this.projectId;
      let that = this;
      getProject(this.queryParams.projectId).then(response => {
        this.projectName = response.data.projectName;
      });

      listResources().then(response => {
        this.users = response.data;
      });
      this.getList(false);
      this.handleAutoSave();
      this.rowDrop();
    },
    onAssignUserChange(row) {
      let dm = {};
      // dm.taskId = this.removeAfterPipe(row.id);
      let cRow = this.dataList.find(a => a.id === row.id)
      if (cRow) {
        cRow.assignUserId = row.assignUserId
      }
      // dm.assignUserId = row.assignUserId
      // assignUser(dm).then(response => {
      // });
    },
    onExpandChange(row, e) {
      if (e) {
        this.expandRowKeys.push(row._id);
      } else {
        if (row) {
          this.expandRowKeys = this.expandRowKeys.filter(item => item !== row._id);
        }
      }
    },

    findPaths(node, currentPath = [], paths = []) {
      // 添加当前节点到路径
      currentPath.push(node.taskName);
      // 如果当前节点是叶节点（没有子节点），则将当前路径添加到结果中
      if (!node.children.length) {
        // currentPath.push(node.taskName);
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


    updateTaskPlan(row) {
      // console.info("row", row)
      if (!row) {
        return;
      }

      let list = this.treeToList(this.dataTree, "children", false)

      let cRow = this.dataList.find(a => a.id === row.id);
      let pRow = list[row.predecessor - 1];
      cRow.startDate = row.startDate;
      cRow.endDate = row.endDate;
      cRow.duration = row.duration;
      cRow.assignUserId = row.assignUserId;
      //有指定前置任务
      if (pRow) {
        let ll = [];
        ll.push(cRow.taskName);
        ll.push(pRow.taskName);
        // 判断 this.paths的多个数组中 包含 ll 的元素 的数组
        let isContained = false;
        for (let i = 0; i < this.paths.length; i++) {
          let path = this.paths[i];
          let index = path.indexOf(pRow.taskName)
          path = path.slice(0, index + 1);
          // console.info("ll", ll)
          // console.info("path", path)
          isContained = ll.every(element => path.includes(element));
          if (isContained) {
            // console.info("包含")
            // isContained = true;
            break
          } else {
            // console.info("不包含")
          }
        }

        cRow.predecessorId = pRow.id
        //自己引用自己
        if (pRow.id === row.id) {
          cRow.predecessorId = null;
          cRow.predecessor = "";
        }
        //互相引用
        if (pRow.predecessorId === row.id) {
          cRow.predecessorId = null;
          cRow.predecessor = "";
        }
        //console.info("包含.........................", isContained)
        if (isContained) {
          //console.info("包含.........................")
          cRow.predecessorId = null;
          cRow.predecessor = "";
        }

        if (cRow.predecessorId === row.pId) {
          cRow.predecessorId = null;
          cRow.predecessor = "";
        }


      } else {
        cRow.predecessorId = null;
        cRow.predecessor = "";
      }

      // this.selectNum = row.sortNum;
      // alert()
      this.initTaskList();

    },
    onFocus(event) {
      event.currentTarget.select();
    },
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
    removeAfterPipe(str) {
      if (str === null) {
        return null;
      }
      const index = str.indexOf('|'); // 找到 '|' 字符的位置
      if (index !== -1) {
        return str.substring(0, index); // 截取 '|' 字符之前的部分
      } else {
        return str; // 如果没有 '|' 字符，直接返回原始字符串
      }
    },
    traverseDownFromLeaf(root, callback) {
      const levels = this.getLeafPaths(root);
      levels.forEach(level => {
        level.forEach(node => {
          callback(node);
        });
      });
    },
    getLeafPaths(root) {
      if (!root) return [];
      const result = []; // 存储每一层的节点
      const queue = [root]; // 使用队列存储待遍历的节点

      while (queue.length > 0) {
        const levelSize = queue.length; // 当前层的节点数量
        const currentLevel = []; // 存储当前层的节点

        for (let i = 0; i < levelSize; i++) {
          const currentNode = queue.shift(); // 取出队列中的第一个节点
          currentLevel.push(currentNode); // 将当前节点加入当前层

          // 将当前节点的子节点加入队列
          if (currentNode.children && currentNode.children.length > 0) {
            queue.push(...currentNode.children);
          }
        }
        result.push(currentLevel); // 将当前层加入结果
      }
      return result;
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
    /** 将树数据转化为平铺数据 */
    treeToList(treeData1, childKey = 'children', flag = true) {
      // let treeData = JSON.parse(JSON.stringify(treeData1));
      // let that = this;
      const arr = []
      const expanded = data => {
        if (data && data.length > 0) {
          data.filter(d => d).forEach(e => {
            arr.push(e)
            expanded(e[childKey] || [])
          })
        }
      }
      expanded(treeData1)
      if (flag) {
        arr.forEach(item => {
          item.children = []
        });
      }
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
    /** 行拖拽 */
    rowDrop() {
      // 此时找到的元素是要拖拽元素的父容器
      const tbody = document.querySelector('.el-dialog__wrapper .el-table__body-wrapper tbody');
      //console.info(tbody);
      const self = this;

      Sortable.create(tbody, {
        animation: 100,
        delay: 0,
        invertSwap: true,
        handle: ".current-row",
        onStart: (e) => {
          self.startScreenX = e.originalEvent.screenX;
          self.startScreenY = e.originalEvent.screenY;
          // console.info("onStart_screenX:" + e.originalEvent.screenX)
        },

        onEnd: (e) => {
          self.activeRows = this.treeToList(this.dataTree, 'children', false);
          // console.info("dataTree:", this.dataTree)
          // console.info("activeRows:", self.activeRows)
          if (e.newIndex === e.oldIndex) {
            let cRow = self.activeRows[e.newIndex];
            if (self.startScreenX > e.originalEvent.screenX) {
              //  console.info("crow-", cRow)
              self.onDragLeftward(self.activeRows, cRow)
            }
            if (self.startScreenX < e.originalEvent.screenX) {
              self.onDragRightward(self.activeRows, cRow)
            }
          } else {
            let cRow = self.activeRows.splice(e.oldIndex, 1)[0];
            self.activeRows.splice(e.newIndex, 0, cRow);
            let pRow = self.activeRows[e.newIndex - 1];
            if (self.startScreenY > e.originalEvent.screenY) {
              self.onDragUpward(self.activeRows, cRow, pRow)
            }
            if (self.startScreenY < e.originalEvent.screenY) {
              self.onDragDownward(self.activeRows, cRow, pRow)
            }
          }

          let sortNum = 0;
          self.activeRows.forEach(item => {
            item.children = []
            sortNum++;
            item.sortNum = sortNum;
          });


          this.dataList = self.activeRows
          console.info("activeRows:", self.activeRows)
          this.initTaskList()


          // this.$nextTick(() => {
          //   let list = this.deepCopyTaskList(self.activeRows);
          //   this.loading = true
          //   updateSortNum(list).then(res => {
          //     this.getList();
          //   });
          // });


        },

      })
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
    findMinMaxTimes(timeSlots) {
      const allTimes = timeSlots.flat();
      let minTime = allTimes[0];
      let maxTime = allTimes[0];
      for (const time of allTimes) {
        if (time < minTime) {
          minTime = time;
        }
        if (time > maxTime) {
          maxTime = time;
        }
      }
      return {min: minTime, max: maxTime};
    },
    formatDatYYYYMMDDHHMMSS(input) {
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
      let hours = String(date.getHours()).padStart(2, '0');
      let minutes = String(date.getMinutes()).padStart(2, '0');

      // 返回格式化后的字符串
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    generateUUID() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0;
        const v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    },
    sumChildrenDurations(children) {
      // 安全获取子节点数组，处理不存在或非数组的情况
      // 使用reduce累加duration，处理无效值为0
      return children.reduce((total, child) => {
        const duration = Number(child?.duration) || 0;
        return total + duration;
      }, 0);
    },
    /** 查询字典数据列表 */
    getList(isScroll) {
      // this.loading = true;
      // let that = this;
      getProjectCalendarInfo(this.queryParams.projectId).then(r => {
        this.calendarInfo = r.data
        //查询r.data.workHours最小得的开始时间
        listTask(this.queryParams).then(response => {
          this.dataList = response.data;
          this.dataList.forEach(item => {
            item.state = 'edit'
            item.collaboratorUserIds = item.collaboratorUserList.map(a => a.userId)
          })
          this.initTaskList();


        });

      })
    },
    initTaskList(isScroll = false) {
      this.timeNum = new Date().getTime();
      this.dataMap = []
      this.idList = []
      this.dataList.forEach(item => {
        this.dataMap[item.id] = item;
        this.idList.push(item.id)
      });
      let workHours = this.findMinMaxTimes(this.calendarInfo.workHours)
      //排序
      let pidTree = this.listToTree(this.dataList, "id", "pId", "children")
      this.dataList = this.treeToList(pidTree)
      //console.log(dataList);
      let predecessorIdTree = this.listToTree(this.dataList, "id", "predecessorId", "children")
      let root = {
        id: "0",
        taskName: "root",
        children: predecessorIdTree
      };
      this.timeNum = new Date().getTime();
      //遍历树
      //计算节点的开始时间、结束时间、持续时间
      this.traverseDownFromLeaf(root, node => {
        //设置前置任务编号
        node["_id"] = node.id + "|" + this.timeNum
        node['_pId'] = node.pId + "|" + this.timeNum
        if (node.predecessorId) {
          node['predecessor'] = this.idList.indexOf(node.predecessorId) + 1
        }
        // console.info("---------------------")
        //手动任务
        if (node.taskType === '1') {
          //计算持续时间
          let startDate = new Date(node.startDate);
          startDate.setHours(workHours.min.split(":")[0], workHours.min.split(":")[1], 0, 0)
          //转换成字符窜  YYYY-MM-DD HH:mm格式
          let startDateStr = this.formatDatYYYYMMDDHHMMSS(startDate)
          //console.log("startDateStr", startDateStr); // 输出: "YYYY-MM-DD HH:mm"
          let endDate = new Date(node.endDate);
          endDate.setHours(workHours.max.split(":")[0], workHours.max.split(":")[1], 0, 0)
          //转换成字符窜  YYYY-MM-DD HH:mm格式
          let endDateStr = this.formatDatYYYYMMDDHHMMSS(endDate)
          //console.log("endDateStr", endDateStr); // 输出: "YYYY-MM-DD HH:mm"
          let obj = calculateWorkingDuration(startDateStr, endDateStr, this.calendarInfo.workWeekDays, this.calendarInfo.workDates, this.calendarInfo.workHours, this.calendarInfo.exceptionDates)
          node.duration = obj.workDays;
          node.endDate = new Date(endDate);
          node.startDate = new Date(startDate);
          this.dataMap[node.id].startDate = startDate;
          this.dataMap[node.id].endDate = endDate;
          this.dataMap[node.id].duration = obj.workDays;
          //计算持续时间
        }

        //自动任务
        if (node.taskType === '0') {
          if (!node.predecessorId) {
            return
          }
          //根据predecessorId 开始时间 和结束时间
          // console.log("node", node)
          let predecessorNode = this.dataMap[node.predecessorId]
          // let cNode = dataMap[node.id]
          // console.log("predecessorNode-taskName", predecessorNode.taskName)
          // console.log("predecessorNode-endDate", predecessorNode.endDate)

          let startDate
          if (predecessorNode) {
            startDate = predecessorNode.endDate
          } else {
            startDate = '1999-01-01 ' + workHours.min
            startDate = new Date(startDate)
          }

          // let startDate = predecessorNode.endDate;
          let startDateStr = this.formatDatYYYYMMDDHHMMSS(startDate)
          //console.info(startDateStr)
          let duration = node.duration
          //console.info("++++++++++++++++++++++")
          let endDate = calculateEndTime(startDateStr, duration, this.calendarInfo.exceptionDates, this.calendarInfo.workDates, this.calendarInfo.workWeekDays, this.calendarInfo.workHours)
          endDate = new Date(endDate)
          node.startDate = startDate
          node.endDate = endDate
          this.dataMap[node.id].startDate = startDate;
          this.dataMap[node.id].endDate = endDate;
          // console.info("node.startDate", startDate)
          // console.info("node.taskName", node.taskName)
          // console.info("node.endDate", node.endDate)
          //计算结束时间
        }


      });
      this.dataList = this.treeToList(root.children)
      // console.info("dataList", this.dataList)
      //this.dataList 按 sortNum 排序
      this.dataList.sort((a, b) => {
        return a.sortNum - b.sortNum;
      });

      this.dataTree = this.listToTree(this.dataList, "id", "pId", "children");
      //汇总时间
      root.children = this.dataTree
      this.traverseUpFromLeaf(root, node => {
        //console.info(node.taskName)
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
      });
      if (this.expandRowKeys.length === 0) {
        this.expandRowKeys = this.dataList.map(item => item.pId + "|" + this.timeNum);
      } else {
        let l = this.expandRowKeys.map(item => this.removeAfterPipe(item));
        this.expandRowKeys = l.map(item => item + "|" + this.timeNum);
      }

      //console.info(this.dataTree);

      let dataTreeStr = JSON.stringify(this.dataTree);
      let tree = JSON.parse(dataTreeStr);
      this.dataList = this.treeToList(tree, "children", true)
      // this.predecessorDataTree = this.listToTree(this.dataList)
      // let root1 = {
      //   id: null,
      //   name: '根节点',
      //   children: this.predecessorDataTree
      // }
      // this.paths = this.findPaths(root1)
      this.loading = false;

      // if (!isScroll)
      //   return;
      // this.$nextTick(() => {
      //   // console.info("scroll")
      //   // let table = document.getElementsByClassName("el-table__body-wrapper is-scrolling-none")[0];
      //   // console.info("table",table)
      //   //
      //   // table.scrollTop = table.scrollHeight;
      //
      //
      //   const table = this.$refs.myTable;
      //   if (table && table.bodyWrapper) {
      //     // 直接操作滚动容器
      //     table.bodyWrapper.scrollTop = table.bodyWrapper.scrollHeight;
      //   }


      // })


    },
    headerStyle(e) {
      return 'tr_' + e.row.id;
    },
    getProw(list, cRow, pRow) {
      //获取所有class为el-table__row 的元素
      let rows = document.getElementsByClassName("el-table__row");
      let rs = [];
      for (let r of rows) {
        if (window.getComputedStyle(r).display !== 'none') {
          for (let classListKeyValue of r.classList) {
            if (typeof (classListKeyValue) === 'string' && classListKeyValue.includes("tr_")) {
              if (rs.indexOf(classListKeyValue) === -1) {
                rs.push(classListKeyValue);
                break
              }
            }
          }
        }
      }

      //console.info(rs)
      let cRowIndex = rs.indexOf("tr_" + cRow.id);
      //console.info(cRowIndex, cRowIndex)
      let pRowId = rs[cRowIndex - 1];
      //console.info("pRowId", pRowId)
      pRowId = pRowId.replace("tr_", "");
      //console.info("pRowId", pRowId)
      let ppRows = list.filter(item => item.id === pRowId)
      // console.info("ppRows")
      // console.info(ppRows)
      return ppRows[0];

    },
    onDragDownward(list, cRow, pRow) {
      console.info("向下拖动:")
      let bRow = this.getProw(list, cRow, pRow);
      console.info("cRow:", cRow)
      console.info("pRow:", pRow)
      console.info("bRow:", bRow)
      let pRowHasChild = (pRow.children.length > 0);
      let pRowExpand = false;
      //前节点折叠的时候处理
      if (pRowHasChild === true) {
        if (this.expandRowKeys.indexOf(pRow._id) === -1) {
          pRowExpand = true;
        }
      }
      if (pRowExpand === true) {
        console.info("处理1")
        cRow.pId = pRow.pId;
      } else {
        if (pRowHasChild === true) {
          console.info("处理2")
          cRow.pId = pRow.id;
        } else {
          if (cRow.id === pRow.pId) {
            console.info("处理3")
          } else {
            console.info("处理4")
            cRow.pId = pRow.pId;
          }
        }
      }
    },
    onDragUpward(list, cRow, pRow) {
      console.info("向上拖动")
      if (!pRow) {
        cRow.pId = null;
        return
      }
      let bRow = this.getProw(list, cRow, pRow);
      this.onDragDownward(list, cRow, bRow);
    },
    onDragLeftward(list, cRow) {
      if (!cRow.pId) {
        return
      }
      let pRow = list.find(item => item.id === cRow.pId)
      console.info("pRow:", pRow)
      cRow.pId = pRow.pId;
      console.info("crow:", cRow)
    },
    onDragRightward(list, cRow) {
      // console.info("向右拖动:")
      console.info("cRow:" + cRow.taskName)
      let bRow = this.getProw(list, cRow);
      let rows = list.filter(item => item.pId === cRow.pId);
      let index = rows.findIndex(item => item.id === cRow.id);
      // console.info("rows:", rows)
      // console.info("index:", index)
      if (index === 0) {
        return
      }
      index = index - 1;
      let pRow = rows[index];
      console.info("pRow:" + pRow.taskName)
      cRow.pId = pRow.id;

      //
      // if (cRow.pId === bRow.pId) {
      //   console.info("同级处理")
      //   cRow.pId = bRow.id;
      // } else {
      //   console.info("不同级处理")
      //   cRow.pId = bRow.pId;
      //   // if (cRow.pId === bRow.id) {
      //   // } else {
      //   //   cRow.pId = bRow.pId;
      //   // }
      // }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        taskIds: [],
        taskName: undefined,
        taskType: "0",
        duration: 1,
        progress: 0,
        startDate: new Date(),
        endDate: new Date(),
        description: undefined,
        assignmentUser: undefined,
        collaboratorUserIds: []
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.loading = true;
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 返回按钮操作 */
    handleClose() {
      const obj = {path: "/pms/task"};
      this.$tab.closePage();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      // this.queryParams.projectId = this.queryParams.projectId;
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.title = "添加任务";
      this.form.projectId = this.projectId;

      if (row !== undefined) {
        this.form.pId = this.removeAfterPipe(row.id);
        this.form.sortNum = this.dataList.length + 1000
      } else {
        this.form.pId = null;
        this.form.sortNum = this.dataList.length + 1000;
      }
      this.open = true;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      // console.info(this.ids)
      this.single = selection.length < 0
      this.multiple = !selection.length
      // this.dataList.forEach(item => {
      //   item.select = false;
      // });
      // selection.forEach(item => {
      //   item.select = true;
      // });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      let jsonStr = JSON.stringify(row);
      this.form = JSON.parse(jsonStr);
      //  this.form.collaboratorUserIds = this.form.collaboratorUserList.map(a=>a.userId)
      // console.info(this.form)
      this.open = true;
      this.title = "修改任务";
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        let row;
        if (this.form.id) {
          row = this.dataList.find(item => item.id === this.form.id)
          //let rowIndex = this.dataList.indexOf(row);
          row.taskName = this.form.taskName;
          row.taskType = this.form.taskType;
          row.duration = this.form.duration;
          row.startDate = this.form.startDate;
          row.endDate = this.form.endDate;
          row.description = this.form.description;
          row.assignUserId = this.form.assignUserId;
          row.progress = this.form.progress;
          // row.assignUserName = this.form.assignUserName;
          // row.assignmentUser = this.form.assignmentUser;
          row.collaboratorUserIds = this.form.collaboratorUserIds;

          //this.dataList.splice(rowIndex, 1, row);

        } else {
          row = {
            id: this.generateUUID(),
            taskName: this.form.taskName,
            taskType: this.form.taskType,
            duration: this.form.duration,
            startDate: this.form.startDate,
            endDate: this.form.endDate,
            description: this.form.description,
            assignUserId: this.form.assignUserId,
            progress: this.form.progress,
            // assignUserName: this.form.assignUserName,
            // assignmentUser: this.form.assignmentUser,
            collaboratorUserIds: this.form.collaboratorUserIds,
            pId: this.form.pId,
            projectId: this.form.projectId,
            sortNum: this.dataList.length + 1000,
            state: "add"
          }
          // console.info("taskIds",this.ids)
          if (this.ids) {
            // let filteredRows = this.dataList.filter(row => this.ids.includes(row.taskId))
            // filteredRows.forEach(item=>{item.pId = row.id})
            this.dataList.forEach(item => {
              if (this.ids.includes(item.id)) {
                item.pId = row.id
              }
            })
          }
          this.dataList.push(row)
          this.ids = [];
        }

        //console.info("save row", row)

        this.open = false;
        this.initTaskList(true)

        if (!this.form.id) {
          if (!this.form.pId) {
            this.$nextTick(() => {
              const table = this.$refs.myTable;
              if (table && table.bodyWrapper) {
                // 直接操作滚动容器
                table.bodyWrapper.scrollTop = table.bodyWrapper.scrollHeight;
              }
            })
          }
        }


        //  this.$modal.msgSuccess("保存成功");
        //this.open = false;
        // saveTask(this.form).then(response => {
        //   this.$store.dispatch('project/setListLastTime');
        //   this.$modal.msgSuccess("新增成功");
        //   this.open = false;
        //   if (this.form.taskIds.length > 0 || this.form.pId === null) {
        //     this.getList();
        //   } else {
        //     this.getList(true);
        //   }
        //   // this.expandRowKeys.push(this.form.pId + "|" + this.timeNum);
        // });


      });
    },
    handleAbstract() {
      this.reset();
      this.title = "添加摘要";
      this.open = true;
      this.form.projectId = this.queryParams.projectId;
      let l = [];
      this.dataList.forEach(item => {
        if (item.select) {
          l.push(this.removeAfterPipe(item.id));
        }
      });
      this.form.taskIds = l;
      this.form.sortNum = 0;
    },
    handleAutoSave() {
      //每10秒保存一次
      // setInterval(() => {
      //   // console.info("autoSave", this.autoSave)
      //   if (this.autoSave)
      //     this.handleSave()
      // }, 30000)

    },
    /** 保存任务列表 */
    handleSave() {
      this.loading = true;
      let list = this.treeToList(this.dataTree, "children", false)
      list.forEach(item => {
        item.startDate = this.formatDatYYYYMMDDHHMMSS(new Date(item.startDate))
        item.endDate = this.formatDatYYYYMMDDHHMMSS(new Date(item.endDate))
      });
      saveTask(list).then(response => {
        this.getList(false);
        this.loading = false;
        this.$modal.msgSuccess("保存成功");
      }).catch((e) => {
        //console.info(e)
        this.$modal.msgError("保存失败");
      })
      // this.$modal.msgSuccess("保存成功");
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let that = this;
      this.$modal.confirm('是否确认删除任务数据').then(function () {
        let list = that.treeToList(that.dataTree, "children", false)
        let r = list.find(a => a.id === row.id);
        let list2 = that.treeToList(r.children)
        let delIds = list2.map(a => a.id);
        delIds.push(r.id);
        that.dataList = that.dataList.filter(item => !delIds.includes(item.id));
        that.initTaskList();
        //that.$modal.msgSuccess("删除成功");
        //that.$emit('updateProject', '')
        //that.$store.dispatch('project/setListLastTime');
      });
    },
    handleDeleteRows() {
      let that = this;
      this.$modal.confirm('是否确认删除任务数据').then(function () {
        //从this.datalist 中找出 id 为 list 的数据
        let delIds = []
        that.ids.forEach(id => {
          let list = that.treeToList(that.dataTree, "children", false)
          let r = list.find(a => a.id === id);
          let list2 = that.treeToList(r.children)
          list2.forEach(a => delIds.push(a.id))
          delIds.push(r.id);
        })
        that.dataList = that.dataList.filter(item => !delIds.includes(item.id));
        that.initTaskList();
        //that.$modal.msgSuccess("删除成功");
        //that.$emit('updateProject', '')
        //that.$store.dispatch('project/setListLastTime');
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      let list = this.treeToList(this.dataTree, "children", false)
      list.forEach(item => {
        item.startDate = this.formatDatYYYYMMDDHHMMSS(new Date(item.startDate))
        item.endDate = this.formatDatYYYYMMDDHHMMSS(new Date(item.endDate))
      });
      saveTask(list).then(response => {
        this.getList(false);
        this.loading = false;
        this.pmsDownload('pms/task/export?projectId=' + this.projectId, this.projectName + '.xlsx')
        this.$modal.msgSuccess("保存成功");
      }).catch((e) => {
        //console.info(e)
        this.$modal.msgError("保存失败");
      })
    },
    getRowClass(row) {
      return {
        'bold-text': row.children.length > 0,
        'black-text': row.children.length > 0
      };
    },
  }
  ,
  watch: {}
  ,
  filters: {
    formatDate(value) {
      const date = new Date(value);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
  }
}
</script>
<style lang="scss">
.task-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 84px);

  .top {
    flex: 0 0 auto; /* 上面的空间不随内容变化而改变大小 */
  }

  .bottom {
    flex: 1; /* 下面的空间根据剩余空间自适应高度 */
  }

  .centered-input .el-input__inner {
    text-align: center;
  }

  .el-table {
    font-size: 12px;
  }

  .el-table__cell {
    padding: 2px 0;
  }

  .bold-text {
    font-weight: bold;
  }

  .black-text {
    color: black;
  }

  .el-dialog__body {
    height: calc(100vh * 0.75);
    overflow: auto;

  }


  .el-dialog.is-fullscreen {
    overflow: hidden !important;
  }


  //.dialog-footer {
  //  display: flex;
  //  justify-content: right; /* 水平居中 */
  //  align-items: center; /* 垂直居中 */
  //}

}
</style>
