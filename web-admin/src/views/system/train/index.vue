<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="培训/交流名称" prop="trainName">
        <el-input
          v-model="queryParams.trainName"
          placeholder="请输入培训/交流名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="培训/交流详情" prop="trainInfo">-->
<!--        <el-input-->
<!--          v-model="queryParams.trainInfo"-->
<!--          placeholder="请输入培训/交流详情"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="培训/交流时间" prop="trainTime">
        <el-date-picker clearable
          v-model="queryParams.trainTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择培训/交流时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="培训/交流单位" prop="trainCompany">
        <el-input
          v-model="queryParams.trainCompany"
          placeholder="请输入培训/交流单位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="培训/交流地点" prop="trainPlace">
        <el-input
          v-model="queryParams.trainPlace"
          placeholder="请输入培训/交流地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="培训/交流人员" prop="trainPerson">
        <el-input
          v-model="queryParams.trainPerson"
          placeholder="请输入培训/交流人员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:train:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:train:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:train:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:train:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="trainList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="${comment}" align="center" prop="id" />-->
      <el-table-column label="培训/交流名称" align="center" prop="trainName" />
      <el-table-column label="培训/交流详情" align="center" prop="trainInfo" />
      <el-table-column label="培训/交流时间" align="center" prop="trainTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.trainTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="培训/交流单位" align="center" prop="trainCompany" />
      <el-table-column label="培训/交流地点" align="center" prop="trainPlace" />
      <el-table-column label="培训/交流人员" align="center" prop="trainPerson" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:train:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:train:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改train对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="培训/交流名称" prop="trainName">
          <el-input v-model="form.trainName" placeholder="请输入培训/交流名称" />
        </el-form-item>
        <el-form-item label="培训/交流详情" prop="trainInfo">
          <el-input v-model="form.trainInfo" placeholder="请输入培训/交流详情" />
        </el-form-item>
        <el-form-item label="培训/交流时间" prop="trainTime">
          <el-date-picker clearable
            v-model="form.trainTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择培训/交流时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="培训/交流单位" prop="trainCompany">
          <el-input v-model="form.trainCompany" placeholder="请输入培训/交流单位" />
        </el-form-item>
        <el-form-item label="培训/交流地点" prop="trainPlace">
          <el-input v-model="form.trainPlace" placeholder="请输入培训/交流地点" />
        </el-form-item>
        <el-form-item label="培训/交流人员" prop="trainPerson">
          <el-input v-model="form.trainPerson" placeholder="请输入培训/交流人员" />
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
import { listTrain, getTrain, delTrain, addTrain, updateTrain } from "@/api/system/train";

export default {
  name: "Train",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // train表格数据
      trainList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        trainName: null,
        trainInfo: null,
        trainTime: null,
        trainCompany: null,
        trainPlace: null,
        trainPerson: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询train列表 */
    getList() {
      this.loading = true;
      listTrain(this.queryParams).then(response => {
        this.trainList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        trainName: null,
        trainInfo: null,
        trainTime: null,
        trainCompany: null,
        trainPlace: null,
        trainPerson: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加train";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTrain(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改train";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTrain(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTrain(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除train编号为"' + ids + '"的数据项？').then(function() {
        return delTrain(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/train/export', {
        ...this.queryParams
      }, `train_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
