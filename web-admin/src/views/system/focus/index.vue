<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="重点工作名称" prop="focusName">
        <el-input
          v-model="queryParams.focusName"
          placeholder="请输入重点工作名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>


      <el-form-item label="重点工作大类" prop="focusType">
        <el-select v-model="queryParams.focusType" placeholder="请选择重点工作大类" clearable>
          <el-option
            v-for="dict in dict.type.focus_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['system:focus:add']"
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
          v-hasPermi="['system:focus:edit']"
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
          v-hasPermi="['system:focus:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:focus:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="focusList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="重点工作大类" align="center" prop="focusType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.focus_type" :value="scope.row.focusType"/>
        </template>
      </el-table-column>
      <el-table-column label="重点工作名称" align="center" prop="focusName" />
      <el-table-column label="重点工作负责人" align="center" prop="focusPerson" />
      <el-table-column label="重点工作详情" align="center" prop="focusInfo" />
      <el-table-column label="重点工作当前进度" align="center" prop="focusProgress" />
      <el-table-column label="重点工作进度完成情况" align="center" prop="focusPersent">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.focus_type1" :value="scope.row.focusPersent"/>
        </template>
      </el-table-column>
      <el-table-column label="重点工作计划完成时间" align="center" prop="focusEndtime" />


      <el-table-column label="重点工作工作计划" align="center" prop="focusPlan" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:focus:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:focus:remove']"
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

    <!-- 添加或修改focus对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="重点工作名称" prop="focusName">
          <el-input v-model="form.focusName" placeholder="请输入重点工作名称" />
        </el-form-item>
        <el-form-item label="重点工作详情" prop="focusInfo">
          <el-input v-model="form.focusInfo" placeholder="请输入重点工作详情" />
        </el-form-item>
        <el-form-item label="重点工作当前进度" prop="focusProgress">
          <el-input v-model="form.focusProgress" placeholder="请输入重点工作当前进度" />
        </el-form-item>
        <el-form-item label="重点工作进度完成情况" prop="focusPersent">
          <el-select v-model="form.focusPersent" placeholder="请选择重点工作进度完成情况">
            <el-option
              v-for="dict in dict.type.focus_type1"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="重点工作计划完成时间" prop="focusEndtime">
          <el-input v-model="form.focusEndtime" placeholder="请输入重点工作计划完成时间" />
        </el-form-item>
        <el-form-item label="重点工作负责人" prop="focusPerson">
          <el-input v-model="form.focusPerson" placeholder="请输入重点工作负责人" />
        </el-form-item>
        <el-form-item label="重点工作大类" prop="focusType">
          <el-select v-model="form.focusType" placeholder="请选择重点工作大类">
            <el-option
              v-for="dict in dict.type.focus_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="重点工作工作计划" prop="focusPlan">
          <el-input v-model="form.focusPlan" placeholder="请输入重点工作工作计划" />
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
import { listFocus, getFocus, delFocus, addFocus, updateFocus } from "@/api/system/focus";

export default {
  name: "Focus",
  dicts: ['focus_type', 'focus_type1'],
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
      // focus表格数据
      focusList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        focusName: null,
        focusInfo: null,
        focusProgress: null,
        focusPersent: null,
        focusEndtime: null,
        focusPerson: null,
        focusType: null,
        focusPlan: null
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
    /** 查询focus列表 */
    getList() {
      this.loading = true;
      listFocus(this.queryParams).then(response => {
        this.focusList = response.rows;
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
        focusName: null,
        focusInfo: null,
        focusProgress: null,
        focusPersent: null,
        focusEndtime: null,
        focusPerson: null,
        focusType: null,
        focusPlan: null
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
      this.title = "添加focus";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFocus(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改focus";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFocus(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFocus(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除focus编号为"' + ids + '"的数据项？').then(function() {
        return delFocus(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/focus/export', {
        ...this.queryParams
      }, `focus_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
